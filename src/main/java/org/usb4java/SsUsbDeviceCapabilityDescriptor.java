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

import javax.usb3.IUsbDescriptor;
import javax.usb3.enumerated.EDescriptorType;

/**
 * 9.6.2.2 SuperSpeed USB Device Capability
 * <p>
 * A structure representing the SuperSpeed USB Device Capability descriptor.
 * This descriptor is documented in section 9.6.2.2 of the USB 3.0
 * specification.
 * <p>
 * This section defines the required device-level capabilities descriptor which
 * shall be implemented by all Enhanced SuperSpeed devices.
 * <p>
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class SsUsbDeviceCapabilityDescriptor implements IUsbDescriptor {
  // Maps to JNI native class

  /**
   * The native pointer to the descriptor structure.
   */
  private long ssUsbDeviceCapabilityDescriptorPointer;

  /**
   * Constructs a new SuperSpeed USB Device Capability descriptor which can be
   * passed to the
   * {@link LibUsb#getSsUsbDeviceCapabilityDescriptor(Context, BosDevCapabilityDescriptor, SsUsbDeviceCapabilityDescriptor)}
   * method.
   */
  public SsUsbDeviceCapabilityDescriptor() {
    // Empty
  }

  /**
   * Returns the native pointer.
   *
   * @return The native pointer.
   */
  public long getPointer() {
    return this.ssUsbDeviceCapabilityDescriptorPointer;
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
   * Returns the device capability type.
   *
   * @return Capability type: SUPERSPEED_USB.
   */
  public native byte bDevCapabilityType();

  /**
   * Returns the bitmap of supported device level features.
   * <p>
   * Bitmap encoding of supported device level features. A value of one in a bit
   * location indicates a feature is supported; a value of zero indicates it is
   * not supported. See 9.6.2.2 for bmAttributes encodings.
   *
   * @return The supported device level features.
   */
  public native byte bmAttributes();

  /**
   * Returns the bitmap encoding of the speed supported by this device when
   * operating in SuperSpeed mode.
   * <p>
   * See 9.6.2.2. for wSpeedsSupported encodings:
   * <p>
   * Bit 0: If bit set the device supports operation at low-Speed USB. <br/>
   * Bit 1: If bit set the device supports operation at full-Speed USB. <br/>
   * Bit 2: If bit set the device supports operation at high-Speed USB.<br/>
   * Bit 3: If bit set the device supports operation at Gen 1 speed.
   *
   * @return The supported speed.
   */
  public native short wSpeedSupported();

  /**
   * The lowest speed at which all the functionality supported by the device is
   * available to the user. For example if the device supports all its
   * functionality when connected at full speed and above then it sets this
   * value to 1. Refer to the wSpeedsSupported field for valid values that can
   * be placed in this field.
   *
   * @return The lowest speed.
   */
  public native byte bFunctionalitySupport();

  /**
   * U1 Device Exit Latency. Worst-case latency to transition from U1 to U0,
   * assuming the latency is limited only by the device and not the device’s
   * link partner. This field applies only to the exit latency associated with
   * an individual port, and does not apply to the total latency through a hub
   * (e.g., from downstream port to upstream port).
   * <p>
   * The following are permissible values:
   * <pre> 00H Zero.
   * 01H Less than 1 μs
   * 02H Less than 2 μs
   * 03H Less than 3 μs
   * 04H Less than 4 μs
   * ... ...
   * 0AH Less than 10 μs
   * 0BH–FFH Reserved</pre> For a hub, this is the value for both its upstream
   * and downstream ports.
   *
   * @return The U1 Device Exit Latency.
   */
  public native byte bU1DevExitLat();

  /**
   * U2 Device Exit Latency. Worst-case latency to transition from U2 to U0,
   * assuming the latency is limited only by the device and not the device’s
   * link partner. Applies to all ports on a device. The following are
   * permissible values:
   * <pre> 0000H Zero
   * 0001H Less than 1 μs
   * 0002H Less than 2 μs
   * 0003H Less than 3 μs
   * 0004H Less than 4 μs
   * ... ...
   * 07FFH Less than 2047 μs
   * 0800H-FFFFH Reserved</pre> For a hub, this is the value for both its
   * upstream and downstream ports.
   *
   * @return The U2 Device Exit Latency.
   */
  public native short bU2DevExitLat();

  /**
   * Returns a dump of this descriptor.
   *
   * @return The descriptor dump.
   */
  public String dump() {
    return String.format(
      "SuperSpeed USB Device Capability Descriptor:%n"
      + "  bLength %18d%n"
      + "  bDescriptorType %10d%n"
      + "  bDevCapabilityType %7d%n"
      + "  bmAttributes %13s%n"
      + "  wSpeedSupported %10d%n"
      + "  bFunctionalitySupport %4d%n"
      + "  bU1DevExitLat %12d%n"
      + "  bU2DevExitLat %12d%n",
      this.bLength() & 0xFF,
      this.bDescriptorType() & 0xFF,
      this.bDevCapabilityType() & 0xFF,
      String.format("0x%02x", this.bmAttributes() & 0xFF),
      this.wSpeedSupported() & 0xFFFF,
      this.bFunctionalitySupport() & 0xFF,
      this.bU1DevExitLat() & 0xFF,
      this.bU2DevExitLat() & 0xFFFF);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 11 * hash + (int) (this.ssUsbDeviceCapabilityDescriptorPointer ^ (this.ssUsbDeviceCapabilityDescriptorPointer >>> 32));
    hash += this.bLength();
    hash += this.bDescriptorType();
    hash += this.bDevCapabilityType();
    hash += this.bmAttributes();
    hash += this.wSpeedSupported();
    hash += this.bFunctionalitySupport();
    hash += this.bU1DevExitLat();
    hash += this.bU2DevExitLat();
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
    final SsUsbDeviceCapabilityDescriptor other = (SsUsbDeviceCapabilityDescriptor) obj;
    return this.hashCode() == other.hashCode();
  }

  @Override
  public String toString() {
    return this.dump();
  }

}
