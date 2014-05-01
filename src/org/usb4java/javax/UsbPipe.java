/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.util.List;
import javax.usb.*;
import javax.usb.event.IUsbPipeListener;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.exception.UsbException;
import javax.usb.exception.UsbNotActiveException;
import javax.usb.exception.UsbNotClaimedException;
import javax.usb.exception.UsbNotOpenException;
import javax.usb.ri.UsbControlIrp;
import javax.usb.ri.UsbIrp;

/**
 * usb4java implementation of IUsbUsbPipe.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class UsbPipe implements IUsbPipe {

  /**
   * The endpoint this pipe belongs to.
   */
  private final UsbEndpoint endpoint;

  /**
   * The USB pipe listeners.
   */
  private final PipeListenerList listeners = new PipeListenerList();

  /**
   * If pipe is open or not.
   */
  private boolean opened;

  /**
   * The request queue.
   */
  private final IrpQueue queue;

  /**
   * Construct a new USB Pipe attached to the indicated UsbEndpoint.
   * <p>
   * @param endpoint The endpoint this pipe belongs to.
   */
  public UsbPipe(final UsbEndpoint endpoint) {
    this.endpoint = endpoint;
    this.queue = new IrpQueue(this);
  }

  /**
   * Returns the USB device.
   * <p>
   * @return The USB device.
   */
  public IUsbDevice getDevice() {
    return this.endpoint.getUsbInterface().getUsbConfiguration().getUsbDevice();
  }

  /**
   * Ensures the pipe is active.
   * <p>
   * @throws UsbNotActiveException When pipe is not active
   */
  private void checkActive() {
    if (!isActive()) {
      throw new UsbNotActiveException("Pipe is not active.");
    }
  }

  /**
   * Ensures the interface is active.
   * <p>
   * @throws UsbNotClaimedException When interface is not claimed.
   */
  private void checkClaimed() {
    if (!this.endpoint.getUsbInterface().isClaimed()) {
      throw new UsbNotClaimedException("Interface is not claimed.");
    }
  }

  /**
   * Ensures the device is connected.
   * <p>
   * @throws UsbDisconnectedException When device has been disconnected.
   */
//  private void checkConnected() {    getDevice().checkConnected();  }
  /**
   * Ensures the pipe is open.
   * <p>
   * @throws UsbNotOpenException When pipe is not open.
   */
  private void checkOpen() {
    if (!isOpen()) {
      throw new UsbNotOpenException("Pipe is not open.");
    }
  }

  @Override
  public void open() throws UsbException {
    checkActive();
    checkClaimed();
//    checkConnected();
    if (this.opened) {
      throw new UsbException("Pipe is already open");
    }
    this.opened = true;
  }

  @Override
  public void close() throws UsbException {
    checkActive();
    checkClaimed();
//    checkConnected();
    if (!this.opened) {
      throw new UsbException("Pipe is already closed");
    }
    if (this.queue.isBusy()) {
      throw new UsbException("Pipe is still busy");
    }
    this.opened = false;
  }

  @Override
  public boolean isActive() {
    final IUsbInterface iface = this.endpoint.getUsbInterface();
    final IUsbConfiguration config = iface.getUsbConfiguration();
    return iface.isActive() && config.isActive();
  }

  @Override
  public boolean isOpen() {
    return this.opened;
  }

  @Override
  public IUsbEndpoint getUsbEndpoint() {
    return this.endpoint;
  }

  @Override
  public int syncSubmit(final byte[] data) throws UsbException {
    final IUsbIrp irp = asyncSubmit(data);
    irp.waitUntilComplete();
    if (irp.isUsbException()) {
      throw irp.getUsbException();
    }
    return irp.getActualLength();
  }

  @Override
  public IUsbIrp asyncSubmit(final byte[] data) {
    if (data == null) {
      throw new IllegalArgumentException("USB I/O Request Packet (IRP) data must not be null");
    }
    final IUsbIrp irp = createUsbIrp();
    irp.setAcceptShortPacket(true);
    irp.setData(data);
    asyncSubmit(irp);
    return irp;
  }

  @Override
  public void syncSubmit(final IUsbIrp irp) throws UsbException {
    if (irp == null) {
      throw new IllegalArgumentException("USB I/O Request Packet (IRP) must not be null");
    }
    asyncSubmit(irp);
    irp.waitUntilComplete();
    if (irp.isUsbException()) {
      throw irp.getUsbException();
    }
  }

  @Override
  public void asyncSubmit(final IUsbIrp irp) {
    if (irp == null) {
      throw new IllegalArgumentException("USB I/O Request Packet (IRP) must not be null");
    }
    checkActive();
//    checkConnected();
    checkOpen();
    this.queue.add(irp);
  }

  @Override
  public void syncSubmit(final List<IUsbIrp> list) throws UsbException {
    for (final IUsbIrp irp : list) {
      syncSubmit(irp);
    }
  }

  @Override
  public void asyncSubmit(final List<IUsbIrp> list) {
    for (final IUsbIrp irp : list) {
      asyncSubmit(irp);
    }
  }

  @Override
  public void abortAllSubmissions() {
    checkActive();
//    checkConnected();
    checkOpen();
    this.queue.abort();
  }

  @Override
  public IUsbIrp createUsbIrp() {
    return new UsbIrp();
  }

  @Override
  public IUsbControlIrp createUsbControlIrp(final byte bmRequestType,
                                            final byte bRequest,
                                            final short wValue,
                                            final short wIndex) {
    return new UsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
  }

  @Override
  public void addUsbPipeListener(final IUsbPipeListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void removeUsbPipeListener(final IUsbPipeListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Sends event to all event listeners.
   * <p>
   * @param irp Then request package
   */
  public void sendEvent(final IUsbIrp irp) {
    if (irp.isUsbException()) {
      this.listeners.errorEventOccurred(new UsbPipeErrorEvent(this, irp));
    } else {
      this.listeners.dataEventOccurred(new UsbPipeDataEvent(this, irp));
    }
  }

  @Override
  public String toString() {
    return String.format("USB pipe of endpoint %02x",
                         this.endpoint.getUsbEndpointDescriptor().bEndpointAddress());
  }
}
