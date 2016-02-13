/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package javax.usb;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javax.usb.enumerated.EDataFlowtype;
import javax.usb.enumerated.EEndpointDirection;
import javax.usb.exception.ExceptionUtility;
import javax.usb.exception.UsbAbortException;
import javax.usb.exception.UsbException;
import javax.usb.exception.UsbShortPacketException;
import javax.usb.request.BEndpointAddress;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;

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
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class UsbIrpQueue extends AUsbIrpQueue<IUsbIrp> {

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
  private final EDataFlowtype endpointTransferType;
  /**
   * The PIPE end point descriptor. This is set upon instantiation and proxied
   * in a class-level field to speed up do/while loops buried within.
   */
  private final IUsbEndpointDescriptor endpointDescriptor;

  /**
   * Constructor.
   *
   * @param pipe The USB pipe
   */
  public UsbIrpQueue(final UsbPipe pipe) {
    super(pipe.getDevice());
    this.pipe = pipe;
    this.endPointDirection = pipe.getUsbEndpoint().getDirection();
    this.endpointTransferType = pipe.getUsbEndpoint().getType();
    this.endpointDescriptor = this.pipe.getUsbEndpoint().getUsbEndpointDescriptor();
  }

  /**
   * Processes the IRP.
   *
   * @param irp The IRP to processUsbIrpQueue.
   * @throws UsbException When processUsbIrpQueueing the IRP fails.
   */
  @Override
  protected void processIrp(final IUsbIrp irp) throws UsbException {
    final IUsbEndpoint endpoint = this.pipe.getUsbEndpoint();
    if (EDataFlowtype.CONTROL.equals(endpoint.getType())) {
      processControlIrp((IUsbControlIrp) irp);
      return;
    }
    switch (endpoint.getDirection()) {
      case OUT:
      case HOST_TO_DEVICE:
        writeUsbIrp(irp);
//        irp.setActualLength(write(irp.getData(), irp.getOffset(), irp.getLength()));
        if (irp.getActualLength() < irp.getLength() && !irp.getAcceptShortPacket()) {
          throw new UsbShortPacketException();
        }
        break;
      case IN:
      case DEVICE_TO_HOST:
        readUsbIrp(irp);
//        irp.setActualLength(read(irp.getData(), irp.getOffset(), irp.getLength()));
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
   *
   * @param irp The IRP which has been finished.
   */
  @Override
  protected void finishIrp(final IUsbIrp irp) {
    ((UsbPipe) this.pipe).sendEvent(irp);
  }

  /**
   * Reads bytes from an interrupt endpoint into the specified I/O Request
   * Packet.
   *
   * @param irp A USB I/O Request Packet (IRP) instance
   * @throws UsbException if the Device cannot be opened or cannot be read from
   */
  private void readUsbIrp(final IUsbIrp irp) throws UsbException {
    /**
     * Open the USB device and returns the USB device handle. If device was
     * already open then the old handle is returned.
     */
    final DeviceHandle deviceHandle = this.usbDevice.open();
    int read = 0;
    while (read < irp.getLength()) {
      final int size = Math.min(irp.getLength() - read, endpointDescriptor.wMaxPacketSize() & 0xffff);
      final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
      final int result = transfer(deviceHandle, endpointDescriptor, buffer);
      buffer.rewind();
      buffer.get(irp.getData(), irp.getOffset() + read, result);
      read += result;
      /**
       * Short packet detected, abort the WHILE loop.
       */
      if (result < size) {
        break;
      }
    }
    irp.setActualLength(read);
  }

  /**
   * Write an I/O Request Packet to an interrupt endpoint.
   *
   * @param irp A USB I/O Request Packet (IRP) instance
   * @throws UsbException if the Device cannot be opened or cannot be written to
   */
  private void writeUsbIrp(final IUsbIrp irp) throws UsbException {
    final DeviceHandle handle = this.usbDevice.open();
    int written = 0;
    while (written < irp.getLength()) {
      final int size = Math.min(irp.getLength() - written, endpointDescriptor.wMaxPacketSize() & 0xffff);
      final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
      buffer.put(irp.getData(), irp.getOffset() + written, size);
      buffer.rewind();
      final int result = transfer(handle, endpointDescriptor, buffer);
      written += result;
      // Short packet detected, aborting
      if (result < size) {
        break;
      }
    }
    irp.setActualLength(written);
  }

  /**
   * Transfers data from or to the device.
   *
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
    switch (endpointTransferType) {
      case BULK:
        return transferBulk(handle, descriptor.bEndpointAddress(), buffer);
      case INTERRUPT:
        return transferInterrupt(handle, descriptor.bEndpointAddress(), buffer);
      case CONTROL:
        throw new UsbException("Unsupported endpoint type: " + endpointTransferType + ": Control transfers require a Control-Type IRP.");
      case ISOCHRONOUS:
        throw new UsbException("Unsupported endpoint type: " + endpointTransferType + ". libusb asynchronous (non-blocking) API for USB device I/O not yet implemented.");
      default:
        throw new AssertionError(endpointTransferType.name());
    }
  }

  /**
   * Transfers bulk data from or to the device.
   *
   * @param handle  The device handle.
   * @param address The endpoint address.
   * @param buffer  The data buffer.
   * @return The number of transferred bytes.
   * @throws UsbException When data transfer fails.
   */
  private int transferBulk(final DeviceHandle handle,
                           final BEndpointAddress address,
                           final ByteBuffer buffer) throws UsbException {
    final IntBuffer transferred = IntBuffer.allocate(1);
    int result;
    do {
      result = LibUsb.bulkTransfer(handle, address.getByteCode(), buffer, transferred, UsbServiceInstanceConfiguration.TIMEOUT);
      if (result == LibUsb.ERROR_TIMEOUT && isAborting()) {
        throw new UsbAbortException();
      }
    } while (EEndpointDirection.DEVICE_TO_HOST.equals(endPointDirection) && result == LibUsb.ERROR_TIMEOUT);
    if (result < 0) {
      throw ExceptionUtility.createPlatformException("Transfer error on bulk endpoint", result);
    }
    return transferred.get(0);
  }

  /**
   * Transfers interrupt data from or to the device.
   *
   * @param handle  The device handle.
   * @param address The endpoint address.
   * @param buffer  The data buffer.
   * @return The number of transferred bytes.
   * @throws UsbException When data transfer fails.
   */
  private int transferInterrupt(final DeviceHandle handle,
                                final BEndpointAddress address,
                                final ByteBuffer buffer) throws UsbException {
    final IntBuffer transferred = IntBuffer.allocate(1);
    int result;
    do {
      result = LibUsb.interruptTransfer(handle, address.getByteCode(), buffer, transferred, UsbServiceInstanceConfiguration.TIMEOUT);
      if (result == LibUsb.ERROR_TIMEOUT && isAborting()) {
        throw new UsbAbortException();
      }
    } while (EEndpointDirection.DEVICE_TO_HOST.equals(endPointDirection) && result == LibUsb.ERROR_TIMEOUT);
    if (result < 0) {
      throw ExceptionUtility.createPlatformException("Transfer error on interrupt endpoint", result);
    }
    return transferred.get(0);
  }
}
