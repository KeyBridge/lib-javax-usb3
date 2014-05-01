/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax.descriptors;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.usb.IUsbStringDescriptor;

/**
 * Simple string descriptor.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class SimpleUsbStringDescriptor extends SimpleUsbDescriptor implements IUsbStringDescriptor {

  /**
   * The serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The string data in UTF-16LE encoding.
   */
  private final byte[] bString;

  /**
   * Constructs a new string descriptor by reading the descriptor data from the
   * specified byte buffer.
   * <p>
   * @param data The descriptor data as a byte buffer.
   */
  public SimpleUsbStringDescriptor(final ByteBuffer data) {
    super(data.get(0), data.get(1));

    data.position(2);
    this.bString = new byte[bLength() - 2];
    /**
     * Copy bytes from the 'data' buffer into the 'bString' destination array.
     */
    data.get(this.bString);
  }

  /**
   * Constructs a new string descriptor with the specified data.
   * <p>
   * @param bLength         The descriptor length.
   * @param bDescriptorType The descriptor type.
   * @param string          The string.
   * @throws UnsupportedEncodingException When system does not support UTF-16LE
   *                                      encoding.
   */
  public SimpleUsbStringDescriptor(final byte bLength,
                                   final byte bDescriptorType, final String string)
    throws UnsupportedEncodingException {
    super(bLength, bDescriptorType);
    this.bString = string.getBytes("UTF-16LE");
  }

  /**
   * Copy constructor.
   * <p>
   * @param descriptor The descriptor from which to copy the data.
   */
  public SimpleUsbStringDescriptor(final IUsbStringDescriptor descriptor) {
    super(descriptor.bLength(), descriptor.bDescriptorType());
    this.bString = descriptor.bString().clone();
  }

  @Override
  public byte[] bString() {
    return this.bString.clone();
  }

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
