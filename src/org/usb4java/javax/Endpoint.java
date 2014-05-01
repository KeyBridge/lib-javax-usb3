/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.*;
import org.usb4java.EndpointDescriptor;
import org.usb4java.javax.descriptors.SimpleUsbEndpointDescriptor;

/**
 * usb4java implementation of UsbEndpoint.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Endpoint implements UsbEndpoint {

  /**
   * The interface this endpoint belongs to.
   */
  private final Interface iface;

  /**
   * The endpoint descriptor.
   */
  private final UsbEndpointDescriptor descriptor;

  /**
   * The USB pipe for this endpoint.
   */
  private final Pipe pipe;

  /**
   * Constructor.
   * <p>
   * @param iface      The interface this endpoint belongs to.
   * @param descriptor The libusb endpoint descriptor.
   */
  public Endpoint(final Interface iface, final EndpointDescriptor descriptor) {
    this.iface = iface;
    this.descriptor = new SimpleUsbEndpointDescriptor(descriptor);
    this.pipe = new Pipe(this);
  }

  @Override
  public UsbInterface getUsbInterface() {
    return this.iface;
  }

  @Override
  public UsbEndpointDescriptor getUsbEndpointDescriptor() {
    return this.descriptor;
  }

  @Override
  public byte getDirection() {
    final byte address = this.descriptor.bEndpointAddress();
    return (byte) (address & UsbConst.ENDPOINT_DIRECTION_MASK);
  }

  @Override
  public byte getType() {
    final byte attribs = this.descriptor.bmAttributes();
    return (byte) (attribs & UsbConst.ENDPOINT_TYPE_MASK);
  }

  @Override
  public UsbPipe getUsbPipe() {
    return this.pipe;
  }
}
