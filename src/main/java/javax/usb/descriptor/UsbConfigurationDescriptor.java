/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * Copyright (C) 2014 Jesse Caulfield
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
package javax.usb.descriptor;

import javax.usb.request.ConfigurationAttributes;
import org.usb4java.ConfigDescriptor;

/**
 * 9.6.3 Configuration Descriptor implementation.
 * <p>
 * Devices report their attributes using descriptors. A descriptor is a data
 * structure with a defined format.
 * <p>
 * The configuration descriptor describes information about a specific device
 * configuration. The descriptor contains a bConfigurationValue field with a
 * value that, when used as a parameter to the SetConfiguration() request,
 * causes the device to assume the described configuration.
 * <p>
 * The descriptor describes the number of interfaces provided by the
 * configuration. Each interface may operate independently.
 * <p>
 * For example, an ISDN device might be configured with two interfaces, each
 * providing 64 Kb/s bi-directional channels that have separate data sources or
 * sinks on the host. Another configuration might present the ISDN device as a
 * single interface, bonding the two channels into one 128 Kb/s bi-directional
 * channel.
 * <p>
 * When the host requests the configuration descriptor all related interface and
 * endpoint descriptors are returned.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class UsbConfigurationDescriptor extends AUsbConfigurationDescriptor {

  /**
   * Construct a new UsbConfigurationDescriptor instance.
   *
   * @param wTotalLength        The total length of data returned for this
   *                            configuration. Includes the combined length of
   *                            all descriptors (configuration, interface,
   *                            endpoint, and class- or vendor-specific)
   *                            returned for this configuration.
   * @param bNumInterfaces      The number of interfaces supported by this
   *                            configuration
   * @param bConfigurationValue The configuration value to use as an argument to
   *                            the SetConfiguration() request to select this
   *                            configuration
   * @param iConfiguration      The configuration string descriptor index.
   * @param bmAttributes        The configuration attributes.
   * @param bMaxPower           The maximum power.
   */
  public UsbConfigurationDescriptor(final short wTotalLength,
                                    final byte bNumInterfaces,
                                    final byte bConfigurationValue,
                                    final byte iConfiguration,
                                    final ConfigurationAttributes bmAttributes,
                                    final byte bMaxPower) {
    super(wTotalLength,
          bNumInterfaces,
          bConfigurationValue,
          iConfiguration,
          bmAttributes,
          bMaxPower);
  }

  /**
   * Construct a new UsbConfigurationDescriptor instance from a libusb4java JNI
   * ConfigDescriptor instance.
   *
   * @param descriptor The JNI descriptor instance from which to copy the data.
   */
  public UsbConfigurationDescriptor(final ConfigDescriptor descriptor) {
    super(descriptor.wTotalLength(),
          descriptor.bNumInterfaces(),
          descriptor.bConfigurationValue(),
          descriptor.iConfiguration(),
          ConfigurationAttributes.getInstance(descriptor.bmAttributes()),
          descriptor.bMaxPower());
  }

}
