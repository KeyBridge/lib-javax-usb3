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

import java.util.Objects;
import javax.usb3.IUsbDescriptor;
import javax.usb3.enumerated.EDescriptorType;

/**
 * A structure representing the Binary Device Object Store (BOS) descriptor.
 * <p>
 * This descriptor is documented in section 9.6.2 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 * <p>
 * The BOS descriptor defines a root descriptor that is similar to the
 * configuration descriptor, and is the base descriptor for accessing a family
 * of related descriptors. A host can read a BOS descriptor and learn from the
 * wTotalLength field the entire size of the device-level descriptor set, or it
 * can read in the entire BOS descriptor set of device capabilities. The host
 * accesses this descriptor using the GetDescriptor() request. The descriptor
 * type in the GetDescriptor() request is set to BOS (see Table 9-12). There is
 * no way for a host to read individual device capability descriptors. The
 * entire set can only be accessed via reading the BOS descriptor with a
 * GetDescriptor() request and using the length reported in the wTotalLength
 * field.
 * <p>
 * Individual technology-specific or generic device-level capabilities are
 * reported via Device Capability descriptors. The format of the Device
 * Capability descriptor is defined in Table 9-13. The Device Capability
 * descriptor has a generic header, with a sub-type field (bDevCapabilityType)
 * which defines the layout of the remainder of the descriptor. The codes for
 * bDevCapabilityType are defined in Table 9-14.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class BosDescriptor implements IUsbDescriptor {
  // Maps to JNI native class

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
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.bosDescriptorPointer;
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
   * Returns the length of this descriptor and all of its sub descriptors.
   *
   * @return The total descriptor length.
   */
  public native short wTotalLength();

  /**
   * Returns the number of separate device capability descriptors in the BOS.
   *
   * @return The number of device capability descriptors.
   */
  public native byte bNumDeviceCaps();

  /**
   * Returns the array with the device capability descriptors.
   *
   * @return The array with device capability descriptors.
   */
  public native BosDevCapabilityDescriptor[] devCapability();

  /**
   * Returns a dump of this descriptor.
   *
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
    return this.hashCode() == obj.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }

}
