/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.IUsbControlIrp;
import javax.usb.IUsbIrp;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.exception.UsbException;

/**
 * A queue for USB control I/O request packets.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class ControlIrpQueue extends AIrpQueue<IUsbControlIrp> {

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
  protected void processIrp(IUsbControlIrp irp) throws UsbException {
    processControlIrp(irp);
  }

  @Override
  protected void finishIrp(final IUsbIrp irp) {
    this.listeners.dataEventOccurred(new UsbDeviceDataEvent(getDevice(), (IUsbControlIrp) irp));
  }

}
