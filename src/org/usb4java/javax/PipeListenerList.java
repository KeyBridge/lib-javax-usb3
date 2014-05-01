/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.IUsbPipeListener;

/**
 * USB pipe listener list.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class PipeListenerList extends EventListenerList<IUsbPipeListener> implements IUsbPipeListener {

  /**
   * Constructs a new USB pipe listener list.
   */
  public PipeListenerList() {
    super();
  }

  @Override
  public IUsbPipeListener[] toArray() {
    return getListeners().toArray(new IUsbPipeListener[getListeners().size()]);
  }

  @Override
  public void errorEventOccurred(final UsbPipeErrorEvent event) {
    for (final IUsbPipeListener listener : toArray()) {
      listener.errorEventOccurred(event);
    }
  }

  @Override
  public void dataEventOccurred(final UsbPipeDataEvent event) {
    for (final IUsbPipeListener listener : toArray()) {
      listener.dataEventOccurred(event);
    }
  }
}
