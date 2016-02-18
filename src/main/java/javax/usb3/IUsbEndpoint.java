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

import javax.usb3.enumerated.EDataFlowtype;
import javax.usb3.enumerated.EEndpointDirection;

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
 * For high-speed bulk and control OUT endpoints, the bInterval field (for
 * polling endpoint for data transfers) is only used for compliance purposes;
 * the host controller is not required to change its behavior based on the value
 * in this field.
 *
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
public interface IUsbEndpoint extends Comparable<IUsbEndpoint> {

  /**
   * Get the parent IUsbInterface that this IUsbEndpoint belongs to.
   *
   * @return The parent interface.
   */
  public IUsbInterface getUsbInterface();

  /**
   * Get the descriptor for this IUsbEndpoint.
   * <p>
   * The descriptor may be cached.
   *
   * @return The descriptor for this IUsbEndpoint.
   */
  public IUsbEndpointDescriptor getUsbEndpointDescriptor();

  /**
   * Get this endpoint direction.
   * <p>
   * This is the logical AND of the ENDPOINT_DIRECTION_MASK and the endpoint
   * descriptor's address.
   *
   * @return This endpoint direction.
   * @see javax.usb.enumerated.EEndpointDirection#DEVICE_TO_HOST
   * @see javax.usb.enumerated.EEndpointDirection#HOST_TO_DEVICE
   */
  public EEndpointDirection getDirection();

  /**
   * Get this endpoint type.
   * <p>
   * This is the logical AND of the type mask and the endpoint descriptor's
   * attributes.
   *
   * @return This endpoint type.
   * @see javax.usb.enumerated.EDataFlowtype#CONTROL
   * @see javax.usb.enumerated.EDataFlowtype#BULK
   * @see javax.usb.enumerated.EDataFlowtype#INTERRUPT
   * @see javax.usb.enumerated.EDataFlowtype#ISOCHRONOUS
   */
  public EDataFlowtype getType();

  /**
   * Get the IUsbPipe for this IUsbEndpoint.
   * <p>
   * This is the only method of communication to this endpoint.
   *
   * @return This IUsbEndpoint's IUsbPipe.
   */
  public IUsbPipe getUsbPipe();
}
