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
 * An enumerated list of Endpoint Interrupt Types encoded into the endpoint on
 * the USB device described by this descriptor bmAttributes field.
 * <p>
 * If an interrupt endpoint, bits 5..2 are defined as the [Interrupt] Usage
 * Type: Bits 3..2: Reserved, Bits 5..4: Usage Type.
 *
 * @author Jesse Caulfield
 */
public enum EEndpointInterruptType {

  PERIODIC((byte) 0x00),
  NOTIFICATION((byte) 0x08),
  RESERVED((byte) 0x10),
  RESERVED_((byte) 0x18);

  private final byte byteCode;
  private static final byte MASK = (byte) 0x18;

  private EEndpointInterruptType(byte byteCode) {
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
   * Get a EEndpointSynchronizationType instance from a Standard Endpoint
   * Descriptor bmAttributes byte.
   *
   * @param bmAttributes the bmAttributes byte
   * @return the corresponding EEndpointSynchronizationType instance
   */
  public static EEndpointInterruptType fromByte(byte bmAttributes) {
    for (EEndpointInterruptType type : EEndpointInterruptType.values()) {
      if ((bmAttributes & MASK) == type.getByteCode()) {
        return type;
      }
    }
    return null;
  }

}
