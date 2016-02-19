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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import javax.usb3.IUsbEndpointDescriptor;
import javax.usb3.IUsbInterfaceDescriptor;
import javax.usb3.descriptor.UsbEndpointDescriptor;
import javax.usb3.enumerated.EDescriptorType;
import javax.usb3.enumerated.EUSBClassCode;
import javax.usb3.utility.DescriptorDumpUtility;

/**
 * A structure representing the standard USB interface descriptor.
 * <p>
 * The interface descriptor describes a specific interface within a
 * configuration. A configuration provides one or more interfaces, each with
 * zero or more endpoint descriptors. When a configuration supports more than
 * one interface, the endpoint descriptors for a particular interface follow the
 * interface descriptor in the data returned by the GetConfiguration() request.
 * As mentioned earlier in this chapter, Enhanced SuperSpeed devices shall
 * return Endpoint Companion descriptors for each of the endpoints in that
 * interface to return additional information about its endpoint capabilities.
 * The Endpoint Companion descriptor shall immediately follow the endpoint
 * descriptor it is associated with in the configuration information. An
 * interface descriptor is always returned as part of a configuration
 * descriptor. Interface descriptors cannot be directly accessed with a
 * GetDescriptor() or SetDescriptor() request.
 * <p>
 * An interface may include alternate settings that allow the endpoints and/or
 * their characteristics to be varied after the device has been configured. The
 * default setting for an interface is always alternate setting zero. The
 * SetInterface() request is used to select an alternate setting or to return to
 * the default setting. The GetInterface() request returns the selected
 * alternate setting.
 * <p>
 * Alternate settings allow a portion of the device configuration to be varied
 * while other interfaces remain in operation. If a configuration has alternate
 * settings for one or more of its interfaces, a separate interface descriptor
 * and its associated endpoint and endpoint companion (when reporting its
 * Enhanced SuperSpeed configuration) descriptors are included for each setting.
 * <p>
 * If a device configuration supported a single interface with two alternate
 * settings, the configuration descriptor would be followed by an interface
 * descriptor with the bInterfaceNumber and bAlternateSetting fields set to zero
 * and then the endpoint and endpoint companion (when reporting its Enhanced
 * SuperSpeed configuration) descriptors for that setting, followed by another
 * interface descriptor and its associated endpoint and endpoint companion
 * descriptors. The second interface descriptor’s bInterfaceNumber field would
 * also be set to zero, but the bAlternateSetting field of the second interface
 * descriptor would be set to one.
 * <p>
 * If an interface uses only the Default Control Pipe, no endpoint descriptors
 * follow the interface descriptor. In this case, the bNumEndpoints field shall
 * be set to zero. An interface descriptor never includes the Default Control
 * Pipe in the number of endpoints.
 * <p>
 * This descriptor is documented in section 9.6.5 of the USB 3.0 specification.
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class InterfaceDescriptor implements IUsbInterfaceDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long interfaceDescriptorPointer;

  /**
   * Package-private constructor to prevent manual instantiation. Interface
   * descriptors are always created by JNI.
   */
  protected InterfaceDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.interfaceDescriptorPointer;
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
   * @inherit
   */
  @Override
  public native byte bInterfaceNumber();

  /**
   * @inherit
   */
  @Override
  public native byte bAlternateSetting();

  /**
   * @inherit
   */
  @Override
  public native byte bNumEndpoints();

  /**
   * @inherit
   */
  @Override
  public EUSBClassCode interfaceClass() {
    return EUSBClassCode.fromByteCode(bInterfaceClass());
  }

  /**
   * @inherit
   */
  @Override
  public native byte bInterfaceClass();

  /**
   * @inherit
   */
  @Override
  public native byte bInterfaceSubClass();

  /**
   * @inherit
   */
  @Override
  public native byte bInterfaceProtocol();

  /**
   * @inherit
   */
  @Override
  public native byte iInterface();

  /**
   * @inherit
   */
  @Override
  public native EndpointDescriptor[] endpoint();

  /**
   * Extra descriptors.
   * <p>
   * If libusb encounters unknown interface descriptors, it will store them
   * here, should you wish to parse them.
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
   * Convert the native EndpointDescriptor arrays from {@link #endpoint()} to a
   * array of UsbEndpointDescriptor instances.
   *
   * @return a non-null (but possibly empty) array
   */
  public IUsbEndpointDescriptor[] usbEndpoint() {
    EndpointDescriptor[] endpoint = endpoint();
    Collection<IUsbEndpointDescriptor> usbEndpoint = new ArrayList<>();
    for (EndpointDescriptor endpointDescriptor : endpoint) {
      usbEndpoint.add(new UsbEndpointDescriptor(endpointDescriptor));
    }
    return usbEndpoint.toArray(new IUsbEndpointDescriptor[usbEndpoint.size()]);
  }

  /**
   * Returns a dump of this descriptor.
   *
   * @return The descriptor dump.
   */
  public String dump() {
    final StringBuilder builder = new StringBuilder();

    builder.append(String.format("%s"
                                 + "  extralen %17d%n"
                                 + "  extra:%n"
                                 + "%s",
                                 DescriptorDumpUtility.dump(this),
                                 this.extraLength(),
                                 DescriptorDumpUtility.dump(this.extra()).replaceAll("(?m)^", "    ")));

    for (final EndpointDescriptor epDesc : this.endpoint()) {
      builder.append(String.format("%n")).append(epDesc.dump());
    }

    return builder.toString();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 83 * hash + (int) (this.interfaceDescriptorPointer ^ (this.interfaceDescriptorPointer >>> 32));
    hash += bLength();
    hash += bDescriptorType();
    hash += bInterfaceNumber();
    hash += bAlternateSetting();
    hash += bNumEndpoints();
    hash += bInterfaceClass();
    hash += bInterfaceSubClass();
    hash += bInterfaceProtocol();
    hash += iInterface();
    hash += extraLength();
    hash += Objects.hash(endpoint(), extra());
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
    final InterfaceDescriptor other = (InterfaceDescriptor) obj;
    return this.hashCode() == other.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }

}
