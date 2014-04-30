/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java;

/**
 * Thrown when JNI library could not be loaded.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class LoaderException extends RuntimeException {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * <p>
   * @param message The error message.
   */
  public LoaderException(final String message) {
    super(message);
  }

  /**
   * Constructor.
   * <p>
   * @param message The error message.
   * @param cause   The root cause.
   */
  public LoaderException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
