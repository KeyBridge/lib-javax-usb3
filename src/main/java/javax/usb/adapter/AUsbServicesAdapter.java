/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
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
