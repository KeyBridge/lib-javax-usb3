/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax.adapter;

import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.IUsbPipeListener;

/**
 * An abstract adapter class for receiving USB pipe events.
 * <p>
 * The methods in this class are empty. This class exists as convenience for
 * creating listener objects.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
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
