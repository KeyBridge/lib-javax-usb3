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
import javax.usb3.IUsbConfigurationDescriptor;
import javax.usb3.enumerated.EDescriptorType;
import javax.usb3.utility.DescriptorDumpUtility;

/**
 * A structure representing the standard USB configuration descriptor.
 * <p>
 * The configuration descriptor describes information about a specific device
 * configuration. The descriptor contains a bConfigurationValue field with a
 * value that, when used as a parameter to the SetConfiguration() request,
 * causes the device to assume the described configuration.
 * <p>
 * The descriptor describes the number of interfaces provided by the
 * configuration. Each interface may operate independently. For example, a Video
 * Class device might be configured with two interfaces, each providing 64-MBps
 * bi-directional channels that have separate data sources or sinks on the host.
 * Another configuration might present the Video Class device as a single
 * interface, bonding the two channels into one 128-MBps bi-directional channel.
 * <p>
 * When the host requests the configuration descriptor, all related interface,
 * endpoint, and endpoint companion descriptors are returned (refer to Section
 * 9.4.3).
 * <p>
 * A device has one or more configuration descriptors. Each configuration has
 * one or more interfaces and each interface has zero or more endpoints. An
 * endpoint is not shared among interfaces within a single configuration unless
 * the endpoint is used by alternate settings of the same interface. Endpoints
 * may be shared among interfaces that are part of different configurations
 * without this restriction.
 * <p>
 * Once configured, devices may support limited adjustments to the
 * configuration. If a particular interface has alternate settings, an alternate
 * may be selected after configuration.
 * <p>
 * This descriptor is documented in section 9.6.3 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class ConfigDescriptor implements IUsbConfigurationDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long configDescriptorPointer;

  /**
   * Constructs a new config descriptor which can be passed to the
   * {@link LibUsb#getConfigDescriptor(Device, byte, ConfigDescriptor)} method.
   */
  public ConfigDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.configDescriptorPointer;
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
  public native short wTotalLength();

  /**
   * @inherit
   */
  @Override
  public native byte bNumInterfaces();

  /**
   * @inherit
   */
  @Override
  public native byte bConfigurationValue();

  /**
   * @inherit
   */
  @Override
  public native byte iConfiguration();

  /**
   * @inherit
   */
  @Override
  public native byte bmAttributes();

  /**
   * @inherit
   */
  @Override
  public native byte bMaxPower();

  /**
   * Returns the array with interfaces supported by this configuration.
   *
   * @return The array with interfaces.
   */
  public native Interface[] iface();

  /**
   * Extra descriptors.
   * <p>
   * If libusb encounters unknown interface descriptors, it will store them
   * here, should you wish to parse them.
   *
   * @return The extra descriptors.
   */
  public native ByteBuffer extra();

  /**
   * Length of the extra descriptors, in bytes.
   *
   * @return The extra descriptors length.
   */
  public native int extraLength();

  /**
   * Returns a dump of this descriptor.
   *
   * @return The descriptor dump.
   */
  public String dump() {
    final StringBuilder builder = new StringBuilder();

    builder.append(String.format("%s"
                                 + "  extralen %17d%n"
                                 + "  extra:%n"
                                 + "%s",
                                 DescriptorDumpUtility.dump(this),
                                 this.extraLength(),
                                 DescriptorDumpUtility.dump(this.extra()).replaceAll("(?m)^", "    ")));

    for (final Interface iface : this.iface()) {
      builder.append(String.format("%n")).append(iface.dump());
    }

    return builder.toString();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += this.bLength();
    hash += this.bDescriptorType();
    hash += this.wTotalLength();
    hash += this.bNumInterfaces();
    hash += this.bConfigurationValue();
    hash += this.iConfiguration();
    hash += this.bmAttributes();
    hash += this.bMaxPower();
    hash += Objects.hashCode(this.iface());
    hash += Objects.hashCode(this.extra());
    hash += this.extraLength();
    hash = 59 * hash + (int) (this.configDescriptorPointer ^ (this.configDescriptorPointer >>> 32));
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
