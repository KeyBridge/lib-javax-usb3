/*
 * Copyright (C) 2013 Klaus Reimer
 * Copyright (C) 2014 Key Bridge LLC. All Rights Reserved.
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
package javax.usb3.utility;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.usb4java.Device;
import org.usb4java.DeviceList;

/**
 * Iterator for device list.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
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
   *
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
