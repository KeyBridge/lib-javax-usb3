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
 * Structure representing a handle on a USB device.
 * <p>
 * This is an opaque type for which you are only ever provided with a pointer,
 * usually originating from {@link LibUsb#open(Device, DeviceHandle)}.
 * <p>
 * A device handle is used to perform I/O and other operations. When finished
 * with a device handle, you should call {@link LibUsb#close(DeviceHandle)}.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class DeviceHandle {
  // Maps to JNI native class

  /**
   * The native pointer to the device handle structure.
   */
  private long deviceHandlePointer;

  /**
   * Private constructor. To get a device handle instance use
   * {@link #getInstance()}
   */
  private DeviceHandle() {
    // Empty
  }

  /**
   * Constructs a new device handle.
   * <p>
   * The returned instance must be configured before use by passing it to
   * {@link LibUsb#open(Device, DeviceHandle)}.
   */
  public static DeviceHandle getInstance() {
    return new DeviceHandle();
  }

  /**
   * Returns the native pointer to the device handle structure.
   *
   * @return The native pointer to the device handle structure.
   */
  public long getPointer() {
    return this.deviceHandlePointer;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (int) (this.deviceHandlePointer ^ (this.deviceHandlePointer >>> 32));
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
    final DeviceHandle other = (DeviceHandle) obj;
    return this.deviceHandlePointer == other.getPointer();
  }

  @Override
  public String toString() {
    return String.format("libusb device handle 0x%x",
                         this.deviceHandlePointer);
  }
}
