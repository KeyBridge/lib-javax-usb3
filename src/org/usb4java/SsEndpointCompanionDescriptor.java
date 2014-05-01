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

/**
 * A structure representing the SuperSpeed USB Device EndPoint companion
 * descriptor.
 * <p>
 * This descriptor is documented in section 9.6.7 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
  * @author Jesse Caulfield <jesse@caulfield.org>
 */
public final class SsEndpointCompanionDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long ssEndpointCompanionDescriptorPointer;

  /**
   * Constructs a new descriptor which can be passed to the null null null null
   * null null null null null   {@link LibUsb#getSsEndpointCompanionDescriptor(Context,
     * EndpointDescriptor, SsEndpointCompanionDescriptor)} method.
   */
  public SsEndpointCompanionDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   * <p>
   * @return The native pointer.
   */
  public long getPointer() {
    return this.ssEndpointCompanionDescriptorPointer;
  }

  /**
   * Returns the size of this descriptor (in bytes).
   * <p>
   * @return The descriptor size in bytes;
   */
  public native byte bLength();

  /**
   * Returns the descriptor type.
   * <p>
   * @return The descriptor type.
   */
  public native byte bDescriptorType();

  /**
   * Returns the maximum number of packets the endpoint can send or receive as
   * part of a burst.
   * <p>
   * @return The maximum number of packets as part of a burst.
   */
  public native byte bMaxBurst();

  /**
   * Returns the attributes. In bulk endpoint: bits 4:0 represents the maximum
   * number of streams the EP supports. In isochronous endpoint: bits 1:0
   * represents the Mult - a zero based value that determines the maximum number
   * of packets within a service interval.
   * <p>
   * @return The attributes.
   */
  public native byte bmAttributes();

  /**
   * Returns the total number of bytes this endpoint will transfer every service
   * interval. Valid only for periodic endpoints.
   * <p>
   * @return The total number of bytes per service interval.
   */
  public native short wBytesPerInterval();

  /**
   * Returns a dump of this descriptor.
   * <p>
   * @return The descriptor dump.
   */
  public String dump() {
    return String.format(
      "SuperSpeed Endpoint Companion Descriptor:%n"
      + "  bLength %18d%n"
      + "  bDescriptorType %10d%n"
      + "  bMaxBurst %16s%n"
      + "  bmAttributes %13d%n"
      + "  wBytesPerInterval %8d%n",
      this.bLength() & 0xFF,
      this.bDescriptorType() & 0xFF,
      this.bMaxBurst() & 0xFF,
      this.bmAttributes() & 0xFF,
      this.wBytesPerInterval() & 0xFFFF);
  }

//  @Override
//  public int hashCode() {
//    return new HashCodeBuilder()
//      .append(this.bLength())
//      .append(this.bDescriptorType())
//      .append(this.bMaxBurst())
//      .append(this.bmAttributes())
//      .append(this.wBytesPerInterval())
//      .toHashCode();
//  }
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + (int) (this.ssEndpointCompanionDescriptorPointer ^ (this.ssEndpointCompanionDescriptorPointer >>> 32));
    hash += this.bLength();
    hash += this.bDescriptorType();
    hash += this.bmAttributes();
    hash += this.bMaxBurst();
    hash += this.wBytesPerInterval();
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
    final SsEndpointCompanionDescriptor other = (SsEndpointCompanionDescriptor) obj;
    return this.hashCode() == other.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }
}
