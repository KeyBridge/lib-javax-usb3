/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb;

import javax.usb.event.IUsbDeviceListener;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;

/**
 * USB device listener list.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Key Bridge (keybridge.ch)
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
