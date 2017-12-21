/*
 * Copyright (C) 2013 Klaus Reimer
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
package javax.usb3.exception;

import javax.usb3.ri.UsbDeviceId;

/**
 * Thrown when a USB device was not found by id.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class UsbDeviceNotFoundException extends RuntimeException {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The device id.
   */
  private final UsbDeviceId id;

  /**
   * Constructor.
   *
   * @param id The ID of the device which was not found.
   */
  public UsbDeviceNotFoundException(final UsbDeviceId id) {
    super("USB Device not found: " + id);
    this.id = id;
  }

  /**
   * Returns the device id.
   *
   * @return The device id.
   */
  public UsbDeviceId getId() {
    return this.id;
  }
}
