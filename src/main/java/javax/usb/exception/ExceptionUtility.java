/*
 * Copyright 2014 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb.exception;

import org.usb4java.LibUsb;

/**
 * Utility methods for exception handling.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Key Bridge (keybridge.ch)
 */
public final class ExceptionUtility {

  /**
   * Private constructor to prevent instantiation.
   */
  private ExceptionUtility() {
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
