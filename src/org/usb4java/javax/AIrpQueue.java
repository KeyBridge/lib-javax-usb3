/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.usb.IUsbControlIrp;
import javax.usb.IUsbDevice;
import javax.usb.IUsbIrp;
import javax.usb.exception.UsbException;
import javax.usb.exception.UsbShortPacketException;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.javax.exception.ExceptionUtils;

/**
 * Abstract base class for a concurrent queue of USB I/O Request packets.
 * <p>
 * An IrpQueue contains a thread safe FIFO queue and a threaded
 * processUsbIrpQueueor to handle each IRP that is placed into the queue.
 * <p>
 * Developer note: The default operation of an IrpQueue is to support
 * Asynchronous operation (e.g. processUsbIrpQueue in a separate thread.) To
 * implement synchronous IRP queue handling implement a WAIT lock on the
 * {@link IUsbIrp.isComplete() isComplete} method IUsbIrp.isComplete().
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield <jesse@caulfield.org>
 * @param <T> The type of IRPs this queue holds.
 */
public abstract class AIrpQueue<T extends IUsbIrp> {

  /**
   * The queued USB IRP packets. These are placed in a ConcurrentLinkedQueue: an
   * unbounded thread-safe {@linkplain Queue queue} based on linked nodes. This
   * queue orders elements FIFO (first-in-first-out). The <em>head</em> of the
   * queue is that element that has been on the queue the longest time. The
   * <em>tail</em> of the queue is that element that has been on the queue the
   * shortest time. New elements are inserted at the tail of the queue, and the
   * queue retrieval operations obtain elements at the head of the queue.
   */
  private final Queue<T> usbIrpQueue = new ConcurrentLinkedQueue<>();

  /**
   * The queue processUsbIrpQueueor thread.
   */
  private volatile Thread usbIrpQueueProcessorThread;

  /**
   * If queue is currently aborting.
   */
  private volatile boolean aborting;

  /**
   * The USB device instance upon which the QUEUE is to be processed. This is
   * either a UsbHub or UsbDevice implementation.
   */
  protected final AUsbDevice usbDevice;

  /**
   * Constructor.
   * <p>
   * @param usbDevice The USB usbDevice. Must not be null.
   */
  public AIrpQueue(final IUsbDevice usbDevice) {
    if (usbDevice == null) {
      throw new IllegalArgumentException("USB device must be set");
    }
    this.usbDevice = (AUsbDevice) usbDevice;
  }

  /**
   * Queues the specified control IRP for processUsbIrpQueueing.
   * <p>
   * @param irp The control IRP to queue.
   */
  public final void add(final T irp) {
    /**
     * Add the USB IRP to the queue.
     */
    this.usbIrpQueue.add(irp);
    /**
     * Spawn a new UsbIrpQueueProcessorThread if one is not already running. If
     * a thread is already running then it will handle the just-added IRP as
     * that thread iterates through the FIFO queue.
     */
    if (this.usbIrpQueueProcessorThread == null) {
      this.usbIrpQueueProcessorThread = new Thread(new Runnable() {
        @Override
        public void run() {
          processUsbIrpQueue();
        }
      }, "usb4java IRP Queue Processor");
      /**
       * Developer note: Mark this thread as a daemon thread. A daemon thread in
       * Java is one that doesn't prevent the JVM from exiting. When the JVM
       * halts any remaining daemon threads are abandoned: finally blocks are
       * not executed, stacks are not unwound - JVM just exits.
       */
      this.usbIrpQueueProcessorThread.setDaemon(true);
      /**
       * Start the thread. This will begin processing all IRPs in the queue in a
       * separate thread and immediately return (e.g. asynchronously).
       */
      this.usbIrpQueueProcessorThread.start();
    }
  }

  /**
   * Internal method to processUsbIrpQueue all IRPs in the FIFO queue. This
   * method returns after all IRP objects in the queue have been
   * processUsbIrpQueueed.
   * <p>
   * This method should be called from within a separate thread to enable
   * asynchronous operation.
   */
  private void processUsbIrpQueue() {
    /**
     * Get the first IRP from the queue ready for processing.
     */
    T usbIrp = this.usbIrpQueue.poll();
    /**
     * If there are no IRPs to process then mark the thread as closing right
     * away. Otherwise process the IRP (and more IRPs from the queue if
     * present).
     */
    if (usbIrp == null) {
      this.usbIrpQueueProcessorThread = null;
    } else {
      while (usbIrp != null) {
        // Process the IRP
        try {
          processIrp(usbIrp);
        } catch (final UsbException e) {
          usbIrp.setUsbException(e);
        }
        /**
         * Developer note: Get next IRP and (if necessary) mark the thread as
         * closing before sending events for the previous IRP. This is important
         * for asynchronous notification.
         */
        final T usbIrpNext = this.usbIrpQueue.poll();
        if (usbIrpNext == null) {
          this.usbIrpQueueProcessorThread = null;
        }
        /**
         * Finish the previous IRP.
         */
        usbIrp.complete();
        finishIrp(usbIrp);
        /**
         * Set the usbIrp variable to the next IRP. This will continue the WHILE
         * loop one more time (if not null)
         */
        usbIrp = usbIrpNext;
      }
    }

    // No more IRPs are present in the queue so terminate the thread.
    synchronized (this.usbIrpQueue) {
      this.usbIrpQueue.notifyAll();
    }
  }

  /**
   * Processes the IRP.
   * <p>
   * @param irp The IRP to processUsbIrpQueue.
   * @throws UsbException When processUsbIrpQueueing the IRP fails.
   */
  protected abstract void processIrp(final T irp) throws UsbException;

  /**
   * Called after IRP has finished. This can be implemented to send events for
   * example.
   * <p>
   * @param irp The IRP which has been finished.
   */
  protected abstract void finishIrp(final IUsbIrp irp);

  /**
   * Aborts all queued IRPs. The IRP which is currently processUsbIrpQueueed
   * can't be aborted. This method returns as soon as no more IRPs are in the
   * queue and no more are processUsbIrpQueueed.
   */
  public final void abort() {
    this.aborting = true;
    this.usbIrpQueue.clear();
    while (isBusy()) {
      try {
        synchronized (this.usbIrpQueue) {
          if (isBusy()) {
            this.usbIrpQueue.wait();
          }
        }
      } catch (final InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    this.aborting = false;
  }

  /**
   * Checks if queue is busy. A busy queue is a queue which is currently
   * processUsbIrpQueueing IRPs or which still has IRPs in the queue.
   * <p>
   * @return True if queue is busy, false if not.
   */
  public final boolean isBusy() {
    return !this.usbIrpQueue.isEmpty() || this.usbIrpQueueProcessorThread != null;
  }

  /**
   * Returns the configuration.
   * <p>
   * @return The configuration.
   */
  protected final ServicesInstanceConfiguration getConfig() {
    return Services.getInstance().getConfig();
  }

  /**
   * Returns the USB usbDevice.
   * <p>
   * @return The USB usbDevice. Never null.
   */
//  protected final IUsbDevice getDevice() {    return this.usbDevice;  }
  /**
   * Processes the control IRP.
   * <p>
   * @param irp The IRP to processUsbIrpQueue.
   * @throws UsbException When processUsbIrpQueueing the IRP fails.
   */
  protected final void processControlIrp(final IUsbControlIrp irp) throws UsbException {
    final ByteBuffer buffer = ByteBuffer.allocateDirect(irp.getLength());
    buffer.put(irp.getData(), irp.getOffset(), irp.getLength());
    buffer.rewind();
    final DeviceHandle deviceHandle = usbDevice.open();
    final int result = LibUsb.controlTransfer(deviceHandle, irp.bmRequestType(),
                                              irp.bRequest(), irp.wValue(), irp.wIndex(), buffer,
                                              getConfig().getTimeout());
    if (result < 0) {
      throw ExceptionUtils.createPlatformException("Unable to submit control message", result);
    }
    buffer.rewind();
    buffer.get(irp.getData(), irp.getOffset(), result);
    irp.setActualLength(result);
    if (irp.getActualLength() != irp.getLength() && !irp.getAcceptShortPacket()) {
      throw new UsbShortPacketException();
    }
  }

  /**
   * Checks if this queue is currently aborting.
   * <p>
   * @return True if queue is aborting, false if not.
   */
  protected final boolean isAborting() {
    return this.aborting;
  }
}
