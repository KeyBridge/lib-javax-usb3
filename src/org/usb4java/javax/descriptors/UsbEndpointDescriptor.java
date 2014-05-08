/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax.descriptors;

import java.util.Objects;
import javax.usb.IUsbEndpointDescriptor;
import javax.usb.ri.enumerated.EDescriptorType;
import javax.usb.ri.request.BEndpointAddress;
import org.usb4java.EndpointDescriptor;
import org.usb4java.libusbutil.DescriptorUtils;

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
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
public final class UsbEndpointDescriptor extends AUsbDescriptor implements IUsbEndpointDescriptor {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The poll interval.
   */
  private final byte bInterval;

  /**
   * The maximum packet size.
   */
  private final short wMaxPacketSize;

  /**
   * The endpoint attributes.
   */
  private final byte bmAttributes;

  /**
   * The endpoint address.
   */
  private final BEndpointAddress bEndpointAddress;

  /**
   * Construct a new UsbEndpointDescriptor instance.
   * <p>
   * @param bEndpointAddress The address of the endpoint.
   * @param bmAttributes     The endpoint attributes.
   * @param wMaxPacketSize   The maximum packet size.
   * @param bInterval        The poll interval.
   */
  public UsbEndpointDescriptor(final BEndpointAddress bEndpointAddress,
                               final byte bmAttributes,
                               final short wMaxPacketSize,
                               final byte bInterval) {
    super(EDescriptorType.ENDPOINT);
    this.bEndpointAddress = bEndpointAddress;
    this.wMaxPacketSize = wMaxPacketSize;
    this.bmAttributes = bmAttributes;
    this.bInterval = bInterval;
  }

  /**
   * Construct a new UsbEndpointDescriptor instance from a libusb4java endpoint
   * descriptor.
   * <p>
   * @param descriptor The descriptor from which to copy the data.
   */
  public UsbEndpointDescriptor(final EndpointDescriptor descriptor) {
    this(BEndpointAddress.getInstance(descriptor.bEndpointAddress()),
         descriptor.bmAttributes(),
         descriptor.wMaxPacketSize(),
         descriptor.bInterval());
  }

  /**
   * The address of the endpoint on the USB device described by this descriptor.
   * The address is encoded as follows: <br/>
   * Bit 3...0: The endpoint number <br/>
   * Bit 6...4: Reserved, reset to zero <br/>
   * Bit 7: Direction, ignored for control endpoints: 0 = OUT, 1 = IN
   * <p>
   * <p>
   * @return This descriptor's bEndpointAddress.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public BEndpointAddress bEndpointAddress() {
    return this.bEndpointAddress;
  }

  /**
   * This field describes the endpoint’s attributes when it is configured using
   * the bConfigurationValue.
   * <p>
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
   * Bits 1..0: Transfer Type 00 = Control 01 = Isochronous 10 = Bulk 11 =
   * Interrupt If not an isochronous endpoint, bits 5..2 are reserved and must
   * be set to zero. If isochronous, they are defined as follows: Bits 3..2:
   * Synchronization Type 00 = No Synchronization 01 = Asynchronous 10 =
   * Adaptive 11 = Synchronous Bits 5..4: Usage Type 00 = Data endpoint 01 =
   * Feedback endpoint 10 = Implicit feedback Data endpoint 11 = Reserved Refer
   * to Chapter 5 for more information. All other bits are reserved and must be
   * reset to zero. Reserved bits must be ignored by the host.
   * <p>
   * @return This descriptor's bmAttributes.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public byte bmAttributes() {
    return this.bmAttributes;
  }

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
   * For high-speed isochronous and interrupt endpoints: Bits 12..11 specify the
   * number of additional transaction opportunities per microframe: 00 = None (1
   * transaction per microframe) 01 = 1 additional (2 per microframe) 10 = 2
   * additional (3 per microframe) 11 = Reserved Bits 15..13 are reserved and
   * must be set to zero.
   * <p>
   * @return This descriptor's wMaxPacketSize.
   * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
   */
  @Override
  public short wMaxPacketSize() {
    return this.wMaxPacketSize;
  }

  /**
   * Interval for polling endpoint for data transfers. Expressed in frames or
   * microframes depending on the device operating speed (i.e., either 1
   * millisecond or 125 μs units).
   * <p>
   * For full-/high-speed isochronous endpoints, this value must be in the range
   * from 1 to 16. The bInterval value is used as the exponent for a
   * 2^(bInterval-1) value; e.g., a bInterval of 4 means a period of 8 =
   * 2^(4-1).
   * <p>
   * For full-/low-speed interrupt endpoints, the value of this field may be
   * from 1 to 255.
   * <p>
   * For high-speed interrupt endpoints, the bInterval value is used as the
   * exponent for a 2^(bInterval-1) value; e.g., a bInterval of 4 means a period
   * of 8 = 2^(4-1). This value must be from 1 to 16.
   * <p>
   * For high-speed bulk/control OUT endpoints, the bInterval must specify the
   * maximum NAK rate of the endpoint. A value of 0 indicates the endpoint never
   * NAKs. Other values indicate at most 1 NAK each bInterval number of
   * microframes. This value must be in the range from 0 to 255.
   * <p>
   * @return This descriptor's bInterval.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  @Override
  public byte bInterval() {
    return this.bInterval;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash += 67 * super.hashCode();
    hash += 67 * hash + this.bInterval;
    hash += 67 * hash + this.wMaxPacketSize;
    hash += 67 * hash + this.bmAttributes;
    hash += 67 * hash + Objects.hashCode(this.bEndpointAddress);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    return this.hashCode() == obj.hashCode();
  }

  @Override
  public String toString() {
    return String.format(
      "Endpoint Descriptor:%n"
      + "  bLength %18d%n"
      + "  bDescriptorType %10d%n"
      + "  bEndpointAddress %9s  EP %d %s%n"
      + "  bmAttributes %13d%n"
      + "    Transfer Type             %s%n"
      + "    Synch Type                %s%n"
      + "    Usage Type                %s%n"
      + "  wMaxPacketSize %11d%n"
      + "  bInterval %16d%n",
      bLength() & 0xff,
      bDescriptorType() & 0xff,
      //      String.format("0x%02x", bEndpointAddress() & 0xff),
      bEndpointAddress(),
      //      DescriptorUtils.getDirectionName(bEndpointAddress()),
      bmAttributes() & 0xff,
      DescriptorUtils.getTransferTypeName(bmAttributes()),
      DescriptorUtils.getSynchTypeName(bmAttributes()),
      DescriptorUtils.getUsageTypeName(bmAttributes()),
      wMaxPacketSize() & 0xffff,
      bInterval() & 0xff);
  }
}
