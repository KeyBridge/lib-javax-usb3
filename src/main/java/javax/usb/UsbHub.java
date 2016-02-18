/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
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

import java.util.List;
import javax.usb.exception.UsbPlatformException;
import org.usb4java.Device;

/**
 * UsbHub implementation.
 * <p>
 * This must be set up before use and/or connection to the topology tree. To set
 * up, see {@link com.javax.UsbDevice UsbDevice documentation}. The number of
 * ports may be set in the constructor, or it will default to 1. The number of
 * ports can be dynamically {@link #resize(int) resized} if needed.
 * <p>
 * The port numbering is 1-based, not 0-based.
 * <p>
 * Hubs are a type of USB device that provide additional attachment points to
 * the USB.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class UsbHub extends AUsbDevice implements IUsbHub, IUsbPorts {

  /**
   * The hub ports.
   */
  private final UsbPorts ports = new UsbPorts(this);

  /**
   * Constructs a new USB hub device. This creates a hub with a initial number
   * of ports.
   *
   * @param deviceManager The USB device manager which is responsible for this *
   *                      device.
   * @param id            THe device id. Must not be null.
   * @param parentId      The parent id. may be null if this device has no
   *                      parent.
   * @param speed         The device speed.
   * @param device        The libusb device. This reference is only valid during
   *                      the constructor execution, so don't store it in a
   *                      property or something like that.
   * @throws UsbPlatformException When device configuration could not be read.
   */
  public UsbHub(final UsbDeviceManager deviceManager, final UsbDeviceId id, final UsbDeviceId parentId, final int speed, final Device device) throws UsbPlatformException {
    super(deviceManager, id, parentId, speed, device);
  }

  /**
   * @return the number of ports for this hub
   */
  @Override
  public byte getNumberOfPorts() {
    return this.ports.getNumberOfPorts();
  }

  /**
   * @return an iteration of UsbPort objects attached to this hub
   */
  @Override
  public List<IUsbPort> getUsbPorts() {
    return this.ports.getUsbPorts();
  }

  /**
   * Get the specified port.
   *
   * @param number The number (1-based) of the port to get.
   * @return The port with the specified number, or null.
   */
  @Override
  public IUsbPort getUsbPort(final byte number) {
    return this.ports.getUsbPort(number);
  }

  /**
   * @return an iteration of devices currently attached to this hub
   */
  @Override
  public List<IUsbDevice> getAttachedUsbDevices() {
    return this.ports.getAttachedUsbDevices();
  }

  @Override
  public boolean isUsbDeviceAttached(final IUsbDevice device) {
    return this.ports.isUsbDeviceAttached(device);
  }

  @Override
  public boolean isRootUsbHub() {
    return false;
  }

  @Override
  public void connectUsbDevice(final IUsbDevice device) {
    this.ports.connectUsbDevice(device);
  }

  @Override
  public void disconnectUsbDevice(final IUsbDevice device) {
    this.ports.disconnectUsbDevice(device);
  }

  /**
   * TRUE. USB4Java UsbHub instances are always UsbHubs. If the device is not a
   * hub it will be identified as a UsbDevice implementation.
   *
   * @return TRUE
   */
  @Override
  public boolean isUsbHub() {
    return true;
  }
}
