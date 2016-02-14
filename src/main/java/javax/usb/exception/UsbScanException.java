/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb.exception;

/**
 * Thrown when USB device scan fails.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Key Bridge (keybridge.ch)
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
