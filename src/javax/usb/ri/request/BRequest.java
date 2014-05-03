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

import javax.usb.ri.enumerated.EDeviceRequest;

/**
 * Control-type USB IRP (I/O Request Packet) helper class to set and get the
 * bmRequest field.
 * <p>
 * This is basically a wrapper of the EDeviceRequest enumerated list, but
 * renamed to match the control type IRP field to minimize developer confusion.
 * <p>
 * @author Jesse Caulfield <jesse@caulfield.org>
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
//  private final byte index;
  /**
   * Construct a default BRequest instance for the indicated request type. The
   * index value is set to ZERO.
   * <p>
   * @param deviceRequest a Standard Device Requests instance
   */
  public BRequest(EDeviceRequest deviceRequest) {
    this.deviceRequest = deviceRequest;
  }

  /**
   * Construct a default BRequest instance for the indicated request type and
   * descriptor index.
   * <p>
   * @param deviceRequest a Standard Device Requests instance
   * @param index         The descriptor index. The range is 0 to one less than
   *                      the number of descriptors of the indicated type.
   */
//  public BRequest(EDeviceRequest deviceRequest, final byte index) {
//    this.deviceRequest = deviceRequest;
//    this.index = index;
//  }
  /**
   * Get a BRequest instance as a byte code. The default INDEX value of zero is
   * used.
   * <p>
   * This is a helper class to simplify creation and use of the BRequest class.
   * <p>
   * @param deviceRequest the enumerated device request type
   * @return the coded byte for a bRequest field
   */
  public static byte getInstance(EDeviceRequest deviceRequest) {
    return new BRequest(deviceRequest).getByteCode();
  }

  /**
   * Get a BRequest instance as a byte code.
   * <p>
   * This is a helper class to simplify creation and use of the BRequest class.
   * <p>
   * @param deviceRequest the enumerated device request type
   * @param index         The descriptor index. The range is 0 to one less than
   *                      the number of descriptors of the indicated type.
   * @return the coded byte for a bRequest field
   */
//  public static byte getInstance(EDeviceRequest deviceRequest, final byte index) {
//    return new BRequest(deviceRequest, index).getByteCode();
//  }
  /**
   * Get the byte code for this enumerated Standard Device Request instance.
   * This is the value used in the <code>bRequest</code> field of a control-type
   * USB IRP (I/O Request Packet).
   * <p>
   * the descriptor type byte code is shifted to the high byte ) and the
   * descriptor index to the low byte.
   * <p>
   * @return the coded byte for a bRequest field
   */
  public byte getByteCode() {
//    return (byte) ((deviceRequest.getByteCode() & 0xff) << 8 | (index & 0xff));
    return deviceRequest.getByteCode();
  }
}
