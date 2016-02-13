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
 * A structure representing the SuperSpeed USB Device Capability descriptor.
 * This descriptor is documented in section 9.6.2.2 of the USB 3.0
 * specification.
 * <p>
 * 9.6.2.2 SuperSpeed USB Device Capability
 * <p>
 * This section defines the required device-level capabilities descriptor which
 * shall be implemented by all Enhanced SuperSpeed devices.
 * <p>
 * All multiple-byte fields are represented in host-endian format.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class SsUsbDeviceCapabilityDescriptor {
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
   * Returns the size of this descriptor (in bytes).
   *
   * @return The descriptor size in bytes;
   */
  public native byte bLength();

  /**
   * Returns the descriptor type.
   *
   * @return Constant DEVICE CAPABILITY Descriptor type
   */
  public native byte bDescriptorType();

  /**
   * Returns the device capability type.
   *
   * @return Constant Capability type: SUPERSPEED_USB.
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
   * Returns the lowest speed at which all the functionality supported by the
   * device is available to the user.
   *
   * @return The lowest speed.
   */
  public native byte bFunctionalitySupport();

  /**
   * Returns the U1 Device Exit Latency.
   *
   * @return The U1 Device Exit Latency.
   */
  public native byte bU1DevExitLat();

  /**
   * Returns the U2 Device Exit Latency.
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
