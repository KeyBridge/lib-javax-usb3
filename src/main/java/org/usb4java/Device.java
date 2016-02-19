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

/**
 * Structure representing a USB device detected on the system.
 * <p>
 * This is an opaque type for which you are only ever provided with a pointer,
 * usually originating from {@link LibUsb#getDeviceList(Context, DeviceList)}.
 * <p>
 * Certain operations can be performed on a device, but in order to do any I/O
 * you will have to first obtain a device handle using
 * {@link LibUsb#open(Device, DeviceHandle)}.
 * <p>
 * Devices are reference counted with {@link LibUsb#refDevice(Device)} and
 * {@link LibUsb#unrefDevice(Device)}, and are freed when the reference count
 * reaches 0. New devices presented by
 * {@link LibUsb#getDeviceList(Context, DeviceList)} have a reference count of
 * 1, and {@link LibUsb#freeDeviceList(DeviceList, boolean)} can optionally
 * decrease the reference count on all devices in the list.
 * {@link LibUsb#open(Device, DeviceHandle)} adds another reference which is
 * later destroyed by {@link LibUsb#close(DeviceHandle)}.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class Device {
  // Maps to JNI native class

  /**
   * The native pointer to the device structure.
   */
  private long devicePointer;

  /**
   * Package-private constructor to prevent manual instantiation. Devices are
   * always created by JNI.
   */
  protected Device() {
    // Empty
  }

  /**
   * Returns the native pointer to the device structure.
   *
   * @return The native pointer to the device structure.
   */
  public long getPointer() {
    return this.devicePointer;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 43 * hash + (int) (this.devicePointer ^ (this.devicePointer >>> 32));
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
    final Device other = (Device) obj;
    return this.devicePointer == other.getPointer();
  }

  @Override
  public String toString() {
    return String.format("libusb device 0x%x", this.devicePointer);
  }
}
