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
package javax.usb3.ri;

import java.util.Arrays;
import java.util.List;
import javax.usb3.*;
import javax.usb3.descriptor.UsbDeviceDescriptor;
import javax.usb3.enumerated.EDevicePortSpeed;
import javax.usb3.enumerated.EUSBClassCode;
import javax.usb3.enumerated.EUSBSubclassCode;
import javax.usb3.event.IUsbDeviceListener;
import javax.usb3.exception.UsbDisconnectedException;
import javax.usb3.exception.UsbException;

/**
 * The USB (virtual) root hub. (Tier 1 in a bus topology)
 * <p>
 * Root Hub: A USB hub directly attached to or integrated into the host
 * controller.
 * <p>
 * The root hub provides the connection between the Host Controller and one or
 * more USB ports. The root hub provides the same functionality for dealing with
 * USB topology as other hubs (see Chapter 11), except that the hardware and
 * software interface between the root hub and the Host Controller is defined by
 * the specific hardware implementation.
 * <p>
 * The USB connects USB devices with the USB host. The USB physical interconnect
 * is a tiered star topology. A hub is at the center of each star. There is only
 * one host in any USB system. A UsbRootHub is integrated within the host system
 * to provide one or more attachment points.
 * <p>
 * Due to timing constraints allowed for hub and cable propagation times, the
 * maximum number of tiers allowed is seven (including the root tier). Note that
 * in seven tiers, five non-root hubs maximum can be supported in a
 * communication path between the host and any device.
 * <p>
 * Developer note: For the (virtual) root hub signals from the upstream facing
 * port state machines are implementation dependent. This implementation uses
 * the {@code libusb} library to interface with the host operating system.
 *
 * @see <a href="http://www.libusb.org/">libusb</a>
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class UsbRootHub implements IUsbHub, IUsbPorts {

  /**
   * The UsbRootHub (virtual) manufacturer string.
   */
  private static final String MANUFACTURER = "Javax USB";

  /**
   * The UsbRootHub (virtual) product string.
   */
  private static final String PRODUCT = "Virtual Root Hub";

  /**
   * The UsbRootHub (virtual) serial number.
   */
  private static final String SERIAL_NUMBER = "1.0.0";

  /**
   * The USB (virtual) root hub configurations.
   */
  private final List<IUsbConfiguration> configurations;

  /**
   * The USB (virtual) root hub device descriptor.
   */
  private final IUsbDeviceDescriptor deviceDescriptor;

  /**
   * Container of all USB device listeners.
   */
  private final UsbDeviceListener listeners;

  /**
   * The USB (virtual) root hub ports.
   */
  private final UsbPorts ports;

  /**
   * Construct a new USB (virtual) root hub.
   */
  public UsbRootHub() {
    this.deviceDescriptor = new UsbDeviceDescriptor((byte) 0x0300,
                                                    EUSBClassCode.HUB,
                                                    EUSBSubclassCode.FULL_SPEED_HUB.getBytecodeSubclass(),
                                                    EUSBSubclassCode.FULL_SPEED_HUB.getBytecodeProtocol(),
                                                    (byte) 0xffff,
                                                    (short) 0x1d6b, // 1d6b  Linux Foundation
                                                    (short) 0x0003, // 	0003  3.0 root hub
                                                    (byte) 0x00,
                                                    (byte) 1,
                                                    (byte) 2,
                                                    (byte) 3,
                                                    (byte) 1);
    this.configurations = Arrays.asList(new UsbRootHubConfiguration(this));
    this.listeners = new UsbDeviceListener();
    this.ports = new UsbPorts(this);
  }

  /**
   * Get the Virtual USB Root HUB Device ID.
   * <p>
   * The Virtual USB Root HUB is hard coded to attach at bus zero, port zero,
   * device zero.
   * <p>
   * Device ID encapsulates a USB Device's location to uniquely identify the
   * device without needing to know or inspect the internal configuration of the
   * device.
   *
   * @inherit
   */
  @Override
  public UsbDeviceId getDeviceId() {
    return new UsbDeviceId(0, 0, 0, deviceDescriptor);
  }

  /**
   * @inherit
   *
   * @deprecated the USB root hub as no parent.
   */
  @Override
  public IUsbPort getParentUsbPort() {
    return null;
  }

  /**
   * @inherit
   *
   * @deprecated the USB root hub as no parent.
   */
  public void setParentUsbPort(IUsbPort port) {
    throw new RuntimeException("Can't set a parent port to the Root USB hub.");
  }

  /**
   * @inherit
   *
   * @return TRUE
   */
  @Override
  public boolean isUsbHub() {
    return true;
  }

  /**
   * @inherit
   *
   * @return {@value #MANUFACTURER}
   */
  @Override
  public String getManufacturerString() {
    return MANUFACTURER;
  }

  /**
   * @inherit
   *
   * @return {@value #SERIAL_NUMBER}
   */
  @Override
  public String getSerialNumberString() {
    return SERIAL_NUMBER;
  }

  /**
   * @inherit
   *
   * @return {@value #PRODUCT}
   */
  @Override
  public String getProductString() {
    return PRODUCT;
  }

  /**
   * @inherit
   *
   * @return {@linkplain javax.usb.enumerated.EDevicePortSpeed#HIGH}
   */
  @Override
  public EDevicePortSpeed getSpeed() {
    return EDevicePortSpeed.HIGH;
  }

  /**
   * @inherit
   */
  @Override
  public List<IUsbConfiguration> getUsbConfigurations() {
    return this.configurations;
  }

  /**
   * @inherit
   */
  @Override
  public IUsbConfiguration getUsbConfiguration(final byte number) {
    if (number != 1) {
      return null;
    }
    return this.configurations.get(0);
  }

  /**
   * @inherit
   *
   * @return 1
   */
  @Override
  public boolean containsUsbConfiguration(final byte number) {
    return number == 1;
  }

  /**
   * @inherit
   *
   * @return 1
   */
  @Override
  public byte getActiveUsbConfigurationNumber() {
    return 1;
  }

  /**
   * @inherit
   */
  @Override
  public IUsbConfiguration getActiveUsbConfiguration() {
    return this.configurations.get(0);
  }

  /**
   * @inherit
   *
   * @return TRUE
   */
  @Override
  public boolean isConfigured() {
    return true;
  }

  /**
   * @inherit
   */
  @Override
  public IUsbDeviceDescriptor getUsbDeviceDescriptor() {
    return this.deviceDescriptor;
  }

  /**
   * @inherit
   *
   * @deprecated Can't get USB string descriptor from a virtual device
   */
  @Override
  public IUsbStringDescriptor getUsbStringDescriptor(final byte index) throws UsbException {
    throw new UsbException("Can't get USB string descriptor from a virtual device");
  }

  /**
   * @inherit
   *
   * @deprecated Can't get string from a virtual device
   */
  @Override
  public String getString(final byte index) throws UsbException {
    throw new UsbException("Can't get string from a virtual device");
  }

  /**
   * @inherit
   *
   * @deprecated Can't syncSubmit a virtual device
   */
  @Override
  public void syncSubmit(final IUsbControlIrp irp) throws UsbException {
    throw new UsbException("Can't syncSubmit a virtual device");
  }

  /**
   * @inherit
   *
   * @deprecated Can't asyncSubmit a virtual device
   */
  @Override
  public void asyncSubmit(final IUsbControlIrp irp) throws UsbException {
    throw new UsbException("Can't asyncSubmit a virtual device");
  }

  /**
   * @inherit
   *
   * @deprecated Can't syncSubmit a virtual device
   */
  @Override
  public void syncSubmit(final List<IUsbControlIrp> list) throws UsbException {
    throw new UsbException("Can't syncSubmit a virtual device");
  }

  /**
   * @inherit
   *
   * @deprecated Can't asyncSubmit a virtual device
   */
  @Override
  public void asyncSubmit(final List<IUsbControlIrp> list) throws UsbException {
    throw new UsbException("Can't asyncSubmit a virtual device");
  }

  /**
   * @inherit
   */
  @Override
  public IUsbControlIrp createUsbControlIrp(final byte bmRequestType,
                                            final byte bRequest,
                                            final short wValue,
                                            final short wIndex) {
    return new UsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
  }

  /**
   * @inherit
   */
  @Override
  public IUsbControlIrp createUsbControlIrp(byte bmRequestType,
                                            byte bRequest,
                                            short wValue,
                                            short wIndex,
                                            byte[] data) {
    return new UsbControlIrp(bmRequestType, bRequest, wValue, wIndex, data);
  }

  /**
   * @inherit
   */
  @Override
  public void addUsbDeviceListener(final IUsbDeviceListener listener) {
    this.listeners.add(listener);
  }

  /**
   * @inherit
   */
  @Override
  public void removeUsbDeviceListener(final IUsbDeviceListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * @inherit
   */
  @Override
  public byte getNumberOfPorts() {
    return this.ports.getNumberOfPorts();
  }

  /**
   * @inherit
   */
  @Override
  public List<IUsbPort> getUsbPorts() {
    return this.ports.getUsbPorts();
  }

  /**
   * @inherit
   */
  @Override
  public IUsbPort getUsbPort(final byte number) {
    return this.ports.getUsbPort(number);
  }

  /**
   * @inherit
   */
  @Override
  public List<IUsbDevice> getAttachedUsbDevices() {
    return this.ports.getAttachedUsbDevices();
  }

  /**
   * @inherit
   */
  @Override
  public boolean isUsbDeviceAttached(final IUsbDevice device) {
    return this.ports.isUsbDeviceAttached(device);
  }

  /**
   * @inherit
   */
  @Override
  public boolean isRootUsbHub() {
    return true;
  }

  /**
   * @inherit
   *
   * @return TRUE
   */
  @Override
  public boolean isConnected() throws UsbDisconnectedException {
    return true;
  }

  /**
   * @inherit
   *
   * @return TRUE
   */
  @Override
  public boolean isInterfaceClaimed(byte number) {
    return true;
  }

  /**
   * @inherit
   */
  @Override
  public void connectUsbDevice(final IUsbDevice device) {
    this.ports.connectUsbDevice(device);
  }

  /**
   * @inherit
   */
  @Override
  public void disconnectUsbDevice(final IUsbDevice device) {
    this.ports.disconnectUsbDevice(device);
  }

  /**
   * Virtual Root Hub is always first.
   *
   * @inherit
   */
  @Override
  public int compareTo(IUsbDevice o) {
    return -1;
  }

  @Override
  public String toString() {
    return this.getManufacturerString()
           + " " + this.getProductString()
           + " (" + getNumberOfPorts() + " ports)";
  }

}
