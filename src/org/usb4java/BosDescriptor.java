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

import java.util.Objects;

/**
 * A structure representing the Binary Device Object Store (BOS) descriptor.
 * <p>
 * This descriptor is documented in section 9.6.2 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class BosDescriptor {

  /**
   * The native pointer to the descriptor structure.
   */
  private long bosDescriptorPointer;

  /**
   * Constructs a new BOS descriptor which can be passed to the
   * {@link LibUsb#getBosDescriptor(DeviceHandle, BosDescriptor)} method.
   */
  public BosDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   * <p>
   * @return The native pointer.
   */
  public long getPointer() {
    return this.bosDescriptorPointer;
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
   * Returns the length of this descriptor and all of its sub descriptors.
   * <p>
   * @return The total descriptor length.
   */
  public native short wTotalLength();

  /**
   * Returns the number of separate device capability descriptors in the BOS.
   * <p>
   * @return The number of device capability descriptors.
   */
  public native byte bNumDeviceCaps();

  /**
   * Returns the array with the device capability descriptors.
   * <p>
   * @return The array with device capability descriptors.
   */
  public native BosDevCapabilityDescriptor[] devCapability();

  /**
   * Returns a dump of this descriptor.
   * <p>
   * @return The descriptor dump.
   */
  public String dump() {
    final StringBuilder builder = new StringBuilder();

    builder.append(String.format(
      "BOS Descriptor:%n"
      + "  bLength %18d%n"
      + "  bDescriptorType %10d%n"
      + "  wTotalLength %13s%n"
      + "  bNumDeviceCaps %11s%n",
      this.bLength() & 0xFF,
      this.bDescriptorType() & 0xFF,
      this.wTotalLength() & 0xFFFF,
      this.bNumDeviceCaps() & 0xFF));

    for (final BosDevCapabilityDescriptor descriptor : this.devCapability()) {
      builder.append(descriptor.dump().replaceAll("(?m)^", "  "));
    }

    return builder.toString();
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash += this.bLength();
    hash += this.bDescriptorType();
    hash += this.wTotalLength();
    hash += this.bNumDeviceCaps();
    hash += Objects.hashCode(this.devCapability());
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
    final BosDevCapabilityDescriptor other = (BosDevCapabilityDescriptor) obj;
    return this.hashCode() == other.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }
}
