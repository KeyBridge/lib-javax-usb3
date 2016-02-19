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

/**
 * A collection of alternate settings for a particular USB interface.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class Interface {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long interfacePointer;

  /**
   * Package-private constructor to prevent manual instantiation. Interfaces are
   * always created by JNI.
   */
  protected Interface() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.interfacePointer;
  }

  /**
   * Returns the array with interface descriptors. The length of this array is
   * determined by the {@link #numAltsetting()} field.
   *
   * @return The array with interface descriptors.
   */
  public native InterfaceDescriptor[] altsetting();

  /**
   * Returns the number of alternate settings that belong to this interface.
   *
   * @return The number of alternate settings.
   */
  public native int numAltsetting();

  /**
   * Returns a dump of this interface.
   *
   * @return The interface dump.
   */
  public String dump() {
    final StringBuilder builder = new StringBuilder();

    builder.append(String.format("Interface:%n  numAltsetting %10d",
                                 this.numAltsetting()));

    for (final InterfaceDescriptor intDesc : this.altsetting()) {
      builder.append(String.format("%n")).append(intDesc.dump());
    }

    return builder.toString();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 83 * hash + (int) (this.interfacePointer ^ (this.interfacePointer >>> 32));
    hash += this.numAltsetting();
    hash += Objects.hashCode(this.altsetting());
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
    final Interface other = (Interface) obj;
    return this.hashCode() == other.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }
}
