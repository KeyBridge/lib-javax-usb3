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
 * A generic representation of a Binary Device Object Store (BOS) Device
 * Capability descriptor.
 * <p>
 * Device Capability descriptors are always returned as part of the BOS
 * information returned by a GetDescriptor(BOS) request. A Device Capability
 * cannot be directly accessed with a GetDescriptor() or SetDescriptor()
 * request.
 * <p>
 * It is advised to check bDevCapabilityType and call the matching
 * get*Descriptor method to get a structure fully matching the type.
 * <p>
 * Individual technology-specific or generic device-level capabilities are
 * reported via Device Capability descriptors. The format of the Device
 * Capability descriptor is defined in Table 9-13.
 * <p>
 * See Table 9-13. Format of a Device Capability Descriptor of the USB 3.1 spec.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class BosDevCapabilityDescriptor implements IUsbDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long bosDevCapabilityDescriptorPointer;

  /**
   * Package-private constructor to prevent manual instantiation. BOS device
   * capability descriptors are always created by JNI.
   */
  protected BosDevCapabilityDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.bosDevCapabilityDescriptorPointer;
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
   * Returns the device capability type code. Table 9-14. Device Capability Type
   * Codes
   * <pre>
   * Capability Code Value Description
   * Wireless_USB                01H Defines the set of Wireless USB-specific device level capabilities
   * USB 2.0 EXTENSION           02H USB 2.0 Extension Descriptor
   * SUPERSPEED_USB              03H Defines the set of SuperSpeed USB specific device level capabilities
   * CONTAINER_ID                04H Defines the instance unique ID used to identify the instance across all operating modes
   * PLATFORM                    05H Defines a device capability specific to a particular platform/operating system
   * POWER_DELIVERY_CAPABILITY   06H Defines the various PD Capabilities of this device
   * BATTERY_INFO_CAPABILITY     07H Provides information on each battery supported by the device
   * PD_CONSUMER_PORT_CAPABILITY 08H The consumer characteristics of a port on the device
   * PD_PROVIDER_PORT_CAPABILITY 09H The provider characteristics of a port on the device
   * SUPERSPEED_PLUS             0AH Defines the set of SuperSpeed Plus USB specific device level capabilities
   * PRECISION_TIME_MEASUREMENT  0BH Precision Time Measurement (PTM) Capability Descriptor
   * Wireless_USB_Ext            0CH Defines the set of Wireless USB 1.1-specific device level capabilities
   * Reserved                    00H, 0D-FFH Reserved for future use
   * </pre>
   *
   * @return The device capability type.
   */
  public native byte bDevCapabilityType();

  /**
   * Returns the device capability data (bLength - 3 bytes).
   *
   * @return The device capability data.
   */
  public native ByteBuffer devCapabilityData();

  /**
   * Returns a dump of this descriptor.
   *
   * @return The descriptor dump.
   */
  public String dump() {
    return String.format("BOS Device Capability Descriptor:%n"
      + "  bLength %18d%n"
      + "  bDescriptorType %10d%n"
      + "  bDevCapabilityType %7s%n"
      + "  devCapabilityData:%n%s%n",
                         this.bLength() & 0xFF,
                         this.bDescriptorType() & 0xFF,
                         this.bDevCapabilityType() & 0xFF,
                         DescriptorDumpUtility.dump(this.devCapabilityData())
                           .replaceAll("(?m)^", "    "));
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash += this.bLength();
    hash += this.bDescriptorType();
    hash += this.bDevCapabilityType();
    hash += Objects.hashCode(this.devCapabilityData());
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
