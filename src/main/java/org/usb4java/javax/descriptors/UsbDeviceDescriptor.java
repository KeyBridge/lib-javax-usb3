/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax.descriptors;

import javax.usb.ri.descriptor.AUsbDeviceDescriptor;
import javax.usb.ri.enumerated.EUSBClassCode;
import org.usb4java.DeviceDescriptor;

/**
 * 9.6.1 Device Descriptor implementation.
 * <p>
 * Devices report their attributes using descriptors. A descriptor is a data
 * structure with a defined format.
 * <p>
 * A device descriptor describes general information about a USB device. It
 * includes information that applies globally to the device and all of the
 * device’s configurations. A USB device has only one device descriptor.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class UsbDeviceDescriptor extends AUsbDeviceDescriptor {

  /**
   * Construct a new UsbDeviceDescriptor instance.
   * <p>
   * A device descriptor describes general information about a USB device. It
   * includes information that applies globally to the device and all of the
   * device’s configurations. A USB device has only one device descriptor.
   * <p>
   * @param bcdUSB             The USB specification version number.
   * @param bDeviceClass       The device class.
   * @param bDeviceSubClass    The device sub class.
   * @param bDeviceProtocol    The device protocol.
   * @param bMaxPacketSize0    The maximum packet size for endpoint zero.
   * @param idVendor           The vendor ID.
   * @param idProduct          The product ID.
   * @param bcdDevice          The device release number.
   * @param iManufacturer      The manufacturer string descriptor index.
   * @param iProduct           The product string descriptor index.
   * @param iSerialNumber      The serial number string descriptor index.
   * @param bNumConfigurations The number of configurations.
   */
  public UsbDeviceDescriptor(final short bcdUSB,
                             final EUSBClassCode bDeviceClass,//  final byte bDeviceClass,
                             final byte bDeviceSubClass,
                             final byte bDeviceProtocol,
                             final byte bMaxPacketSize0,
                             final short idVendor,
                             final short idProduct,
                             final short bcdDevice,
                             final byte iManufacturer,
                             final byte iProduct,
                             final byte iSerialNumber,
                             final byte bNumConfigurations) {
    super(bcdUSB,
          bDeviceClass,
          bDeviceSubClass,
          bDeviceProtocol,
          bMaxPacketSize0,
          idVendor,
          idProduct,
          bcdDevice,
          iManufacturer,
          iProduct,
          iSerialNumber,
          bNumConfigurations);
  }

  /**
   * Construct a new UsbDeviceDescriptor instance from a libusb4java device
   * descriptor.
   * <p>
   * @param descriptor The descriptor from which to copy the data.
   */
  public UsbDeviceDescriptor(final DeviceDescriptor descriptor) {
    super(descriptor.bcdUSB(),
          EUSBClassCode.fromByteCode(descriptor.bDeviceClass()),
          descriptor.bDeviceSubClass(),
          descriptor.bDeviceProtocol(),
          descriptor.bMaxPacketSize0(),
          descriptor.idVendor(),
          descriptor.idProduct(),
          descriptor.bcdDevice(),
          descriptor.iManufacturer(),
          descriptor.iProduct(),
          descriptor.iSerialNumber(),
          descriptor.bNumConfigurations());
  }

}
