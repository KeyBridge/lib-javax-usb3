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

import javax.usb3.exception.UsbPlatformException;
import org.usb4java.Device;

/**
 * A basic (non-hub) USB device implementation. USB devices present a standard
 * USB interface.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public class UsbDevice extends AUsbDevice {

  /**
   * Constructs a new (non-hub) USB device.
   *
   * @param deviceManager The USB device manager which is responsible for this *
   *                      device.
   * @param deviceId      The device id. Must not be null.
   * @param parentId      The parent device id. May be null if this device has
   *                      no parent (Because it is a root device).
   * @param speed         The device USB port speed.
   * @param device        The libusb native device reference. This reference is
   *                      only valid during the constructor execution, so don't
   *                      store it in a property or something like that.
   * @throws UsbPlatformException When device configuration could not be read.
   */
  public UsbDevice(final UsbDeviceManager deviceManager,
                   final UsbDeviceId deviceId,
                   final UsbDeviceId parentId,
                   final int speed,
                   final Device device) throws UsbPlatformException {
    super(deviceManager, deviceId, parentId, speed, device);
  }

  /**
   * @inherit
   *
   * @return FALSE. UsbDevice instances are never UsbHubs. If the device is a
   *         hub it will be identified as a UsbHub implementation.
   */
  @Override
  public boolean isUsbHub() {
    return false;
  }
}
