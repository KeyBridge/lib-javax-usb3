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
package javax.usb.enumerated;

/**
 * The USB Endpoint Direction (ignored for control endpoints). This provides
 * endcode/decode logic to determine the data flow direction from a Standard
 * Endpoint Descriptor bEndpointAddress field (bit 7).
 * <p>
 * See Table 9-13. Standard Endpoint Descriptor of the USB 2.0 specification for
 * more details.
 * <p>
 * This is also encoded into the BMRequestType as the bit D7 value in a
 * Control-type USB IRP (I/O Request Packet).
 *
 * @author Jesse Caulfield
 */
public enum EEndpointDirection {
  // Table 9-13 bEndpointAddress
//  public static final byte ENDPOINT_DIRECTION_MASK = (byte) 0x80;
//  public static final byte ENDPOINT_DIRECTION_OUT = (byte) 0x00;
//  public static final byte ENDPOINT_DIRECTION_IN = (byte) 0x80;

  /**
   * Copy of DEVICE_TO_HOST for programmer convenience.
   *
   * @deprecated recommend using the proper DEVICE_TO_HOST instance
   */
  IN((byte) 0x80),
  /**
   * IN.
   * <p>
   * Data direction is Device to Host. This is typically called "IN" to identify
   * a READ transaction from a USB device.
   */
  DEVICE_TO_HOST((byte) 0x80), // in

  /**
   * Copy of HOST_TO_DEVICE for programmer convenience.
   *
   * @deprecated recommend using the proper HOST_TO_DEVICE instance
   */
  OUT((byte) 0x00),
  /**
   * OUT.
   * <p>
   * Data direction is Host to Device. This is typically called "OUT" to
   * identify a WRITE transaction to a USB device.
   */
  HOST_TO_DEVICE((byte) 0x00); // out

  private final byte byteCode;
  private static final byte MASK = (byte) 0x80;

  private EEndpointDirection(byte byteCode) {
    this.byteCode = byteCode;
  }

  /**
   * Get the direction from a it value.
   *
   * @param byteCode the bEndpointAddress or bmRequestType byte
   * @return the corresponding direction.
   */
  public static EEndpointDirection fromByte(byte byteCode) {
    return (byteCode & MASK) == 0 ? HOST_TO_DEVICE : DEVICE_TO_HOST;
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

}
