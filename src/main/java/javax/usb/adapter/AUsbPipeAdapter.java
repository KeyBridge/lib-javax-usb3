/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb.adapter;

import javax.usb.event.IUsbPipeListener;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;

/**
 * An abstract adapter class for receiving USB pipe events.
 * <p>
 * The methods in this class are empty. This class exists as convenience for
 * creating listener objects.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public abstract class AUsbPipeAdapter implements IUsbPipeListener {

  @Override
  public void errorEventOccurred(final UsbPipeErrorEvent event) {
    // Empty
  }

  @Override
  public void dataEventOccurred(final UsbPipeDataEvent event) {
    // Empty
  }
}
