/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
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
import java.util.Objects;
import javax.usb.utility.DescriptorUtils;

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
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class ConfigDescriptor {
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
   * Returns the size of this descriptor (in bytes).
   *
   * @return The size of this descriptor (in bytes).
   */
  public native byte bLength();

  /**
   * Returns the descriptor type. Will have value {@link LibUsb#DT_CONFIG} in
   * this context.
   *
   * @return The descriptor type.
   */
  public native byte bDescriptorType();

  /**
   * Returns the total length of data returned for this configuration.
   * <p>
   * Total length of data returned for this configuration. Includes the combined
   * length of all descriptors (configuration, interface, endpoint, and class-
   * or vendor-specific) returned for this configuration
   *
   * @return The total length of data.
   */
  public native short wTotalLength();

  /**
   * Returns the number of interfaces supported by this configuration.
   *
   * @return The number of supported interfaces.
   */
  public native byte bNumInterfaces();

  /**
   * Returns the identifier value for this configuration.
   *
   * @return The identifier value.
   */
  public native byte bConfigurationValue();

  /**
   * Returns the index of string descriptor describing this configuration.
   *
   * @return The string descriptor index.
   */
  public native byte iConfiguration();

  /**
   * Returns the configuration characteristics.
   * <p>
   * Configuration characteristics:
   * <pre>
   * D7:Reserved (set to one)
   * D6:Self-powered
   * D5:Remote Wakeup
   * D4...0:Reserved (reset to zero)</pre>
   * <p>
   * D7 is reserved and shall be set to one for historical reasons.
   * <p>
   * A device configuration that uses power from the bus and a local source
   * reports a non-zero value in bMaxPower to indicate the amount of bus power
   * required and sets D6. The actual power source at runtime may be determined
   * using the GetStatus(DEVICE) request (refer to Section 9.4.5).
   * <p>
   * If a device configuration supports remote wakeup, D5 is set to one.
   *
   * @return The configuration characteristics.
   */
  public native byte bmAttributes();

  /**
   * Returns the maximum power consumption of the USB device from this bus in
   * this configuration when the device is fully operation. Expressed in units
   * of 2 mA.
   * <p>
   * Maximum power consumption of the device from the bus in this specific
   * configuration when the device is fully operational. Expressed in 2-mA units
   * when the device is operating in high-speed mode and in 8-mA units when
   * operating at Gen X speed. (i.e., 50 = 100 mA when operating at high-speed
   * and 50 = 400 mA when operating at Gen X speed).
   * <p>
   * Note: A device configuration reports whether the configuration is
   * bus-powered or self-powered. Device status reports whether the device is
   * currently self-powered. If a device is disconnected from its external power
   * source, it updates device status to indicate that it is no longer
   * self-powered.
   * <p>
   * A device may not increase its power draw from the bus, when it loses its
   * external power source, beyond the amount reported by its configuration.
   * <p>
   * If a device can continue to operate when disconnected from its external
   * power source, it continues to do so. If the device cannot continue to
   * operate, it shall return to the Powered state.
   *
   * @return The maximum power consumption.
   */
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
