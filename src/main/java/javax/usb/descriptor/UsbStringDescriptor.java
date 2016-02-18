/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.usb.IUsbStringDescriptor;
import javax.usb.enumerated.EDescriptorType;

/**
 * 9.6.9 String Descriptor implementation.
 * <p>
 * Devices report their attributes using descriptors. A descriptor is a data
 * structure with a defined format.
 * <p>
 * String descriptors are optional. If a device does not support string
 * descriptors, all references to string descriptors within device,
 * configuration, and interface descriptors must be reset to zero.
 * <p>
 * String descriptors use UNICODE encodings. When requesting a string
 * descriptor, the requester specifies the desired language using a sixteen- bit
 * language ID (LANGID) defined by the USB-IF. A USB device may omit all string
 * descriptors. USB devices that omit all string descriptors must not return an
 * array of LANGID codes.
 * <p>
 * String index zero for all languages returns a string descriptor that contains
 * an array of 2-byte LANGID codes supported by the device. e.g. To get a list
 * of supported languages first request the String Descriptor as index zero.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class UsbStringDescriptor extends AUsbDescriptor implements IUsbStringDescriptor {

  /**
   * The string data in UTF-16LE encoding.
   */
  private final byte[] bString;

  /**
   * Constructs a new String descriptor by reading the descriptor data from the
   * specified byte buffer.
   *
   * @param data The descriptor data as a byte buffer.
   */
  public UsbStringDescriptor(final ByteBuffer data) {
    super(EDescriptorType.STRING);
    this.bLength = data.get(0);
    /**
     * Instantiate the String array.
     */
    this.bString = new byte[bLength - 2];
    /**
     * Set the ByteBuffer position to the data.
     */
    data.position(2);
    /**
     * Copy bytes from the 'data' buffer into the 'bString' destination array.
     */
    data.get(this.bString);
  }

  /**
   * Constructs a new string descriptor with the specified data.
   *
   * @param bLength         The descriptor length.
   * @param bDescriptorType The descriptor type.
   * @param string          The string.
   * @throws UnsupportedEncodingException When system does not support UTF-16LE
   *                                      encoding.
   */
  public UsbStringDescriptor(final byte bLength,
                             final byte bDescriptorType,
                             final String string) throws UnsupportedEncodingException {
    super(EDescriptorType.STRING);
    this.bLength = bLength;
    this.bString = string.getBytes("UTF-16LE");
  }

  /**
   * Copy constructor.
   *
   * @param descriptor The descriptor from which to copy the data.
   */
  public UsbStringDescriptor(final IUsbStringDescriptor descriptor) {
    super(EDescriptorType.STRING);
    this.bLength = descriptor.bLength();
    this.bString = descriptor.bString().clone();
  }

  /**
   * Get this descriptor's UNICODE encoded string.
   * <p>
   * Modifications to the returned byte[] will not affect the StringDescriptor's
   * bString (i.e. a copy of the bString is returned).
   *
   * @return This descriptor's UNICODE encoded string.
   */
  @Override
  public byte[] bString() {
    return this.bString.clone();
  }

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
  @Override
  public String getString() throws UnsupportedEncodingException {
    return new String(this.bString, "UTF-16LE");
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash += 11 * super.hashCode();
    hash += 11 * hash + Arrays.hashCode(this.bString);
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
    return this.hashCode() == obj.hashCode();
  }

  @Override
  public String toString() {
    return new String(this.bString, Charset.forName("UTF-16LE"));
  }
}
