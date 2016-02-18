/*
 * Copyright (C) 2011 Klaus Reimer 
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

import javax.usb.IUsbDescriptor;
import javax.usb.enumerated.EDescriptorType;

/**
 * 9.5 and 9.6. Abstract USB Standard USB Descriptor Definition. This is a base
 * class to be extended by all Standard USB Descriptor implementations.
 * <p>
 * USB devices report their attributes using descriptors. A descriptor is a data
 * structure with a defined format. Each descriptor begins with a byte-wide
 * field that contains the total number of bytes in the descriptor followed by a
 * byte-wide field that identifies the descriptor type.
 * <p>
 * Using descriptors allows concise storage of the attributes of individual
 * configurations because each configuration may reuse descriptors or portions
 * of descriptors from other configurations that have the same characteristics.
 * In this manner, the descriptors resemble individual data records in a
 * relational database.
 * <p>
 * A device may return class- or vendor-specific descriptors in two ways:
 * <ol>
 * <li>If the class or vendor specific descriptors use the same format as
 * standard descriptors (e.g., start with a length byte and followed by a type
 * byte), they must be returned interleaved with standard descriptors in the
 * configuration information returned by a GetDescriptor(Configuration) request.
 * In this case, the class or vendor-specific descriptors must follow a related
 * standard descriptor they modify or extend.
 * </li>
 * <li>If the class or vendor specific descriptors are independent of
 * configuration information or use a non- standard format, a GetDescriptor()
 * request specifying the class or vendor specific descriptor type and index may
 * be used to retrieve the descriptor from the device. A class or vendor
 * specification will define the appropriate way to retrieve these descriptors.
 * </li>
 * </ol>
 * <p>
 * See 9.5 and 9.6 of USB 3.1 Specification
 *
 * @author Jesse Caulfield
 * @author Klaus Reimer 
 */
public abstract class AUsbDescriptor implements IUsbDescriptor {

  /**
   * The Standard USB descriptor definition enumerated type. This is used to
   * pre-populate the instance with configurations from the USB specification. *
   */
  protected final EDescriptorType descriptorType;

  /**
   * The descriptor length.
   */
  protected byte bLength;

  /**
   * The descriptor type byte value.
   * <p>
   * This is included for completeness and backwards compatibility. It provides
   * no information not otherwise learned from the enumerated
   * {@link #descriptorType} field.
   */
  protected final byte bDescriptorType;

  /**
   * Construct a Standard USB Descriptor Definition for the indicated enumerated
   * type.
   *
   * @param descriptorType An enumerated standard Descriptor Type
   */
  public AUsbDescriptor(EDescriptorType descriptorType) {
    this.descriptorType = descriptorType;
    this.bLength = (byte) descriptorType.getLength();
    this.bDescriptorType = descriptorType.getByteCode();
  }

  /**
   * Get the Standard USB descriptor definition enumerated type.
   *
   * @return the descriptor type
   */
  @Override
  public EDescriptorType descriptorType() {
    return descriptorType;
  }

  /**
   * Get this descriptor's bDescriptorType byte value.
   *
   * @return the Descriptor Type byte value
   */
  @Override
  public byte bDescriptorType() {
    return this.bDescriptorType;
  }

  /**
   * Get this descriptor's bLength.
   *
   * @return Size of this descriptor in bytes
   */
  @Override
  public byte bLength() {
    return this.bLength;
  }

  @Override
  public String toString() {
    return descriptorType.name();
  }

}
