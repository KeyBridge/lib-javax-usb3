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
 * An enumerated list of standard Descriptor Types available when using the
 * GET_DESCRIPTOR standard device request.
 * <p>
 * The actual request value field specifies the descriptor type in the high byte
 * (refer to Table 9-5) and the descriptor index in the low byte.
 */
public enum EDescriptorType {

  /**
   * 9.6.1 Device.
   * <p>
   * A device descriptor describes general information about a USB device. It
   * includes information that applies globally to the device and all of the
   * device’s configurations. A USB device has only one device descriptor.
   */
  DEVICE((byte) 0x01, 18),
  /**
   * 9.6.3 Configuration.
   * <p>
   * The configuration descriptor describes information about a specific device
   * configuration. The descriptor contains a bConfigurationValue field with a
   * value that, when used as a parameter to the SetConfiguration() request,
   * causes the device to assume the described configuration.
   */
  CONFIGURATION((byte) 0x02, 9),
  /**
   * 9.6.7 String.
   * <p>
   * String descriptors are optional. As noted previously, if a device does not
   * support string descriptors, all references to string descriptors within
   * device, configuration, and interface descriptors must be reset to zero.
   * <p>
   * Developer note: Minimum length is 2 bytes (bLength + bDescriptorType).
   * Actual length is the size of the (String) character array (in bytes) plus
   * 6. See Table 9-15 for encoding details.
   */
  STRING((byte) 0x03, 3),
  /**
   * 9.6.5 Interface.
   * <p>
   * The interface descriptor describes a specific interface within a
   * configuration. A configuration provides one or more interfaces, each with
   * zero or more endpoint descriptors describing a unique set of endpoints
   * within the configuration. When a configuration supports more than one
   * interface, the endpoint descriptors for a particular interface follow the
   * interface descriptor in the data returned by the GetConfiguration()
   * request. An interface descriptor is always returned as part of a
   * configuration descriptor. Interface descriptors cannot be directly accessed
   * with a GetDescriptor() or SetDescriptor() request.
   */
  INTERFACE((byte) 0x04, 9),
  /**
   * 9.6.6 Endpoint.
   * <p>
   * Each endpoint used for an interface has its own descriptor. This descriptor
   * contains the information required by the host to determine the bandwidth
   * requirements of each endpoint. An endpoint descriptor is always returned as
   * part of the configuration information returned by a
   * GetDescriptor(Configuration) request. An endpoint descriptor cannot be
   * directly accessed with a GetDescriptor() or SetDescriptor() request. There
   * is never an endpoint descriptor for endpoint zero.
   */
  ENDPOINT((byte) 0x05, 7),
  /**
   * 9.6.2 Device_Qualifier.
   * <p>
   * The device_qualifier descriptor describes information about a high-speed
   * capable device that would change if the device were operating at the other
   * speed. For example, if the device is currently operating at full-speed, the
   * device_qualifier returns information about how it would operate at
   * high-speed and vice-versa.
   */
  DEVICE_QUALIFIER((byte) 0x06, 10),
  /**
   * 9.6.4 Other_Speed_Configuration.
   * <p>
   * The other_speed_configuration descriptor describes a configuration of a
   * high- speed capable device if it were operating at its other possible
   * speed. The structure of the other_speed_configuration is identical to a
   * configuration descriptor.
   */
  OTHER_SPEED_CONFIGURATION((byte) 0x07, 9),
  /**
   * The INTERFACE_POWER descriptor is defined in the current revision of the
   * USB Interface Power Management Specification.
   */
  INTERFACE_POWER((byte) 0x08, 9);
  /**
   * The descriptor type byte code value. This is set in the
   */
  private final byte byteCode;
  /**
   * Standard descriptor length, in bytes. This is the actual byte array length
   * to be allocated.
   */
  private final int length;

  private EDescriptorType(byte byteCode, int length) {
    this.byteCode = byteCode;
    this.length = length;
  }

  /**
   * Get the EDescriptorType byte code. This is the byte value to write or read
   * from the Standard Device Request wValue field.
   * <p>
   * @return the EDescriptorType byte code
   */
  public byte getByteCode() {
    return byteCode;
  }

  /**
   * Get the Standard descriptor length, in bytes. This is minimum length of a
   * descriptor data array.
   * <p>
   * @return descriptor length, in bytes
   */
  public int getLength() {
    return length;
  }

}
