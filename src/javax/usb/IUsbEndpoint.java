/**
 * Original Copyright (c) 1999 - 2001, International Business Machines
 * Corporation. All Rights Reserved. Provided and licensed under the terms and
 * conditions of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 * <p>
 * Modifications and improvements Copyright (c) 2014 Key Bridge Global LLC. All
 * Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javax.usb;

import javax.usb.ri.enumerated.EEndpointDirection;
import javax.usb.ri.enumerated.EEndpointTransferType;

/**
 * Interface for a USB endpoint.
 * <p>
 * An endpoint is a uniquely identifiable portion of a USB device that is the
 * terminus of a communication flow between the host and device. Each USB
 * logical device is composed of a collection of independent endpoints. Each
 * logical device has a unique address assigned by the system at device
 * attachment time. Each endpoint on a device is given at design time a unique
 * device-determined identifier called the endpoint number. Each endpoint has a
 * device-determined direction of data flow. The combination of the device
 * address, endpoint number, and direction allows each endpoint to be uniquely
 * referenced. Each endpoint is a simplex connection that supports data flow in
 * one direction: either input (from device to host) or output (from host to
 * device).
 * <p>
 * An endpoint has characteristics that determine the type of transfer service
 * required between the endpoint and the client software. An endpoint describes
 * itself by: <ul>
 * <li> Bus access frequency/latency requirement</li>
 * <li> Bandwidth requirement</li>
 * <li> Endpoint number</li>
 * <li> Error handling behavior requirements</li>
 * <li> Maximum packet size that the endpoint is capable of sending or
 * receiving</li>
 * <li> The transfer type for the endpoint (refer to Section 5.4 for
 * details)</li>
 * <li> The direction in which data is transferred between the endpoint and the
 * host </li></ul>
 * Endpoints other than those with endpoint number zero are in an unknown state
 * before being configured and may not be accessed by the host before being
 * configured.
 * <p>
 * Endpoint Zero Requirements
 * <p>
 * All USB devices are required to implement a default control method that uses
 * both the input and output endpoints with endpoint number zero. The USB System
 * Software uses this default control method to initialize and generically
 * manipulate the logical device (e.g., to configure the logical device) as the
 * Default Control Pipe (see Section 5.3.2). The Default Control Pipe provides
 * access to the device’s configuration information and allows generic USB
 * status and control access. The Default Control Pipe supports control
 * transfers as defined in Section 5.5. The endpoints with endpoint number zero
 * are always accessible once a device is attached, powered, and has received a
 * bus reset.
 * <p>
 * Low-speed functions are limited to two optional endpoints beyond the two
 * required to implement the Default Control Pipe. Full- speed devices can have
 * additional endpoints only limited by the protocol definition (i.e., a maximum
 * of 15 additional input endpoints and 15 additional output endpoints).
 * Endpoints other than those for the Default Control Pipe cannot be used until
 * the device is configured as a normal part of the device configuration
 * process.
 * <p>
 * @author Dan Streetman
 */
public interface IUsbEndpoint {

  /**
   * Get the parent IUsbInterface that this IUsbEndpoint belongs to.
   * <p>
   * @return The parent interface.
   */
  public IUsbInterface getUsbInterface();

  /**
   * Get the descriptor for this IUsbEndpoint.
   * <p>
   * The descriptor may be cached.
   * <p>
   * @return The descriptor for this IUsbEndpoint.
   */
  public IUsbEndpointDescriptor getUsbEndpointDescriptor();

  /**
   * Get this endpoint's direction.
   * <p>
   * This is the logical AND of the
   * {@link javax.usb.UsbConst#ENDPOINT_DIRECTION_MASK direction mask} and the
   * {@link #getUsbEndpointDescriptor() endpoint descriptor}'s
   * {@link javax.usb.UsbEndpointDescriptor#bEndpointAddress() address}.
   * <p>
   * @return This endpoint's direction.
   * @see javax.usb.UsbConst#ENDPOINT_DIRECTION_IN
   * @see javax.usb.UsbConst#ENDPOINT_DIRECTION_OUT
   */
  public EEndpointDirection getDirection();

  /**
   * Get this endpoint's type.
   * <p>
   * This is the logical AND of the
   * {@link javax.usb.UsbConst#ENDPOINT_TYPE_MASK type mask} and the
   * {@link #getUsbEndpointDescriptor() endpoint descriptor}'s
   * {@link javax.usb.UsbEndpointDescriptor#bmAttributes() attributes}.
   * <p>
   * @return This endpoint's type.
   * @see javax.usb.UsbConst#ENDPOINT_TYPE_CONTROL
   * @see javax.usb.UsbConst#ENDPOINT_TYPE_BULK
   * @see javax.usb.UsbConst#ENDPOINT_TYPE_INTERRUPT
   * @see javax.usb.UsbConst#ENDPOINT_TYPE_ISOCHRONOUS
   */
  public EEndpointTransferType getType();

  /**
   * Get the IUsbPipe for this IUsbEndpoint.
   * <p>
   * This is the only method of communication to this endpoint.
   * <p>
   * @return This IUsbEndpoint's IUsbPipe.
   */
  public IUsbPipe getUsbPipe();
}
