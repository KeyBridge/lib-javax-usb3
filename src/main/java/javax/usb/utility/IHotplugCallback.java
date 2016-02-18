/*
 * Copyright 2013 Luca Longinotti
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
package javax.usb.utility;

import org.usb4java.Context;
import org.usb4java.Device;

/**
 * USB Hot Plug callback interface.
 * <p>
 * When requesting hot plug event notifications, you pass a callback of this
 * type.
 *
 * @author Luca Longinotti
 * @author Jesse Caulfield
 */
public interface IHotplugCallback {

  /**
   * Process a hot plug event.
   * <p>
   * This callback may be called by an internal event thread and as such it is
   * recommended the callback do minimal processing before returning.
   * <p>
   * {@code libusb} will call this function later, when a matching event had
   * happened on a matching device.
   * <p>
   * It is safe to call {@code LibUsb.hotplugRegisterCallback(...)}} from within
   * a callback.
   *
   * @param context  Context of this notification.
   * @param device   Device this event occurred on.
   * @param event    Event that occurred.
   * @param userData user data provided when this callback was registered
   * @return Whether this callback is finished processing events. Returning 1
   *         will cause this callback to be deregistered.
   */
  public int processEvent(Context context, Device device, int event, Object userData);
}
