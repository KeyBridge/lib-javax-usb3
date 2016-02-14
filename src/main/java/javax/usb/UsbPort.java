/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb;

/**
 * usb4java implementation of IUsbUsbPort.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class UsbPort implements IUsbPort {

  /**
   * The USB hub this port belongs to.
   */
  private final IUsbHub hub;

  /**
   * The port number.
   */
  private final byte portNumber;

  /**
   * The attached device.
   */
  private IUsbDevice device;

  /**
   * Constructor.
   *
   * @param hub        The USB hub this port belongs to.
   * @param portNumber The port number.
   */
  public UsbPort(final IUsbHub hub, final byte portNumber) {
    this.hub = hub;
    this.portNumber = portNumber;
  }

  @Override
  public byte getPortNumber() {
    return this.portNumber;
  }

  @Override
  public IUsbHub getUsbHub() {
    return this.hub;
  }

  @Override
  public IUsbDevice getUsbDevice() {
    return this.device;
  }

  @Override
  public boolean isUsbDeviceAttached() {
    return this.device != null;
  }

  /**
   * Connects the specified device to this port.
   *
   * @param device The device to connect.
   */
  void connectUsbDevice(final IUsbDevice device) {
    if (device == null) {
      throw new IllegalArgumentException("UsbDevice must not be null");
    }
    if (this.device != null) {
      throw new IllegalStateException("Port already has a connected device");
    }
    this.device = device;
    ((AUsbDevice) device).setParentUsbPort(this);
  }

  /**
   * Disconnects the currently connected device.
   */
  void disconnectUsbDevice() {
    if (this.device == null) {
      throw new IllegalStateException("USB Port has no connected device");
    }
    final IUsbDevice usbDevice = this.device;
    this.device = null;
    ((AUsbDevice) usbDevice).setParentUsbPort(null);
  }

  @Override
  public String toString() {
    return "Port " + portNumber;
  }

}
