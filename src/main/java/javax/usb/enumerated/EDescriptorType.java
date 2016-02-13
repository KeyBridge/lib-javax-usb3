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
package javax.usb.enumerated;

/**
 * An enumerated list of standard Descriptor Types available when using the
 * GET_DESCRIPTOR standard device request.
 * <p>
 * The actual request value field specifies the descriptor type in the high byte
 * (refer to Table 9-5) and the descriptor index in the low byte.
 * <p>
 * Devices report their attributes using descriptors. A descriptor is a data
 * structure with a defined format. Each descriptor begins with a byte-wide
 * field that contains the total number of bytes in the descriptor followed by a
 * byte-wide field that identifies the descriptor type.
 * <p>
 * Using descriptors allows concise storage of the attributes of individual
 * configurations because each configuration may reuse descriptors or portions
 * of descriptors from other configurations that have the same characteristics.
 * In this manner, the descriptors resemble individual data records in a
 * relational database.
 * <p>
 * Where appropriate, descriptors contain references to string descriptors that
 * provide displayable information describing a descriptor in human-readable
 * form. The inclusion of string descriptors is optional. However, the reference
 * fields within descriptors are mandatory. If a device does not support string
 * descriptors, string reference fields shall be reset to zero to indicate no
 * string descriptor is available.
 * <p>
 * If a descriptor returns with a value in its length field that is less than
 * defined by this specification, the descriptor is invalid and should be
 * rejected by the host. If the descriptor returns with a value in its length
 * field that is greater than defined by this specification, the extra bytes are
 * ignored by the host, but the next descriptor is located using the length
 * returned rather than the length expected.
 *
 * @author Jesse Caulfield
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
  RESERVED_0x06((byte) 0x06, 0),
  RESERVED_0x07((byte) 0x07, 0),
  /**
   * 9.6.2 Device_Qualifier.
   * <p>
   * The device_qualifier descriptor describes information about a high-speed
   * capable device that would change if the device were operating at the other
   * speed. For example, if the device is currently operating at full-speed, the
   * device_qualifier returns information about how it would operate at
   * high-speed and vice-versa.
   *
   * @deprecated USB 2.0 descriptor removed in USB 3.1 specification
   */
  DEVICE_QUALIFIER((byte) 0x06, 10),
  /**
   * 9.6.4 Other_Speed_Configuration.
   * <p>
   * The other_speed_configuration descriptor describes a configuration of a
   * high- speed capable device if it were operating at its other possible
   * speed. The structure of the other_speed_configuration is identical to a
   * configuration descriptor.
   *
   * @deprecated USB 2.0 descriptor removed in USB 3.1 specification
   */
  OTHER_SPEED_CONFIGURATION((byte) 0x07, 9),
  /**
   * The INTERFACE_POWER descriptor is defined in the current revision of the
   * USB Interface Power Management Specification.
   */
  INTERFACE_POWER((byte) 0x08, 9),
  /**
   * @deprecated USB 3.1 feature is not yet implemented in this project.
   */
  OTG((byte) 0x09, 0),
  /**
   * @deprecated USB 3.1 feature is not yet implemented in this project.
   */
  DEBUG((byte) 0x0a, 0),
  /**
   * 9.6.4 Interface Association
   * <p>
   * The Interface Association Descriptor is used to describe that two or more
   * interfaces are associated to the same function. An “association” includes
   * two or more interfaces and all of their alternate setting interfaces. A
   * device must use an Interface Association descriptor for each device
   * function that requires more than one interface. An Interface Association
   * descriptor is always returned as part of the configuration information
   * returned by a GetDescriptor(Configuration) request. An interface
   * association descriptor cannot be directly accessed with a GetDescriptor()
   * or SetDescriptor() request.
   * <p>
   * An interface association descriptor must be located before the set of
   * interface descriptors (including all alternate settings) for the interfaces
   * it associates. All of the interface numbers in the set of associated
   * interfaces must be contiguous. Table 9-22 shows the standard interface
   * association descriptor. The interface association descriptor includes
   * function class, subclass, and protocol fields. The values in these fields
   * can be the same as the interface class, subclass, and protocol values from
   * any one of the associated interfaces. The preferred implementation, for
   * existing device classes, is to use the interface class, subclass, and
   * protocol field values from the first interface in the list of associated
   * interfaces.
   *
   * @deprecated USB 3.1 feature is not yet implemented in this project.
   */
  INTERFACE_ASSOCIATION((byte) 0x0b, 8),
  /**
   * 9.6.2 Binary Device Object Store (BOS)
   * <p>
   * The BOS descriptor defines a root descriptor that is similar to the
   * configuration descriptor, and is the base descriptor for accessing a family
   * of related descriptors. A host can read a BOS descriptor and learn from the
   * wTotalLength field the entire size of the device-level descriptor set, or
   * it can read in the entire BOS descriptor set of device capabilities. The
   * host accesses this descriptor using the GetDescriptor() request. The
   * descriptor type in the GetDescriptor() request is set to BOS (see Table
   * 9-12). There is no way for a host to read individual device capability
   * descriptors. The entire set can only be accessed via reading the BOS
   * descriptor with a GetDescriptor() request and using the length reported in
   * the wTotalLength field.
   * <p>
   * Individual technology-specific or generic device-level capabilities are
   * reported via Device Capability descriptors. The format of the Device
   * Capability descriptor is defined in Table 9-13. The Device Capability
   * descriptor has a generic header, with a sub-type field (bDevCapabilityType)
   * which defines the layout of the remainder of the descriptor. The codes for
   * bDevCapabilityType are defined in Table 9-14 of the USB 3.1 spec.
   */
  BOS((byte) 0x0f, 5),
  /**
   * 9.6.2 Binary Device Object Store (BOS) Device Capability Descriptor
   * <p>
   * Table 9-13. Format of a Device Capability Descriptor
   * <p>
   * Device Capability descriptors are always returned as part of the BOS
   * information returned by a GetDescriptor(BOS) request. A Device Capability
   * cannot be directly accessed with a GetDescriptor() or SetDescriptor()
   * request.
   *
   * @see EBOSDeviceCapabilityType
   */
  DEVICE_CAPABILITY((byte) 0x10, 4),
  /**
   * 9.6.7 SuperSpeed Endpoint Companion
   * <p>
   * This descriptor shall only be returned by Enhanced SuperSpeed devices that
   * are operating at Gen X speed. Each endpoint described in an interface is
   * followed by a SuperSpeed Endpoint Companion descriptor. This descriptor is
   * returned as part of the configuration information returned by a
   * GetDescriptor(Configuration) request and cannot be directly accessed with a
   * GetDescriptor() or SetDescriptor() request. The Default Control Pipe does
   * not have an Endpoint Companion descriptor. The Endpoint Companion
   * descriptor shall immediately follow the endpoint descriptor it is
   * associated with in the configuration information.
   *
   * @deprecated USB 3.1 feature is not yet implemented in this project.
   */
  SUPERSPEED_USB_ENDPOINT_COMPANION((byte) 0x30, 5),
  /**
   * 9.6.8 SuperSpeedPlus Isochronous Endpoint Companion
   * <p>
   * This descriptor contains additional endpoint characteristics that are only
   * defined for endpoints of devices operating at above Gen 1 speed. This
   * descriptor shall only be returned (as part of the devices’ complete
   * configuration descriptor) by an Enhanced SuperSpeed device that is
   * operating at above Gen 1 speed. This descriptor shall be returned for each
   * Isochronous endpoint that requires more than 48K bytes per Service
   * Interval.
   * <p>
   * This descriptor is returned as part of the configuration information
   * returned by a GetDescriptor(Configuration) request and cannot be directly
   * accessed with a GetDescriptor() or SetDescriptor() request.
   * <p>
   * The SuperSpeedPlus Isochronous Endpoint Companion descriptor shall
   * immediately follow the SuperSpeed Endpoint Companion descriptor that
   * follows the Isochronous endpoint descriptor in the configuration
   * information.
   * <p>
   * When an alternate setting is selected that has an Isochronous endpoint that
   * has a SuperSpeedPlus Isochronous Endpoint Companion descriptor the endpoint
   * shall operate with the characteristics as described in the SuperSpeedPlus
   * Isochronous Endpoint Companion descriptor.
   *
   * @deprecated USB 3.1 feature is not yet implemented in this project.
   */
  SUPERSPEEDPLUS_ISOCHRONOUS_ENDPOINT_COMPANION((byte) 0x31, 9);
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
   *
   * @return the EDescriptorType byte code
   */
  public byte getByteCode() {
    return byteCode;
  }

  /**
   * Get the Standard descriptor length, in bytes. This is minimum length of a
   * descriptor data array.
   *
   * @return descriptor length, in bytes
   */
  public int getLength() {
    return length;
  }

  /**
   * Get the {@code wValue} encoded byte for a GET_DESCRIPTOR query.
   *
   * @param index The descriptor index. The range is 0 to one less than the
   *              number of descriptors of the indicated type.
   * @return the coded byte for a bRequest field
   */
  public byte getWValue(byte index) {
    return (byte) ((byteCode & 0xff) << 8 | (index & 0xff));
  }

  /**
   * Get an enumerated EDescriptorType to match the input byte code.
   *
   * @param bytecode the bytecode to match
   * @return the corresponding EDescriptorType instance
   */
  public static EDescriptorType fromBytecode(byte bytecode) {
    for (EDescriptorType eDescriptorType : EDescriptorType.values()) {
      if (eDescriptorType.getByteCode() == bytecode) {
        return eDescriptorType;
      }
    }
    return null;
  }

}
