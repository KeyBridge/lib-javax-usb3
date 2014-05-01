/*
 * Copyright 2014 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.exception.UsbPlatformException;
import org.usb4java.LibUsb;

/**
 * Utility methods for exception handling.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class ExceptionUtils {

  /**
   * Private constructor to prevent instantiation.
   */
  private ExceptionUtils() {
    // Empty
  }

  /**
   * Creates a USB platform exception.
   * <p>
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
