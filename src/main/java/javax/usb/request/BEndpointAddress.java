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

import javax.usb.enumerated.EEndpointDirection;

/**
 * Helper class to encode and decode the 9.6.6 Standard Endpoint Descriptor
 * bEndpointAddress field.
 * <p>
 * The address of the endpoint on the USB device described by this descriptor.
 * The address is encoded as follows:
 * <pre> Bit 3...0: The endpoint number
 * Bit 6...4: Reserved, reset to zero
 * Bit 7    : Direction, (ignored for control endpoints)
 *            0 = HOST_TO_DEVICE (OUT) endpoint
 *            1 = DEVICE_TO_HOST (IN) endpoint}</pre> See Table 9-13. Standard Endpoint
 * Descriptor
 *
 * @author Jesse Caulfield
 */
public class BEndpointAddress implements Comparable<BEndpointAddress> {

  /**
   * The Endpoint number
   */
  private final int endPointNumber;
  /**
   * The Endpoint direction [IN, OUT]
   */
  private final EEndpointDirection direction;
  /**
   * The {@code bEndpointAddress} byte code value
   */
  private final byte byteCode;

  /**
   * Construct a BEndpointAddress instance
   *
   * @param endPointNumber the end point number (0 to 7)
   * @param direction      IN or OUT
   */
  public BEndpointAddress(int endPointNumber, EEndpointDirection direction) {
    this.endPointNumber = endPointNumber;
    this.direction = direction;
    this.byteCode = getByteCode();
  }

  /**
   * Construct a BEndpointAddress instance from a byte code value.
   *
   * @param bEndpointAddress The address of the endpoint on the USB device
   *                         described by this descriptor.
   */
  public BEndpointAddress(byte bEndpointAddress) {
    /**
     * Set the byte code.
     */
    this.byteCode = bEndpointAddress;
    /**
     * The bEndpointAddress is encoded with the endpoint number at Bit 3...0.
     */
    this.endPointNumber = bEndpointAddress & 0x07;
    /**
     * The bEndpointAddress is encoded with the direction at Bit 7 as 0 = OUT, 1
     * = IN
     */
    this.direction = EEndpointDirection.fromByte(bEndpointAddress);
  }

  /**
   * Static constructor to get a BEndpointAddress instance from a
   * {@code bEndpointAddress} byte code value.
   *
   * @param bEndpointAddress The address of the endpoint on the USB device
   *                         described by this descriptor.
   * @return a BEndpointAddress instance
   */
  public static BEndpointAddress getInstance(byte bEndpointAddress) {
    return new BEndpointAddress(bEndpointAddress);
  }

  /**
   * Get the BEndpointAddress as a byte.
   *
   * @return the BEndpointAddress encoded as a byte.
   */
  public final byte getByteCode() {
    /**
     * Left shift the direction to bit seven and then OR mask it with the end
     * point number.
     */
//    return (byte) (direction.getByteCode() | endPointNumber);
    return byteCode;
  }

  /**
   * Get the Endpoint direction
   *
   * @return the Endpoint direction
   */
  public EEndpointDirection getDirection() {
    return direction;
  }

  /**
   * Get the Endpoint number.
   *
   * @return the Endpoint number.
   */
  public int getEndPointNumber() {
    return endPointNumber;
  }

  @Override
  public int compareTo(BEndpointAddress o) {
    return Integer.compare(endPointNumber, o.getEndPointNumber());
  }

  @Override
  public String toString() {
    return //"BEndpointAddress"      +
            "endPointNumber [" + endPointNumber
            + "] direction [" + direction
            + ']';
  }

}
