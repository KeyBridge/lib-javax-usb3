/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.IUsbDeviceListener;

/**
 * USB device listener list.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DeviceListenerList extends EventListenerList<IUsbDeviceListener> implements IUsbDeviceListener {

  /**
   * Constructs a new USB device listener list.
   */
  public DeviceListenerList() {
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
