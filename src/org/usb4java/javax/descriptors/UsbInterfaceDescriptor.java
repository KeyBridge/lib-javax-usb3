/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax.descriptors;

import javax.usb.IUsbInterfaceDescriptor;
import javax.usb.ri.enumerated.EDescriptorType;
import javax.usb.ri.enumerated.EUSBClassCode;
import org.usb4java.InterfaceDescriptor;

/**
 * 9.6.5 Interface Standard USB Descriptor Definition
 * <p>
 * The interface descriptor describes a specific interface within a
 * configuration. A configuration provides one or more interfaces, each with
 * zero or more endpoint descriptors describing a unique set of endpoints within
 * the configuration. When a configuration supports more than one interface, the
 * endpoint descriptors for a particular interface follow the interface
 * descriptor in the data returned by the GetConfiguration() request. An
 * interface descriptor is always returned as part of a configuration
 * descriptor. Interface descriptors cannot be directly accessed with a
 * GetDescriptor() or SetDescriptor() request.
 * <p>
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
public final class UsbInterfaceDescriptor extends AUsbDescriptor implements IUsbInterfaceDescriptor {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The interface number.
   */
  private final byte bInterfaceNumber;

  /**
   * The alternate setting number.
   */
  private final byte bAlternateSetting;

  /**
   * The number of endpoints.
   */
  private final byte bNumEndpoints;

  /**
   * The interface class.
   */
  private final byte bInterfaceClass;

  /**
   * The interface sub class.
   */
  private final byte bInterfaceSubClass;

  /**
   * The interface protocol.
   */
  private final byte bInterfaceProtocol;

  /**
   * The interface string descriptor index.
   */
  private final byte iInterface;

  /**
   * Construct a new UsbInterfaceDescriptor instance.
   * <p>
   * @param bInterfaceNumber   The interface number.
   * @param bAlternateSetting  The alternate setting number.
   * @param bNumEndpoints      The number of endpoints.
   * @param bInterfaceClass    The interface class. Set to HUB_CLASSCODE =
   *                           (byte) 0x09; for a HUB interface.
   * @param bInterfaceSubClass The interface sub class.
   * @param bInterfaceProtocol The interface protocol.
   * @param iInterface         The interface string descriptor index.
   */
  public UsbInterfaceDescriptor(final byte bInterfaceNumber,
                                final byte bAlternateSetting,
                                final byte bNumEndpoints,
                                final byte bInterfaceClass,
                                final byte bInterfaceSubClass,
                                final byte bInterfaceProtocol,
                                final byte iInterface) {
    super(EDescriptorType.INTERFACE);
    this.bInterfaceNumber = bInterfaceNumber;
    this.bAlternateSetting = bAlternateSetting;
    this.bNumEndpoints = bNumEndpoints;
    this.bInterfaceClass = bInterfaceClass;
    this.bInterfaceSubClass = bInterfaceSubClass;
    this.bInterfaceProtocol = bInterfaceProtocol;
    this.iInterface = iInterface;
  }

  /**
   * Construct a new UsbInterfaceDescriptor instance from a libusb4java
   * interface descriptor.
   * <p>
   * @param descriptor The descriptor from which to copy the data.
   */
  public UsbInterfaceDescriptor(final InterfaceDescriptor descriptor) {
    this(descriptor.bInterfaceNumber(),
         descriptor.bAlternateSetting(),
         descriptor.bNumEndpoints(),
         descriptor.bInterfaceClass(),
         descriptor.bInterfaceSubClass(),
         descriptor.bInterfaceProtocol(),
         descriptor.iInterface());
  }

  /**
   * Number of this interface. Zero-based value identifying the index in the
   * array of concurrent interfaces supported by this configuration.
   * <p>
   * @return This descriptor's bInterfaceNumber.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public byte bInterfaceNumber() {
    return this.bInterfaceNumber;
  }

  /**
   * Value used to select this alternate setting for the interface identified in
   * the prior field
   * <p>
   * @return This descriptor's bAlternateSetting.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public byte bAlternateSetting() {
    return this.bAlternateSetting;
  }

  /**
   * Number of endpoints used by this interface (excluding endpoint zero). If
   * this value is zero, this interface only uses the Default Control Pipe.
   * <p>
   * @return This descriptor's bNumEndpoints.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public byte bNumEndpoints() {
    return this.bNumEndpoints;
  }

  /**
   * Class code (assigned by the USB-IF).
   * <p>
   * A value of zero is reserved for future standardization.
   * <p>
   * If this field is set to FFH, the interface class is vendor-specific. All
   * other values are reserved for assignment by the USB-IF.
   * <p>
   * @return This descriptor's bInterfaceClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public EUSBClassCode bInterfaceClass() {
    return EUSBClassCode.fromByteCode(bInterfaceClass);
  }

  /**
   * Subclass code (assigned by the USB-IF). These codes are qualified by the
   * value of the bInterfaceClass field.
   * <p>
   * If the bInterfaceClass field is reset to zero, this field must also be
   * reset to zero. If the bInterfaceClass field is not set to FFH, all values
   * are reserved for assignment by the USB-IF.
   * <p>
   * @return This descriptor's bInterfaceSubClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public byte bInterfaceSubClass() {
    return this.bInterfaceSubClass;
  }

  /**
   * Protocol code (assigned by the USB). These codes are qualified by the value
   * of the bInterfaceClass and the bInterfaceSubClass fields. If an interface
   * supports class-specific requests, this code identifies the protocols that
   * the device uses as defined by the specification of the device class.
   * <p>
   * If this field is reset to zero, the device does not use a class-specific
   * protocol on this interface.
   * <p>
   * If this field is set to FFH, the device uses a vendor-specific protocol for
   * this interface.
   * <p>
   * @return This descriptor's bInterfaceProtocol.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public byte bInterfaceProtocol() {
    return this.bInterfaceProtocol;
  }

  /**
   * Index of string descriptor describing this interface
   * <p>
   * @return This descriptor's iInterface.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public byte iInterface() {
    return this.iInterface;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += 67 * hash + super.hashCode();
    hash += 67 * hash + this.bInterfaceNumber;
    hash += 67 * hash + this.bAlternateSetting;
    hash += 67 * hash + this.bNumEndpoints;
    hash += 67 * hash + this.bInterfaceClass;
    hash += 67 * hash + this.bInterfaceSubClass;
    hash += 67 * hash + this.bInterfaceProtocol;
    hash += 67 * hash + this.iInterface;
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
      "Interface Descriptor:%n"
      + "  bLength %18d%n"
      + "  bDescriptorType %10d%n"
      + "  bInterfaceNumber %9d%n"
      + "  bAlternateSetting %8d%n"
      + "  bNumEndpoints %12d%n"
      + "  bInterfaceClass %10d %s%n"
      + "  bInterfaceSubClass %7d%n"
      + "  bInterfaceProtocol %7d%n"
      + "  iInterface %15d%n",
      bLength() & 0xff,
      bDescriptorType() & 0xff,
      bInterfaceNumber() & 0xff,
      bAlternateSetting() & 0xff,
      bNumEndpoints() & 0xff,
      bInterfaceClass().getByteCode(),
      bInterfaceClass(),
      bInterfaceSubClass() & 0xff,
      bInterfaceProtocol() & 0xff,
      iInterface() & 0xff);
  }
}
