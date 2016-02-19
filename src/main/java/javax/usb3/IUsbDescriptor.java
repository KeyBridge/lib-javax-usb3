/*
 * Copyright (C) 1999 - 2001, International Business Machines
 * Corporation. All Rights Reserved. Provided and licensed under the terms and
 * conditions of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 *
 * Copyright (C) 2014 Key Bridge LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javax.usb3;

import javax.usb3.enumerated.EDescriptorType;

/**
 * Interface for a USB descriptor.
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
 * Where appropriate, descriptors contain references to string descriptors that
 * provide displayable information describing a descriptor in human-readable
 * form. The inclusion of string descriptors is optional. However, the reference
 * fields within descriptors are mandatory. If a device does not support string
 * descriptors, string reference fields must be reset to zero to indicate no
 * string descriptor is available.
 * <p>
 * If a descriptor returns with a value in its length field that is less than
 * defined by this specification, the descriptor is invalid and should be
 * rejected by the host. If the descriptor returns with a value in its length
 * field that is greater than defined by this specification, the extra bytes are
 * ignored by the host, but the next descriptor is located using the length
 * returned rather than the length expected.
 * <p>
 * A device may return class- or vendor-specific descriptors in two ways:
 * <ol>
 * <li>If the class or vendor specific descriptors use the same format as
 * standard descriptors (e.g., start with a length byte and followed by a type
 * byte), they must be returned interleaved with standard descriptors in the
 * configuration information returned by a GetDescriptor(Configuration) request.
 * In this case, the class or vendor-specific descriptors must follow a related
 * standard descriptor they modify or extend.</li>
 * <li>If the class or vendor specific descriptors are independent of
 * configuration information or use a non- standard format, a GetDescriptor()
 * request specifying the class or vendor specific descriptor type and index may
 * be used to retrieve the descriptor from the device. A class or vendor
 * specification will define the appropriate way to retrieve these
 * descriptors.</li>
 * </ul>
 * <p>
 * See USB 2.0 section 9.5 Descriptors.
 *
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
public interface IUsbDescriptor {

  /**
   * Get the Size of this descriptor in bytes.
   *
   * @return Size of this descriptor in bytes
   */
  public byte bLength();

  /**
   * Get the Standard USB descriptor definition enumerated type.
   *
   * @return the descriptor type
   */
  public EDescriptorType descriptorType();

  /**
   * Get the the descriptor type byte value.
   *
   * @return The descriptor Type.
   */
  public byte bDescriptorType();

}
