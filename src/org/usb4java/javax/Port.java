/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.UsbDevice;
import javax.usb.UsbHub;
import javax.usb.UsbPort;

/**
 * usb4java implementation of UsbPort.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Port implements UsbPort {

  /**
   * The USB hub this port belongs to.
   */
  private final UsbHub hub;

  /**
   * The port number.
   */
  private final byte portNumber;

  /**
   * The attached device.
   */
  private UsbDevice device;

  /**
   * Constructor.
   * <p>
   * @param hub        The USB hub this port belongs to.
   * @param portNumber The port number.
   */
  public Port(final UsbHub hub, final byte portNumber) {
    this.hub = hub;
    this.portNumber = portNumber;
  }

  @Override
  public byte getPortNumber() {
    return this.portNumber;
  }

  @Override
  public UsbHub getUsbHub() {
    return this.hub;
  }

  @Override
  public UsbDevice getUsbDevice() {
    return this.device;
  }

  @Override
  public boolean isUsbDeviceAttached() {
    return this.device != null;
  }

  /**
   * Connects the specified device to this port.
   * <p>
   * @param device The device to connect.
   */
  void connectUsbDevice(final UsbDevice device) {
    if (device == null) {
      throw new IllegalArgumentException("device must not be null");
    }
    if (this.device != null) {
      throw new IllegalStateException(
        "Port already has a connected device");
    }
    this.device = device;
    ((AUsbDevice) device).setParentUsbPort(this);
  }

  /**
   * Disconnects the currently connected device.
   */
  void disconnectUsbDevice() {
    if (this.device == null) {
      throw new IllegalStateException("Port has no connected device");
    }
    final UsbDevice device = this.device;
    this.device = null;
    ((AUsbDevice) device).setParentUsbPort(null);
  }
}
