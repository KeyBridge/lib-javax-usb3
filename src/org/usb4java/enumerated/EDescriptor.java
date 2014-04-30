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
package org.usb4java.enumerated;

/**
 * Descriptor types as defined by the USB specification in Table 9-5. Descriptor
 * Types of the USB 2.0 specification.
 * <p>
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
public enum EDescriptor {

  /**
   * Device descriptor. A device descriptor describes general information about
   * a USB device. It includes information that applies globally to the device
   * and all of the device’s configurations. A USB device has only one device
   * descriptor.
   * <p>
   * @see DeviceDescriptor
   */
  DT_DEVICE((byte) 0x01),
  /**
   * Configuration descriptor.
   * <p>
   * The configuration descriptor describes information about a specific device
   * configuration. The descriptor contains a bConfigurationValue field with a
   * value that, when used as a parameter to the SetConfiguration() request,
   * causes the device to assume the described configuration. The descriptor
   * describes the number of interfaces provided by the configuration. Each
   * interface may operate independently.
   * <p>
   * @see ConfigDescriptor
   */
  DT_CONFIG((byte) 0x02),
  /**
   * String descriptor.
   */
  DT_STRING((byte) 0x03),
  /**
   * Interface descriptor.
   * <p>
   * The interface descriptor describes a specific interface within a
   * configuration. A configuration provides one or more interfaces, each with
   * zero or more endpoint descriptors describing a unique set of endpoints
   * within the configuration. When a configuration supports more than one
   * interface, the endpoint descriptors for a particular interface follow the
   * interface descriptor in the data returned by the GetConfiguration()
   * request. An interface descriptor is always returned as part of a
   * configuration descriptor. Interface descriptors cannot be directly accessed
   * with a GetDescriptor() or SetDescriptor() request. An interface may include
   * alternate settings that allow the endpoints and/or their characteristics to
   * be varied after the device has been configured. The default setting for an
   * interface is always alternate setting zero.
   * <p>
   * @see InterfaceDescriptor
   */
  DT_INTERFACE((byte) 0x04),
  /**
   * Endpoint descriptor. Each endpoint used for an interface has its own
   * descriptor. This descriptor contains the information required by the host
   * to determine the bandwidth requirements of each endpoint.
   * <p>
   * @see EndpointDescriptor
   */
  DT_ENDPOINT((byte) 0x05);

  /**
   * The Descriptor Type byte code.
   */
  private byte bytecode;

  private EDescriptor(byte bytecode) {
    this.bytecode = bytecode;
  }

}
