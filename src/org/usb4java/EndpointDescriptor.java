/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
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

/**
 * A structure representing the standard USB endpoint descriptor.
 * <p>
 * This descriptor is documented in section 9.6.6 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class EndpointDescriptor {

  /**
   * The native pointer to the descriptor structure.
   */
  private long endpointDescriptorPointer;

  /**
   * Package-private constructor to prevent manual instantiation. Endpoint
   * descriptors are always created by JNI.
   */
  EndpointDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   * <p>
   * @return The native pointer.
   */
  public long getPointer() {
    return this.endpointDescriptorPointer;
  }

  /**
   * Returns the size of this descriptor (in bytes).
   * <p>
   * @return The size of this descriptor (in bytes).
   */
  public native byte bLength();

  /**
   * Returns the descriptor type. Will have value {@link LibUsb#DT_ENDPOINT} in
   * this context.
   * <p>
   * @return The descriptor type.
   */
  public native byte bDescriptorType();

  /**
   * The address of the endpoint described by this descriptor. Bits 0:3 are the
   * endpoint number. Bits 4:6 are reserved. Bit 7 indicates direction (Either
   * {@link LibUsb#ENDPOINT_IN} or {@link LibUsb#ENDPOINT_OUT}).
   * <p>
   * @return The endpoint address.
   */
  public native byte bEndpointAddress();

  /**
   * Attributes which apply to the endpoint when it is configured using the
   * bConfigurationValue. Bits 0:1 determine the transfer type and correspond to
   * the LibUsb.TRANSFER_TYPE_* constants. Bits 2:3 are only used for
   * isochronous endpoints and correspond to the LibUsb.ISO_SYNC_TYPE_*
   * constants. Bits 4:5 are also only used for isochronous endpoints and
   * correspond to the LibUsb.ISO_USAGE_TYPE_* constants. Bits 6:7 are reserved.
   * <p>
   * @return The attributes.
   */
  public native byte bmAttributes();

  /**
   * Returns the maximum packet size this endpoint is capable of
   * sending/receiving.
   * <p>
   * @return The maximum packet size.
   */
  public native short wMaxPacketSize();

  /**
   * Returns the interval for polling endpoint for data transfers.
   * <p>
   * @return The polling interval.
   */
  public native byte bInterval();

  /**
   * For audio devices only: the rate at which synchronization feedback is
   * provided.
   * <p>
   * @return The synchronization feedback rate.
   */
  public native byte bRefresh();

  /**
   * For audio devices only: the address of the synch endpoint.
   * <p>
   * @return The synch endpoint address.
   */
  public native byte bSynchAddress();

  /**
   * Extra descriptors.
   * <p>
   * If libusb encounters unknown endpoint descriptors, it will store them here,
   * should you wish to parse them.
   * <p>
   * @return The extra descriptors.
   */
  public native ByteBuffer extra();

  /**
   * Length of the extra descriptors, in bytes.
   * <p>
   * @return The extra descriptors length.
   */
  public native int extraLength();

  /**
   * Returns a dump of this descriptor.
   * <p>
   * @return The descriptor dump.
   */
  public String dump() {
    return String.format(
      "%s"
      + "  extralen %17d%n"
      + "  extra:%n"
      + "%s",
      DescriptorUtils.dump(this),
      this.extraLength(),
      DescriptorUtils.dump(this.extra()).replaceAll("(?m)^", "    "));
  }

//  @Override
//  public int hashCode() {
//    return new HashCodeBuilder()
//      .append(this.bLength())
//      .append(this.bDescriptorType())
//      .append(this.bEndpointAddress())
//      .append(this.bmAttributes())
//      .append(this.wMaxPacketSize())
//      .append(this.bInterval())
//      .append(this.bRefresh())
//      .append(this.bSynchAddress())
//      .append(this.extra())
//      .append(this.extraLength())
//      .toHashCode();
//  }
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
