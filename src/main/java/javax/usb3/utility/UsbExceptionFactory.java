/*
 * Copyright 2014 Klaus Reimer 
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
package javax.usb3.utility;

import javax.usb3.exception.UsbPlatformException;
import org.usb4java.LibUsb;

/**
 * Utility methods for exception handling.
 *
 * @author Klaus Reimer 
 * @author Jesse Caulfield
 * @author Jesse Caulfield
 */
public final class UsbExceptionFactory {

  /**
   * Private constructor to prevent instantiation.
   */
  private UsbExceptionFactory() {
    // Empty
  }

  /**
   * Creates a USB platform exception.
   *
   * @param message   The error message.
   * @param errorCode The error code.
   * @return The USB platform exception.
   */
  public static UsbPlatformException createPlatformException(final String message, final int errorCode) {
    return new UsbPlatformException(String.format("USB error %d: %s: %s",
                                                  -errorCode,
                                                  message,
                                                  LibUsb.strError(errorCode)),
                                    errorCode);
  }
}
