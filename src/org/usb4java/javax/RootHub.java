/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.util.ArrayList;
import java.util.List;
import javax.usb.*;
import javax.usb.event.UsbDeviceListener;
import javax.usb.exception.UsbException;
import javax.usb.util.DefaultUsbControlIrp;
import org.usb4java.javax.descriptors.SimpleUsbDeviceDescriptor;

/**
 * The virtual USB root hub.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class RootHub implements UsbHub, IUsbPorts {

  /**
   * The manufacturer string.
   */
  private static final String MANUFACTURER = "usb4java";

  /**
   * The manufacturer string.
   */
  private static final String PRODUCT = "root hub";

  /**
   * The serial number.
   */
  private static final String SERIAL_NUMBER = "1.0.0";

  /**
   * The configurations.
   */
  private final List<UsbConfiguration> configurations
    = new ArrayList<>(1);

  /**
   * The device descriptor.
   */
  private final UsbDeviceDescriptor descriptor = new SimpleUsbDeviceDescriptor(UsbConst.DESCRIPTOR_MIN_LENGTH_DEVICE,
                                                                               UsbConst.DESCRIPTOR_TYPE_DEVICE,
                                                                               (short) 0x101,
                                                                               UsbConst.HUB_CLASSCODE,
                                                                               (byte) 0,
                                                                               (byte) 0,
                                                                               (byte) 8,
                                                                               (short) 0xffff,
                                                                               (short) 0xffff,
                                                                               (byte) 0,
                                                                               (byte) 1,
                                                                               (byte) 2,
                                                                               (byte) 3,
                                                                               (byte) 1);

  /**
   * The device listeners.
   */
  private final DeviceListenerList listeners = new DeviceListenerList();

  /**
   * The root hub ports.
   */
  private final Ports rootPorts = new Ports(this);

  /**
   * Constructor.
   */
  public RootHub() {
    this.configurations.add(new RootHubConfiguration(this));
  }

  @Override
  public UsbPort getParentUsbPort() {
    return null;
  }

  @Override
  public boolean isUsbHub() {
    return true;
  }

  @Override
  public String getManufacturerString() {
    return MANUFACTURER;
  }

  @Override
  public String getSerialNumberString() {
    return SERIAL_NUMBER;
  }

  @Override
  public String getProductString() {
    return PRODUCT;
  }

  @Override
  public Object getSpeed() {
    return UsbConst.DEVICE_SPEED_UNKNOWN;
  }

  @Override
  public List<UsbConfiguration> getUsbConfigurations() {
    return this.configurations;
  }

  @Override
  public UsbConfiguration getUsbConfiguration(final byte number) {
    if (number != 1) {
      return null;
    }
    return this.configurations.get(0);
  }

  @Override
  public boolean containsUsbConfiguration(final byte number) {
    return number == 1;
  }

  @Override
  public byte getActiveUsbConfigurationNumber() {
    return 1;
  }

  @Override
  public UsbConfiguration getActiveUsbConfiguration() {
    return this.configurations.get(0);
  }

  @Override
  public boolean isConfigured() {
    return true;
  }

  @Override
  public UsbDeviceDescriptor getUsbDeviceDescriptor() {
    return this.descriptor;
  }

  @Override
  public UsbStringDescriptor getUsbStringDescriptor(final byte index)
    throws UsbException {
    throw new UsbException(
      "Can't get USB string descriptor from virtual device");
  }

  @Override
  public String getString(final byte index) throws UsbException {
    throw new UsbException("Can't get string from virtual device");
  }

  @Override
  public void syncSubmit(final UsbControlIrp irp) throws UsbException {
    throw new UsbException("Can't syncSubmit on virtual device");
  }

  @Override
  public void asyncSubmit(final UsbControlIrp irp) throws UsbException {
    throw new UsbException("Can't asyncSubmit on virtual device");
  }

  @Override
  public void syncSubmit(final List<UsbControlIrp> list) throws UsbException {
    throw new UsbException("Can't syncSubmit on virtual device");
  }

  @Override
  public void asyncSubmit(final List<UsbControlIrp> list) throws UsbException {
    throw new UsbException("Can't asyncSubmit on virtual device");
  }

  @Override
  public UsbControlIrp createUsbControlIrp(final byte bmRequestType,
                                           final byte bRequest,
                                           final short wValue, final short wIndex) {
    return new DefaultUsbControlIrp(bmRequestType, bRequest, wValue,
                                    wIndex);
  }

  @Override
  public void addUsbDeviceListener(final UsbDeviceListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void removeUsbDeviceListener(final UsbDeviceListener listener) {
    this.listeners.remove(listener);
  }

  @Override
  public byte getNumberOfPorts() {
    return this.rootPorts.getNumberOfPorts();
  }

  @Override
  public List<UsbPort> getUsbPorts() {
    return this.rootPorts.getUsbPorts();
  }

  @Override
  public UsbPort getUsbPort(final byte number) {
    return this.rootPorts.getUsbPort(number);
  }

  @Override
  public List<UsbDevice> getAttachedUsbDevices() {
    return this.rootPorts.getAttachedUsbDevices();
  }

  @Override
  public boolean isUsbDeviceAttached(final UsbDevice device) {
    return this.rootPorts.isUsbDeviceAttached(device);
  }

  @Override
  public boolean isRootUsbHub() {
    return true;
  }

  @Override
  public void connectUsbDevice(final UsbDevice device) {
    this.rootPorts.connectUsbDevice(device);
  }

  @Override
  public void disconnectUsbDevice(final UsbDevice device) {
    this.rootPorts.disconnectUsbDevice(device);
  }

  @Override
  public String toString() {
    return this.getManufacturerString()
      + " " + this.getProductString()
      + " " + this.getSerialNumberString();
  }
}
