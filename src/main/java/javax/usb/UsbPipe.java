/*
 * Copyright (C) 2011 Klaus Reimer 
 * Copyright (C) 2014 Jesse Caulfield
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package javax.usb;

import java.util.List;
import javax.usb.event.IUsbPipeListener;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.exception.UsbException;
import javax.usb.exception.UsbNotActiveException;
import javax.usb.exception.UsbNotClaimedException;
import javax.usb.exception.UsbNotOpenException;

/**
 * usb4java implementation of IUsbUsbPipe.
 *
 * @author Klaus Reimer 
 * @author Jesse Caulfield
 */
public final class UsbPipe implements IUsbPipe {

  /**
   * The endpoint this pipe belongs to.
   */
  private final UsbEndpoint endpoint;

  /**
   * The USB pipe listeners.
   */
  private final UsbPipeListener listeners = new UsbPipeListener();

  /**
   * If pipe is open or not.
   */
  private boolean opened;

  /**
   * The USB I/O Request Packet (IRP) queue manager.
   */
  private final UsbIrpQueue iprQueue;

  /**
   * Construct a new USB Pipe attached to the indicated UsbEndpoint.
   *
   * @param endpoint The endpoint this pipe belongs to.
   */
  public UsbPipe(final UsbEndpoint endpoint) {
    this.endpoint = endpoint;
    this.iprQueue = new UsbIrpQueue(this);
  }

  /**
   * Returns the USB device.
   *
   * @return The USB device.
   */
  public IUsbDevice getDevice() {
    return this.endpoint.getUsbInterface().getUsbConfiguration().getUsbDevice();
  }

  /**
   * Ensures the pipe is active.
   *
   * @throws UsbNotActiveException When pipe is not active
   */
  private void checkActive() {
    if (!isActive()) {
      throw new UsbNotActiveException("Pipe is not active.");
    }
  }

  /**
   * Ensures the interface is active.
   *
   * @throws UsbNotClaimedException When interface is not claimed.
   */
  private void checkClaimed() {
    if (!this.endpoint.getUsbInterface().isClaimed()) {
      throw new UsbNotClaimedException("Interface is not claimed.");
    }
  }

  /**
   * Ensures the device is connected.
   *
   * @throws UsbDisconnectedException When device has been disconnected.
   */
//  private void checkConnected() {    getDevice().checkConnected();  }
  /**
   * Ensures the pipe is open.
   *
   * @throws UsbNotOpenException When pipe is not open.
   */
  private void checkOpen() {
    if (!isOpen()) {
      throw new UsbNotOpenException("Pipe is not open.");
    }
  }

  /**
   * @inerit
   *
   * @throws UsbException if the Pipe is already open
   */
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

  /**
   * @inerit
   *
   * @throws UsbException if the Pipe is already closed or the Pipe is still
   *                      busy
   */
  @Override
  public void close() throws UsbException {
    checkActive();
    checkClaimed();
//    checkConnected();
    if (!this.opened) {
      throw new UsbException("Pipe is already closed");
    }
    if (this.iprQueue.isBusy()) {
      throw new UsbException("Pipe is still busy");
    }
    this.opened = false;
  }

  /**
   * @inherit
   */
  @Override
  public boolean isActive() {
    final IUsbInterface iface = this.endpoint.getUsbInterface();
    final IUsbConfiguration config = iface.getUsbConfiguration();
    return iface.isActive() && config.isActive();
  }

  /**
   * @inherit
   */
  @Override
  public boolean isOpen() {
    return this.opened;
  }

  /**
   * @inherit
   */
  @Override
  public IUsbEndpoint getUsbEndpoint() {
    return this.endpoint;
  }

  /**
   * @inherit
   */
  @Override
  public int syncSubmit(final byte[] data) throws UsbException {
    final IUsbIrp irp = asyncSubmit(data);
    irp.waitUntilComplete();
    if (irp.isUsbException()) {
      throw irp.getUsbException();
    }
    return irp.getActualLength();
  }

  /**
   * @inherit
   *
   * @throws IllegalArgumentException if data is null
   */
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

  /**
   * @inherit
   *
   * @throws IllegalArgumentException if IRP is null
   */
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

  /**
   * @inherit
   *
   * @throws IllegalArgumentException if IRP is null
   */
  @Override
  public void asyncSubmit(final IUsbIrp irp) {
    if (irp == null) {
      throw new IllegalArgumentException("USB I/O Request Packet (IRP) must not be null");
    }
    checkActive();
//    checkConnected();
    checkOpen();
    this.iprQueue.add(irp);
  }

  /**
   * @inherit
   *
   * @throws IllegalArgumentException if IRP is null
   */
  @Override
  public void syncSubmit(final List<IUsbIrp> list) throws UsbException {
    for (final IUsbIrp irp : list) {
      syncSubmit(irp);
    }
  }

  /**
   * @inherit
   */
  @Override
  public void asyncSubmit(final List<IUsbIrp> list) {
    for (final IUsbIrp irp : list) {
      asyncSubmit(irp);
    }
  }

  /**
   * @inherit
   */
  @Override
  public void abortAllSubmissions() {
    checkActive();
//    checkConnected();
    checkOpen();
    this.iprQueue.abort();
  }

  /**
   * @inherit
   */
  @Override
  public IUsbIrp createUsbIrp() {
    return new UsbIrp();
  }

  /**
   * @inherit
   */
  @Override
  public IUsbControlIrp createUsbControlIrp(final byte bmRequestType,
                                            final byte bRequest,
                                            final short wValue,
                                            final short wIndex) {
    return new UsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
  }

  /**
   * @inherit
   */
  @Override
  public void addUsbPipeListener(final IUsbPipeListener listener) {
    this.listeners.add(listener);
  }

  /**
   * @inherit
   */
  @Override
  public void removeUsbPipeListener(final IUsbPipeListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Sends event to all event listeners.
   *
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
    return String.format("USB pipe of endpoint %s",
                         this.endpoint.getUsbEndpointDescriptor());
  }
}
