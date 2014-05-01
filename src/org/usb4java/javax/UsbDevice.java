/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.exception.UsbPlatformException;
import org.usb4java.Device;

/**
 * A basic USB device implementation.
 * <p>
 * In the USB4JAVA implementation this is NOT a hub device, but rather an
 * endpoint.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public class UsbDevice extends AUsbDevice {

  /**
   * Constructs a new (non-hub) USB device.
   * <p>
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
  public UsbDevice(final DeviceManager deviceManager, final DeviceId deviceId, final DeviceId parentId, final int speed, final Device device) throws UsbPlatformException {
    super(deviceManager, deviceId, parentId, speed, device);
  }

  /**
   * FALSE. USB4Java UsbDevice instances are never UsbHubs. If the device is a
   * hub it will be identified as a UsbHub implementation.
   * <p>
   * @return FALSE
   */
  @Override
  public boolean isUsbHub() {
    return false;
  }
}
