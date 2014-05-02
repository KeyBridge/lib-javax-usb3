/*
 * Copyright 2014 Jesse Caulfield <jesse@caulfield.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE_2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.usb.ri.request;

import javax.usb.util.ByteUtil;

/**
 * Control-type USB IRP (I/O Request Packet) helper class to set and get the
 * bmRequestType field.
 * <p>
 * The bmRequestType bit-mapped field identifies the characteristics of the
 * specific request.
 * <p>
 * Direction: identifies the direction of data transfer in the second phase of
 * the control transfer. For Host-to-device control IRPs the state of the
 * EDirection bit is ignored if the IRP wLength field is zero, signifying there
 * is no Data stage.
 * <p>
 * Type: The USB Specification defines a series of standard requests that all
 * devices must support. These are enumerated in Table 9_3. In addition, a
 * device class may define additional requests. A device vendor may also define
 * requests supported by the device.
 * <p>
 * Recipient: Requests may be directed to the device, an interface on the
 * device, or a specific endpoint on a device. This field also specifies the
 * intended recipient of the request. When an interface or endpoint is
 * specified, the wIndex field identifies the interface or endpoint.
 * <p>
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
public class BMRequestType {

  /**
   * The USB Device Request data transfer direction.
   * <p>
   * Developer note: For READING configurations off the device use
   * DEVICE_TO_HOST. For setting EEPROM configurations on the device use
   * HOST_TO_DEVICE.
   */
  private final EDirection direction;
  /**
   * The USB Device Request control type.
   * <p>
   * The USB Specification defines a series of standard requests that all
   * devices must support. In addition, a device class may define additional
   * requests. A device vendor may also define requests supported by the device.
   * <p>
   * Standard control requests may be instantiated by calling the getInstance()
   * method of this class.
   */
  private final EType type;
  /**
   * The USB Device Request intended recipient. Requests may be directed to the
   * device, an interface on the device, or a specific endpoint on a device.
   * This field also specifies the intended recipient of the request.
   */
  private final ERecipient recipient;

  public BMRequestType(EDirection direction, EType type, ERecipient recipient) {
    this.direction = direction;
    this.type = type;
    this.recipient = recipient;
  }

  public BMRequestType(byte bmRequestType) {
    this.direction = EDirection.fromByte(bmRequestType);
    this.type = EType.fromByte(bmRequestType);
    this.recipient = ERecipient.fromByte(bmRequestType);
  }

  /**
   * Get a standard device parameter READ request type. The returned
   * BMRequestType is configured as [DEVICE_TO_HOST, STANDARD, DEVICE].
   * <p>
   * @return A Standard READ BMRequestType configuration.
   */
  public static BMRequestType getStandardReadInstance() {
    /**
     * Developer note: Get a STANDARD Request Type where the data will flow
     * DEVICE_TO_HOST and the control message recipient is the DEVICE.
     */
    return new BMRequestType(EDirection.DEVICE_TO_HOST, EType.STANDARD, ERecipient.DEVICE);
  }

  /**
   * Get a standard device parameter WRITE request type. The returned
   * BMRequestType is configured as [HOST_TO_DEVICE, STANDARD, DEVICE].
   * <p>
   * The recipient can be changed to INTERFACE or ENDPOINT if required.
   * <p>
   * @return A Standard READ BMRequestType configuration.
   */
  public static BMRequestType getStandardWriteInstance() {
    /**
     * Developer note: Get a STANDARD Request Type where the data will flow
     * DEVICE_TO_HOST and the control message recipient is the DEVICE.
     */
    return new BMRequestType(EDirection.HOST_TO_DEVICE, EType.STANDARD, ERecipient.DEVICE);
  }

  /**
   * Get this BMRequestType instance as a single BYTE. This encodes the
   * configuration into a byte.
   * <p>
   * @return the BMRequestType encoded as a byte.
   */
  public byte asByte() {
    return (byte) (direction.getByteCode() | type.getByteCode() | recipient.getByteCode());
  }

  /**
   * bmRequestType Data transfer direction encoded in bit D7.
   */
  public enum EDirection {

    DEVICE_TO_HOST((byte) 0x80), // in
    HOST_TO_DEVICE((byte) 0x00); // out
    private final byte byteCode;
    private static final byte MASK = (byte) 0x80;

    private EDirection(byte byteCode) {
      this.byteCode = byteCode;
    }

    /**
     * Get the Type from a bmRequestType byte.
     * <p>
     * @param bmRequestType the bmRequestType byte
     * @return The bmRequestType Type
     */
    public static EDirection fromByte(byte bmRequestType) {
      return ByteUtil.getBit(bmRequestType, 7) == 1 ? DEVICE_TO_HOST : HOST_TO_DEVICE;
    }

    /**
     * Get the enumerated instance as a byte.
     * <p>
     * @return a byte with the bits set to the corresponding configuration
     */
    public byte getByteCode() {
      return byteCode;
    }
  }

  /**
   * bmRequestType Type classifier bits D6...5.
   */
  public enum EType {

    STANDARD((byte) 0x00),
    CLASS((byte) 0x20),
    VENDOR((byte) 0x40),
    RESERVED((byte) 0x60);
    private final byte byteCode;
    private static final byte MASK = (byte) 0x60;

    private EType(byte byteCode) {
      this.byteCode = byteCode;
    }

    /**
     * Get the Type from a bmRequestType byte.
     * <p>
     * @param bmRequestType the bmRequestType byte
     * @return The bmRequestType Type
     */
    public static EType fromByte(byte bmRequestType) {
      for (EType eType : EType.values()) {
        if ((bmRequestType & MASK) == eType.byteCode) {
          return eType;
        }
      }
      return null;
    }

    /**
     * Get the enumerated instance as a byte.
     * <p>
     * @return a byte with the bits set to the corresponding configuration
     */
    public byte getByteCode() {
      return byteCode;
    }
  }

  /**
   * bmRequestType Recipient indicator bits D4...0.
   */
  public enum ERecipient {

    DEVICE((byte) 0x00),
    INTERFACE((byte) 0x01),
    ENDPOINT((byte) 0x02),
    OTHER((byte) 0x03);
    private final byte byteCode;
    /**
     * 0x1f = 0001 1111 = right 5 bits.
     */
    private static final byte MASK = (byte) 0x1f;

    private ERecipient(byte byteCode) {
      this.byteCode = byteCode;
    }

    /**
     * Get the Type from a bmRequestType byte.
     * <p>
     * @param bmRequestType the bmRequestType byte
     * @return The bmRequestType Type
     */
    public static ERecipient fromByte(byte bmRequestType) {
      for (ERecipient eType : ERecipient.values()) {
        if ((bmRequestType & MASK) == eType.byteCode) {
          return eType;
        }
      }
      return null;
    }

    /**
     * Get the enumerated instance as a byte.
     * <p>
     * @return a byte with the bits set to the corresponding configuration
     */
    public byte getByteCode() {
      return byteCode;
    }
  }

}
