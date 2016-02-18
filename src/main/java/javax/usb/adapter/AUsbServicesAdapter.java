/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
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
package javax.usb.adapter;

import javax.usb.event.IUsbServicesListener;
import javax.usb.event.UsbServicesEvent;

/**
 * An abstract adapter class for receiving USB service events.
 * <p>
 * The methods in this class are empty. This class exists as convenience for
 * creating listener objects.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public abstract class AUsbServicesAdapter implements IUsbServicesListener {

  @Override
  public void usbDeviceAttached(final UsbServicesEvent event) {
    // Empty
  }

  @Override
  public void usbDeviceDetached(final UsbServicesEvent event) {
    // Empty
  }
}
