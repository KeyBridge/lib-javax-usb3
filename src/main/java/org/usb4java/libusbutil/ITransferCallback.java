/*
 * Copyright 2013 Luca Longinotti <l@longi.li>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.libusbutil;

import org.usb4java.Transfer;

/**
 * Asynchronous transfer callback.
 * <p>
 * When submitting asynchronous transfers, you pass a callback of this type via
 * the callback member of the {@link Transfer} structure.
 * <p>
 * @author Luca Longinotti (l@longi.li)
 */
public interface ITransferCallback {

  /**
   * Processes a transfer notification.
   * <p>
   * libusb will call this function later, when the transfer has completed or
   * failed.
   * <p>
   * @param transfer The {@link Transfer} the callback is being notified about.
   */
  public void processTransfer(Transfer transfer);
}
