/*
 * Copyright (C) 2013 Klaus Reimer
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
package javax.usb3.descriptor;

import javax.usb3.IUsbEndpointDescriptor;
import javax.usb3.request.BEndpointAddress;
import org.usb4java.EndpointDescriptor;

/**
 * 9.6.6 Endpoint Descriptor implementation.
 * <p>
 * Devices report their attributes using descriptors. A descriptor is a data
 * structure with a defined format.
 * <p>
 * Each endpoint used for an interface has its own descriptor. This descriptor
 * contains the information required by the host to determine the bandwidth
 * requirements of each endpoint. An endpoint descriptor is always returned as
 * part of the configuration information returned by a
 * GetDescriptor(Configuration) request. An endpoint descriptor cannot be
 * directly accessed with a GetDescriptor() or SetDescriptor() request. There is
 * never an endpoint descriptor for endpoint zero.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class UsbEndpointDescriptor extends AUsbEndpointDescriptor {

  /**
   * Construct a new UsbEndpointDescriptor instance from a endpoint descriptor.
   *
   * @param descriptor The descriptor from which to copy the data.
   */
  public UsbEndpointDescriptor(final IUsbEndpointDescriptor descriptor) {
    super(descriptor.endpointAddress(),
          descriptor.bmAttributes(),
          descriptor.wMaxPacketSize(),
          descriptor.bInterval());
  }

  /**
   * Construct a new UsbEndpointDescriptor instance from a JNI endpoint
   * descriptor.
   *
   * @param descriptor The descriptor from which to copy the data.
   */
  public UsbEndpointDescriptor(EndpointDescriptor descriptor) {
    super(BEndpointAddress.getInstance(descriptor.bEndpointAddress()),
          descriptor.bmAttributes(),
          descriptor.wMaxPacketSize(),
          descriptor.bInterval());
  }

}
