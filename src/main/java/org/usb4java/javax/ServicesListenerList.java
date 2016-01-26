/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.event.UsbServicesEvent;
import javax.usb.event.IUsbServicesListener;

/**
 * USB services listener list.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class ServicesListenerList extends EventListenerList<IUsbServicesListener> implements IUsbServicesListener {

  /**
   * Constructs a new USB services listener list.
   */
  public ServicesListenerList() {
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
