/*
 * Copyright (C) 1999 - 2001, International Business Machines
 * Corporation. All Rights Reserved. Provided and licensed under the terms and
 * conditions of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 *
 * Copyright (C) 2014 Key Bridge LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javax.usb3;

import javax.usb3.request.BEndpointAddress;

/**
 * Interface for a USB endpoint descriptor.
 * <p>
 * Each endpoint used for an interface has its own descriptor. This descriptor
 * contains the information required by the host to determine the bandwidth
 * requirements of each endpoint. An endpoint descriptor is always returned as
 * part of the configuration information returned by a
 * GetDescriptor(Configuration) request. An endpoint descriptor cannot be
 * directly accessed with a GetDescriptor() or SetDescriptor() request. There is
 * never an endpoint descriptor for endpoint zero.
 * <p>
 * See the USB 2.0 specification section 9.6.6.
 *
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
public interface IUsbEndpointDescriptor extends IUsbDescriptor {

  /**
   * The address of the endpoint on the USB device described by this descriptor,
   * wrapped as an enumerated instance.
   *
   * @return This descriptor's bEndpointAddress.
   */
  public BEndpointAddress endpointAddress();

  /**
   * The address of the endpoint on the USB device described by this descriptor.
   * The address is encoded as follows: <ul>
   * <li>Bit 3...0: The endpoint number </li>
   * <li>Bit 6...4: Reserved, reset to zero </li>
   * <li>Bit 7: Direction, ignored for control endpoints: <ul>
   * <li>0 = OUT</li>
   * <li>1 = IN</li></ul></ul>
   *
   * @return This descriptor's bEndpointAddress.
   */
  public byte bEndpointAddress();

  /**
   * The bmAttributes field provides information about the endpoint’s Transfer
   * Type (bits 1..0) and Synchronization Type (bits 3..2). In addition, the
   * Usage Type bit (bits 5..4) indicate whether this is an endpoint used for
   * normal data transfers (bits 5..4=00B), whether it is used to convey
   * explicit feedback information for one or more data endpoints (bits
   * 5..4=01B) or whether it is a data endpoint that also serves as an implicit
   * feedback endpoint for one or more data endpoints (bits 5..4=10B). Bits 5..2
   * are only meaningful for isochronous endpoints and must be reset to zero for
   * all other transfer types.
   * <p>
   * This field describes the endpoint’s attributes when it is configured using
   * the bConfigurationValue.
   * <p>
   * Bits 1..0: Transfer Type <ul>
   * <li>00 = Control </li>
   * <li>01 = Isochronous </li>
   * <li>10 = Bulk </li>
   * <li>11 = Interrupt </li></ul>
   * If not an isochronous endpoint, bits 5..2 are reserved and must be set to
   * zero. If isochronous, they are defined as follows:
   * <p>
   * Bits 3..2: Synchronization Type <ul>
   * <li>00 = No Synchronization </li>
   * <li>01 = Asynchronous</li>
   * <li>10 = Adaptive </li>
   * <li>11 = Synchronous </li></ul>
   * Bits 5..4: Usage Type <ul>
   * <li>00 = Data endpoint </li>
   * <li>01 = Feedback endpoint </li>
   * <li>10 = Implicit feedback Data endpoint </li>
   * <li>11 = Reserved</li></ul>
   * Refer to USB 2.0 Chapter 5 <em>USB Data Flow Model</em> for more
   * information. All other bits are reserved and must be reset to zero.
   * Reserved bits must be ignored by the host.
   *
   * @return This descriptor's bmAttributes.
   */
  public byte bmAttributes();

  /**
   * Maximum packet size this endpoint is capable of sending or receiving when
   * this configuration is selected.
   * <p>
   * For isochronous endpoints, this value is used to reserve the bus time in
   * the schedule, required for the per-(micro)frame data payloads. The pipe
   * may, on an ongoing basis, actually use less bandwidth than that reserved.
   * The device reports, if necessary, the actual bandwidth used via its normal,
   * non-USB defined mechanisms.
   * <p>
   * For all endpoints, bits 10..0 specify the maximum packet size (in bytes).
   * <p>
   * For high-speed isochronous and interrupt endpoints:
   * <p>
   * Bits 12..11 specify the number of additional transaction opportunities per
   * microframe:
   * <ul><li>00 = None (1 transaction per microframe) </li>
   * <li>01 = 1 additional (2 per microframe) </li>
   * <li>10 = 2 additional (3 per microframe) </li>
   * <li>11 = Reserved </li></ul>
   * Bits 15..13 are reserved and must be set to zero.
   * <p>
   * For control endpoints this field shall be set to 512. For bulk endpoint
   * types this field shall be set to 1024.
   * <p>
   * For interrupt and isochronous endpoints this field shall be set to 1024 if
   * this endpoint defines a value in the bMaxBurst field greater than zero. If
   * the value in the bMaxBurst field is set to zero then this field can have
   * any value from 0 to 1024 for an isochronous endpoint and 1 to 1024 for an
   * interrupt endpoint.
   *
   * @return This descriptor's wMaxPacketSize.
   */
  public short wMaxPacketSize();

  /**
   * Interval for polling endpoint for data transfers. Expressed in frames or
   * microframes depending on the device operating speed (i.e., either 1
   * millisecond or 125 μs units).
   * <p>
   * For full-/high-speed isochronous endpoints, this value must be in the range
   * from 1 to 16. The bInterval value is used as the exponent for a
   * 2<sup>(bInterval-1)</sup> value; e.g., a bInterval of 4 means a period of 8
   * = 2^(4-1).
   * <p>
   * For full-/low-speed interrupt endpoints, the value of this field may be
   * from 1 to 255.
   * <p>
   * For high-speed interrupt endpoints, the bInterval value is used as the
   * exponent for a 2<sup>(bInterval-1)</sup> value; e.g., a bInterval of 4
   * means a period of 8 = 2<sup>(4-1)</sup>. This value must be from 1 to 16.
   * <p>
   * For high-speed bulk/control OUT endpoints, the bInterval must specify the
   * maximum NAK rate of the endpoint. A value of 0 indicates the endpoint never
   * NAKs. Other values indicate at most 1 NAK each bInterval number of
   * microframes. This value must be in the range from 0 to 255.
   * <p>
   * For Enhanced SuperSpeed isochronous and interrupt endpoints, this value
   * shall be in the range from 1 to 16. However, the valid ranges are 8 to 16
   * for Notification type Interrupt endpoints. The bInterval value is used as
   * the exponent for a 2<sup>(bInterval-1)</sup> value; e.g., a bInterval of 4
   * means a period of 8 (2(4-1) → 23 → 8). This field is reserved and shall not
   * be used for Enhanced SuperSpeed bulk or control endpoints.
   *
   * @return This descriptor's polling bInterval.
   */
  public byte bInterval();
}
