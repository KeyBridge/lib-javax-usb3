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

import java.nio.ByteBuffer;
import javax.usb3.IUsbEndpointDescriptor;
import javax.usb3.enumerated.EDescriptorType;
import javax.usb3.request.BEndpointAddress;
import javax.usb3.utility.DescriptorDumpUtility;

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
public final class EndpointDescriptor implements IUsbEndpointDescriptor {
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
   * {@inheritDoc}
   */
  @Override
  public native byte bLength();

  /**
   * {@inheritDoc}
   */
  @Override
  public EDescriptorType descriptorType() {
    return EDescriptorType.fromBytecode(bDescriptorType());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public native byte bDescriptorType();

  /**
   * {@inheritDoc}
   */
  @Override
  public BEndpointAddress endpointAddress() {
    return BEndpointAddress.getInstance(bEndpointAddress());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public native byte bEndpointAddress();

  /**
   * {@inheritDoc}
   */
  @Override
  public native byte bmAttributes();

  /**
   * {@inheritDoc}
   */
  @Override
  public native short wMaxPacketSize();

  /**
   * {@inheritDoc}
   */
  @Override
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
