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
package javax.usb.enumerated;

/**
 * Enumerated list of Device Capability Type Code values returned in a Binary
 * Device Object Store (BOS) Device Capability Descriptor. The value is encoded
 * in the {@code bDevCapabilityType} field.
 * <p>
 * See Table 9-14. Device Capability Type Codes of the USB 3.1 specification.
 * <p>
 * @author Jesse Caulfield
 */
public enum EBosDeviceCapabilityType {

  WIRELESS_USB((byte) 0x01, "Defines the set of Wireless USB-specific device level capabilities"),
  USB_20_EXTENSION((byte) 0x02, "USB 2.0 Extension Descriptor"),
  SUPERSPEED_USB((byte) 0x03, "Defines the set of SuperSpeed USB specific device level capabilities"),
  CONTAINER_ID((byte) 0x04, "Defines the instance unique ID used to identify the instance across all operating modes"),
  PLATFORM((byte) 0x05, "Defines a device capability specific to a particular platform/operating system"),
  POWER_DELIVERY_CAPABILITY((byte) 0x06, "Defines the various PD Capabilities of this device"),
  BATTERY_INFO_CAPABILITY((byte) 0x07, "Provides information on each battery supported by the device"),
  PD_CONSUMER_PORT_CAPABILITY((byte) 0x08, "The consumer characteristics of a port on the device"),
  PD_PROVIDER_PORT_CAPABILITY((byte) 0x09, "The provider characteristics of a port on the device"),
  SUPERSPEED_PLUS((byte) 0x0A, "Defines the set of SuperSpeed Plus USB specific device level capabilities"),
  PRECISION_TIME_MEASUREMENT((byte) 0x0B, "Precision Time Measurement (PTM) Capability Descriptor"),
  WIRELESS_USB_EXT((byte) 0x0C, "Defines the set of Wireless USB 1.1-specific device level capabilities"),
  RESERVED((byte) 0x00, "0x00 and 0x0D to 0xFF are reserved for future use");
  /**
   * Device Capability Type Code value
   */
  private final byte byteCode;
  /**
   * Device Capability Type Code description
   */
  private final String description;

  private EBosDeviceCapabilityType(byte byteCode, String description) {
    this.byteCode = byteCode;
    this.description = description;
  }

  /**
   * Get the Device Capability Type Code value
   * <p>
   * @return Device Capability Type Code byte value
   */
  public byte getByteCode() {
    return byteCode;
  }

  /**
   * Get the Device Capability Type Code description
   * <p>
   * @return a human readable description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get a EBosDeviceCapabilityType instance from the bDevCapabilityType number.
   * <p>
   * @param bDevCapabilityType the bDevCapabilityType field in a BOS Device
   *                           Capability Descriptor
   * @return the corresponding EBosDeviceCapabilityType instance
   */
  public static EBosDeviceCapabilityType fromByte(byte bDevCapabilityType) {
    for (EBosDeviceCapabilityType type : EBosDeviceCapabilityType.values()) {
      if ((type.getByteCode() & bDevCapabilityType) == 0) {
        return type;
      }
    }
    return RESERVED;
  }

}
