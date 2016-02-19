/*
 * Copyright 2016 Caulfield IP Holdings (Caulfield) and affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * Software Code is protected by copyright. Caulfield hereby
 * reserves all rights and copyrights and no license is
 * granted under said copyrights in this Software License Agreement.
 * Caulfield generally licenses software for commercialization
 * pursuant to the terms of either a Standard Software Source Code
 * License Agreement or a Standard Product License Agreement.
 * A copy of these agreements may be obtained by sending a request
 * via email to info@caufield.org.
 */
package javax.usb3.ri;

import java.util.ArrayList;
import java.util.List;
import javax.usb3.descriptor.UsbDeviceDescriptor;
import javax.usb3.enumerated.EUSBClassCode;
import javax.usb3.exception.UsbPlatformException;
import javax.usb3.utility.JNINativeLibraryLoader;
import javax.usb3.utility.UsbExceptionFactory;
import org.junit.Test;
import org.usb4java.*;

/**
 *
 * @author Key Bridge LLC
 */
public class UsbDeviceManagerTest {

  public UsbDeviceManagerTest() {
  }

  @Test
  public void testSomeMethod() throws UsbPlatformException {
    JNINativeLibraryLoader.load();
    Context jniContext = new Context();
    final int init = LibUsb.init(jniContext);

    final List<UsbDeviceId> current = new ArrayList<>();

    // Get device list from libusb and abort if it failed
    final DeviceList deviceList = new DeviceList();
    final int result = LibUsb.getDeviceList(jniContext, deviceList);
    if (result < 0) {
      throw UsbExceptionFactory.createPlatformException("Unable to get USB device list", result);
    }

    try {
      // Iterate over all currently connected devices
      for (final Device libUsbDevice : deviceList) {
        try {
          final UsbDeviceId deviceId = createDeviceId(libUsbDevice);

          final Device parent = LibUsb.getParent(libUsbDevice);
          final UsbDeviceId parentId = parent == null ? null : createDeviceId(parent);
          final int speed = LibUsb.getDeviceSpeed(libUsbDevice);

          if (EUSBClassCode.HUB.equals(deviceId.getDeviceDescriptor().deviceClass())) {
            System.out.println("HUB " + deviceId + " " + parentId + " " + speed);
//            AUsbDevice device = new UsbHub(this, deviceId, parentId, speed, libUsbDevice);
          } else {
            System.out.println("DEV " + deviceId + " " + parentId + " " + speed);
//            AUsbDevice device = new UsbDevice(this, deviceId, parentId, speed, libUsbDevice);
          }
          /**
           * Add new device to global device list.
           */
//            this.devices.put(deviceId, device);

          /**
           * Remember current device as "current".
           */
          current.add(deviceId);
        } catch (UsbPlatformException e) {
          /**
           * Devices which can't be enumerated are ignored.
           */
        }
      }
      /**
       * Retain only the elements in this set that are contained in the
       * specified collection (optional operation). In other words, removes from
       * this set all of its elements that are not contained in the specified
       * collection.
       */
//      this.devices.keySet().retainAll(current);
    } finally {
      LibUsb.freeDeviceList(deviceList, true);
    }
  }

  /**
   * Creates a DeviceId from the specified (JNI) Device instance.
   *
   * @param device The libusb device. Must not be null.
   * @return The device id.
   * @throws UsbPlatformException When device descriptor could not be read from
   *                              the specified device.
   */
  private UsbDeviceId createDeviceId(final Device device) throws UsbPlatformException {
    if (device == null) {
      throw new IllegalArgumentException("Device must be set");
    }
    final int busNumber = LibUsb.getBusNumber(device);
    final int addressNumber = LibUsb.getDeviceAddress(device);
    final int portNumber = LibUsb.getPortNumber(device);
    final DeviceDescriptor deviceDescriptor = new DeviceDescriptor();
    final int result = LibUsb.getDeviceDescriptor(device, deviceDescriptor);
    if (result < 0) {
      throw UsbExceptionFactory.createPlatformException("Unable to get device descriptor for device " + addressNumber + " at bus " + busNumber, result);
    }
    return new UsbDeviceId(busNumber, addressNumber, portNumber, new UsbDeviceDescriptor(deviceDescriptor));
  }
}
