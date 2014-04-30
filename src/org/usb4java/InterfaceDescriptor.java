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

/**
 * A structure representing the standard USB interface descriptor.
 * <p>
 * This descriptor is documented in section 9.6.5 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class InterfaceDescriptor {

  /**
   * The native pointer to the descriptor structure.
   */
  private long interfaceDescriptorPointer;

  /**
   * Package-private constructor to prevent manual instantiation. Interface
   * descriptors are always created by JNI.
   */
  InterfaceDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   * <p>
   * @return The native pointer.
   */
  public long getPointer() {
    return this.interfaceDescriptorPointer;
  }

  /**
   * Returns the size of this descriptor (in bytes).
   * <p>
   * @return The size of this descriptor (in bytes).
   */
  public native byte bLength();

  /**
   * Returns the descriptor type. Will have value {@link LibUsb#DT_INTERFACE} in
   * this context.
   * <p>
   * @return The descriptor type.
   */
  public native byte bDescriptorType();

  /**
   * Returns the number of this interface.
   * <p>
   * @return The interface number.
   */
  public native byte bInterfaceNumber();

  /**
   * Returns the value used to select this alternate setting for this interface.
   * <p>
   * @return The alternate setting value.
   */
  public native byte bAlternateSetting();

  /**
   * Returns the number of endpoints used by this interface (excluding the
   * control endpoint).
   * <p>
   * @return The number of endpoints.
   */
  public native byte bNumEndpoints();

  /**
   * Returns the USB-IF class code for this interface. See LibUSB.CLASS_*
   * constants.
   * <p>
   * @return The USB-IF class code.
   */
  public native byte bInterfaceClass();

  /**
   * Returns the USB-IF subclass code for this interface, qualified by the
   * bInterfaceClass value.
   * <p>
   * @return The USB-IF subclass code.
   */
  public native byte bInterfaceSubClass();

  /**
   * Returns the USB-IF protocol code for this interface, qualified by the
   * bInterfaceClass and bInterfaceSubClass values.
   * <p>
   * @return The USB-IF protocol code.
   */
  public native byte bInterfaceProtocol();

  /**
   * Returns the index of string descriptor describing this interface.
   * <p>
   * @return The string descriptor index.
   */
  public native byte iInterface();

  /**
   * Returns the array with endpoints.
   * <p>
   * @return The array with endpoints.
   */
  public native EndpointDescriptor[] endpoint();

  /**
   * Extra descriptors.
   * <p>
   * If libusb encounters unknown interface descriptors, it will store them
   * here, should you wish to parse them.
   * <p>
   * @return The extra descriptors.
   */
  public native ByteBuffer extra();

  /**
   * Length of the extra descriptors, in bytes.
   * <p>
   * @return The extra descriptors length.
   */
  public native int extraLength();

  /**
   * Returns a dump of this descriptor.
   * <p>
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

//  @Override
//  public int hashCode() {
//    return new HashCodeBuilder()
//      .append(this.bLength())
//      .append(this.bDescriptorType())
//      .append(this.bInterfaceNumber())
//      .append(this.bAlternateSetting())
//      .append(this.bNumEndpoints())
//      .append(this.bInterfaceClass())
//      .append(this.bInterfaceSubClass())
//      .append(this.bInterfaceProtocol())
//      .append(this.iInterface())
//      .append(this.endpoint())
//      .append(this.extra())
//      .append(this.extraLength())
//      .toHashCode();
//  }
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
