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
package javax.usb.exception;

/**
 * Thrown when USB device scan fails.
 *
 * @author Klaus Reimer 
 * @author Jesse Caulfield
 */
public final class UsbScanException extends RuntimeException {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   *
   * @param message The error message.
   * @param cause   The root cause.
   */
  public UsbScanException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
