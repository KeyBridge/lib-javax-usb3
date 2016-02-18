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
package javax.usb3.ri;

import javax.usb3.event.IUsbServicesListener;
import javax.usb3.event.UsbServicesEvent;

/**
 * USB services listener list.
 *
 * @author Klaus Reimer 
 * @author Jesse Caulfield
 */
public final class UsbServicesListener extends UsbEventListener<IUsbServicesListener> implements IUsbServicesListener {

  /**
   * Constructs a new USB services listener list.
   */
  public UsbServicesListener() {
    super();
  }

  @Override
  public IUsbServicesListener[] toArray() {
    return getListeners().toArray(new IUsbServicesListener[getListeners().size()]);
  }

  @Override
  public void usbDeviceAttached(final UsbServicesEvent event) {
    for (final IUsbServicesListener listener : toArray()) {
      listener.usbDeviceAttached(event);
    }
  }

  @Override
  public void usbDeviceDetached(final UsbServicesEvent event) {
    for (final IUsbServicesListener listener : toArray()) {
      listener.usbDeviceDetached(event);
    }
  }
}
