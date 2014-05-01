/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import javax.usb.*;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.IUsbDeviceListener;
import javax.usb.exception.UsbClaimException;
import javax.usb.exception.UsbDisconnectedException;
import javax.usb.exception.UsbException;
import javax.usb.exception.UsbPlatformException;
import javax.usb.ri.UsbControlIrp;
import org.usb4java.ConfigDescriptor;
import org.usb4java.Device;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;
import org.usb4java.javax.descriptors.SimpleUsbStringDescriptor;

/**
 * A Usb device.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
@SuppressWarnings("ProtectedField")
public abstract class AUsbDevice implements IUsbDevice {

  /**
   * The USB device manager.
   */
  protected final DeviceManager manager;

  /**
   * The device id.
   */
  protected final DeviceId id;

  /**
   * The parent id. Null if no parent exists.
   */
  protected final DeviceId parentId;

  /**
   * The device speed.
   */
  protected final int speed;

  /**
   * The device configurations.
   */
  protected List<IUsbConfiguration> configurations;

  /**
   * Mapping from configuration value to configuration.
   */
  protected final Map<Byte, IUsbConfiguration> configMapping = new HashMap<>();

  /**
   * The USB device listener list.
   */
  protected final DeviceListenerList listeners = new DeviceListenerList();

  /**
   * The device handle. Null if not open.
   */
  protected DeviceHandle handle;

  /**
   * The number of the currently active configuration.
   */
  protected byte activeConfigurationNumber = 0;

  /**
   * The numbers of the currently claimed interface.
   */
  protected final Set<Byte> claimedInterfaceNumbers = new HashSet<>();

  /**
   * The port this device is connected to.
   */
  protected IUsbPort port;

  /**
   * The IRP queue.
   */
  protected final ControlIrpQueue queue = new ControlIrpQueue(this, this.listeners);

  /**
   * If kernel driver was detached when interface was claimed.
   */
  protected boolean detachedKernelDriver;

  /**
   * Constructs a new device.
   * <p>
   * @param manager  The USB device manager which is responsible for this
   *                 device.
   * @param id       The device id. Must not be null.
   * @param parentId The parent device id. May be null if this device has no
   *                 parent (Because it is a root device).
   * @param speed    The device speed.
   * @param device   The libusb device. This reference is only valid during the
   *                 constructor execution, so don't store it in a property or
   *                 something like that.
   * @throws UsbPlatformException When device configuration could not be read.
   */
  public AUsbDevice(final DeviceManager manager, final DeviceId id, final DeviceId parentId, final int speed, final Device device) throws UsbPlatformException {
    if (manager == null) {
      throw new IllegalArgumentException("manager must be set");
    }
    if (id == null) {
      throw new IllegalArgumentException("id must be set");
    }
    this.manager = manager;
    this.id = id;
    this.parentId = parentId;
    this.speed = speed;

    // Read device configurations
    final int numConfigurations = id.getDeviceDescriptor().bNumConfigurations() & 0xff;
    final List<IUsbConfiguration> configurationTemp = new ArrayList<>(numConfigurations);
    for (int i = 0; i < numConfigurations; i += 1) {
      final ConfigDescriptor configDescriptor = new ConfigDescriptor();
      final int result = LibUsb.getConfigDescriptor(device, (byte) i,
                                                    configDescriptor);
      if (result < 0) {
        throw ExceptionUtils.createPlatformException(
          "Unable to get configuation " + i + " for device " + id,
          result);
      }
      try {
        final Configuration config = new Configuration(
          this, configDescriptor);
        configurationTemp.add(config);
        this.configMapping.put(configDescriptor.bConfigurationValue(),
                               config);
      } finally {
        LibUsb.freeConfigDescriptor(configDescriptor);
      }
    }
    this.configurations = Collections.unmodifiableList(configurationTemp);
    /**
     * Determine the active configuration number
     */
    final ConfigDescriptor configDescriptor = new ConfigDescriptor();
    final int result = LibUsb.getActiveConfigDescriptor(device, configDescriptor);
    /**
     * ERROR_NOT_FOUND is returned when device is in unconfigured state. On OSX
     * it may return INVALID_PARAM in this case because of a bug in libusb
     */
    if (result == LibUsb.ERROR_NOT_FOUND || result == LibUsb.ERROR_INVALID_PARAM) {
      this.activeConfigurationNumber = 0;
    } else if (result < 0) {
      throw ExceptionUtils.createPlatformException("Unable to read active config descriptor from device " + id, result);
    } else {
      this.activeConfigurationNumber = configDescriptor.bConfigurationValue();
      LibUsb.freeConfigDescriptor(configDescriptor);
    }
  }

  /**
   * Returns the device id.
   * <p>
   * @return The device id.
   */
  public final DeviceId getId() {
    return this.id;
  }

  /**
   * Returns the parent device id.
   * <p>
   * @return The parent device id or null of there is no parent.
   */
  public final DeviceId getParentId() {
    return this.parentId;
  }

  /**
   * Ensures the device is connected.
   * <p>
   * @throws UsbDisconnectedException When device is disconnected.
   */
  final void checkConnected() {
    if (this.port == null) {
      throw new UsbDisconnectedException();
    }
  }

  /**
   * Opens the USB device and returns the USB device handle. If device was
   * already open then the old handle is returned.
   * <p>
   * @return The USB device handle.
   * @throws UsbException When USB device could not be opened.
   */
  public final DeviceHandle open() throws UsbException {
    if (this.handle == null) {
      final Device device = this.manager.getLibUsbDevice(this.id);
      try {
        final DeviceHandle deviceHandle = DeviceHandle.getInstance();
        final int result = LibUsb.open(device, deviceHandle);
        if (result < 0) {
          throw ExceptionUtils.createPlatformException("Can't open device " + this.id, result);
        }
        this.handle = deviceHandle;
      } finally {
        this.manager.releaseDevice(device);
      }
    }
    return this.handle;
  }

  /**
   * Closes the device. If device is not open then nothing is done.
   */
  public final void close() {
    if (this.handle != null) {
      LibUsb.close(this.handle);
      this.handle = null;
    }
  }

  @Override
  public final IUsbPort getParentUsbPort() {
    checkConnected();
    return this.port;
  }

  /**
   * Sets the parent USB port. If port is unset then a usbDeviceDetached event
   * is send.
   * <p>
   * @param port The port to set. Null to unset.
   */
  final void setParentUsbPort(final IUsbPort port) {
    if (this.port == null && port == null) {
      throw new IllegalStateException("Device already detached");
    }
    if (this.port != null && port != null) {
      throw new IllegalStateException("Device already attached");
    }

    // Disconnect client devices
    if (port == null && isUsbHub()) {
      final Hub hub = (Hub) this;
      for (final IUsbDevice device : hub.getAttachedUsbDevices()) {
        hub.disconnectUsbDevice(device);
      }
    }

    this.port = port;

    final Services services = Services.getInstance();

    if (port == null) {
      this.listeners.usbDeviceDetached(new UsbDeviceEvent(this));
      services.usbDeviceDetached(this);
    } else {
      services.usbDeviceAttached(this);
    }
  }

  @Override
  public final String getManufacturerString() throws UsbException,
                                                     UnsupportedEncodingException {
    checkConnected();
    final byte index = getUsbDeviceDescriptor().iManufacturer();
    if (index == 0) {
      return null;
    }
    return getString(index);
  }

  @Override
  public final String getSerialNumberString() throws UsbException,
                                                     UnsupportedEncodingException {
    checkConnected();
    final byte index = getUsbDeviceDescriptor().iSerialNumber();
    if (index == 0) {
      return null;
    }
    return getString(index);
  }

  @Override
  public final String getProductString() throws UsbException,
                                                UnsupportedEncodingException {
    checkConnected();
    final byte index = getUsbDeviceDescriptor().iProduct();
    if (index == 0) {
      return null;
    }
    return getString(index);
  }

  @Override
  public final Object getSpeed() {
    switch (this.speed) {
      case LibUsb.SPEED_FULL:
        return IUsbConst.DEVICE_SPEED_FULL;
      case LibUsb.SPEED_LOW:
        return IUsbConst.DEVICE_SPEED_LOW;
      default:
        return IUsbConst.DEVICE_SPEED_UNKNOWN;
    }
  }

  @Override
  public final List<IUsbConfiguration> getUsbConfigurations() {
    return this.configurations;
  }

  @Override
  public final IUsbConfiguration getUsbConfiguration(final byte number) {
    return this.configMapping.get(number);
  }

  @Override
  public final boolean containsUsbConfiguration(final byte number) {
    return this.configMapping.containsKey(number);
  }

  @Override
  public final byte getActiveUsbConfigurationNumber() {
    return this.activeConfigurationNumber;
  }

  /**
   * Sets the active USB configuration.
   * <p>
   * @param number The number of the USB configuration to activate.
   * @throws UsbException When configuration could not be activated.
   */
  final void setActiveUsbConfigurationNumber(final byte number)
    throws UsbException {
    if (number != this.activeConfigurationNumber) {
      if (!this.claimedInterfaceNumbers.isEmpty()) {
        throw new UsbException("Can't change configuration while an "
          + "interface is still claimed");
      }

      final int result = LibUsb.setConfiguration(open(), number & 0xff);
      if (result < 0) {
        throw ExceptionUtils.createPlatformException(
          "Unable to set configuration", result);
      }
      this.activeConfigurationNumber = number;
    }
  }

  /**
   * Claims the specified interface.
   * <p>
   * @param number The number of the interface to claim.
   * @param force  If claim should be forces if possible.
   * @throws UsbException When interface could not be claimed.
   */
  final void claimInterface(final byte number, final boolean force)
    throws UsbException {
    if (this.claimedInterfaceNumbers.contains(number)) {
      throw new UsbClaimException("An interface is already claimed");
    }

    final DeviceHandle deviceHandle = open();

    // Detach existing driver from the device if requested and
    // libusb supports it.
    if (force) {
      int result = LibUsb.kernelDriverActive(deviceHandle, number);
      if (result == LibUsb.ERROR_NO_DEVICE) {
        throw new UsbDisconnectedException();
      }
      if (result == 1) {
        result = LibUsb.detachKernelDriver(deviceHandle, number);
        if (result < 0) {
          throw ExceptionUtils.createPlatformException(
            "Unable to detach kernel driver", result);
        }
        this.detachedKernelDriver = true;
      }
    }

    final int result = LibUsb.claimInterface(deviceHandle, number & 0xff);
    if (result < 0) {
      throw ExceptionUtils.createPlatformException(
        "Unable to claim interface", result);
    }
    this.claimedInterfaceNumbers.add(number);
  }

  /**
   * Releases a claimed interface.
   * <p>
   * @param number The number of the interface to release.
   * @throws UsbException When interface could not be claimed.
   */
  final void releaseInterface(final byte number) throws UsbException {
    if (this.claimedInterfaceNumbers.isEmpty()) {
      throw new UsbClaimException("No interface is claimed");
    }
    if (!this.claimedInterfaceNumbers.contains(number)) {
      throw new UsbClaimException("Interface not claimed");
    }

    final DeviceHandle deviceHandle = open();
    int result = LibUsb.releaseInterface(deviceHandle, number & 0xff);
    if (result < 0) {
      throw ExceptionUtils.createPlatformException("Unable to release interface", result);
    }

    if (this.detachedKernelDriver) {
      result = LibUsb.attachKernelDriver(deviceHandle, number & 0xff);
      if (result < 0) {
        throw ExceptionUtils.createPlatformException("Uanble to re-attach kernel driver", result);
      }
    }

    this.claimedInterfaceNumbers.remove(number);
  }

  /**
   * Checks if the specified interface is claimed.
   * <p>
   * @param number The number of the interface to check.
   * @return True if interface is claimed, false if not.
   */
  final boolean isInterfaceClaimed(final byte number) {
    return this.claimedInterfaceNumbers.contains(number);
  }

  @Override
  public final IUsbConfiguration getActiveUsbConfiguration() {
    return getUsbConfiguration(getActiveUsbConfigurationNumber());
  }

  @Override
  public final boolean isConfigured() {
    return getActiveUsbConfigurationNumber() != 0;
  }

  @Override
  public final IUsbDeviceDescriptor getUsbDeviceDescriptor() {
    return this.id.getDeviceDescriptor();
  }

  @Override
  public final IUsbStringDescriptor getUsbStringDescriptor(final byte index)
    throws UsbException {
    checkConnected();
    final short[] languages = getLanguages();
    final DeviceHandle deviceHandle = open();
    final short langId = languages.length == 0 ? 0 : languages[0];
    final ByteBuffer data = ByteBuffer.allocateDirect(256);
    final int result = LibUsb.getStringDescriptor(deviceHandle, index, langId, data);
    if (result < 0) {
      throw ExceptionUtils.createPlatformException("Unable to get string descriptor " + index + " from device " + this, result);
    }
    return new SimpleUsbStringDescriptor(data);
  }

  @Override
  public final String getString(final byte index) throws UsbException,
                                                         UnsupportedEncodingException {
    return getUsbStringDescriptor(index).getString();
  }

  /**
   * Returns the languages the specified device supports.
   * <p>
   * @return Array with supported language codes. Never null. May be empty.
   * @throws UsbException When string descriptor languages could not be read.
   */
  protected short[] getLanguages() throws UsbException {
    final DeviceHandle deviceHandle = open();
    final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
    final int result = LibUsb.getDescriptor(deviceHandle, LibUsb.DT_STRING, (byte) 0, buffer);
    if (result < 0) {
      throw ExceptionUtils.createPlatformException("Unable to get string descriptor languages", result);
    }
    if (result < 2) {
      throw new UsbException("Received illegal descriptor length: " + result);
    }
    final short[] languages = new short[(result - 2) / 2];
    if (languages.length == 0) {
      return languages;
    }
    buffer.position(2);
    buffer.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(languages);
    return languages;
  }

  @Override
  public final void syncSubmit(final IUsbControlIrp irp) throws UsbException {
    if (irp == null) {
      throw new IllegalArgumentException("irp must not be null");
    }
    checkConnected();
    this.queue.add(irp);
    irp.waitUntilComplete();
    if (irp.isUsbException()) {
      throw irp.getUsbException();
    }
  }

  @Override
  public final void asyncSubmit(final IUsbControlIrp irp) {
    if (irp == null) {
      throw new IllegalArgumentException("irp must not be null");
    }
    checkConnected();
    this.queue.add(irp);
  }

  @Override
  public final void syncSubmit(final List<IUsbControlIrp> list) throws UsbException {
    if (list == null) {
      throw new IllegalArgumentException("list must not be null");
    }
    checkConnected();
    for (final IUsbControlIrp item : list) {
      syncSubmit(item);
    }
  }

  @Override
  public final void asyncSubmit(final List<IUsbControlIrp> list) {
    if (list == null) {
      throw new IllegalArgumentException("list must not be null");
    }
    checkConnected();
    for (final IUsbControlIrp item : list) {
      asyncSubmit(item);
    }
  }

  @Override
  public final IUsbControlIrp createUsbControlIrp(final byte bmRequestType,
                                                 final byte bRequest, final short wValue, final short wIndex) {
    return new UsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
  }

  @Override
  public final void addUsbDeviceListener(final IUsbDeviceListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public final void removeUsbDeviceListener(final IUsbDeviceListener listener) {
    this.listeners.remove(listener);
  }

  @Override
  public final String toString() {
    return this.id.toString();
  }
}
