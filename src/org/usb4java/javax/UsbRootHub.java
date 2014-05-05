/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.util.ArrayList;
import java.util.List;
import javax.usb.*;
import javax.usb.event.IUsbDeviceListener;
import javax.usb.exception.UsbException;
import javax.usb.ri.UsbControlIrp;
import javax.usb.ri.enumerated.EDevicePortSpeed;
import javax.usb.ri.enumerated.EUSBClassCode;
import org.usb4java.javax.descriptors.UsbDeviceDescriptor;

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
 * Developer note: For the root hub, the signals from the upstream facing port
 * state machines are implementation dependent. This implementation uses the
 * <code>libusb</code> library to interface with the host operating system.
 * <p>
 * @see <a href="http://www.libusb.org/">libusb</a>
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
public final class UsbRootHub implements IUsbHub, IUsbPorts {

  /**
   * The UsbRootHub (virtual) manufacturer string.
   */
  private static final String MANUFACTURER = "org.usb4java";

  /**
   * The UsbRootHub (virtual) product string.
   */
  private static final String PRODUCT = "Root Hub";

  /**
   * The UsbRootHub (virtual) serial number.
   */
  private static final String SERIAL_NUMBER = "1.0.0";

  /**
   * The configurations.
   */
  private final List<IUsbConfiguration> configurations = new ArrayList<>(1);

  /**
   * The device descriptor.
   */
  private final IUsbDeviceDescriptor descriptor = new UsbDeviceDescriptor((short) 0x101,
                                                                          EUSBClassCode.HUB,
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
  private final UsbPorts rootPorts = new UsbPorts(this);

  /**
   * Constructor.
   */
  public UsbRootHub() {
    this.configurations.add(new UsbRootHubConfiguration(this));
  }

  /**
   * Get the IUsbPort on the parent UsbHub that this device is connected to.
   * <p>
   * @return The port on the parent UsbHub that this is attached to.
   */
  @Override
  public IUsbPort getParentUsbPort() {
    return null;
  }

  /**
   * If this is a UsbHub.
   * <p>
   * @return true if this is a UsbHub.
   */
  @Override
  public boolean isUsbHub() {
    return true;
  }

  /**
   * Get the manufacturer String.
   * <p>
   * This is a convienence method, which uses
   * {@link #getString(byte) getString}.
   * <p>
   * @return The manufacturer String, or null.
   */
  @Override
  public String getManufacturerString() {
    return MANUFACTURER;
  }

  /**
   * Get the serial number String.
   * <p>
   * This is a convienence method, which uses
   * {@link #getString(byte) getString}.
   * <p>
   * @return The serial number String, or null.
   */
  @Override
  public String getSerialNumberString() {
    return SERIAL_NUMBER;
  }

  /**
   * Get the product String.
   * <p>
   * This is a convienence method, which uses
   * {@link #getString(byte) getString}.
   * <p>
   * @return The product String, or null.
   */
  @Override
  public String getProductString() {
    return PRODUCT;
  }

  /**
   * Get the speed of the device.
   * <p>
   * The speed will be one of:
   * <ul>
   * <li>{@link javax.usb.UsbConst#DEVICE_SPEED_UNKNOWN UsbConst.DEVICE_SPEED_UNKNOWN}</li>
   * <li>{@link javax.usb.UsbConst#DEVICE_SPEED_LOW UsbConst.DEVICE_SPEED_LOW}</li>
   * <li>{@link javax.usb.UsbConst#DEVICE_SPEED_FULL UsbConst.DEVICE_SPEED_FULL}</li>
   * </ul>
   * <p>
   * @return The speed of this device. (Always DEVICE_SPEED_UNKNOWN for a
   *         virtual device)
   */
  @Override
  public EDevicePortSpeed getSpeed() {
    return EDevicePortSpeed.DEVICE_SPEED_UNKNOWN;
  }

  /**
   * Get all IUsbConfigurations for this device.
   * <p>
   * The List is unmodifiable.
   * <p>
   * @return All IUsbConfigurations for this device.
   */
  @Override
  public List<IUsbConfiguration> getUsbConfigurations() {
    return this.configurations;
  }

  /**
   * Get the specified IUsbConfiguration.
   * <p>
   * If the specified IUsbConfiguration does not exist, null is returned. Config
   * number 0 is reserved for the Not Configured state (see the USB 1.1
   * specification section 9.4.2). Obviously, no IUsbConfiguration exists for
   * the Not Configured state.
   * <p>
   * @param number the bytecode address of the configuration value
   * @return The specified IUsbConfiguration, or null.
   */
  @Override
  public IUsbConfiguration getUsbConfiguration(final byte number) {
    if (number != 1) {
      return null;
    }
    return this.configurations.get(0);
  }

  /**
   * If this IUsbDevice contains the specified IUsbConfiguration.
   * <p>
   * This will return false for zero (the Not Configured state).
   * <p>
   * @param number the bytecode address of the configuration value
   * @return If the specified IUsbConfiguration is contained in this IUsbDevice.
   */
  @Override
  public boolean containsUsbConfiguration(final byte number) {
    return number == 1;
  }

  /**
   * Get the number of the active IUsbConfiguration.
   * <p>
   * If the device is in a Not Configured state, this will return zero.
   * <p>
   * @return The active config number. (Always -1 for virtual device)
   */
  @Override
  public byte getActiveUsbConfigurationNumber() {
    return 1;
  }

  /**
   * Get the active IUsbConfiguration.
   * <p>
   * If this device is Not Configured, this returns null.
   * <p>
   * @return The active IUsbConfiguration, or null.
   */
  @Override
  public IUsbConfiguration getActiveUsbConfiguration() {
    return this.configurations.get(0);
  }

  /**
   * If this IUsbDevice is configured.
   * <p>
   * This returns true if the device is in the configured state as shown in the
   * USB 1.1 specification table 9.1.
   * <p>
   * @return If this is in the Configured state. (Always TRUE for virtual
   *         device)
   */
  @Override
  public boolean isConfigured() {
    return true;
  }

  /**
   * Get the device descriptor.
   * <p>
   * The descriptor may be cached.
   * <p>
   * @return The device descriptor.
   */
  @Override
  public IUsbDeviceDescriptor getUsbDeviceDescriptor() {
    return this.descriptor;
  }

  /**
   * Get the specified string descriptor.
   * <p>
   * This is a convienence method. The IUsbStringDescriptor may be cached. If
   * the device does not support strings or does not define the specified string
   * descriptor, this returns null.
   * <p>
   * @param index The index of the string descriptor to get.
   * @return The specified string descriptor.
   * @exception UsbException If an error occurred while getting the string
   *                         descriptor.
   * @deprecated Can't get USB string descriptor from virtual device
   */
  @Override
  public IUsbStringDescriptor getUsbStringDescriptor(final byte index) throws UsbException {
    throw new UsbException("Can't get USB string descriptor from virtual device");
  }

  /**
   * Get the String from the specified string descriptor.
   * <p>
   * This is a convienence method, which uses
   * {@link #getUsbStringDescriptor(byte) getIUsbStringDescriptor()}.
   * {@link javax.usb.UsbStringDescriptor#getString() getString()}.
   * <p>
   * @param index The index of the string to get.
   * @return The specified String.
   * @exception UsbException If an error occurred while getting the String.
   * @deprecated Can't get string from virtual device
   */
  @Override
  public String getString(final byte index) throws UsbException {
    throw new UsbException("Can't get string from virtual device");
  }

  /**
   * Submit a IUsbControlIrp synchronously to the Default Control Pipe.
   * <p>
   * @param irp The IUsbControlIrp.
   * @exception UsbException             If an error occurrs.
   * @throws IllegalArgumentException If the IUsbControlIrp is not valid.
   * @deprecated Can't syncSubmit on virtual device
   */
  @Override
  public void syncSubmit(final IUsbControlIrp irp) throws UsbException {
    throw new UsbException("Can't syncSubmit on virtual device");
  }

  /**
   * Submit a IUsbControlIrp asynchronously to the Default Control Pipe.
   * <p>
   * @param irp The IUsbControlIrp.
   * @exception UsbException             If an error occurrs.
   * @throws IllegalArgumentException If the IUsbControlIrp is not valid.
   * @deprecated Can't asyncSubmit on virtual device
   */
  @Override
  public void asyncSubmit(final IUsbControlIrp irp) throws UsbException {
    throw new UsbException("Can't asyncSubmit on virtual device");
  }

  /**
   * Submit a List of IUsbControlIrps synchronously to the Default Control Pipe.
   * <p>
   * All IUsbControlIrps are guaranteed to be atomically (with respect to other
   * clients of this API) submitted to the Default Control Pipe. Atomicity on a
   * native level is implementation-dependent.
   * <p>
   * @param list The List of IUsbControlIrps.
   * @exception UsbException             If an error occurrs.
   * @throws IllegalArgumentException If the List contains non-IUsbControlIrp
   *                                  objects or those UsbIrp(s) are invalid.
   * @deprecated Can't syncSubmit on virtual device
   */
  @Override
  public void syncSubmit(final List<IUsbControlIrp> list) throws UsbException {
    throw new UsbException("Can't syncSubmit on virtual device");
  }

  /**
   * Submit a List of IUsbControlIrps asynchronously to the Default Control
   * Pipe.
   * <p>
   * All IUsbControlIrps are guaranteed to be atomically (with respect to other
   * clients of this API) submitted to the Default Control Pipe. Atomicity on a
   * native level is implementation-dependent.
   * <p>
   * @param list The List of IUsbControlIrps.
   * @exception UsbException             If an error occurrs.
   * @throws IllegalArgumentException If the List contains non-IUsbControlIrp
   *                                  objects or those UsbIrp(s) are invalid.
   * @deprecated Can't asyncSubmit on virtual device
   */
  @Override
  public void asyncSubmit(final List<IUsbControlIrp> list) throws UsbException {
    throw new UsbException("Can't asyncSubmit on virtual device");
  }

  /**
   * Create a IUsbControlIrp.
   * <p>
   * This creates a IUsbControlIrp that may be optimized for use on this
   * IUsbDevice. Using this UsbIrp instead of a
   * {@link javax.usb.util.DefaultUsbControlIrp DefaultIUsbControlIrp} may
   * increase performance or decrease memory requirements.
   * <p>
   * The IUsbDevice cannot require this IUsbControlIrp to be used, all submit
   * methods <i>must</i> accept any IUsbControlIrp implementation.
   * <p>
   * @param bmRequestType The bmRequestType.
   * @param bRequest      The bRequest.
   * @param wValue        The wValue.
   * @param wIndex        The wIndex.
   * @return A IUsbControlIrp ready for use.
   */
  @Override
  public IUsbControlIrp createUsbControlIrp(final byte bmRequestType,
                                            final byte bRequest,
                                            final short wValue,
                                            final short wIndex) {
    return new UsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
  }

  /**
   * Add a IIUsbDeviceListener to this IUsbDevice.
   * <p>
   * @param listener The IIUsbDeviceListener to add.
   */
  @Override
  public void addUsbDeviceListener(final IUsbDeviceListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Remove a IIUsbDeviceListener from this IUsbDevice.
   * <p>
   * @param listener The listener to remove.
   */
  @Override
  public void removeUsbDeviceListener(final IUsbDeviceListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Get the number of (downstream) ports this hub has.
   * <p>
   * This is only the number of ports on the hub, not all ports are necessarily
   * enabled, available, usable, or in some cases physically present. This only
   * represents the number of downstream ports the hub claims to have. Note that
   * all hubs have exactly one upstream port, which allows it to connect to the
   * system (or another upstream hub). There is also a internal port which is
   * generally only used by the hub itself. See the USB 1.1 specification sec
   * 11.4 for details on the internal port, sec 11.5 for details on the
   * downstream ports, and sec 11.6 for details on the upstream port.
   * <p>
   * @return The number of (downstream) ports for this hub.
   */
  @Override
  public byte getNumberOfPorts() {
    return this.rootPorts.getNumberOfPorts();
  }

  /**
   * Get all the ports this hub has.
   * <p>
   * The port numbering is 1-based.
   * <p>
   * The List will be unmodifiable.
   * <p>
   * @return All ports this hub has.
   * @see #getUsbPort( byte number )
   */
  @Override
  public List<IUsbPort> getUsbPorts() {
    return this.rootPorts.getUsbPorts();
  }

  /**
   * Get a specific IUsbPort by port number.
   * <p>
   * This gets the IUsbPort with the specified number. The port numbering is
   * 1-based (not 0-based), and the max port number is 255. See the USB 1.1
   * specification table 11.8 offset 7.
   * <p>
   * If the specified port does not exist, this returns null.
   * <p>
   * @param number The number (1-based) of the port to get.
   * @return The specified port, or null.
   */
  @Override
  public IUsbPort getUsbPort(final byte number) {
    return this.rootPorts.getUsbPort(number);
  }

  /**
   * Get all attached IUsbDevices.
   * <p>
   * The List will be unmodifiable.
   * <p>
   * @return All devices currently attached to this hub.
   */
  @Override
  public List<IUsbDevice> getAttachedUsbDevices() {
    return this.rootPorts.getAttachedUsbDevices();
  }

  @Override
  public boolean isUsbDeviceAttached(final IUsbDevice device) {
    return this.rootPorts.isUsbDeviceAttached(device);
  }

  /**
   * If this is the
   * {@link javax.usb.UsbServices#getRootUsbHub() virtual root hub}.
   * <p>
   * @return If this is the virtual root hub.
   */
  @Override
  public boolean isRootUsbHub() {
    return true;
  }

  @Override
  public void connectUsbDevice(final IUsbDevice device) {
    this.rootPorts.connectUsbDevice(device);
  }

  @Override
  public void disconnectUsbDevice(final IUsbDevice device) {
    this.rootPorts.disconnectUsbDevice(device);
  }

  @Override
  public String toString() {
    return this.getManufacturerString()
      + " " + this.getProductString()
      + " " + this.getSerialNumberString();
  }
}
