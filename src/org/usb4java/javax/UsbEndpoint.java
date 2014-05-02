/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.*;
import javax.usb.ri.enumerated.EEndPointDirection;
import javax.usb.ri.request.EEndpointTransferType;
import org.usb4java.EndpointDescriptor;
import org.usb4java.javax.descriptors.UsbEndpointDescriptor;

/**
 * usb4java implementation of IUsbUsbEndpoint.
 * <p>
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
   * <p>
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
   * <p>
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
   * <p>
   * @return The descriptor for this IUsbEndpoint.
   */
  @Override
  public IUsbEndpointDescriptor getUsbEndpointDescriptor() {
    return this.descriptor;
  }

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
  @Override
  public byte getDirection() {
    return EEndPointDirection.fromByte(this.descriptor.bEndpointAddress()).getByteCode();
//    final byte address = this.descriptor.bEndpointAddress();
//    return (byte) (address & IUsbConst.ENDPOINT_DIRECTION_MASK);
  }

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
  @Override
  public byte getType() {
    return EEndpointTransferType.fromByte(this.descriptor.bmAttributes()).getByteCode();
//    final byte attribs = this.descriptor.bmAttributes();
//    return (byte) (attribs & IUsbConst.ENDPOINT_TYPE_MASK);
  }

  /**
   * Get the IUsbPipe for this IUsbEndpoint.
   * <p>
   * This is the only method of communication to this endpoint.
   * <p>
   * @return This IUsbEndpoint's IUsbPipe.
   */
  @Override
  public IUsbPipe getUsbPipe() {
    return this.pipe;
  }
}
