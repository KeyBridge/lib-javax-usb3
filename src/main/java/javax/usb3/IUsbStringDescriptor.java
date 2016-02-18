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

import java.io.UnsupportedEncodingException;

/**
 * Interface for a USB string descriptor.
 * <p>
 * String descriptors are optional: if a device does not support string
 * descriptors, all references to string descriptors within device,
 * configuration, and interface descriptors must be reset to zero.
 * <p>
 * String descriptors use UNICODE encodings as defined by The Unicode Standard,
 * Worldwide Character Encoding, Version 3.0, The Unicode Consortium,
 * Addison-Wesley Publishing Company, Reading, Massachusetts (URL:
 * http://www.unicode.com). The strings in a USB device may support multiple
 * languages. When requesting a string descriptor, the requester specifies the
 * desired language using a sixteen- bit language ID (LANGID) defined by the
 * USB-IF. The list of currently defined USB LANGIDs can be found at
 * http://www.usb.org/developers/docs.html. String index zero for all languages
 * returns a string descriptor that contains an array of two-byte LANGID codes
 * supported by the device. Table 9-15 shows the LANGID code array. A USB device
 * may omit all string descriptors. USB devices that omit all string descriptors
 * must not return an array of LANGID codes.
 * <p>
 * The array of LANGID codes is not NULL-terminated. The size of the array (in
 * bytes) is computed by subtracting two from the value of the first byte of the
 * descriptor.
 *
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
public interface IUsbStringDescriptor extends IUsbDescriptor {

  /**
   * Get this descriptor's UNICODE encoded string.
   * <p>
   * Modifications to the returned byte[] will not affect the StringDescriptor's
   * bString (i.e. a copy of the bString is returned).
   *
   * @return This descriptor's UNICODE encoded string.
   */
  public byte[] bString();

  /**
   * Get this descriptor's translated String.
   * <p>
   * This is the String translation of the {@link #bString() bString}. The
   * translation is done using the best available Unicode encoding that this JVM
   * provides. USB strings are 16-bit little-endian; if no 16-bit little-endian
   * encoding is available, and the string can be converted to 8-bit (all high
   * bytes are zero), then 8-bit encoding is used. If no encoding is available,
   * an UnsupportedEncodingException is thrown.
   * <p>
   * For information about Unicode see
   * <a href="http://www.unicode.org/">the Unicode website</a>.
   *
   * @return This descriptor's String.
   * @exception UnsupportedEncodingException If no encoding is available.
   */
  public String getString() throws UnsupportedEncodingException;
}
