/*
 * Copyright 2014 Jesse Caulfield <jesse@caulfield.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.usb.request;

import javax.usb.enumerated.EEndpointDirection;

/**
 * Control-type USB IRP (I/O Request Packet) helper class to set and get the
 * bmRequestType field.
 * <p>
 * The bmRequestType bit-mapped field identifies the characteristics of the
 * specific request.
 * <p>
 * Direction: identifies the direction of data transfer in the second phase of
 * the control transfer. For Host-to-device control IRPs the state of the
 * EEndpointDirection bit is ignored if the IRP wLength field is zero,
 * signifying there is no Data stage.
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
 * @author Jesse Caulfield
 */
public class BMRequestType {

  /**
   * The USB Device Request data transfer direction.
   * <p>
   * Developer note: For READING configurations off the device use
   * DEVICE_TO_HOST. For setting EEPROM configurations on the device use
   * HOST_TO_DEVICE.
   */
  private final EEndpointDirection direction;
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
   * The USB Device Request intended recipient.
   * <p>
   * Requests may be directed to the device, an interface on the device, or a
   * specific endpoint on a device. This field also specifies the intended
   * recipient of the request.
   */
  private final ERecipient recipient;

  public BMRequestType(EEndpointDirection direction, EType type, ERecipient recipient) {
    this.direction = direction;
    this.type = type;
    this.recipient = recipient;
  }

  public BMRequestType(byte bmRequestType) {
    this.direction = EEndpointDirection.fromByte(bmRequestType);
    this.type = EType.fromByte(bmRequestType);
    this.recipient = ERecipient.fromByte(bmRequestType);
  }

  /**
   * Get a standard device parameter READ request type. The returned
   * BMRequestType is configured as [DEVICE_TO_HOST, STANDARD, DEVICE].
   * <p>
   * This is a helper class to simplify creation and use of the BRequest class.
   * <p>
   * @return A Standard READ BMRequestType configuration encoded as a byte.
   */
  public static byte getInstanceStandardRead() {
    /**
     * Developer note: Get a STANDARD Request Type where the data will flow
     * DEVICE_TO_HOST and the control message recipient is the DEVICE.
     */
    return new BMRequestType(EEndpointDirection.DEVICE_TO_HOST, EType.STANDARD, ERecipient.DEVICE).getByteCode();
  }

  /**
   * Get a standard device parameter READ request type for the indicated
   * RECIPIENT.
   * <p>
   * @param recipient the desired recipient.
   * @return A Standard READ BMRequestType configuration encoded as a byte.
   */
  public static byte getInstanceStandardRead(ERecipient recipient) {
    return new BMRequestType(EEndpointDirection.DEVICE_TO_HOST, EType.STANDARD, recipient).getByteCode();
  }

  /**
   * Get a standard device parameter WRITE request type. The returned
   * BMRequestType is configured as [HOST_TO_DEVICE, STANDARD, DEVICE].
   * <p>
   * The recipient can be changed to INTERFACE or ENDPOINT if required.
   * <p>
   * This is a helper class to simplify creation and use of the BRequest class.
   * <p>
   * @return A Standard READ BMRequestType configuration encoded as a byte.
   */
  public static byte getInstanceStandardWrite() {
    /**
     * Developer note: Get a STANDARD Request Type where the data will flow
     * DEVICE_TO_HOST and the control message recipient is the DEVICE.
     */
    return new BMRequestType(EEndpointDirection.HOST_TO_DEVICE, EType.STANDARD, ERecipient.DEVICE).getByteCode();
  }

  /**
   * Get a standard device parameter WRITE request type. The returned
   * BMRequestType is configured as [HOST_TO_DEVICE, STANDARD, DEVICE].
   * <p>
   * The recipient can be changed to INTERFACE or ENDPOINT if required.
   * <p>
   * This is a helper class to simplify creation and use of the BRequest class.
   * <p>
   * @param recipient the desired recipient.
   * @return A Standard READ BMRequestType configuration encoded as a byte.
   */
  public static byte getInstanceStandardWrite(ERecipient recipient) {
    /**
     * Developer note: Get a STANDARD Request Type where the data will flow
     * DEVICE_TO_HOST and the control message recipient is the DEVICE.
     */
    return new BMRequestType(EEndpointDirection.HOST_TO_DEVICE, EType.STANDARD, recipient).getByteCode();
  }

  /**
   * Get this BMRequestType instance as a single BYTE. This encodes the
   * configuration into a byte.
   * <p>
   * @return the BMRequestType encoded as a byte.
   */
  public byte getByteCode() {
    return (byte) (direction.getByteCode() | type.getByteCode() | recipient.getByteCode());
  }

  /**
   * The USB Device Request data transfer direction.
   * <p>
   * @return DEVICE_TO_HOST is IN, HOST_TO_DEVICE is OUT
   */
  public EEndpointDirection getDirection() {
    return direction;
  }

  /**
   * The USB Device Request control type.
   * <p>
   * @return STANDARD, CLASS, VENDOR
   */
  public EType getType() {
    return type;
  }

  /**
   * The USB Device Request intended recipient.
   * <p>
   * @return DEVICE, INTERFACE, ENDPOINT, OTHER
   */
  public ERecipient getRecipient() {
    return recipient;
  }

  /**
   * The USB Device Request Type.
   * <p>
   * bmRequestType Type classifier bits D6...5.
   */
  public enum EType {

    /**
     * 0.00. Standard Device Requests.
     * <p>
     * All USB devices respond to standard device requests, even if the device
     * has not yet been assigned an address or has not been configured.
     */
    STANDARD((byte) 0x00),
    /**
     * USB Class type device requests. These apply to all instances of a given
     * USB Class. See EUSBClassCode for a list of enumerated USB Class
     * instances.
     */
    CLASS((byte) 0x20),
    /**
     * 0x40. Vendor specific device requests. Refer to vendor documentation of
     * your USB device for details.
     */
    VENDOR((byte) 0x40),
    /**
     * 0x60. Hands off for now!
     * <p>
     * @deprecated Marked as deprecated to discourage use by developers (bad
     * code).
     */
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
   * Requests may be directed to a device, an interface on the device, or to a
   * specific endpoint on a device.
   * <p>
   * This field specifies the intended recipient of the request. When an
   * interface is specified, the wIndex field identifies the interface. When an
   * endpoint is specified, the wIndex field identifies the endpoint.
   * <p>
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

  @Override
  public String toString() {
    return "[" + direction + "|" + type + "|" + recipient + ']';
  }

}
