/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javax.usb.*;
import javax.usb.exception.UsbAbortException;
import javax.usb.exception.UsbException;
import javax.usb.exception.UsbShortPacketException;
import javax.usb.ri.enumerated.EEndpointDirection;
import javax.usb.ri.enumerated.EEndpointTransferType;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.javax.exception.ExceptionUtils;

/**
 * A concurrent queue manager for USB I/O Request packets.
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
 */
public final class IrpQueue extends AIrpQueue<IUsbIrp> {

  /**
   * The USB pipe.
   */
  private final IUsbPipe pipe;

  /**
   * The PIPE end point direction. [IN, OUT]. This is set upon instantiation and
   * proxied in a class-level field to speed up do/while loops buried within.
   */
  private final EEndpointDirection endPointDirection;
  /**
   * The PIPE end point transfer type. This is set upon instantiation and
   * proxied in a class-level field to speed up do/while loops buried within.
   */
  private final EEndpointTransferType endpointTransferType;
  /**
   * The PIPE end point descriptor. This is set upon instantiation and proxied
   * in a class-level field to speed up do/while loops buried within.
   */
  private final IUsbEndpointDescriptor endpointDescriptor;

  /**
   * Constructor.
   * <p>
   * @param pipe The USB pipe
   */
  public IrpQueue(final UsbPipe pipe) {
    super(pipe.getDevice());
    this.pipe = pipe;
    this.endPointDirection = pipe.getUsbEndpoint().getDirection();
    this.endpointTransferType = pipe.getUsbEndpoint().getType();
    this.endpointDescriptor = this.pipe.getUsbEndpoint().getUsbEndpointDescriptor();
  }

  /**
   * Processes the IRP.
   * <p>
   * @param irp The IRP to processUsbIrpQueue.
   * @throws UsbException When processUsbIrpQueueing the IRP fails.
   */
  @Override
  protected void processIrp(final IUsbIrp irp) throws UsbException {
    final IUsbEndpoint endpoint = this.pipe.getUsbEndpoint();
    if (EEndpointTransferType.CONTROL.equals(endpoint.getType())) {
      processControlIrp((IUsbControlIrp) irp);
      return;
    }
    switch (endpoint.getDirection()) {
      case OUT:
      case HOST_TO_DEVICE:
        irp.setActualLength(write(irp.getData(), irp.getOffset(), irp.getLength()));
        if (irp.getActualLength() < irp.getLength() && !irp.getAcceptShortPacket()) {
          throw new UsbShortPacketException();
        }
        break;
      case IN:
      case DEVICE_TO_HOST:
        irp.setActualLength(read(irp.getData(), irp.getOffset(), irp.getLength()));
        if (irp.getActualLength() < irp.getLength() && !irp.getAcceptShortPacket()) {
          throw new UsbShortPacketException();
        }
        break;

      default:
        throw new UsbException("Invalid direction: " + endpoint.getDirection());
    }
  }

  /**
   * Called after IRP has finished. This can be implemented to send events for
   * example.
   * <p>
   * @param irp The IRP which has been finished.
   */
  @Override
  protected void finishIrp(final IUsbIrp irp) {
    ((UsbPipe) this.pipe).sendEvent(irp);
  }

  /**
   * Reads bytes from an interrupt endpoint into the specified data array.
   * <p>
   * @param data   The data array to write the read bytes to.
   * @param offset The offset in the data array to write the read bytes to.
   * @param len    The number of bytes to read.
   * @throws UsbException When transfer fails.
   * @return The number of read bytes.
   */
  private int read(final byte[] data, final int offset, final int len) throws UsbException {
    /**
     * Open the USB device and returns the USB device handle. If device was
     * already open then the old handle is returned.
     */
    final DeviceHandle deviceHandle = ((AUsbDevice) getDevice()).open();
    int read = 0;
    while (read < len) {
      final int size = Math.min(len - read, endpointDescriptor.wMaxPacketSize() & 0xffff);
      final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
      final int result = transfer(deviceHandle, endpointDescriptor, buffer);
      buffer.rewind();
      buffer.get(data, offset + read, result);
      read += result;

      // Short packet detected, aborting
      if (result < size) {
        break;
      }
    }
    /**
     * Close the device. If device is not open then nothing is done.
     */
    ((AUsbDevice) getDevice()).close();
    return read;
  }

  /**
   * Writes the specified bytes to a interrupt endpoint.
   * <p>
   * @param data   The data array with the bytes to write.
   * @param offset The offset in the data array to write.
   * @param len    The number of bytes to write.
   * @throws UsbException When transfer fails.
   * @return The number of written bytes.
   */
  private int write(final byte[] data, final int offset, final int len) throws UsbException {
//    final byte type = this.pipe.getUsbEndpoint().getType().getByteCode();
    final DeviceHandle handle = ((AUsbDevice) getDevice()).open();
    int written = 0;
    while (written < len) {
      final int size = Math.min(len - written, endpointDescriptor.wMaxPacketSize() & 0xffff);
      final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
      buffer.put(data, offset + written, size);
      buffer.rewind();
      final int result = transfer(handle, endpointDescriptor, buffer);
      written += result;

      // Short packet detected, aborting
      if (result < size) {
        break;
      }
    }
    /**
     * Close the device. If device is not open then nothing is done.
     */
//    ((AUsbDevice) getDevice()).close();
    return written;
  }

  /**
   * Transfers data from or to the device.
   * <p>
   * @param handle     The device handle.
   * @param descriptor The endpoint descriptor.
   * @param type       The endpoint type.
   * @param buffer     The data buffer.
   * @return The number of transferred bytes.
   * @throws UsbException When data transfer fails.
   */
  private int transfer(final DeviceHandle handle,
                       final IUsbEndpointDescriptor descriptor,
                       final ByteBuffer buffer) throws UsbException {
    if (EEndpointTransferType.BULK.equals(endpointTransferType)) {
      return transferBulk(handle, descriptor.bEndpointAddress(), buffer);
    } else if (EEndpointTransferType.INTERRUPT.equals(endpointTransferType)) {
      return transferInterrupt(handle, descriptor.bEndpointAddress(), buffer);
    } else {
      throw new UsbException("Unsupported endpoint type: " + endpointTransferType);
    }
  }

  /**
   * Transfers bulk data from or to the device.
   * <p>
   * @param handle  The device handle.
   * @param address The endpoint address.
   * @param buffer  The data buffer.
   * @return The number of transferred bytes.
   * @throws UsbException When data transfer fails.
   */
  private int transferBulk(final DeviceHandle handle,
                           final byte address,
                           final ByteBuffer buffer) throws UsbException {
    final IntBuffer transferred = IntBuffer.allocate(1);
    int result;
    do {
      result = LibUsb.bulkTransfer(handle, address, buffer, transferred, getConfig().getTimeout());
      if (result == LibUsb.ERROR_TIMEOUT && isAborting()) {
        throw new UsbAbortException();
      }
    } while (EEndpointDirection.IN.equals(endPointDirection) && result == LibUsb.ERROR_TIMEOUT);
    if (result < 0) {
      throw ExceptionUtils.createPlatformException("Transfer error on bulk endpoint", result);
    }
    return transferred.get(0);
  }

  /**
   * Transfers interrupt data from or to the device.
   * <p>
   * @param handle  The device handle.
   * @param address The endpoint address.
   * @param buffer  The data buffer.
   * @return The number of transferred bytes.
   * @throws UsbException When data transfer fails.
   */
  private int transferInterrupt(final DeviceHandle handle,
                                final byte address,
                                final ByteBuffer buffer) throws UsbException {
    final IntBuffer transferred = IntBuffer.allocate(1);
    int result;
    do {
      result = LibUsb.interruptTransfer(handle, address, buffer, transferred, getConfig().getTimeout());
      if (result == LibUsb.ERROR_TIMEOUT && isAborting()) {
        throw new UsbAbortException();
      }
    } while (EEndpointDirection.IN.equals(endPointDirection) && result == LibUsb.ERROR_TIMEOUT);
    if (result < 0) {
      throw ExceptionUtils.createPlatformException(
        "Transfer error on interrupt endpoint", result);
    }
    return transferred.get(0);
  }
}
