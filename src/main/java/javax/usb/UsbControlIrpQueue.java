/*
 * Copyright (C) 2011 Klaus Reimer 
 * Copyright (C) 2014 Jesse Caulfield
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package javax.usb;

import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.exception.UsbException;

/**
 * A concurrent queue manager for USB I/O Request packets.
 * <p>
 * An IrpQueue contains a thread safe FIFO queue and a threaded
 * processUsbIrpQueueor to handle each IRP that is placed into the queue.
 * <p>
 * Developer note: The default operation of an IrpQueue is to support
 * Asynchronous operation (e.g. processUsbIrpQueue in a separate thread.) To
 * implement synchronous IRP queue handling implement a WAIT lock on the
 * {@link IUsbIrp.isComplete() isComplete} method IUsbIrp.isComplete().
 *
 * @author Klaus Reimer 
 * @author Jesse Caulfield
 */
public final class UsbControlIrpQueue extends AUsbIrpQueue<IUsbControlIrp> {

  /**
   * The USB device listener list.
   */
  private final UsbDeviceListener listeners;

  /**
   * Constructor.
   *
   * @param device    The USB device.
   * @param listeners The USB device listener list.
   */
  public UsbControlIrpQueue(final AUsbDevice device, final UsbDeviceListener listeners) {
    super(device);
    this.listeners = listeners;
  }

  /**
   * Processes the IRP.
   *
   * @param irp The IRP to processUsbIrpQueue.
   * @throws UsbException When processUsbIrpQueueing the IRP fails.
   */
  @Override
  protected void processIrp(IUsbControlIrp irp) throws UsbException {
    processControlIrp(irp);
  }

  /**
   * Called after IRP has finished. This can be implemented to send events for
   * example.
   *
   * @param irp The IRP which has been finished.
   */
  @Override
  protected void finishIrp(final IUsbIrp irp) {
    this.listeners.dataEventOccurred(new UsbDeviceDataEvent(this.usbDevice, (IUsbControlIrp) irp));
  }
}
