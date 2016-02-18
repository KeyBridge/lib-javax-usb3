/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
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

import java.io.Serializable;
import javax.usb.database.UsbDescription;
import javax.usb.database.UsbDescriptionUtility;
import javax.usb.descriptor.UsbDeviceDescriptor;

/**
 * A Unique USB Device ID. This encapsulates a USB Device's BUS location to
 * uniquely identify the device without needing to know or inspect the internal
 * configuration of the device.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class UsbDeviceId implements Serializable {

  /**
   * The serial versionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The bus number.
   */
  private final int busNumber;

  /**
   * The device address.
   */
  private final int deviceAddress;

  /**
   * The port this device is connected to. 0 if unknown.
   */
  private final int portNumber;

  /**
   * The device descriptor.
   */
  private final UsbDeviceDescriptor deviceDescriptor;

  /**
   * Constructs a new device id.
   *
   * @param busNumber        The number of the bus the device is connected to.
   * @param deviceAddress    The address of the device.
   * @param portNumber       The number of the port the device is connected to.
   *                         0 if unknown.
   * @param deviceDescriptor The device descriptor. Must not be null.
   */
  public UsbDeviceId(final int busNumber,
                     final int deviceAddress,
                     final int portNumber,
                     final UsbDeviceDescriptor deviceDescriptor) {
    if (deviceDescriptor == null) {
      throw new IllegalArgumentException("deviceDescriptor must be set");
    }
    this.busNumber = busNumber;
    this.portNumber = portNumber;
    this.deviceAddress = deviceAddress;
    this.deviceDescriptor = deviceDescriptor;
  }

  /**
   * Returns the bus number.
   *
   * @return The bus number.
   */
  public int getBusNumber() {
    return this.busNumber;
  }

  /**
   * Returns the device address.
   *
   * @return The device address.
   */
  public int getDeviceAddress() {
    return this.deviceAddress;
  }

  /**
   * Returns the number of the port the device is connected to.
   *
   * @return The port number or 0 if unknown.
   */
  public int getPortNumber() {
    return this.portNumber;
  }

  /**
   * Returns the device descriptor.
   *
   * @return The device descriptor. Never null.
   */
  public UsbDeviceDescriptor getDeviceDescriptor() {
    return this.deviceDescriptor;
  }

  /**
   * Look up and retrieve the USB device description for this vendor and device
   * ID.
   *
   * @return the USB description.
   */
  public UsbDescription getUsbDescription() {
    try {
      return UsbDescriptionUtility.lookup(this.deviceDescriptor.idVendor(), this.deviceDescriptor.idProduct());
    } catch (Exception ex) {
      return null;
    }
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash += 67 * hash + this.busNumber;
    hash += 67 * hash + this.deviceAddress;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    return hashCode() == obj.hashCode();
  }

  /**
   * Checks if the specified two device IDs are equal. They are also equal if
   * they are both null.
   *
   * @param a The first device ID.
   * @param b The second device ID.
   * @return True if the device IDs are equal, false if not.
   */
  public static boolean equals(final UsbDeviceId a, final UsbDeviceId b) {
    if (a == null && b == null) {
      return true;
    }
    if (a == null || b == null) {
      return false;
    }
    return a.equals(b);
  }

  @Override
  public String toString() {
    UsbDescription description = getUsbDescription();
    if (description != null) {
      return String.format("Bus %03d Device %03d: %s",
                           this.busNumber,
                           this.deviceAddress,
                           description);
    } else {
      return String.format("Bus %03d Device %03d: %04x:%04x",
                           this.busNumber,
                           this.deviceAddress,
                           this.deviceDescriptor.idVendor(),
                           this.deviceDescriptor.idProduct());
    }
  }

}
