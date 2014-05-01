/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.usb.UsbDevice;
import javax.usb.UsbHub;
import javax.usb.UsbPort;

/**
 * A list of USB ports.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Ports implements IUsbPorts {

  /**
   * The hub ports.
   */
  private final List<UsbPort> ports = new ArrayList<>();

  /**
   * The hub these ports belong to.
   */
  private final UsbHub hub;

  /**
   * Constructor.
   * <p>
   * @param hub The hub the port belongs to.
   */
  public Ports(final UsbHub hub) {
    this.hub = hub;
    addPort();
  }

  /**
   * Adds a new port and returns it.
   * <p>
   * @return The added port.
   */
  private Port addPort() {
    final byte portNo = (byte) (this.ports.size() + 1);
    final Port port = new Port(this.hub, portNo);
    this.ports.add(port);
    return port;
  }

  /**
   * Returns the first free port or adds a new one if no free port was found.
   * <p>
   * @return The first free port.
   */
  private UsbPort getFreePort() {
    for (final UsbPort port : this.ports) {
      if (!port.isUsbDeviceAttached()) {
        return port;
      }
    }
    return addPort();
  }

  @Override
  public byte getNumberOfPorts() {
    return (byte) this.ports.size();
  }

  @Override
  public List<UsbPort> getUsbPorts() {
    return Collections.unmodifiableList(this.ports);
  }

  @Override
  public UsbPort getUsbPort(final byte number) {
    final int index = (number & 0xff) - 1;
    if (index < 0 || index >= this.ports.size()) {
      return null;
    }
    return this.ports.get(index);
  }

  @Override
  public List<UsbDevice> getAttachedUsbDevices() {
    final List<UsbDevice> devices = new ArrayList<>();
    synchronized (this.ports) {
      for (final UsbPort port : this.ports) {
        if (port.isUsbDeviceAttached()) {
          devices.add(port.getUsbDevice());
        }
      }
    }
    return Collections.unmodifiableList(devices);
  }

  @Override
  public boolean isUsbDeviceAttached(final UsbDevice device) {
    synchronized (this.ports) {
      for (final UsbPort port : this.ports) {
        if (device.equals(port.getUsbDevice())) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void connectUsbDevice(final UsbDevice device) {
    synchronized (this.ports) {
      final UsbPort port = getFreePort();
      ((Port) port).connectUsbDevice(device);
    }
  }

  @Override
  public void disconnectUsbDevice(final UsbDevice device) {
    synchronized (this.ports) {
      for (final UsbPort port : this.ports) {
        if (device.equals(port.getUsbDevice())) {
          ((Port) port).disconnectUsbDevice();
        }
      }
    }
  }
}
