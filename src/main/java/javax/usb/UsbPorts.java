/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A list of USB ports.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class UsbPorts implements IUsbPorts {

  /**
   * The hub ports.
   */
  private final List<IUsbPort> ports = new ArrayList<>();

  /**
   * The hub these ports belong to.
   */
  private final IUsbHub hub;

  /**
   * Constructor.
   *
   * @param hub The hub the port belongs to.
   */
  public UsbPorts(final IUsbHub hub) {
    this.hub = hub;
    addPort();
  }

  /**
   * Adds a new port and returns it.
   *
   * @return The added port.
   */
  private UsbPort addPort() {
    final byte portNo = (byte) (this.ports.size() + 1);
    final UsbPort port = new UsbPort(this.hub, portNo);
    this.ports.add(port);
    return port;
  }

  /**
   * Returns the first free port or adds a new one if no free port was found.
   *
   * @return The first free port.
   */
  private IUsbPort getFreePort() {
    for (final IUsbPort port : this.ports) {
      if (!port.isUsbDeviceAttached()) {
        return port;
      }
    }
    return addPort();
  }

  /**
   * @return the number of ports for this hub
   */
  @Override
  public byte getNumberOfPorts() {
    return (byte) this.ports.size();
  }

  /**
   * @return an iteration of UsbPort objects attached to this hub
   */
  @Override
  public List<IUsbPort> getUsbPorts() {
    return Collections.unmodifiableList(this.ports);
  }

  /**
   * Get the specified port.
   *
   * @param number The number (1-based) of the port to get.
   * @return The port with the specified number, or null.
   */
  @Override
  public IUsbPort getUsbPort(final byte number) {
    final int index = (number & 0xff) - 1;
    if (index < 0 || index >= this.ports.size()) {
      return null;
    }
    return this.ports.get(index);
  }

  /**
   * @return an iteration of devices currently attached to this hub
   */
  @Override
  public List<IUsbDevice> getAttachedUsbDevices() {
    final List<IUsbDevice> devices = new ArrayList<>();
    synchronized (this.ports) {
      for (final IUsbPort port : this.ports) {
        if (port.isUsbDeviceAttached()) {
          devices.add(port.getUsbDevice());
        }
      }
    }
    return Collections.unmodifiableList(devices);
  }

  @Override
  public boolean isUsbDeviceAttached(final IUsbDevice device) {
    synchronized (this.ports) {
      for (final IUsbPort port : this.ports) {
        if (device.equals(port.getUsbDevice())) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void connectUsbDevice(final IUsbDevice device) {
    synchronized (this.ports) {
      final IUsbPort port = getFreePort();
      ((UsbPort) port).connectUsbDevice(device);
    }
  }

  @Override
  public void disconnectUsbDevice(final IUsbDevice device) {
    synchronized (this.ports) {
      for (final IUsbPort port : this.ports) {
        if (device.equals(port.getUsbDevice())) {
          ((UsbPort) port).disconnectUsbDevice();
        }
      }
    }
  }
}
