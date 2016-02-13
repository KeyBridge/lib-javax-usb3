/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://libusb.info/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2013 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2013 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2013 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2013 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012-2013 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */
package org.usb4java;

import java.nio.ByteBuffer;
import java.util.Objects;
import javax.usb.utility.DescriptorUtils;

/**
 * A structure representing the standard USB interface descriptor.
 * <p>
 * The interface descriptor describes a specific interface within a
 * configuration. A configuration provides one or more interfaces, each with
 * zero or more endpoint descriptors. When a configuration supports more than
 * one interface, the endpoint descriptors for a particular interface follow the
 * interface descriptor in the data returned by the GetConfiguration() request.
 * As mentioned earlier in this chapter, Enhanced SuperSpeed devices shall
 * return Endpoint Companion descriptors for each of the endpoints in that
 * interface to return additional information about its endpoint capabilities.
 * The Endpoint Companion descriptor shall immediately follow the endpoint
 * descriptor it is associated with in the configuration information. An
 * interface descriptor is always returned as part of a configuration
 * descriptor. Interface descriptors cannot be directly accessed with a
 * GetDescriptor() or SetDescriptor() request.
 * <p>
 * An interface may include alternate settings that allow the endpoints and/or
 * their characteristics to be varied after the device has been configured. The
 * default setting for an interface is always alternate setting zero. The
 * SetInterface() request is used to select an alternate setting or to return to
 * the default setting. The GetInterface() request returns the selected
 * alternate setting.
 * <p>
 * Alternate settings allow a portion of the device configuration to be varied
 * while other interfaces remain in operation. If a configuration has alternate
 * settings for one or more of its interfaces, a separate interface descriptor
 * and its associated endpoint and endpoint companion (when reporting its
 * Enhanced SuperSpeed configuration) descriptors are included for each setting.
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
 * be set to zero. An interface descriptor never includes the Default Control
 * Pipe in the number of endpoints.
 * <p>
 * This descriptor is documented in section 9.6.5 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class InterfaceDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long interfaceDescriptorPointer;

  /**
   * Package-private constructor to prevent manual instantiation. Interface
   * descriptors are always created by JNI.
   */
  protected InterfaceDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.interfaceDescriptorPointer;
  }

  /**
   * Returns the size of this descriptor (in bytes).
   *
   * @return The size of this descriptor (in bytes).
   */
  public native byte bLength();

  /**
   * Returns the descriptor type. Will have value {@link LibUsb#DT_INTERFACE} in
   * this context.
   *
   * @return The descriptor type.
   */
  public native byte bDescriptorType();

  /**
   * Returns the number of this interface.
   *
   * @return The interface number.
   */
  public native byte bInterfaceNumber();

  /**
   * Returns the value used to select this alternate setting for this interface.
   *
   * @return The alternate setting value.
   */
  public native byte bAlternateSetting();

  /**
   * Returns the number of endpoints used by this interface (excluding the
   * control endpoint).
   *
   * @return The number of endpoints.
   */
  public native byte bNumEndpoints();

  /**
   * Returns the USB-IF class code for this interface. See LibUSB.CLASS_*
   * constants.
   *
   * @return The USB-IF class code.
   */
  public native byte bInterfaceClass();

  /**
   * Returns the USB-IF subclass code for this interface, qualified by the
   * bInterfaceClass value.
   *
   * @return The USB-IF subclass code.
   */
  public native byte bInterfaceSubClass();

  /**
   * Returns the USB-IF protocol code for this interface, qualified by the
   * bInterfaceClass and bInterfaceSubClass values.
   *
   * @return The USB-IF protocol code.
   */
  public native byte bInterfaceProtocol();

  /**
   * Returns the index of string descriptor describing this interface.
   *
   * @return The string descriptor index.
   */
  public native byte iInterface();

  /**
   * Returns the array with endpoints.
   *
   * @return The array with endpoints.
   */
  public native EndpointDescriptor[] endpoint();

  /**
   * Extra descriptors.
   * <p>
   * If libusb encounters unknown interface descriptors, it will store them
   * here, should you wish to parse them.
   *
   * @return The extra descriptors.
   */
  public native ByteBuffer extra();

  /**
   * Length of the extra descriptors, in bytes.
   *
   * @return The extra descriptors length.
   */
  public native int extraLength();

  /**
   * Returns a dump of this descriptor.
   *
   * @return The descriptor dump.
   */
  public String dump() {
    final StringBuilder builder = new StringBuilder();

    builder.append(String.format(
            "%s"
            + "  extralen %17d%n"
            + "  extra:%n"
            + "%s",
            DescriptorUtils.dump(this),
            this.extraLength(),
            DescriptorUtils.dump(this.extra()).replaceAll("(?m)^", "    ")));

    for (final EndpointDescriptor epDesc : this.endpoint()) {
      builder.append(String.format("%n")).append(epDesc.dump());
    }

    return builder.toString();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 83 * hash + (int) (this.interfaceDescriptorPointer ^ (this.interfaceDescriptorPointer >>> 32));
    hash += bLength();
    hash += bDescriptorType();
    hash += bInterfaceNumber();
    hash += bAlternateSetting();
    hash += bNumEndpoints();
    hash += bInterfaceClass();
    hash += bInterfaceSubClass();
    hash += bInterfaceProtocol();
    hash += iInterface();
    hash += extraLength();
    hash += Objects.hash(endpoint(), extra());
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
    final InterfaceDescriptor other = (InterfaceDescriptor) obj;
    return this.hashCode() == other.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }
}
