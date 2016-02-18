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

import javax.usb.enumerated.EDescriptorType;

/**
 * 9.6.2 Binary Device Object Store (BOS) Descriptor Definition.
 * <p>
 * The BOS descriptor defines a root descriptor that is similar to the
 * configuration descriptor, and is the base descriptor for accessing a family
 * of related descriptors. A host can read a BOS descriptor and learn from the
 * wTotalLength field the entire size of the device-level descriptor set, or it
 * can read in the entire BOS descriptor set of device capabilities.
 * <p>
 * The host accesses this descriptor using the GetDescriptor() request. The
 * descriptor type in the GetDescriptor() request is set to BOS (see Table
 * 9-12). There is no way for a host to read individual device capability
 * descriptors. The entire set can only be accessed via reading the BOS
 * descriptor with a GetDescriptor() request and using the length reported in
 * the wTotalLength field.
 *
 * @author Jesse Caulfield
 */
public abstract class AUsbBosDescriptor extends AUsbDescriptor {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;
  /**
   * The number of separate device capability descriptors in the Binary Device
   * Object Store (BOS)
   */
  private final int bNumDeviceCaps;

  /**
   * Construct a new UsbBosDescriptor instance.
   *
   * @param bNumDeviceCaps The number of separate device capability descriptors
   *                       in the Binary Device Object Store (BOS)
   */
  public AUsbBosDescriptor(int bNumDeviceCaps) {
    super(EDescriptorType.BOS);
    this.bNumDeviceCaps = bNumDeviceCaps;
  }

  /**
   * Get the number of separate device capability descriptors in the Binary
   * Device Object Store (BOS)
   *
   * @return the number of separate descriptors
   */
  public int getbNumDeviceCaps() {
    return bNumDeviceCaps;
  }

  @Override
  public String toString() {
    return "UsbBosDescriptor{" + bNumDeviceCaps + '}';
  }

}
