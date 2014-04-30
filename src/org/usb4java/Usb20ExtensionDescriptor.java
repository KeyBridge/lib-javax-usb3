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

/**
 * A structure representing the USB 2.0 Extension descriptor. This descriptor is
 * documented in section 9.6.2.1 of the USB 3.0 specification.
 * <p>
 * All multiple-byte fields are represented in host-endian format.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Usb20ExtensionDescriptor {

  /**
   * The native pointer to the descriptor structure.
   */
  private long usb20ExtensionDescriptorPointer;

  /**
   * Constructs a new USB 2.0 Extension descriptor which can be passed to the
   * null null null null null   {@link LibUsb#getUsb20ExtensionDescriptor(Context,
     * BosDevCapabilityDescriptor, Usb20ExtensionDescriptor)} method.
   */
  public Usb20ExtensionDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   * <p>
   * @return The native pointer.
   */
  public long getPointer() {
    return this.usb20ExtensionDescriptorPointer;
  }

  /**
   * Returns the size of this descriptor (in bytes).
   * <p>
   * @return The descriptor size in bytes;
   */
  public native byte bLength();

  /**
   * Returns the descriptor type.
   * <p>
   * @return The descriptor type.
   */
  public native byte bDescriptorType();

  /**
   * Returns the device capability type.
   * <p>
   * @return The device capability type.
   */
  public native byte bDevCapabilityType();

  /**
   * Returns the bitmap of supported device level features.
   * <p>
   * @return The supported device level features.
   */
  public native int bmAttributes();

  /**
   * Returns a dump of this descriptor.
   * <p>
   * @return The descriptor dump.
   */
  public String dump() {
    return String.format(
      "USB 2.0 Extension Descriptor:%n"
      + "  bLength %18d%n"
      + "  bDescriptorType %10d%n"
      + "  bDevCapabilityType %7d%n"
      + "  bmAttributes %13s%n",
      this.bLength() & 0xFF,
      this.bDescriptorType() & 0xFF,
      this.bDevCapabilityType() & 0xFF,
      String.format("0x%08x", this.bmAttributes()));
  }

//  @Override
//  public int hashCode() {
//    return new HashCodeBuilder()
//      .append(this.bLength())
//      .append(this.bDescriptorType())
//      .append(this.bDevCapabilityType())
//      .append(this.bmAttributes())
//      .toHashCode();
//  }
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + (int) (this.usb20ExtensionDescriptorPointer ^ (this.usb20ExtensionDescriptorPointer >>> 32));
    hash += this.bLength();
    hash += this.bDescriptorType();
    hash += this.bDevCapabilityType();
    hash += this.bmAttributes();
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
    final Usb20ExtensionDescriptor other = (Usb20ExtensionDescriptor) obj;
    return this.hashCode() == other.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }
}
