/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax.descriptors;

import java.io.Serializable;
import javax.usb.IUsbDescriptor;

/**
 * Base class for all simple USB descriptors.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public abstract class SimpleUsbDescriptor implements IUsbDescriptor, Serializable {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The descriptor length.
   */
  private final byte bLength;

  /**
   * The descriptor type.
   */
  private final byte bDescriptorType;

  /**
   * Constructor.
   * <p>
   * @param bLength         The descriptor length.
   * @param bDescriptorType The descriptor type.
   */
  public SimpleUsbDescriptor(final byte bLength, final byte bDescriptorType) {
    this.bLength = bLength;
    this.bDescriptorType = bDescriptorType;
  }

  @Override
  public final byte bLength() {
    return this.bLength;
  }

  @Override
  public final byte bDescriptorType() {
    return this.bDescriptorType;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += 37 * hash + super.hashCode();
    hash += 37 * hash + this.bLength;
    hash += 37 * hash + this.bDescriptorType;
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

}
