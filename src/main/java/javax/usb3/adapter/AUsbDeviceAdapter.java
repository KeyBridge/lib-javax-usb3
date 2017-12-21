/*
 * Copyright (C) 2013 Klaus Reimer
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
package javax.usb3.adapter;

import javax.usb3.event.IUsbDeviceListener;
import javax.usb3.event.UsbDeviceDataEvent;
import javax.usb3.event.UsbDeviceErrorEvent;
import javax.usb3.event.UsbDeviceEvent;

/**
 * An abstract adapter class for receiving USB device events. The methods in
 * this class are empty. This class exists as convenience for creating listener
 * objects.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public abstract class AUsbDeviceAdapter implements IUsbDeviceListener {

  @Override
  public void usbDeviceDetached(final UsbDeviceEvent event) {
    // Empty
  }

  @Override
  public void errorEventOccurred(final UsbDeviceErrorEvent event) {
    // Empty
  }

  @Override
  public void dataEventOccurred(final UsbDeviceDataEvent event) {
    // Empty
  }
}
