/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb;

import javax.usb.event.IUsbServicesListener;
import javax.usb.event.UsbServicesEvent;

/**
 * USB services listener list.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Key Bridge (keybridge.ch)
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
