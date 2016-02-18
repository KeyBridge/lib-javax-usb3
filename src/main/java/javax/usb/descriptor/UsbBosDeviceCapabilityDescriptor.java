/*
 * Copyright (C) 2014 Jesse Caulfield 
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
package javax.usb.descriptor;

import org.usb4java.BosDevCapabilityDescriptor;

/**
 * 9.6.2 Binary Device Object Store (BOS) Device Descriptor Definition.
 * <p>
 * Device Capability descriptors are always returned as part of the BOS
 * information returned by a GetDescriptor(BOS) request. A Device Capability
 * cannot be directly accessed with a GetDescriptor() or SetDescriptor()
 * request.
 * <p>
 * It is advised to check bDevCapabilityType and call the matching
 * get*Descriptor method to get a structure fully matching the type.
 * <p>
 * Individual technology-specific or generic device-level capabilities are
 * reported via Device Capability descriptors. The format of the Device
 * Capability descriptor is defined in Table 9-13.
 * <p>
 * See Table 9-13. Format of a Device Capability Descriptor of the USB 3.1 spec.
 *
 * @author Jesse Caulfield
 */
public class UsbBosDeviceCapabilityDescriptor extends AUsbBosDeviceCapabilityDescriptor {

  /**
   * Construct a new UsbBosDeviceCapabilityDescriptor from a libusb4java JNI
   * BosDevCapabilityDescriptor instance.
   *
   * @param bosCapabilityDescriptor The JNI descriptor instance from which to
   *                                copy the data.
   */
  public UsbBosDeviceCapabilityDescriptor(BosDevCapabilityDescriptor bosCapabilityDescriptor) {
    super(bosCapabilityDescriptor.bDevCapabilityType(),
          bosCapabilityDescriptor.devCapabilityData().array());
  }
}
