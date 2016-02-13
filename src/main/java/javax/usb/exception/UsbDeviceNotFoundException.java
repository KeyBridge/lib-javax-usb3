/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package javax.usb.exception;

import javax.usb.UsbDeviceId;

/**
 * Thrown when a USB device was not found by id.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Key Bridge (keybridge.ch)
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
