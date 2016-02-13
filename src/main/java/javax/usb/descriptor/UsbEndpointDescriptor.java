/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package javax.usb.descriptor;

import javax.usb.request.BEndpointAddress;
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
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class UsbEndpointDescriptor extends AUsbEndpointDescriptor {

  /**
   * Construct a new UsbEndpointDescriptor instance from a libusb4java endpoint
   * descriptor.
   *
   * @param descriptor The descriptor from which to copy the data.
   */
  public UsbEndpointDescriptor(final EndpointDescriptor descriptor) {
    super(BEndpointAddress.getInstance(descriptor.bEndpointAddress()),
          descriptor.bmAttributes(),
          descriptor.wMaxPacketSize(),
          descriptor.bInterval());
  }

}
