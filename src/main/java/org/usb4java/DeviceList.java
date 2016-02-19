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

import java.util.Iterator;
import javax.usb3.utility.DeviceListIterator;

/**
 * List of devices as returned by
 * {@link LibUsb#getDeviceList(Context, DeviceList)}.
 *
 * @author Klaus Reimer
 */
public final class DeviceList implements Iterable<Device> {
  // Maps to JNI native class

  /**
   * The native pointer to the devices array.
   */
  private long deviceListPointer;

  /**
   * The number of devices in the list.
   */
  private int size;

  /**
   * Constructs a new device list. Must be passed to
   * {@link LibUsb#getDeviceList(Context, DeviceList)} before using it.
   */
  public DeviceList() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.deviceListPointer;
  }

  /**
   * Returns the number of devices in the list.
   *
   * @return The number of devices in the list.
   */
  public int getSize() {
    return this.size;
  }

  /**
   * Returns the device with the specified index.
   *
   * @param index The device index.
   * @return The device or null when index is out of bounds.
   */
  public native Device get(final int index);

  @Override
  public Iterator<Device> iterator() {
    return new DeviceListIterator(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result)
             + (int) (this.deviceListPointer ^ (this.deviceListPointer >>> 32));
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final DeviceList other = (DeviceList) obj;
    return this.deviceListPointer == other.deviceListPointer;
  }

  @Override
  public String toString() {
    return String.format("libusb device list 0x%x with size %d",
                         this.deviceListPointer, this.size);
  }
}
