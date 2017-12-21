/*
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
 * Copyright 2013 Klaus Reimer
 * Copyright 2014-2016 Jesse Caulfield
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.usb4java;

import java.nio.ByteBuffer;
import java.util.Objects;
import javax.usb3.IUsbDescriptor;
import javax.usb3.enumerated.EDescriptorType;
import javax.usb3.utility.DescriptorDumpUtility;

/**
 * USB 3.1. A Binary Device Object Store (BOS) Container ID descriptor.
 * <p>
 * See USB 3.1 section 9.6.2.3.
 * <p>
 * All multiple-byte fields, except UUIDs, are represented in host-endian
 * format.
 * <p>
 * Container ID descriptor which shall be implemented by all USB hubs, and is
 * optional for other devices. If this descriptor is provided when operating in
 * one mode, it shall be provided when operating in any mode. This descriptor
 * may be used by a host in order to identify a unique device instance across
 * all operating modes. If a device can also connect to a host through other
 * technologies, the same Container ID value contained in this descriptor should
 * also be provided over those other technologies in a technology specific
 * manner.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class ContainerIdDescriptor implements IUsbDescriptor {
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
   * {@inheritDoc}
   */
  @Override
  public native byte bLength();

  /**
   * {@inheritDoc}
   */
  @Override
  public EDescriptorType descriptorType() {
    return EDescriptorType.fromBytecode(bDescriptorType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
   * Returns a 128-bit (16-digit) number that is unique to a device instance
   * that is used to uniquely identify the device instance across all modes of
   * operation. This same value may be provided over other technologies as well
   * to allow the host to identify the device independent of means of
   * connectivity. Refer to IETF RFC 4122 for details on generation of a UUID.
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
