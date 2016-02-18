/*
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
package javax.usb.request;

/**
 * Helper class to encode and decode the Standard Configuration Descriptor
 * bmAttributes field. This class encodes/decodes the {@code bmAttributes} field
 * according to Table 9-10. Standard Configuration Descriptor.
 *
 * @author Jesse Caulfield
 */
public class BMConfigurationAttributes {

  /**
   * Byte D7 is reserved and must be set to one for historical reasons.
   */
  private static final byte D7_RESERVED = (byte) 0x80;
  /**
   * The Self Powered field indicates whether the device is currently
   * self-powered.
   */
  private static final byte D6_SELF_POWERED = (byte) 0x40;
  /**
   * If a device configuration supports remote wakeup, D5 is set to one.
   */
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
   *
   * @param selfPowered  if the device is self powered (TRUE) or takes power
   *                     from the USB.
   * @param remoteWakeup if the device supports remote wakeup
   */
  public BMConfigurationAttributes(boolean selfPowered, boolean remoteWakeup) {
    this.selfPowered = selfPowered;
    this.remoteWakeup = remoteWakeup;
  }

  /**
   * Construct a ConfigurationAttributes instance from a byte code value.
   *
   * @param bmAttributes This field describes the endpoint’s attributes when it
   *                     is configured using the bConfigurationValue.
   */
  public BMConfigurationAttributes(byte bmAttributes) {
    this.selfPowered = (bmAttributes & D6_SELF_POWERED) == 1;
    this.remoteWakeup = (bmAttributes & D5_REMOTE_WAKEUP) == 1;
  }

  /**
   * Get a default BMConfigurationAttributes instance. Returns [FALSE, FALSE]
   * configuration.
   *
   * @return a BMConfigurationAttributes CONTROL instance
   */
  public static BMConfigurationAttributes getInstance() {
    return new BMConfigurationAttributes(false, false);
  }

  /**
   * Get a EndpointAttributes instance from the bmAttributes byte code.
   *
   * @param bmAttributes the USB descriptor bmAttributes byte code
   * @return a EndpointAttributes instance
   */
  public static BMConfigurationAttributes getInstance(byte bmAttributes) {
    return new BMConfigurationAttributes(bmAttributes);
  }

  /**
   * Get the EndpointAttributes as a byte.
   *
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
   *
   * @return whether the device is currently self-powered
   */
  public boolean isRemoteWakeup() {
    return remoteWakeup;
  }

  /**
   * Indicates whether the device is currently enabled to request remote wakeup.
   *
   * @return whether the device is currently enabled to request remote wakeup
   */
  public boolean isSelfPowered() {
    return selfPowered;
  }

}
