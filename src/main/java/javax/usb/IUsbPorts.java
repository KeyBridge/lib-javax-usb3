/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb;

import java.util.List;

/**
 * A usb4Java convenience interface applied to USB HUBS to characterize their
 * physical ports and connected devices. A list of USB ports. This information
 * is useful when examining the USB tree.
 * <p>
 * All ports are IUsbPort type. All Devices are IUsbDevice type, and may be
 * UsbHubs or UsbDevice endpoints.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public interface IUsbPorts {

  /**
   * Returns the number of ports.
   *
   * @return The number of ports.
   */
  public byte getNumberOfPorts();

  /**
   * Returns the ports.
   *
   * @return The ports.
   */
  public List<IUsbPort> getUsbPorts();

  /**
   * Returns the USB port with the specified port number.
   *
   * @param number The USB port number.
   * @return The USB port or null if no such port.
   */
  public IUsbPort getUsbPort(final byte number);

  /**
   * Returns the attached USB devices.
   *
   * @return The attached USB devices.
   */
  public List<IUsbDevice> getAttachedUsbDevices();

  /**
   * Checks if the specified device is attached to one of the ports.
   *
   * @param device The device to search.
   * @return True if device is connected, false if not.
   */
  public boolean isUsbDeviceAttached(IUsbDevice device);

  /**
   * Connects a new device to this hub.
   *
   * @param device The device to add to this hub.
   */
  public void connectUsbDevice(IUsbDevice device);

  /**
   * Disconnects the specified device from the hub.
   *
   * @param device The device to disconnected from the hub.
   */
  public void disconnectUsbDevice(IUsbDevice device);
}
