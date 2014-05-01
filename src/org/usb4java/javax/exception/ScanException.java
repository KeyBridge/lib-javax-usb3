/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax.exception;

/**
 * Thrown when USB device scan fails.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class ScanException extends RuntimeException {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * <p>
   * @param message The error message.
   * @param cause   The root cause.
   */
  public ScanException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
