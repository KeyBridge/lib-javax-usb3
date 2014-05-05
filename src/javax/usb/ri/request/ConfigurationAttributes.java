/*
 * Copyright (C) 2014 Jesse Caulfield <jesse@caulfield.org>
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
package javax.usb.ri.request;

/**
 * Helper class to encode and decode the Standard Configuration Descriptor
 * bmAttributes field. This class encodes/decodes the bmAttributes field
 * according to Table 9-10. Standard Configuration Descriptor.
 * <p>
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
public class ConfigurationAttributes {

  /**
   * Configuration characteristics byte D7 is reserved and must be set to one
   * for historical reasons.
   */
  private static final byte D7_RESERVED = (byte) 0x80;
  private static final byte D6_SELF_POWERED = (byte) 0x40;
  private static final byte D5_REMOTE_WAKEUP = (byte) 0x20;
  /**
   * The Self Powered field indicates whether the device is currently
   * self-powered.
   * <p>
   * A device configuration that uses power from the bus and a local source
   * reports a non-zero value in bMaxPower to indicate the amount of bus power
   * required and sets D6. The actual power source at runtime may be determined
   * using the GetStatus(DEVICE) request (see Section 9.4.5).
   */
  private final boolean selfPowered;
  /**
   * The Remote Wakeup field indicates whether the device is currently enabled
   * to request remote wakeup.
   * <p>
   * If a device configuration supports remote wakeup, D5 is set to one.
   */
  private final boolean remoteWakeup;

  /**
   * Construct a new ConfigurationAttributes instance.
   * <p>
   * @param selfPowered  if the device is self powered (TRUE) or takes power
   *                     from the USB.
   * @param remoteWakeup if the device supports remote wakeup
   */
  public ConfigurationAttributes(boolean selfPowered, boolean remoteWakeup) {
    this.selfPowered = selfPowered;
    this.remoteWakeup = remoteWakeup;
  }

  /**
   * Construct a ConfigurationAttributes instance from a byte code value.
   * <p>
   * @param bmAttributes This field describes the endpoint’s attributes when it
   *                     is configured using the bConfigurationValue.
   */
  public ConfigurationAttributes(byte bmAttributes) {
    this.selfPowered = (bmAttributes & D6_SELF_POWERED) == 1;
    this.remoteWakeup = (bmAttributes & D5_REMOTE_WAKEUP) == 1;
  }

  /**
   * Get a default ConfigurationAttributes instance. Returns [FALSE, FALSE]
   * configuration.
   * <p>
   * @return a ConfigurationAttributes CONTROL instance
   */
  public static ConfigurationAttributes getInstance() {
    return new ConfigurationAttributes(false, false);
  }

  /**
   * Get a EndpointAttributes instance from the bmAttributes byte code.
   * <p>
   * @param bmAttributes the USB descriptor bmAttributes byte code
   * @return a EndpointAttributes instance
   */
  public static ConfigurationAttributes getInstance(byte bmAttributes) {
    return new ConfigurationAttributes(bmAttributes);
  }

  /**
   * Get the EndpointAttributes as a byte.
   * <p>
   * @return the EndpointAttributes encoded as a byte.
   */
  public byte asByte() {
    /**
     * OR mask the byte codes from all attributes.
     */
    return (byte) (D7_RESERVED | (isSelfPowered() ? D6_SELF_POWERED : 0x00) | (isRemoteWakeup() ? D5_REMOTE_WAKEUP : 0x00));
  }

  /**
   * Indicates whether the device is currently self-powered.
   * <p>
   * @return whether the device is currently self-powered
   */
  public boolean isRemoteWakeup() {
    return remoteWakeup;
  }

  /**
   * Indicates whether the device is currently enabled to request remote wakeup.
   * <p>
   * @return whether the device is currently enabled to request remote wakeup
   */
  public boolean isSelfPowered() {
    return selfPowered;
  }

}
