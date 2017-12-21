/*
 * Copyright (C) 2013 Klaus Reimer
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
package javax.usb3.ri;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.usb3.IUsbDeviceDescriptor;
import javax.usb3.database.UsbDeviceDescription;
import javax.usb3.database.UsbRepositoryDatabase;

/**
 * A Unique USB Device ID. This encapsulates a USB Device's BUS location to
 * uniquely identify the device without needing to know or inspect the internal
 * configuration of the device.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class UsbDeviceId implements Comparable<UsbDeviceId> {

  /**
   * The USB bus number that this device is connected to.
   * <p>
   * A {@code BUS} typically corresponds to a physical USB port on a host
   * computer system. For example, a computer having eight physical ports may
   * report this as having eight buses. Note that bus numbering and port
   * numbering need not (and typically do not) match.
   */
  private final int busNumber;
  /**
   * The USB port number that this device is connected to. 0 if unknown.
   * <p>
   * A {@code PORT} is created in software and corresponds a physical access
   * port on a computer system (i.e. a BUS) or a hub.
   */
  private final int portNumber;
  /**
   * The USB device address.
   * <p>
   * This is a simple integer counting the devices on a bus, beginning at 1. For
   * example, if there are two devices on a bus then one device will have
   * address {@code 1} and the other will have address {@code 2}. Very simple.
   */
  private final int deviceAddress;

  /**
   * The device descriptor. Devices report their attributes using descriptors. A
   * descriptor is a data structure with a defined format.
   */
  private final IUsbDeviceDescriptor deviceDescriptor;

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
                     final IUsbDeviceDescriptor deviceDescriptor) {
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
  public IUsbDeviceDescriptor getDeviceDescriptor() {
    return this.deviceDescriptor;
  }

  /**
   * Look up and retrieve the USB device description for this vendor and device
   * ID. This is useful to display human-readable device vendor and product
   * information.
   *
   * @return the USB device description. NULL if not found in the database.
   */
  public UsbDeviceDescription getUsbDeviceDescription() {
    try {
      return UsbRepositoryDatabase.lookup(this.deviceDescriptor.idVendor(), this.deviceDescriptor.idProduct());
    } catch (Exception ex) {
      Logger.getLogger(UsbDeviceId.class.getName()).log(Level.FINE, "USB ID not found in the database: {0}:{1}", new Object[]{this.deviceDescriptor.idVendor(), this.deviceDescriptor.idProduct()});
      return null;
    }
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 59 * hash + this.busNumber;
    hash = 59 * hash + this.deviceAddress;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final UsbDeviceId other = (UsbDeviceId) obj;
    if (this.busNumber != other.busNumber) {
      return false;
    }
    return this.deviceAddress == other.deviceAddress;
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
    UsbDeviceDescription description = getUsbDeviceDescription();
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

  /**
   * Numerical sorting on busNumber : deviceAddress.
   *
   * @param o the other device id
   * @return the sort order.
   */
  @Override
  public int compareTo(UsbDeviceId o) {
    return busNumber == o.getBusNumber()
           ? Integer.compare(deviceAddress, o.getDeviceAddress())
           : Integer.compare(busNumber, o.busNumber);
  }

}
