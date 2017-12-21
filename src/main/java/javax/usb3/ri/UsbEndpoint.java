/*
 * Copyright (C) 2011 Klaus Reimer
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
package javax.usb3.ri;

import javax.usb3.IUsbEndpoint;
import javax.usb3.IUsbEndpointDescriptor;
import javax.usb3.IUsbInterface;
import javax.usb3.IUsbPipe;
import javax.usb3.descriptor.UsbEndpointDescriptor;
import javax.usb3.enumerated.EDataFlowtype;
import javax.usb3.enumerated.EEndpointDirection;

/**
 * Implementation of IUsbUsbEndpoint.
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
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class UsbEndpoint implements IUsbEndpoint {

  /**
   * The (parent) interface this endpoint belongs to.
   */
  private final IUsbInterface usbInterface;

  /**
   * The endpoint descriptor.
   */
  private final IUsbEndpointDescriptor descriptor;

  /**
   * The USB pipe for this endpoint.
   */
  private final UsbPipe pipe;

  /**
   * Constructor.
   *
   * @param usbInterface The interface this endpoint belongs to.
   * @param descriptor   The libusb endpoint descriptor.
   */
  public UsbEndpoint(final IUsbInterface usbInterface, final IUsbEndpointDescriptor descriptor) {
    this.usbInterface = usbInterface;
    this.descriptor = new UsbEndpointDescriptor(descriptor);
    this.pipe = new UsbPipe(this);
  }

  /**
   * Get the parent IUsbInterface that this IUsbEndpoint belongs to.
   *
   * @return The parent interface.
   */
  @Override
  public IUsbInterface getUsbInterface() {
    return this.usbInterface;
  }

  /**
   * Get the descriptor for this IUsbEndpoint.
   * <p>
   * The descriptor may be cached.
   *
   * @return The descriptor for this IUsbEndpoint.
   */
  @Override
  public IUsbEndpointDescriptor getUsbEndpointDescriptor() {
    return this.descriptor;
  }

  /**
   * Get this endpoint's direction.
   * <p>
   * This is the logical AND of the direction mask and the endpoint descriptor's
   * address.
   *
   * @return This endpoint's direction.
   * @see javax.usb.enumerated.EEndpointDirection#DEVICE_TO_HOST
   * @see javax.usb.enumerated.EEndpointDirection#HOST_TO_DEVICE
   */
  @Override
  public EEndpointDirection getDirection() {
    return this.descriptor.endpointAddress().getDirection();
  }

  /**
   * Get this endpoint's type.
   * <p>
   * This is the logical AND of the and the endpoint descriptor's attributes.
   *
   * @return This endpoint type.
   * @see javax.usb.enumerated.EDataFlowtype#CONTROL
   * @see javax.usb.enumerated.EDataFlowtype#BULK
   * @see javax.usb.enumerated.EDataFlowtype#INTERRUPT
   * @see javax.usb.enumerated.EDataFlowtype#ISOCHRONOUS
   */
  @Override
  public EDataFlowtype getType() {
    return EDataFlowtype.fromByte(this.descriptor.bmAttributes());
  }

  /**
   * Get the IUsbPipe for this IUsbEndpoint.
   * <p>
   * This is the only method of communication to this endpoint.
   *
   * @return This IUsbEndpoint's IUsbPipe.
   */
  @Override
  public IUsbPipe getUsbPipe() {
    return this.pipe;
  }

  /**
   * Sort by end point address number.
   *
   * @param o the other endpoint.
   * @return the numerical sort order.
   */
  @Override
  public int compareTo(IUsbEndpoint o) {
    /**
     * Surround with a try catch to avoid (unlikely) null pointer exceptions.
     */
    try {
      return descriptor.endpointAddress().compareTo(o.getUsbEndpointDescriptor().endpointAddress());
    } catch (Exception e) {
      return +1;
    }
  }

  @Override
  public String toString() {
    return "UsbEndpoint"
      + " iface [" + usbInterface
      + "] descriptor [" + descriptor
      + "] pipe [" + pipe
      + ']';
  }

}
