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

import javax.usb.utility.ByteUtility;

/**
 * usb4java implementation of IUsbUsbPort.
 *
 * @author Klaus Reimer 
 * @author Jesse Caulfield
 */
public final class UsbPort implements IUsbPort {

  /**
   * The USB hub this port belongs to.
   */
  private final IUsbHub hub;

  /**
   * The port number.
   * <p>
   * Port numbers are 1-based, not 0-based; the first port on a hub has port
   * number 1. There is a maximum of 255 ports on a single hub (so the maximum
   * port number is 255).
   */
  private final int portNumber;

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
    this.portNumber = ByteUtility.unsignedInt(portNumber);
  }

  /**
   * @inherit
   */
  @Override
  public int getPortNumber() {
    return this.portNumber;
  }

  /**
   * @inherit
   */
  @Override
  public IUsbHub getUsbHub() {
    return this.hub;
  }

  /**
   * @inherit
   */
  @Override
  public IUsbDevice getUsbDevice() {
    return this.device;
  }

  /**
   * @inherit
   */
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

  /**
   * Sort numerically on port number.
   *
   * @param o the other instance
   * @return the sort order
   */
  @Override
  public int compareTo(IUsbPort o) {
    return Integer.compare(portNumber, o.getPortNumber());
  }

  @Override
  public String toString() {
    return "Port " + portNumber;
  }

}
