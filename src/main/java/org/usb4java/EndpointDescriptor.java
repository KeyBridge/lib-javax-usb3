/*
 * Copyright 2013 Klaus Reimer 
 *
 * Based on libusb <http://libusb.info/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2013 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2013 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2013 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2013 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012-2013 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */
package org.usb4java;

import java.nio.ByteBuffer;
import javax.usb.utility.DescriptorDumpUtility;

/**
 * A structure representing the standard USB endpoint descriptor.
 * <p>
 * Each endpoint used for an interface has its own descriptor. This descriptor
 * contains the information required by the host to determine the bandwidth
 * requirements of each endpoint. An endpoint descriptor is always returned as
 * part of the configuration information returned by a
 * GetDescriptor(Configuration) request. An endpoint descriptor cannot be
 * directly accessed with a GetDescriptor() or SetDescriptor() request. There is
 * never an endpoint descriptor for endpoint zero.
 * <p>
 * This descriptor is documented in section 9.6.6 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer 
 * @author Jesse Caulfield
 */
public final class EndpointDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long endpointDescriptorPointer;

  /**
   * Package-private constructor to prevent manual instantiation. Endpoint
   * descriptors are always created by JNI.
   */
  protected EndpointDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.endpointDescriptorPointer;
  }

  /**
   * Returns the size of this descriptor (in bytes).
   *
   * @return The size of this descriptor (in bytes).
   */
  public native byte bLength();

  /**
   * Returns the descriptor type. Will have value {@link LibUsb#DT_ENDPOINT} in
   * this context.
   *
   * @return The descriptor type.
   */
  public native byte bDescriptorType();

  /**
   * The address of the endpoint described by this descriptor.
   * <p>
   * The address is encoded as follows: Bits 0:3 are the endpoint number. Bits
   * 4:6 are reserved. Bit 7 indicates direction (Either
   * {@link LibUsb#ENDPOINT_IN} or {@link LibUsb#ENDPOINT_OUT}).
   *
   * @return The endpoint address.
   */
  public native byte bEndpointAddress();

  /**
   * Attributes which apply to the endpoint when it is configured using the
   * bConfigurationValue.
   * <p>
   * Bits 0:1 determine the transfer type and correspond to the
   * LibUsb.TRANSFER_TYPE_* constants. <br/>
   * Bits 2:3 are only used for isochronous endpoints and correspond to the
   * LibUsb.ISO_SYNC_TYPE_* constants. <br/>
   * Bits 4:5 are also only used for isochronous endpoints and correspond to the
   * LibUsb.ISO_USAGE_TYPE_* constants. <br/>
   * Bits 6:7 are reserved.
   * <p>
   * The bmAttributes field provides information about the endpoint’s Transfer
   * Type (bits 1..0) and Synchronization Type (bits 3..2). For interrupt
   * endpoints, the Usage Type bits (bits 5..4) indicate whether the endpoint is
   * used for infrequent notifications that can tolerate varying latencies (bits
   * 5..4 = 01b), or if it regularly transfers data in consecutive service
   * intervals or is dependent on bounded latencies (bits 5..4 = 00b).
   *
   * @return The attributes.
   */
  public native byte bmAttributes();

  /**
   * Returns the maximum packet size this endpoint is capable of
   * sending/receiving.
   * <p>
   * For control endpoints this field shall be set to 512. For bulk endpoint
   * types this field shall be set to 1024.
   * <p>
   * For interrupt and isochronous endpoints this field shall be set to 1024 if
   * this endpoint defines a value in the bMaxBurst field greater than zero. If
   * the value in the bMaxBurst field is set to zero then this field can have
   * any value from 0 to 1024 for an isochronous endpoint and 1 to 1024 for an
   * interrupt endpoint.
   *
   * @return The maximum packet size.
   */
  public native short wMaxPacketSize();

  /**
   * Returns the interval for polling endpoint for data transfers.
   * <p>
   * Interval for servicing the endpoint for data transfers. Expressed in 125-μs
   * units.
   * <p>
   * For Enhanced SuperSpeed isochronous and interrupt endpoints, this value
   * shall be in the range from 1 to 16. However, the valid ranges are 8 to 16
   * for Notification type Interrupt endpoints. The bInterval value is used as
   * the exponent for a 2(bInterval-1) value; e.g., a bInterval of 4 means a
   * period of 8 (2(4-1) → 23 → 8). This field is reserved and shall not be used
   * for Enhanced SuperSpeed bulk or control endpoints.
   *
   * @return The polling interval.
   */
  public native byte bInterval();

  /**
   * For audio devices only: the rate at which synchronization feedback is
   * provided.
   *
   * @return The synchronization feedback rate.
   */
  public native byte bRefresh();

  /**
   * For audio devices only: the address of the synch endpoint.
   *
   * @return The synch endpoint address.
   */
  public native byte bSynchAddress();

  /**
   * Extra descriptors.
   * <p>
   * If libusb encounters unknown endpoint descriptors, it will store them here,
   * should you wish to parse them.
   *
   * @return The extra descriptors.
   */
  public native ByteBuffer extra();

  /**
   * Length of the extra descriptors, in bytes.
   *
   * @return The extra descriptors length.
   */
  public native int extraLength();

  /**
   * Returns a dump of this descriptor.
   *
   * @return The descriptor dump.
   */
  public String dump() {
    return String.format("%s"
            + "  extralen %17d%n"
            + "  extra:%n"
            + "%s",
            DescriptorDumpUtility.dump(this),
            this.extraLength(),
            DescriptorDumpUtility.dump(this.extra()).replaceAll("(?m)^", "    "));
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 41 * hash + (int) (this.endpointDescriptorPointer ^ (this.endpointDescriptorPointer >>> 32));
    hash += bLength();
    hash += bDescriptorType();
    hash += bEndpointAddress();
    hash += bmAttributes();
    hash += wMaxPacketSize();
    hash += bInterval();
    hash += bRefresh();
    hash += bSynchAddress();
    hash += extraLength();
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
    final EndpointDescriptor other = (EndpointDescriptor) obj;
    return this.hashCode() == other.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }
}
