/*
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
 * Copyright 2013 Klaus Reimer
 * Copyright 2013 Luca Longinotti
 * Copyright 2014-2016 Jesse Caulfield
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
package org.usb4java;

import javax.usb3.IUsbDescriptor;
import javax.usb3.enumerated.EDescriptorType;

/**
 * 9.6.7 SuperSpeed Endpoint Companion
 * <p>
 * A structure representing the SuperSpeed USB Device EndPoint companion
 * descriptor. This descriptor is documented in section 9.6.7 of the USB 3.0
 * specification. All multiple-byte fields are represented in host-endian
 * format.
 * <p>
 * Enhanced SuperSpeed devices that are operating at Gen X speed. Each endpoint
 * described in an interface is followed by a SuperSpeed Endpoint Companion
 * descriptor. This descriptor is returned as part of the configuration
 * information returned by a GetDescriptor(Configuration) request and cannot be
 * directly accessed with a GetDescriptor() or SetDescriptor() request. The
 * Default Control Pipe does not have an Endpoint Companion descriptor. The
 * Endpoint Companion descriptor shall immediately follow the endpoint
 * descriptor it is associated with in the configuration information.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class SsEndpointCompanionDescriptor implements IUsbDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long ssEndpointCompanionDescriptorPointer;

  /**
   * Constructs a new descriptor which can be passed to the
   * {@link LibUsb#getSsEndpointCompanionDescriptor(Context, EndpointDescriptor, SsEndpointCompanionDescriptor)}
   * method.
   */
  public SsEndpointCompanionDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.ssEndpointCompanionDescriptorPointer;
  }

  /**
   * @inherit
   */
  @Override
  public native byte bLength();

  /**
   * @inherit
   */
  @Override
  public EDescriptorType descriptorType() {
    return EDescriptorType.fromBytecode(bDescriptorType());
  }

  /**
   * @inherit
   */
  @Override
  public native byte bDescriptorType();

  /**
   * The maximum number of packets the endpoint can send or receive as part of a
   * burst. Valid values are from 0 to 15. A value of 0 indicates that the
   * endpoint can only burst one packet at a time and a value of 15 indicates
   * that the endpoint can burst up to 16 packets at a time.
   * <p>
   * For endpoints of type control this shall be set to 0.
   *
   * @return The maximum number of packets as part of a burst.
   */
  public native byte bMaxBurst();

  /**
   * Returns the attributes. In bulk endpoint: bits 4:0 represents the maximum
   * number of streams the EP supports. In isochronous endpoint: bits 1:0
   * represents the Mult - a zero based value that determines the maximum number
   * of packets within a service interval.
   * <p>
   * Decoding is dependent upon the endpoint type. See USB 3.1 Table 9-26.
   * SuperSpeed Endpoint Companion Descriptor for details.
   *
   * @return The endpoint attributes.
   */
  public native byte bmAttributes();

  /**
   * Returns the total number of bytes this endpoint will transfer every service
   * interval. Valid only for periodic endpoints.
   *
   * @return The total number of bytes per service interval.
   */
  public native short wBytesPerInterval();

  /**
   * Returns a dump of this descriptor.
   *
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
