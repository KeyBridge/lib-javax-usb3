/*
 * Copyright 2014 Jesse Caulfield .
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
package javax.usb.enumerated;

/**
 * Enumerated list of Standard Device Requests.
 * <p>
 * See Table 9-3 of USB 2.0 and Table 9.4 of USB 3.1.
 * <p>
 * The USB Specification defines a series of standard requests that all devices
 * must support. In addition, a device class may define additional requests. A
 * device vendor may also define requests supported by the device. USB devices
 * must respond to standard device requests, even if the device has not yet been
 * assigned an address or has not been configured.
 * <p>
 * This helper class simplifies accessing the {@code bRequest} field in
 * control-type USB IRP (I/O Request Packet).
 * <p>
 * The {@code bRequest} field specifies the particular request. The Type bits in
 * the bmRequestType field modify the meaning of this field. The USB
 * specification defines values for the bRequest field only when the bits are
 * reset to zero, indicating a standard request
 *
 * @author Jesse Caulfield
 */
public enum EDeviceRequest {
  // USB 2.0 Standard Request Codes Table 9-3
  /**
   * This request returns status for the specified recipient.
   * <ul><li>bmRequestType : DEVICE_TO_HOST [DEVICE, INTERFACE, ENDPOINT] </li>
   * <li>bRequest : GET_STATUS </li>
   * <li>wValue : Zero</li>
   * <li>wIndex : DEVICE, INTERFACE, ENDPOINT</li>
   * <li>wLength : Two</li>
   * <li>Data : Device,Interface, orEndpointStatus</li></ul>
   * The Recipient bits of the bmRequestType field specify the desired
   * recipient. The data returned is the current status of the specified
   * recipient.
   * <p>
   * If wValue or wLength are not as specified as follows or if wIndex is
   * non-zero for a device status request, then the behavior of the device is
   * not specified: <ul>
   * <li>wValue : zero</li>
   * <li>wIndex : Zero, ERecipient.Interface, ERecipient.Endpoint] </li>
   * <li>wLength : Two </li>
   * <li>Data : Device, Interface, or Endpoint Status </li></ul>
   * <p>
   * If an interface or an endpoint is specified that does not exist, then the
   * device responds with a Request Error. A GetStatus() request to a device
   * returns bit-coded information as follows: D0 = Self Powered, D1 = Remote
   * Wakeup, D2 ... D15 Reserved (Reset to zero).
   * <p>
   * The Self Powered field indicates whether the device is currently
   * self-powered. If D0 is reset to zero, the device is bus-powered. If D0 is
   * set to one, the device is self-powered. The Self Powered field may not be
   * changed by the SetFeature() or ClearFeature() requests.
   * <p>
   * The Remote Wakeup field indicates whether the device is currently enabled
   * to request remote wakeup. The default mode for devices that support remote
   * wakeup is disabled. The Remote Wakeup field can be modified by the
   * SetFeature() and ClearFeature() requests using the DEVICE_REMOTE_WAKEUP
   * feature selector. This field is reset to zero when the device is reset.
   * <p>
   * A GetStatus() request to an interface returns ALL ZEROES.
   * <p>
   * A GetStatus() request to an endpoint returns bit-coded information as
   * follows: D0: Halt, D1 ... D15: Reserved (Reset to zero). The Halt feature
   * is required to be implemented for all interrupt and bulk endpoint types.
   * When set by the SetFeature() request, the endpoint exhibits the same stall
   * behavior as if the field had been set by a hardware condition.
   */
  GET_STATUS((byte) 0x00),
  /**
   * This request is used to clear or disable a specific feature.
   * <ul><li>bmRequestType : HOST_TO_DEVICE [DEVICE, INTERFACE, ENDPOINT] </li>
   * <li>bRequest : CLEAR_FEATURE </li>
   * <li>wValue : EFeatureSelector </li>
   * <li>wIndex : DEVICE, INTERFACE, ENDPOINT</li>
   * <li>wLength : Zero </li>
   * <li>Data : None </li></ul>
   * Feature selector values in wValue must be appropriate to the recipient.
   * Only device feature selector values may be used when the recipient is a
   * device, only interface feature selector values may be used when the
   * recipient is an interface, and only endpoint feature selector values may be
   * used when the recipient is an endpoint.
   * <p>
   * Refer to EFeatureSelector.recipient for a definition of which feature
   * selector values are defined for which recipients.
   * <p>
   * A ClearFeature() request that references a feature that cannot be cleared,
   * that does not exist, or that references an interface or endpoint that does
   * not exist, will cause the device to respond with a Request Error.
   * <p>
   * If wLength is non-zero, then the device behavior is not specified.
   */
  CLEAR_FEATURE((byte) 0x01),
  /**
   * @deprecated Reserved for future use.
   */
  RESERVED_0x02((byte) 0x02),
  /**
   * This request is used to set or enable a specific feature.
   * <ul><li>bmRequestType : HOST_TO_DEVICE [DEVICE, INTERFACE, ENDPOINT] </li>
   * <li>bRequest : SET_FEATURE </li>
   * <li>wValue : EFeatureSelector </li>
   * <li>wIndex : TEST_MODE, DEVICE, INTERFACE, ENDPOINT</li>
   * <li>wLength : Zero </li>
   * <li>Data : None </li></ul>
   * Feature selector values in wValue must be appropriate to the recipient.
   * Only device feature selector values may be used when the recipient is a
   * device; only interface feature selector values may be used when the
   * recipient is an interface, and only endpoint feature selector values may be
   * used when the recipient is an endpoint.
   * <p>
   * Refer to ERecipient.recipient value for a definition of which feature
   * selector values are defined for which recipients.
   * <p>
   * A SetFeature() request that references a feature that cannot be set or that
   * does not exist causes a STALL to be returned in the Status stage of the
   * request.
   * <p>
   * If wLength is non-zero, then the behavior of the device is not specified.
   * <p>
   * If an endpoint or interface is specified that does not exist, then the
   * device responds with a Request Error.
   * <p>
   * Developer note: Refer to the USB specification for Test Mode Selectors and
   * a discussion of TEST_MODE feature enablement. TEST_MODE Selectors are not
   * enumerated here.
   */
  SET_FEATURE((byte) 0x03),
  /**
   * @deprecated Reserved for future use.
   */
  RESERVED_0x04((byte) 0x04),
  /**
   * This request sets the device address for all future device accesses.
   * <ul><li>bmRequestType : HOST_TO_DEVICE [DEVICE] </li>
   * <li>bRequest : SET_ADDRESS </li>
   * <li>wValue : Device Address </li>
   * <li>wIndex : DEVICE </li>
   * <li>wLength : Zero </li>
   * <li>Data : None </li></ul>
   * The wValue field specifies the device address to use for all subsequent
   * accesses. wIndex is always zero. wLength is always zero. Data is null/not
   * used.
   * <p>
   * Set Address Requests actually may result in up to three stages. In the
   * first stage, the Setup packet is sent to the device. In the optional second
   * stage, data is transferred between the host and the device. In the final
   * stage, status is transferred between the host and the device. The direction
   * of data and status transfer depends on whether the host is sending data to
   * the device or the device is sending data to the host. The Status stage
   * transfer is always in the opposite direction of the Data stage. If there is
   * no Data stage, the Status stage is from the device to the host.
   * <p>
   * Stages after the initial Setup packet assume the same device address as the
   * Setup packet. The USB device does not change its device address until after
   * the Status stage of this request is completed successfully. Note that this
   * is a difference between this request and all other requests. For all other
   * requests, the operation indicated must be completed before the Status
   * stage.
   * <p>
   * If the specified device address is greater than 127, or if wIndex or
   * wLength are non-zero, then the behavior of the device is not specified.
   */
  SET_ADDRESS((byte) 0x05),
  /**
   * Get Descriptor. This request returns the specified descriptor if the
   * descriptor exists
   * <ul><li>bmRequestType : DEVICE_TO_HOST [DEVICE] </li>
   * <li>bRequest : GET_DESCRIPTOR </li>
   * <li>wValue : Descriptor Type and Descriptor Index </li>
   * <li>wIndex : Zero or Language ID </li>
   * <li>wLength : Descriptor Length </li>
   * <li>Data : Descriptor </li></ul>
   * The wValue field specifies the descriptor type in the high byte (refer to
   * EDescriptorType.asByte()) and the descriptor index in the low byte. The
   * descriptor index is used to select a specific descriptor (only for
   * configuration and string descriptors) when several descriptors of the same
   * type are implemented in a device. The range of values used for a descriptor
   * index is from 0 to one less than the number of descriptors of that type
   * implemented by the device.
   * <p>
   * The wIndex field specifies the Language ID for string descriptors or is
   * reset to zero for other descriptors.
   * <p>
   * The wLength field specifies the number of bytes to return.
   * <p>
   * The standard request to a device supports three types of descriptors:
   * device (also device_qualifier), configuration (also
   * other_speed_configuration), and string.
   * <p>
   * A high-speed capable device supports the device_qualifier descriptor to
   * return information about the device for the speed at which it is not
   * operating (including wMaxPacketSize for the default endpoint and the number
   * of configurations for the other speed). The other_speed_configuration
   * returns information in the same structure as a configuration descriptor,
   * but for a configuration if the device were operating at the other speed. A
   * request for a configuration descriptor returns the configuration
   * descriptor, all interface descriptors, and endpoint descriptors for all of
   * the interfaces in a single request. The first interface descriptor follows
   * the configuration descriptor. The endpoint descriptors for the first
   * interface follow the first interface descriptor. If there are additional
   * interfaces, their interface descriptor and endpoint descriptors follow the
   * first interface’s endpoint descriptors. Class-specific and/or
   * vendor-specific descriptors follow the standard descriptors they extend or
   * modify.
   * <p>
   * All devices must provide a device descriptor and at least one configuration
   * descriptor. If a device does not support a requested descriptor, it
   * responds with a Request Error.
   */
  GET_DESCRIPTOR((byte) 0x06),
  /**
   * This request is optional and may be used to update existing descriptors or
   * new descriptors may be added.
   * <ul><li>bmRequestType : HOST_TO_DEVICE [DEVICE] </li>
   * <li>bRequest : SET_DESCRIPTOR </li>
   * <li>wValue : Descriptor Type and Descriptor Index </li>
   * <li>wIndex : Zero or Language ID </li>
   * <li>wLength : Descriptor Length </li>
   * <li>Data : Descriptor </li></ul>
   * The wValue field specifies an EDescriptorType type in the high byte and the
   * descriptor index in the low byte. The only allowed values for descriptor
   * type are DEVICE, CONFIGURATION, and STRING types.
   * <p>
   * The wIndex field specifies the Language ID for string descriptors or is
   * reset to zero for other descriptors. The wLength field specifies the number
   * of bytes to transfer from the host to the device.
   * <p>
   * If this request is not supported, the device will respond with a Request
   * Error.
   * <p>
   */
  SET_DESCRIPTOR((byte) 0x07),
  /**
   * This request returns the current device configuration value.
   * <ul><li>bmRequestType : DEVICE_TO_HOST [DEVICE] </li>
   * <li>bRequest : GET_CONFIGURATION </li>
   * <li>wValue : Zero </li>
   * <li>wIndex : Zero </li>
   * <li>wLength : One </li>
   * <li>Data : Configuration Value </li></ul>
   * If the returned value is zero, the device is not configured. If wValue,
   * wIndex, or wLength are not valid then the device behavior is not specified.
   */
  GET_CONFIGURATION((byte) 0x08),
  /**
   * This request sets the device configuration.
   * <ul><li>bmRequestType : HOST_TO_DEVICE [DEVICE] </li>
   * <li>bRequest : SET_CONFIGURATION </li>
   * <li>wValue : Configuration Value </li>
   * <li>wIndex : Zero </li>
   * <li>wLength : Zero </li>
   * <li>Data : None </li></ul>
   * The lower byte of the wValue field specifies the desired configuration.
   * This configuration value must be zero or match a configuration value from a
   * configuration descriptor. If the configuration value is zero, the device is
   * placed in its Address state. The upper byte of the wValue field is
   * reserved.
   * <p>
   * If wIndex, wLength, or the upper byte of wValue is non-zero, then the
   * behavior of this request is not specified.
   */
  SET_CONFIGURATION((byte) 0x09),
  /**
   * This request returns the selected alternate setting for the specified
   * interface.
   * <ul><li>bmRequestType : DEVICE_TO_HOST [INTERFACE] </li>
   * <li>bRequest : GET_INTERFACE </li>
   * <li>wValue : Zero </li>
   * <li>wIndex : Interface </li>
   * <li>wLength : One </li>
   * <li>Data : Alternate Setting </li></ul>
   * Some USB devices have configurations with interfaces that have mutually
   * exclusive settings. This request allows the host to determine the currently
   * selected alternate setting.
   * <p>
   * If wValue or wLength are not valid then the device behavior is not
   * specified.
   * <p>
   * If the interface specified does not exist, then the device responds with a
   * Request Error.
   */
  GET_INTERFACE((byte) 0x0a),
  /**
   * This request allows the host to select an alternate setting for the
   * specified interface.
   * <ul><li>bmRequestType : HOST_TO_DEVICE [INTERFACE] </li>
   * <li>bRequest : SET_INTERFACE </li>
   * <li>wValue : Alternate Setting </li>
   * <li>wIndex : Interface </li>
   * <li>wLength : Zero </li>
   * <li>Data : None </li></ul>
   * Some USB devices have configurations with interfaces that have mutually
   * exclusive settings. This request allows the host to select the desired
   * alternate setting. If a device only supports a default setting for the
   * specified interface, then a STALL may be returned in the Status stage of
   * the request. This request cannot be used to change the set of configured
   * interfaces (the SetConfiguration() request must be used instead).
   * <p>
   * If the interface or the alternate setting does not exist, then the device
   * responds with a Request Error. If wLength is non-zero, then the behavior of
   * the device is not specified.
   */
  SET_INTERFACE((byte) 0x0b),
  /**
   * This request is used to set and then report an endpoint’s synchronization
   * frame.
   * <ul><li>bmRequestType : HOST_TO_DEVICE [ENDPOINT] </li>
   * <li>bRequest : SYNCH_FRAME </li>
   * <li>wValue : Zero </li>
   * <li>wIndex : Endpoint </li>
   * <li>wLength : Two </li>
   * <li>Data : Frame Number </li></ul>
   * When an endpoint supports isochronous transfers, the endpoint may also
   * require per-frame transfers to vary in size according to a specific
   * pattern. The host and the endpoint must agree on which frame the repeating
   * pattern begins. The number of the frame in which the pattern began is
   * returned to the host.
   * <p>
   * If a high-speed device supports the Synch Frame request, it must internally
   * synchronize itself to the zeroth microframe and have a time notion of
   * classic frame. Only the frame number is used to synchronize and reported by
   * the device endpoint (i.e., no microframe number). The endpoint must
   * synchronize to the zeroth microframe.
   * <p>
   * This value is only used for isochronous data transfers using implicit
   * pattern synchronization. If wValue is non-zero or wLength is not two, then
   * the behavior of the device is not specified.
   * <p>
   * If the specified endpoint does not support this request, then the device
   * will respond with a Request Error.
   */
  SYNCH_FRAME((byte) 0x0c),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  SET_ENCRYPTION((byte) 0x0d),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  GET_ENCRYPTION((byte) 0x0e),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  SET_HANDSHAKE((byte) 0x0f),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  GET_HANDSHAKE((byte) 0x10),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  SET_CONNECTION((byte) 0x11),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  SET_SECURITY_DATA((byte) 0x12),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  GET_SECURITY_DATA((byte) 0x13),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  SET_WUSB_DATA((byte) 0x14),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  LOOPBACK_DATA_WRITE((byte) 0x15),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  LOOPBACK_DATA_READ((byte) 0x16),
  /**
   * Undocumented. USB 3.1 Standard Request Codes Table 9-5
   */
  SET_INTERFACE_DS((byte) 0x17),
  /**
   * 9.4.12 Set System Exit Latency (SEL)
   * <ul><li>bmRequestType : HOST_TO_DEVICE [DEVICE] </li>
   * <li>bRequest : SET_SEL </li>
   * <li>wValue : Zero </li>
   * <li>wIndex : Interface </li>
   * <li>wLength : Six </li>
   * <li>Data : Exit Latency Values </li></ul>
   * This request sets both the U1 and U2 System Exit Latency and the U1 or U2
   * exit latency for all the links between a device and a root port on the
   * host.
   * <p>
   * The latency values are sent to the device in the data stage of the control
   * transfer in the following format:
   * <pre>
   * Offset Name   Meaning
   * 0      U1SEL  Time in μs for U1 System Exit Latency
   * 1      U1PEL  Time in μs for U1 Device to Host Exit Latency
   * 2      U2SEL  Time in μs for U2 System Exit Latency
   * 4      U2PEL  Time in μs for U2 Device to Host Exit Latency
   * </pre>
   * <p>
   * The U1SEL and U2SEL values represent the total round trip path latency when
   * transitioning the links between the device and host from U1 or U2
   * respectively to U0 under worst-case circumstances when the transition is
   * initiated by the device. This is the sum of times t1, t2, and t4.
   * <p>
   * The U1PEL and U2PEL values represent the device to host latency to
   * transition the entire path of links between the device and host from U1 or
   * U2 respectively to U0 under worst-case circumstances when the transition is
   * initiated by the device. This time includes only t1.
   */
  SET_SEL((byte) 0x30),
  /**
   * 9.4.11 Set Isochronous Delay
   * <ul><li>bmRequestType : HOST_TO_DEVICE [DEVICE] </li>
   * <li>bRequest : SET_ISOCH_DELAY </li>
   * <li>wValue : Delay in ns </li>
   * <li>wIndex : Zero </li>
   * <li>wLength : Zero </li>
   * <li>Data : None </li></ul>
   * This request informs the device of the delay from the time a host transmits
   * a packet to the time it is received by the device.
   * <p>
   * The wValue field specifies a delay from 0 to 65535 ns. This delay
   * represents the time from when the host starts transmitting the first
   * framing symbol of the packet to when the device receives the first framing
   * symbol of that packet. The wValue field shall be calculated as follows.
   * <p>
   * {@code wValue = (sum of wHubDelay values) + (tTPTransmissionDelay * (number of hubs + 1))}
   * <p>
   * Where, a wHubDelay value is provided by the Enhanced SuperSpeed Hub
   * Descriptor of each hub in the path, respectively, and tTPTransmissionDelay
   * is defined in Table 8-35 of the USB 3.1 spec.
   */
  SET_ISOCH_DELAY((byte) 0x31);

  private final byte byteCode;

  private EDeviceRequest(byte byteCode) {
    this.byteCode = byteCode;
  }

  /**
   * Get the byte code for this enumerated Standard Device Request instance.
   * This is the value used in the {@code bRequest} field of a control-type USB
   * IRP (I/O Request Packet).
   *
   * @return the byte code for the bRequest field
   */
  public byte getByteCode() {
    return byteCode;
  }

  /**
   * Get an enumerated EDeviceRequest from its corresponding byte code value.
   * <p>
   * See Table 9-5 Standard Request Codes in the USB 3.1 specification for a
   * list of of values.
   *
   * @param byteCode the Standard Request Codes byte value
   * @return the corresponding Standard Request instance
   */
  public static EDeviceRequest fromByteCode(byte byteCode) {
    for (EDeviceRequest request : EDeviceRequest.values()) {
      if (request.getByteCode() == byteCode) {
        return request;
      }
    }
    return null;
  }

}
