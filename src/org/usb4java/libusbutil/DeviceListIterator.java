/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.libusbutil;

import org.usb4java.DeviceList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.usb4java.Device;

/**
 * Iterator for device list.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DeviceListIterator implements Iterator<Device> {

  /**
   * The devices list.
   */
  private final DeviceList devices;

  /**
   * The current index.
   */
  private int nextIndex;

  /**
   * Constructor.
   * <p>
   * @param devices The devices list.
   */
  public DeviceListIterator(final DeviceList devices) {
    this.devices = devices;
  }

  @Override
  public boolean hasNext() {
    return this.nextIndex < this.devices.getSize();
  }

  @Override
  public Device next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return this.devices.get(this.nextIndex++);
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
