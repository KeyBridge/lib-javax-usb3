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
package javax.usb3.request;

import javax.usb3.enumerated.EDeviceRequest;
import javax.usb3.utility.ByteUtility;

/**
 * Control-type USB IRP (I/O Request Packet) helper class to set and get the
 * bmRequest field.
 * <p>
 * This is basically a wrapper of the EDeviceRequest enumerated list, but
 * renamed to match the control type IRP field to minimize developer confusion.
 * <p>
 * See USB 2.0 sec. 9.3 USB Device Requests and for specific requests refer to
 * Table 9-3.
 *
 * @author Jesse Caulfield
 */
public class BRequest {

  /**
   * The Standard Device Requests instance.
   */
  private final EDeviceRequest deviceRequest;

  /**
   * The descriptor index. The range of values used for a descriptor index is
   * from 0 to one less than the number of descriptors of that type (excluding
   * string descriptors) implemented by the device.
   */
  private final byte bRequest;

  /**
   * Construct a default BRequest instance for the indicated request type. The
   * index value is set to ZERO.
   *
   * @param deviceRequest a Standard Device Requests instance
   */
  public BRequest(EDeviceRequest deviceRequest) {
    this.deviceRequest = deviceRequest;
    this.bRequest = deviceRequest.getByteCode();
  }

  /**
   * Construct a BRequest instance for the indicated request byte code. The
   * index value is set to ZERO.
   * <p>
   * See Table 9-5 Standard Request Codes in the USB 3.1 specification for a
   * list of of values.
   *
   * @param bRequest the Standard Request Codes byte value
   */
  public BRequest(byte bRequest) {
    this.deviceRequest = EDeviceRequest.fromByteCode(bRequest);
    this.bRequest = bRequest;
  }

  /**
   * Get a BRequest instance as a byte code. The default INDEX value of zero is
   * used.
   * <p>
   * This is a helper class to simplify creation and use of the BRequest class.
   *
   * @param deviceRequest the enumerated device request type
   * @return the coded byte for a bRequest field
   */
  public static byte getInstance(EDeviceRequest deviceRequest) {
    return new BRequest(deviceRequest).getByteCode();
  }

  /**
   * Get the byte code for this enumerated Standard Device Request instance.
   * This is the value used in the {@code bRequest} field of a control-type USB
   * IRP (I/O Request Packet).
   * <p>
   * the descriptor type byte code is shifted to the high byte ) and the
   * descriptor index to the low byte.
   *
   * @return the coded byte for a bRequest field
   */
  public byte getByteCode() {
//    return (byte) ((deviceRequest.getByteCode() & 0xff) << 8 | (index & 0xff));
    return deviceRequest.getByteCode();
  }

  /**
   * Returns the DeviceRequest name or, if not defined, the bRequest BYTE hex
   * code value.
   *
   * @return the DeviceRequest name or a hexcode string.
   */
  @Override
  public String toString() {
    return deviceRequest != null ? deviceRequest.name() : ByteUtility.toHexString(bRequest);
  }
}
