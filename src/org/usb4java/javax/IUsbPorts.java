/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.util.List;
import javax.usb.IUsbDevice;
import javax.usb.IUsbPort;

/**
 * A list of USB ports.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @param <P> The USB port type.
 * @param <D> The USB device type.
 */
public interface IUsbPorts {

  /**
   * Returns the number of ports.
   * <p>
   * @return The number of ports.
   */
  public byte getNumberOfPorts();

  /**
   * Returns the ports.
   * <p>
   * @return The ports.
   */
  public List<IUsbPort> getUsbPorts();

  /**
   * Returns the USB port with the specified port number.
   * <p>
   * @param number The USB port number.
   * @return The USB port or null if no such port.
   */
  public IUsbPort getUsbPort(final byte number);

  /**
   * Returns the attached USB devices.
   * <p>
   * @return The attached USB devices.
   */
  public List<IUsbDevice> getAttachedUsbDevices();

  /**
   * Checks if the specified device is attached to one of the ports.
   * <p>
   * @param device The device to search.
   * @return True if device is connected, false if not.
   */
  public boolean isUsbDeviceAttached(IUsbDevice device);

  /**
   * Connects a new device to this hub.
   * <p>
   * @param device The device to add to this hub.
   */
  public void connectUsbDevice(IUsbDevice device);

  /**
   * Disconnects the specified device from the hub.
   * <p>
   * @param device The device to disconnected from the hub.
   */
  public void disconnectUsbDevice(IUsbDevice device);
}
