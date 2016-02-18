/*
 * Copyright (C) 2011 Klaus Reimer 
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
package javax.usb;

import javax.usb.event.IUsbDeviceListener;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;

/**
 * USB device listener list.
 *
 * @author Klaus Reimer 
 * @author Jesse Caulfield
 */
public final class UsbDeviceListener extends UsbEventListener<IUsbDeviceListener> implements IUsbDeviceListener {

  /**
   * Constructs a new USB device listener list.
   */
  public UsbDeviceListener() {
    super();
  }

  @Override
  public IUsbDeviceListener[] toArray() {
    return getListeners().toArray(new IUsbDeviceListener[getListeners().size()]);
  }

  @Override
  public void usbDeviceDetached(final UsbDeviceEvent event) {
    for (final IUsbDeviceListener listener : toArray()) {
      listener.usbDeviceDetached(event);
    }
  }

  @Override
  public void errorEventOccurred(final UsbDeviceErrorEvent event) {
    for (final IUsbDeviceListener listener : toArray()) {
      listener.errorEventOccurred(event);
    }
  }

  @Override
  public void dataEventOccurred(final UsbDeviceDataEvent event) {
    for (final IUsbDeviceListener listener : toArray()) {
      listener.dataEventOccurred(event);
    }
  }
}
