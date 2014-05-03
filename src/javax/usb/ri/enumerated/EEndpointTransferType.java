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
package javax.usb.ri.enumerated;

/**
 * An enumerated list of Endpoint Descriptor Types encoded into the endpoint on
 * the USB device described by this descriptor bmAttributes field.
 * <p>
 * This field describes the endpoint’s attributes when it is configured using
 * the bConfigurationValue. Bits 1..0: Transfer Type
 */
public enum EEndpointTransferType {

  CONTROL((byte) 0x00),
  ISOCHRONOUS((byte) 0x01),
  BULK((byte) 0x02),
  INTERRUPT((byte) 0x03);
  private final byte byteCode;
  private static final byte MASK = (byte) 0x03;

  private EEndpointTransferType(byte byteCode) {
    this.byteCode = byteCode;
  }

  /**
   * Get the byte code corresponding to this instance.
   * <p>
   * @return the byte code
   */
  public byte getByteCode() {
    return byteCode;
  }

  /**
   * Get the byte mask used to identify the values from a bmAttributes byte.
   * <p>
   * @return The bit mask value.
   */
  public static byte getMASK() {
    return MASK;
  }

  /**
   * Get a EEndpointTransferType instance from a Standard Endpoint Descriptor
   * bmAttributes byte.
   * <p>
   * @param bmAttributes the bmAttributes byte
   * @return the corresponding EEndpointTransferType instance
   */
  public static EEndpointTransferType fromByte(byte bmAttributes) {
    for (EEndpointTransferType type : EEndpointTransferType.values()) {
      if ((bmAttributes & MASK) == type.getByteCode()) {
        return type;
      }
    }
    return null;
  }

}
