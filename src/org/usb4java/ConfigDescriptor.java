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

import java.nio.ByteBuffer;

/**
 * A structure representing the standard USB configuration descriptor.
 * <p>
 * This descriptor is documented in section 9.6.3 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class ConfigDescriptor {

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
   * <p>
   * @return The native pointer.
   */
  public long getPointer() {
    return this.configDescriptorPointer;
  }

  /**
   * Returns the size of this descriptor (in bytes).
   * <p>
   * @return The size of this descriptor (in bytes).
   */
  public native byte bLength();

  /**
   * Returns the descriptor type. Will have value {@link LibUsb#DT_CONFIG} in
   * this context.
   * <p>
   * @return The descriptor type.
   */
  public native byte bDescriptorType();

  /**
   * Returns the total length of data returned for this configuration.
   * <p>
   * @return The total length of data.
   */
  public native short wTotalLength();

  /**
   * Returns the number of interfaces supported by this configuration.
   * <p>
   * @return The number of supported interfaces.
   */
  public native byte bNumInterfaces();

  /**
   * Returns the identifier value for this configuration.
   * <p>
   * @return The identifier value.
   */
  public native byte bConfigurationValue();

  /**
   * Returns the index of string descriptor describing this configuration.
   * <p>
   * @return The string descriptor index.
   */
  public native byte iConfiguration();

  /**
   * Returns the configuration characteristics.
   * <p>
   * @return The configuration characteristics.
   */
  public native byte bmAttributes();

  /**
   * Returns the maximum power consumption of the USB device from this bus in
   * this configuration when the device is fully operation. Expressed in units
   * of 2 mA.
   * <p>
   * @return The maximum power consumption.
   */
  public native byte bMaxPower();

  /**
   * Returns the array with interfaces supported by this configuration.
   * <p>
   * @return The array with interfaces.
   */
  public native Interface[] iface();

  /**
   * Extra descriptors.
   * <p>
   * If libusb encounters unknown interface descriptors, it will store them
   * here, should you wish to parse them.
   * <p>
   * @return The extra descriptors.
   */
  public native ByteBuffer extra();

  /**
   * Length of the extra descriptors, in bytes.
   * <p>
   * @return The extra descriptors length.
   */
  public native int extraLength();

  /**
   * Returns a dump of this descriptor.
   * <p>
   * @return The descriptor dump.
   */
  public String dump() {
    final StringBuilder builder = new StringBuilder();

    builder.append(String.format(
      "%s"
      + "  extralen %17d%n"
      + "  extra:%n"
      + "%s",
      DescriptorUtils.dump(this),
      this.extraLength(),
      DescriptorUtils.dump(this.extra()).replaceAll("(?m)^", "    ")));

    for (final Interface iface : this.iface()) {
      builder.append(String.format("%n")).append(iface.dump());
    }

    return builder.toString();
  }

//  @Override
//  public int hashCode() {
//    return new HashCodeBuilder()
//      .append(this.bLength())
//      .append(this.bDescriptorType())
//      .append(this.wTotalLength())
//      .append(this.bNumInterfaces())
//      .append(this.bConfigurationValue())
//      .append(this.iConfiguration())
//      .append(this.bmAttributes())
//      .append(this.bMaxPower())
//      .append(this.iface())
//      .append(this.extra())
//      .append(this.extraLength())
//      .toHashCode();
//  }
  @Override
  public int hashCode() {
    int hash = 7;
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
    final ConfigDescriptor other = (ConfigDescriptor) obj;
    return this.configDescriptorPointer == other.configDescriptorPointer;
  }

  @Override
  public String toString() {
    return this.dump();
  }
}
