/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package javax.usb.descriptor;

import javax.usb.enumerated.EUSBClassCode;
import org.usb4java.InterfaceDescriptor;

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
public final class UsbInterfaceDescriptor extends AUsbInterfaceDescriptor {

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
  public UsbInterfaceDescriptor(final byte bInterfaceNumber,
                                final byte bAlternateSetting,
                                final byte bNumEndpoints,
                                final EUSBClassCode bInterfaceClass,
                                final byte bInterfaceSubClass,
                                final byte bInterfaceProtocol,
                                final byte iInterface) {
    super(bInterfaceNumber,
          bAlternateSetting,
          bNumEndpoints,
          bInterfaceClass,
          bInterfaceSubClass,
          bInterfaceProtocol,
          iInterface);
  }

  /**
   * Construct a new UsbInterfaceDescriptor instance from a libusb4java
   * interface descriptor.
   *
   * @param descriptor The descriptor from which to copy the data.
   */
  public UsbInterfaceDescriptor(final InterfaceDescriptor descriptor) {
    super(descriptor.bInterfaceNumber(),
          descriptor.bAlternateSetting(),
          descriptor.bNumEndpoints(),
          EUSBClassCode.fromByteCode(descriptor.bInterfaceClass()),
          descriptor.bInterfaceSubClass(),
          descriptor.bInterfaceProtocol(),
          descriptor.iInterface());
  }

}
