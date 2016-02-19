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
import javax.usb3.IUsbDeviceDescriptor;
import javax.usb3.enumerated.EDescriptorType;
import javax.usb3.enumerated.EUSBClassCode;
import javax.usb3.utility.BufferUtility;
import javax.usb3.utility.DescriptorDumpUtility;

/**
 * A structure representing the standard USB device descriptor.
 * <p>
 * This descriptor is documented in section 9.6.1 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class DeviceDescriptor implements IUsbDeviceDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long deviceDescriptorPointer;

  /**
   * The Java ByteBuffer which contains the descriptor structure.
   */
  private final ByteBuffer deviceDescriptorBuffer;

  /**
   * Constructs a new device descriptor which can be passed to the
   * {@link LibUsb#getDeviceDescriptor(Device, DeviceDescriptor)} method.
   */
  public DeviceDescriptor() {
    // Assign new buffer.
    this.deviceDescriptorBuffer = BufferUtility.allocateByteBuffer(LibUsb.deviceDescriptorStructSize());
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.deviceDescriptorPointer;
  }

  /**
   * Returns the Java byte buffer which contains the descriptor structure.
   *
   * @return The descriptor structur buffer.
   */
  public ByteBuffer getBuffer() {
    return this.deviceDescriptorBuffer;
  }

  /**
   * @inherit
   */
  @Override
  public native byte bLength();

  /**
   * @inherit
   */
  @Override
  public EDescriptorType descriptorType() {
    return EDescriptorType.fromBytecode(bDescriptorType());
  }

  /**
   * @inherit
   */
  @Override
  public native byte bDescriptorType();

  /**
   * @inherit
   */
  @Override
  public native short bcdUSB();

  /**
   * @inherit
   */
  @Override
  public EUSBClassCode deviceClass() {
    return EUSBClassCode.fromByteCode(bDeviceClass());
  }

  /**
   * @inherit
   */
  @Override
  public native byte bDeviceClass();

  /**
   * @inherit
   */
  @Override
  public native byte bDeviceSubClass();

  /**
   * @inherit
   */
  @Override
  public native byte bDeviceProtocol();

  /**
   * @inherit
   */
  @Override
  public native byte bMaxPacketSize0();

  /**
   * @inherit
   */
  @Override
  public native short idVendor();

  /**
   * @inherit
   */
  @Override
  public native short idProduct();

  /**
   * @inherit
   */
  @Override
  public native short bcdDevice();

  /**
   * @inherit
   */
  @Override
  public native byte iManufacturer();

  /**
   * @inherit
   */
  @Override
  public native byte iProduct();

  /**
   * @inherit
   */
  @Override
  public native byte iSerialNumber();

  /**
   * @inherit
   */
  @Override
  public native byte bNumConfigurations();

  /**
   * Returns a dump of this descriptor.
   *
   * @return The descriptor dump.
   */
  public String dump() {
    return this.dump(null);
  }

  /**
   * Returns a dump of this descriptor.
   *
   * @param handle The USB device handle for resolving string descriptors. If
   *               null then no strings are resolved.
   * @return The descriptor dump.
   */
  public String dump(final DeviceHandle handle) {
    final String sManufacturer = LibUsb.getStringDescriptor(handle, this.iManufacturer());
    final String sProduct = LibUsb.getStringDescriptor(handle, this.iProduct());
    final String sSerialNumber = LibUsb.getStringDescriptor(handle, this.iSerialNumber());
    return DescriptorDumpUtility.dump(this, sManufacturer, sProduct, sSerialNumber);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + (int) (this.deviceDescriptorPointer ^ (this.deviceDescriptorPointer >>> 32));
    hash += 19 * this.bLength();
    hash += 19 * this.bDescriptorType();
    hash += 19 * this.bcdUSB();
    hash += 19 * this.bDeviceClass();
    hash += 19 * this.bDeviceSubClass();
    hash += 19 * this.bDeviceProtocol();
    hash += 19 * this.bMaxPacketSize0();
    hash += 19 * this.idVendor();
    hash += 19 * this.idProduct();
    hash += 19 * this.bcdDevice();
    hash += 19 * this.idProduct();
    hash += 19 * this.iManufacturer();
    hash += 19 * this.idProduct();
    hash += 19 * this.iSerialNumber();
    hash += 19 * this.bNumConfigurations();
    hash += 19 * Objects.hashCode(this.deviceDescriptorBuffer);
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
    return hashCode() == obj.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }

}
