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
package javax.usb3.enumerated;

/**
 * An enumerated list of Endpoint Usage Types encoded into the endpoint on the
 * USB device described by this descriptor bmAttributes field.
 * <p>
 * If not an isochronous endpoint, bits 5..2 are reserved and must be set to
 * zero. If isochronous, they are defined as Bits 5..4: Usage Type.
 *
 * @author Jesse Caulfield
 */
public enum EEndpointUsageType {

  DATA((byte) 0x00),
  FEEDBACK((byte) 0x10),
  IMPLICIT_FEEDBACK_DATA((byte) 0x20),
  RESERVED((byte) 0x30);
  private final byte byteCode;
  private static final byte MASK = (byte) 0x30;

  private EEndpointUsageType(byte byteCode) {
    this.byteCode = byteCode;
  }

  /**
   * Get the byte code corresponding to this instance.
   *
   * @return the byte code
   */
  public byte getByteCode() {
    return byteCode;
  }

  /**
   * Get the byte mask used to identify the values from a bmAttributes byte.
   *
   * @return The bit mask value.
   */
  public static byte getMASK() {
    return MASK;
  }

  /**
   * Get a EEndpointUsageType instance from a Standard Endpoint Descriptor
   * bmAttributes byte.
   *
   * @param bmAttributes the bmAttributes byte
   * @return the corresponding EEndpointUsageType instance
   */
  public static EEndpointUsageType fromByte(byte bmAttributes) {
    for (EEndpointUsageType type : EEndpointUsageType.values()) {
      if ((bmAttributes & MASK) == type.getByteCode()) {
        return type;
      }
    }
    return null;
  }

}
