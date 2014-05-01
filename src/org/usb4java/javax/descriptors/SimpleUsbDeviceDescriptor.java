/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax.descriptors;

import javax.usb.UsbDeviceDescriptor;
import org.usb4java.libusbutil.DescriptorUtils;
import org.usb4java.DeviceDescriptor;

/**
 * Simple USB device descriptor.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class SimpleUsbDeviceDescriptor extends SimpleUsbDescriptor implements UsbDeviceDescriptor {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The USB specification version number.
   */
  private final short bcdUSB;

  /**
   * The device class.
   */
  private final byte bDeviceClass;

  /**
   * The device sub class.
   */
  private final byte bDeviceSubClass;

  /**
   * The device protocol.
   */
  private final byte bDeviceProtocol;

  /**
   * The maximum packet size for endpoint zero.
   */
  private final byte bMaxPacketSize0;

  /**
   * The vendor ID.
   */
  private final short idVendor;

  /**
   * The product ID.
   */
  private final short idProduct;

  /**
   * The device release number.
   */
  private final short bcdDevice;

  /**
   * The manufacturer string descriptor index.
   */
  private final byte iManufacturer;

  /**
   * The product string descriptor index.
   */
  private final byte iProduct;

  /**
   * The serial number string descriptor index.
   */
  private final byte iSerialNumber;

  /**
   * The number of configurations.
   */
  private final byte bNumConfigurations;

  /**
   * Constructor.
   * <p>
   * @param bLength            The descriptor length.
   * @param bDescriptorType    The descriptor type.
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
  public SimpleUsbDeviceDescriptor(final byte bLength,
                                   final byte bDescriptorType, final short bcdUSB,
                                   final byte bDeviceClass, final byte bDeviceSubClass,
                                   final byte bDeviceProtocol, final byte bMaxPacketSize0,
                                   final short idVendor, final short idProduct, final short bcdDevice,
                                   final byte iManufacturer, final byte iProduct,
                                   final byte iSerialNumber, final byte bNumConfigurations) {
    super(bLength, bDescriptorType);
    this.bcdUSB = bcdUSB;
    this.bDeviceClass = bDeviceClass;
    this.bDeviceSubClass = bDeviceSubClass;
    this.bDeviceProtocol = bDeviceProtocol;
    this.bMaxPacketSize0 = bMaxPacketSize0;
    this.idVendor = idVendor;
    this.idProduct = idProduct;
    this.bcdDevice = bcdDevice;
    this.iManufacturer = iManufacturer;
    this.iProduct = iProduct;
    this.iSerialNumber = iSerialNumber;
    this.bNumConfigurations = bNumConfigurations;
  }

  /**
   * Construct from a libusb4java device descriptor.
   * <p>
   * @param descriptor The descriptor from which to copy the data.
   */
  public SimpleUsbDeviceDescriptor(final DeviceDescriptor descriptor) {
    this(descriptor.bLength(),
         descriptor.bDescriptorType(),
         descriptor.bcdUSB(),
         descriptor.bDeviceClass(),
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

  @Override
  public short bcdUSB() {
    return this.bcdUSB;
  }

  @Override
  public byte bDeviceClass() {
    return this.bDeviceClass;
  }

  @Override
  public byte bDeviceSubClass() {
    return this.bDeviceSubClass;
  }

  @Override
  public byte bDeviceProtocol() {
    return this.bDeviceProtocol;
  }

  @Override
  public byte bMaxPacketSize0() {
    return this.bMaxPacketSize0;
  }

  @Override
  public short idVendor() {
    return this.idVendor;
  }

  @Override
  public short idProduct() {
    return this.idProduct;
  }

  @Override
  public short bcdDevice() {
    return this.bcdDevice;
  }

  @Override
  public byte iManufacturer() {
    return this.iManufacturer;
  }

  @Override
  public byte iProduct() {
    return this.iProduct;
  }

  @Override
  public byte iSerialNumber() {
    return this.iSerialNumber;
  }

  @Override
  public byte bNumConfigurations() {
    return this.bNumConfigurations;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash += 61 * hash + super.hashCode();
    hash += 61 * hash + this.bcdUSB;
    hash += 61 * hash + this.bDeviceClass;
    hash += 61 * hash + this.bDeviceSubClass;
    hash += 61 * hash + this.bDeviceProtocol;
    hash += 61 * hash + this.bMaxPacketSize0;
    hash += 61 * hash + this.idVendor;
    hash += 61 * hash + this.idProduct;
    hash += 61 * hash + this.bcdDevice;
    hash += 61 * hash + this.iManufacturer;
    hash += 61 * hash + this.iProduct;
    hash += 61 * hash + this.iSerialNumber;
    hash += 61 * hash + this.bNumConfigurations;
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
    return this.hashCode() == obj.hashCode();
  }

  @Override
  public String toString() {
    return String.format(
      "Device Descriptor:%n"
      + "  bLength %18d%n"
      + "  bDescriptorType %10d%n"
      + "  bcdUSB %19s%n"
      + "  bDeviceClass %13d %s%n"
      + "  bDeviceSubClass %10d%n"
      + "  bDeviceProtocol %10d%n"
      + "  bMaxPacketSize0 %10d%n"
      + "  idVendor %17s%n"
      + "  idProduct %16s%n"
      + "  bcdDevice %16s%n"
      + "  iManufacturer %12d%n"
      + "  iProduct %17d%n"
      + "  iSerial %18d%n"
      + "  bNumConfigurations %7d%n",
      bLength() & 0xff,
      bDescriptorType() & 0xff,
      DescriptorUtils.decodeBCD(bcdUSB()),
      bDeviceClass() & 0xff,
      DescriptorUtils.getUSBClassName(bDeviceClass()),
      bDeviceSubClass() & 0xff,
      bDeviceProtocol() & 0xff,
      bMaxPacketSize0() & 0xff,
      String.format("0x%04x", idVendor() & 0xffff),
      String.format("0x%04x", idProduct() & 0xffff),
      DescriptorUtils.decodeBCD(bcdDevice()),
      iManufacturer() & 0xff,
      iProduct() & 0xff,
      iSerialNumber() & 0xff,
      bNumConfigurations() & 0xff);
  }
}
