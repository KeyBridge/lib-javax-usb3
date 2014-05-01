/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.UsbControlIrp;
import javax.usb.UsbIrp;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.exception.UsbException;

/**
 * A queue for USB control I/O request packets.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class ControlIrpQueue extends AIrpQueue<UsbControlIrp> {

  /**
   * The USB device listener list.
   */
  private final DeviceListenerList listeners;

  /**
   * Constructor.
   * <p>
   * @param device    The USB device.
   * @param listeners The USB device listener list.
   */
  public ControlIrpQueue(final AUsbDevice device, final DeviceListenerList listeners) {
    super(device);
    this.listeners = listeners;
  }

  @Override
  protected void processIrp(UsbControlIrp irp) throws UsbException {
    processControlIrp(irp);
  }

  @Override
  protected void finishIrp(final UsbIrp irp) {
    this.listeners.dataEventOccurred(new UsbDeviceDataEvent(getDevice(), (UsbControlIrp) irp));
  }

}
