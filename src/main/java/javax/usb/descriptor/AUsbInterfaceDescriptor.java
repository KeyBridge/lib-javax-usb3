/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package javax.usb.descriptor;

import java.util.Objects;
import javax.usb.IUsbInterfaceDescriptor;
import javax.usb.enumerated.EDescriptorType;
import javax.usb.enumerated.EUSBClassCode;

/**
 * 9.6.5 Interface Descriptor implementation.
 * <p>
 * Devices report their attributes using descriptors. A descriptor is a data
 * structure with a defined format.
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
 * An interface may include alternate settings that allow the endpoints and/or
 * their characteristics to be varied after the device has been configured. The
 * default setting for an interface is always alternate setting zero. Alternate
 * settings allow a portion of the device configuration to be varied while other
 * interfaces remain in operation. If a configuration has alternate settings for
 * one or more of its interfaces, a separate interface descriptor and its
 * associated endpoint and endpoint companion (when reporting its Enhanced
 * SuperSpeed configuration) descriptors are included for each setting.
 * <p>
 * If a device configuration supported a single interface with two alternate
 * settings, the configuration descriptor would be followed by an interface
 * descriptor with the bInterfaceNumber and bAlternateSetting fields set to zero
 * and then the endpoint and endpoint companion (when reporting its Enhanced
 * SuperSpeed configuration) descriptors for that setting, followed by another
 * interface descriptor and its associated endpoint and endpoint companion
 * descriptors. The second interface descriptor’s bInterfaceNumber field would
 * also be set to zero, but the bAlternateSetting field of the second interface
 * descriptor would be set to one.
 * <p>
 * If an interface uses only the Default Control Pipe, no endpoint descriptors
 * follow the interface descriptor. In this case, the bNumEndpoints field shall
 * be set to zero.
 * <p>
 * An interface descriptor never includes the Default Control Pipe in the number
 * of endpoints.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public abstract class AUsbInterfaceDescriptor extends AUsbDescriptor implements IUsbInterfaceDescriptor {

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
  private final EUSBClassCode bInterfaceClass;

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
   *
   * @param bInterfaceNumber   The interface number.
   * @param bAlternateSetting  The alternate setting number.
   * @param bNumEndpoints      The number of endpoints.
   * @param bInterfaceClass    The interface class. Set to HUB_CLASSCODE =
   *                           (byte) 0x09; for a HUB interface.
   * @param bInterfaceSubClass The interface sub class.
   * @param bInterfaceProtocol The interface protocol.
   * @param iInterface         The interface string descriptor index.
   */
  public AUsbInterfaceDescriptor(final byte bInterfaceNumber,
                                 final byte bAlternateSetting,
                                 final byte bNumEndpoints,
                                 final EUSBClassCode bInterfaceClass,
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
   * Number of this interface. Zero-based value identifying the index in the
   * array of concurrent interfaces supported by this configuration.
   *
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
   *
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
   *
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
   *
   * @return This descriptor's bInterfaceClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public EUSBClassCode bInterfaceClass() {
    return this.bInterfaceClass;
  }

  /**
   * Subclass code (assigned by the USB-IF). These codes are qualified by the
   * value of the bInterfaceClass field.
   * <p>
   * If the bInterfaceClass field is reset to zero, this field must also be
   * reset to zero. If the bInterfaceClass field is not set to FFH, all values
   * are reserved for assignment by the USB-IF.
   *
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
   *
   * @return This descriptor's bInterfaceProtocol.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public byte bInterfaceProtocol() {
    return this.bInterfaceProtocol;
  }

  /**
   * Index of string descriptor describing this interface
   *
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
    hash += 67 * hash + Objects.hashCode(this.bInterfaceClass);
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
            + "  bLength            %8d%n"
            + "  bDescriptorType    %10s%n"
            + "  bInterfaceNumber   %8d%n"
            + "  bAlternateSetting  %8d%n"
            + "  bNumEndpoints      %8d%n"
            + "  bInterfaceClass    %10s%n"
            + "  bInterfaceSubClass %8d%n"
            + "  bInterfaceProtocol %8d%n"
            + "  iInterface         %8d%n",
            bLength() & 0xff,
            EDescriptorType.fromBytecode((byte) (bDescriptorType() & 0xff)),
            bInterfaceNumber() & 0xff,
            bAlternateSetting() & 0xff,
            bNumEndpoints() & 0xff,
            bInterfaceClass(),
            bInterfaceSubClass() & 0xff,
            bInterfaceProtocol() & 0xff,
            iInterface() & 0xff);
  }
}
