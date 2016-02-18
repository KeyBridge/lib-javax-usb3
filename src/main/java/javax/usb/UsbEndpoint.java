/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
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
package javax.usb;

import javax.usb.descriptor.UsbEndpointDescriptor;
import javax.usb.enumerated.EDataFlowtype;
import javax.usb.enumerated.EEndpointDirection;
import org.usb4java.EndpointDescriptor;

/**
 * usb4java implementation of IUsbUsbEndpoint.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class UsbEndpoint implements IUsbEndpoint {

  /**
   * The interface this endpoint belongs to.
   */
  private final UsbInterface iface;

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
   * @param iface      The interface this endpoint belongs to.
   * @param descriptor The libusb endpoint descriptor.
   */
  public UsbEndpoint(final UsbInterface iface, final EndpointDescriptor descriptor) {
    this.iface = iface;
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
    return this.iface;
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
    return this.descriptor.bEndpointAddress().getDirection();
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

  @Override
  public String toString() {
    return "UsbEndpoint"
           + " iface [" + iface
           + "] descriptor [" + descriptor
           + "] pipe [" + pipe
           + ']';
  }

}
