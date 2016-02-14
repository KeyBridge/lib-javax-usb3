/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb;

import javax.usb.event.IUsbPipeListener;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;

/**
 * USB pipe listener list.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Key Bridge (keybridge.ch)
 */
public final class UsbPipeListener extends UsbEventListener<IUsbPipeListener> implements IUsbPipeListener {

  /**
   * Constructs a new USB pipe listener list.
   */
  public UsbPipeListener() {
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
