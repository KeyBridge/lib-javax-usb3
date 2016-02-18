/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
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
import javax.usb.utility.DescriptorDumpUtility;

/**
 * A structure representing the Container ID descriptor.
 * <p>
 * This descriptor is documented in section 9.6.2.3 of the USB 3.0
 * specification.
 * <p>
 * All multiple-byte fields, except UUIDs, are represented in host-endian
 * format.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class ContainerIdDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long containerIdDescriptorPointer;

  /**
   * Constructs a new Container Id descriptor which can be passed to the
   * {@link LibUsb#getContainerIdDescriptor(Context, BosDevCapabilityDescriptor, ContainerIdDescriptor)}
   * method.
   */
  public ContainerIdDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.containerIdDescriptorPointer;
  }

  /**
   * Returns the size of this descriptor (in bytes).
   *
   * @return The descriptor size in bytes;
   */
  public native byte bLength();

  /**
   * Returns the descriptor type.
   *
   * @return The descriptor type.
   */
  public native byte bDescriptorType();

  /**
   * Returns the device capability type.
   *
   * @return The device capability type.
   */
  public native byte bDevCapabilityType();

  /**
   * Returns the reserved field.
   *
   * @return The reserved field.
   */
  public native byte bReserved();

  /**
   * Returns the 128 bit UUID.
   *
   * @return The 128 bit UUID.
   */
  public native ByteBuffer containerId();

  /**
   * Returns a dump of this descriptor.
   *
   * @return The descriptor dump.
   */
  public String dump() {
    return String.format("Container ID Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bDevCapabilityType %7d%n"
            + "  bReserved %16d%n"
            + "  ContainerID:%n%s%n",
            this.bLength() & 0xFF,
            this.bDescriptorType() & 0xFF,
            this.bDevCapabilityType() & 0xFF,
            this.bReserved() & 0xFF,
            DescriptorDumpUtility.dump(this.containerId())
            .replaceAll("(?m)^", "    "));
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash += this.bLength();
    hash += this.bDescriptorType();
    hash += this.bDevCapabilityType();
    hash += this.bReserved();
    hash += Objects.hashCode(this.containerId());
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
