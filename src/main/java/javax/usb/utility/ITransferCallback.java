/*
 * Copyright 2013 Luca Longinotti 
 * Copyright (C) 2014 Key Bridge LLC.
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
package javax.usb.utility;

import org.usb4java.Transfer;

/**
 * Asynchronous transfer callback.
 * <p>
 * When submitting asynchronous transfers, you pass a callback of this type via
 * the callback member of the {@link Transfer} structure.
 *
 * @author Luca Longinotti
 * @author Jesse Caulfield
 */
public interface ITransferCallback {

  /**
   * Processes a transfer notification.
   * <p>
   * libusb will call this function later, when the transfer has completed or
   * failed.
   *
   * @param transfer The {@link Transfer} the callback is being notified about.
   */
  public void processTransfer(Transfer transfer);
}
