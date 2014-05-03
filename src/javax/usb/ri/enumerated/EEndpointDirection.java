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
 * The endpoint Direction (ignored for control endpoints). This handles logic
 * from the bEndpointAddress bit 7.
 */
public enum EEndpointDirection {

  OUT((byte) 0x00),
  IN((byte) 0x80);
  private final byte byteCode;
  private static final byte MASK = (byte) 0x80;

  private EEndpointDirection(byte byteCode) {
    this.byteCode = byteCode;
  }

  /**
   * Get the direction from a it value.
   * <p>
   * @param bEndpointAddress the bEndpointAddress byte
   * @return the corresponding direction.
   */
  public static EEndpointDirection fromByte(byte bEndpointAddress) {
    return (bEndpointAddress & MASK) == 0 ? OUT : IN;
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

}
