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
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield <jesse@caulfield.org>
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
   * <p>
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
