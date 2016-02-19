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

import javax.usb3.IUsbDescriptor;
import javax.usb3.enumerated.EDescriptorType;

/**
 * 9.6.2.1 USB 2.0 Extension
 * <p>
 * A structure representing the USB 2.0 Extension descriptor.
 * <p>
 * This descriptor is documented in section 9.6.2.1 of the USB 3.0
 * specification. An Enhanced SuperSpeed device shall include the USB 2.0
 * Extension descriptor and shall support LPM when operating in USB 2.0
 * High-Speed mode.
 * <p>
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class Usb20ExtensionDescriptor implements IUsbDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long usb20ExtensionDescriptorPointer;

  /**
   * Constructs a new USB 2.0 Extension descriptor which can be passed to the
   * {@link LibUsb#getUsb20ExtensionDescriptor(Context, BosDevCapabilityDescriptor, Usb20ExtensionDescriptor)}
   * method.
   */
  public Usb20ExtensionDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.usb20ExtensionDescriptorPointer;
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
   * Returns the device capability type.
   *
   * @return DEVICE CAPABILITY Descriptor type
   */
  public native byte bDevCapabilityType();

  /**
   * Bitmap encoding of supported device level features. A value of one in a bit
   * location indicates a feature is supported; a value of zero indicates it is
   * not supported. Encodings are:
   * <pre> Bit  Encoding
   * 0    Reserved. Shall be set to zero.
   * 1    LPM. A value of one in this bit location indicates that this device
   *      supports the Link Power Management protocol.
   *      Enhanced SuperSpeed devices shall set this bit to one.
   * 31:2 Reserved. Shall be set to zero.</pre>
   *
   * @return The supported device level features.
   */
  public native int bmAttributes();

  /**
   * Returns a dump of this descriptor.
   *
   * @return The descriptor dump.
   */
  public String dump() {
    return String.format(
            "USB 2.0 Extension Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bDevCapabilityType %7d%n"
            + "  bmAttributes %13s%n",
            this.bLength() & 0xFF,
            this.bDescriptorType() & 0xFF,
            this.bDevCapabilityType() & 0xFF,
            String.format("0x%08x", this.bmAttributes()));
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + (int) (this.usb20ExtensionDescriptorPointer ^ (this.usb20ExtensionDescriptorPointer >>> 32));
    hash += this.bLength();
    hash += this.bDescriptorType();
    hash += this.bDevCapabilityType();
    hash += this.bmAttributes();
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
    final Usb20ExtensionDescriptor other = (Usb20ExtensionDescriptor) obj;
    return this.hashCode() == other.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }
}
