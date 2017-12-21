/*
 * Copyright (C) 2013 Klaus Reimer
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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.usb3.*;
import javax.usb3.descriptor.UsbStringDescriptor;
import javax.usb3.enumerated.EDescriptorType;
import javax.usb3.enumerated.EDevicePortSpeed;
import javax.usb3.event.IUsbDeviceListener;
import javax.usb3.event.UsbDeviceEvent;
import javax.usb3.exception.UsbClaimException;
import javax.usb3.exception.UsbDisconnectedException;
import javax.usb3.exception.UsbException;
import javax.usb3.exception.UsbPlatformException;
import javax.usb3.utility.UsbExceptionFactory;
import org.usb4java.ConfigDescriptor;
import org.usb4java.Device;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;

/**
 * UsbDevice platform-independent implementation.
 * <p>
 * This implements all required functionality of the IUsbDevice interface plus
 * additional functionality required to interface with the native LIBUSB
 * library.
 * <p>
 * This abstract class is extended by: <ul>
 * <li>UsbHub extends AUsbDevice implements IUsbUsbHub, IUsbPorts</li>
 * <li>UsbDevice extends AUsbDevice</li>
 * </ul>
 * This must be set up before use and/or connection to the topology tree.
 * <ul>
 * <li>The UsbDeviceDescriptor must be set, either in the constructor or by its
 * {@code setUsbDeviceDescriptor(UsbDeviceDescriptor) setter}.</li>
 * <li>The UsbDeviceOs may optionally be set, either in the constructor or by
 * its {@code setUsbDeviceOs(UsbDeviceOs) setter}. If not set, it defaults to a
 * {@code DefaultUsbDeviceOs}.</li>
 * <li>The speed must be set by its {@code setSpeed(Object) setter}.</li>
 * <li>All UsbConfigurations must be
 * {@code addUsbConfiguration(UsbConfiguration) added}.</li>
 * <li>The active config number must be
 * {@code setActiveUsbConfigurationNumber(byte) set}, if this device
 * {@code isConfigured() is configured}.</li>
 * </ul>
 * After setup, this may be connected to the topology tree by using the
 * {@code connect(UsbHub,byte) connect} method. If the connect method is not
 * used, there are additional steps:
 * <ul>
 * <li>Set the parent UsbPort by the
 * {@code setParentUsbPort(UsbPort) setter}.</li>
 * <li>Set this on the UsbPort by its
 * {@code UsbPort#attachUsbDevice(UsbDevice) setter}.</li>
 * </ul>
 *
 * @author Dan Streetman
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
@SuppressWarnings("ProtectedField")
public abstract class AUsbDevice implements IUsbDevice {

  /**
   * The USB device deviceManager.
   */
  protected final UsbDeviceManager deviceManager;
  /**
   * The Unique USB Device ID. This encapsulates a USB Device's BUS location to
   * uniquely identify the device without needing to know or inspect the
   * internal configuration of the device.
   */
  protected final UsbDeviceId deviceId;
  /**
   * The parent USB Device ID. Null if no parent exists.
   */
  protected final UsbDeviceId parentId;
  /**
   * The device speed.
   */
  protected final int speed;
  /**
   * The device configurations.
   */
  protected Collection<IUsbConfiguration> configurations;
  /**
   * Mapping from configuration value to configuration.
   */
  protected final Map<Byte, IUsbConfiguration> configMapping = new HashMap<>();
  /**
   * The USB device listener list.
   */
  protected final UsbDeviceListener listeners = new UsbDeviceListener();
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
   * The IRP controlIrpQueue.
   */
  protected final UsbControlIrpQueue controlIrpQueue = new UsbControlIrpQueue(this, this.listeners);
  /**
   * If kernel driver was detached when interface was claimed.
   */
  protected boolean detachedKernelDriver;

  /**
   * Construct a new device.
   *
   * @param deviceManager The USB device deviceManager which is responsible for
   *                      this device.
   * @param deviceId      The device deviceId. Must not be null.
   * @param parentId      The parent device deviceId. May be null if this device
   *                      has no parent (Because it is a root device).
   * @param speed         The device speed code. This is the native (OS)
   *                      negotiated connection speed for the device.
   * @param device        The libusb native device reference. This reference is
   *                      only valdeviceId during the constructor execution, so
   *                      don't store it in a property or something like that.
   * @throws UsbPlatformException     When device configuration could not be
   *                                  read.
   * @throws IllegalArgumentException if the DeviceManager or DeviceId are null
   */
  public AUsbDevice(final UsbDeviceManager deviceManager,
                    final UsbDeviceId deviceId,
                    final UsbDeviceId parentId,
                    final int speed,
                    final Device device) throws UsbPlatformException {
    if (deviceManager == null) {
      throw new IllegalArgumentException("DeviceManager is required.");
    }
    if (deviceId == null) {
      throw new IllegalArgumentException("DeviceId is required.");
    }
    this.deviceManager = deviceManager;
    this.deviceId = deviceId;
    this.parentId = parentId;
    this.speed = speed;
    /**
     * Read the device configurations
     */
    final int numConfigurations = deviceId.getDeviceDescriptor().bNumConfigurations() & 0xff;
    final Collection<IUsbConfiguration> usbConfigurationTemp = new ArrayList<>(numConfigurations);
    for (int i = 0; i < numConfigurations; i += 1) {
      final ConfigDescriptor configDescriptor = new ConfigDescriptor();
      final int result = LibUsb.getConfigDescriptor(device, (byte) i, configDescriptor);
      if (result < 0) {
        throw UsbExceptionFactory.createPlatformException("Unable to get configuation " + i + " for device " + deviceId, result);
      }
      try {
        final UsbConfiguration usbConfiguration = new UsbConfiguration(this, configDescriptor);
        usbConfigurationTemp.add(usbConfiguration);
        this.configMapping.put(configDescriptor.bConfigurationValue(), usbConfiguration);
      } finally {
        LibUsb.freeConfigDescriptor(configDescriptor);
      }
    }
    this.configurations = Collections.unmodifiableCollection(usbConfigurationTemp);
    /**
     * Determine the active configuration number
     */
    final ConfigDescriptor configDescriptor = new ConfigDescriptor();
    final int result = LibUsb.getActiveConfigDescriptor(device, configDescriptor);
    /**
     * ERROR_NOT_FOUND is returned when device is in unconfigured state. On OSX
     * it may return INVALID_PARAM in this case because of a bug in libusb.
     */
    if (result == LibUsb.ERROR_NOT_FOUND || result == LibUsb.ERROR_INVALID_PARAM) {
      this.activeConfigurationNumber = 0;
    } else if (result < 0) {
      throw UsbExceptionFactory.createPlatformException("Unable to read active config descriptor from device " + deviceId, result);
    } else {
      this.activeConfigurationNumber = configDescriptor.bConfigurationValue();
      LibUsb.freeConfigDescriptor(configDescriptor);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final UsbDeviceId getDeviceId() {
    return this.deviceId;
  }

  /**
   * Returns the parent USB device Id.
   *
   * @return The parent device id or null of there is no parent.
   */
  public final UsbDeviceId getParentDeviceId() {
    return this.parentId;
  }

  /**
   * Check and ensures the device is connected.
   *
   * @return TRUE if the device is connected.
   * @throws UsbDisconnectedException When device is disconnected.
   */
  @Override
  public final boolean isConnected() throws UsbDisconnectedException {
    if (this.port == null) {
      throw new UsbDisconnectedException();
    }
    return true;
  }

  /**
   * Opens the USB device and returns the USB device handle. If device was
   * already open then the old handle is returned.
   *
   * @return The USB device handle.
   * @throws UsbException When USB device could not be opened.
   */
  public final DeviceHandle open() throws UsbException {
    if (this.handle == null) {
      final Device device = this.deviceManager.getLibUsbDevice(this.deviceId);
      try {
        final DeviceHandle deviceHandle = DeviceHandle.getInstance();
        final int result = LibUsb.open(device, deviceHandle);
        if (result < 0) {
          throw UsbExceptionFactory.createPlatformException("Can't open device " + this.deviceId, result);
        }
        this.handle = deviceHandle;
      } finally {
        this.deviceManager.releaseDevice(device);
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

  /**
   * {@inheritDoc}
   */
  @Override
  public final IUsbPort getParentUsbPort() {
    isConnected();
    return this.port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setParentUsbPort(final IUsbPort port) {
    if (this.port == null && port == null) {
      throw new IllegalStateException("Device already detached");
    }
    if (this.port != null && port != null) {
      throw new IllegalStateException("Device already attached");
    }

    // Disconnect client devices
    if (port == null && isUsbHub()) {
      final UsbHub hub = (UsbHub) this;
      for (final IUsbDevice device : hub.getAttachedUsbDevices()) {
        hub.disconnectUsbDevice(device);
      }
    }

    this.port = port;

    try {
      final IUsbServices services = UsbHostManager.getUsbServices();
      if (port == null) {
        this.listeners.usbDeviceDetached(new UsbDeviceEvent(this));
        services.usbDeviceDetached(this);
      } else {
        services.usbDeviceAttached(this);
      }
    } catch (UsbException | SecurityException usbException) {
      Logger.getLogger(AUsbDevice.class.getName()).log(Level.SEVERE, "USB Services error. {0}", usbException.getMessage());
      throw new RuntimeException("Unable to attach USB services: " + usbException);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getManufacturerString() throws UsbException, UnsupportedEncodingException {
    isConnected();
    final byte index = getUsbDeviceDescriptor().iManufacturer();
    if (index == 0) {
      return null;
    }
    return getString(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getSerialNumberString() throws UsbException, UnsupportedEncodingException {
    isConnected();
    final byte index = getUsbDeviceDescriptor().iSerialNumber();
    if (index == 0) {
      return null;
    }
    return getString(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getProductString() throws UsbException, UnsupportedEncodingException {
    isConnected();
    final byte index = getUsbDeviceDescriptor().iProduct();
    if (index == 0) {
      return null;
    }
    return getString(index);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final EDevicePortSpeed getSpeed() {
    return EDevicePortSpeed.speedSupported((short) this.speed);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Collection<IUsbConfiguration> getUsbConfigurations() {
    return this.configurations;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final IUsbConfiguration getUsbConfiguration(final byte number) {
    return this.configMapping.get(number);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean containsUsbConfiguration(final byte number) {
    return this.configMapping.containsKey(number);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final byte getActiveUsbConfigurationNumber() {
    return this.activeConfigurationNumber;
  }

  /**
   * Set the active USB configuration.
   *
   * @param number The number of the USB configuration to activate.
   * @throws UsbException if the new configuration could not be activated.
   */
  public final void setActiveUsbConfigurationNumber(final byte number) throws UsbException {
    if (number != this.activeConfigurationNumber) {
      if (!this.claimedInterfaceNumbers.isEmpty()) {
        throw new UsbException("Cannot change configuration while an interface is still claimed");
      }

      final int result = LibUsb.setConfiguration(open(), number & 0xff);
      if (result < 0) {
        throw UsbExceptionFactory.createPlatformException("Unable to set configuration", result);
      }
      this.activeConfigurationNumber = number;
    }
  }

  /**
   * Claim the specified interface.
   *
   * @param number The number of the interface to claim.
   * @param force  If possible, try to force the claim.
   * @throws UsbException When the interface cannot not be claimed.
   */
  public final void claimInterface(final byte number, final boolean force) throws UsbException {
    if (this.claimedInterfaceNumbers.contains(number)) {
      throw new UsbClaimException("An interface is already claimed");
    }
    final DeviceHandle deviceHandle = open();
    /**
     * Detach existing driver from the device if requested and libusb supports
     * it.
     */
    if (force) {
      int result = LibUsb.kernelDriverActive(deviceHandle, number);
      if (result == LibUsb.ERROR_NO_DEVICE) {
        throw new UsbDisconnectedException();
      }
      if (result == 1) {
        result = LibUsb.detachKernelDriver(deviceHandle, number);
        if (result < 0) {
          throw UsbExceptionFactory.createPlatformException("Unable to detach kernel driver", result);
        }
        this.detachedKernelDriver = true;
      }
    }

    final int result = LibUsb.claimInterface(deviceHandle, number & 0xff);
    if (result < 0) {
      throw UsbExceptionFactory.createPlatformException("Unable to claim interface", result);
    }
    this.claimedInterfaceNumbers.add(number);
  }

  /**
   * Release a claimed interface.
   *
   * @param number The number of the interface to release.
   * @throws UsbException When the interface claim cannot be released or the
   *                      interface is not claimed.
   */
  public final void releaseInterface(final byte number) throws UsbException {
    if (this.claimedInterfaceNumbers.isEmpty()) {
      throw new UsbClaimException("No interface is claimed");
    }
    if (!this.claimedInterfaceNumbers.contains(number)) {
      throw new UsbClaimException("Interface not claimed");
    }

    final DeviceHandle deviceHandle = open();
    int result = LibUsb.releaseInterface(deviceHandle, number & 0xff);
    if (result < 0) {
      throw UsbExceptionFactory.createPlatformException("Unable to release interface", result);
    }

    if (this.detachedKernelDriver) {
      result = LibUsb.attachKernelDriver(deviceHandle, number & 0xff);
      if (result < 0) {
        throw UsbExceptionFactory.createPlatformException("Uanble to re-attach kernel driver", result);
      }
    }

    this.claimedInterfaceNumbers.remove(number);
  }

  /**
   * @inherit.
   */
  @Override
  public final boolean isInterfaceClaimed(final byte number) {
    return this.claimedInterfaceNumbers.contains(number);
  }

  /**
   * Get the active IUsbConfiguration.
   * <p>
   * If this device is Not Configured, this returns null.
   *
   * @return The active IUsbConfiguration, or null.
   */
  @Override
  public final IUsbConfiguration getActiveUsbConfiguration() {
    return getUsbConfiguration(getActiveUsbConfigurationNumber());
  }

  /**
   * If this IUsbDevice is configured.
   * <p>
   * This returns true if the device is in the configured state as shown in the
   * USB 1.1 specification table 9.1.
   *
   * @return If this is in the Configured state.
   */
  @Override
  public final boolean isConfigured() {
    return getActiveUsbConfigurationNumber() != 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final IUsbDeviceDescriptor getUsbDeviceDescriptor() {
    return this.deviceId.getDeviceDescriptor();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final IUsbStringDescriptor getUsbStringDescriptor(final byte index) throws UsbException {
    isConnected();
    final short[] languages = getLanguages();
    final DeviceHandle deviceHandle = open();
    final short langId = languages.length == 0 ? 0 : languages[0];
    final ByteBuffer data = ByteBuffer.allocateDirect(256);
    final int result = LibUsb.getStringDescriptor(deviceHandle, index, langId, data);
    if (result < 0) {
      throw UsbExceptionFactory.createPlatformException("Unable to get string descriptor " + index + " from device " + this, result);
    }
    return new UsbStringDescriptor(data);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getString(final byte index) throws UsbException, UnsupportedEncodingException {
    return getUsbStringDescriptor(index).getString();
  }

  /**
   * Returns the languages the specified device supports.
   *
   * @return Array with supported language codes. Never null. May be empty.
   * @throws UsbException When string descriptor languages could not be read.
   */
  protected short[] getLanguages() throws UsbException {
    final DeviceHandle deviceHandle = open();
    final ByteBuffer buffer = ByteBuffer.allocateDirect(256);
    final int result = LibUsb.getDescriptor(deviceHandle, EDescriptorType.STRING, (byte) 0, buffer);
    if (result < 0) {
      throw UsbExceptionFactory.createPlatformException("Unable to get string descriptor languages", result);
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

  /**
   * {@inheritDoc}
   */
  @Override
  public final void syncSubmit(final IUsbControlIrp irp) throws UsbException {
    if (irp == null) {
      throw new IllegalArgumentException("irp must not be null");
    }
    isConnected();
    this.controlIrpQueue.add(irp);
    irp.waitUntilComplete();
    if (irp.isUsbException()) {
      throw irp.getUsbException();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void asyncSubmit(final IUsbControlIrp irp) {
    if (irp == null) {
      throw new IllegalArgumentException("irp must not be null");
    }
    isConnected();
    this.controlIrpQueue.add(irp);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void syncSubmit(final List<IUsbControlIrp> list) throws UsbException {
    if (list == null) {
      throw new IllegalArgumentException("list must not be null");
    }
    isConnected();
    for (final IUsbControlIrp item : list) {
      syncSubmit(item);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void asyncSubmit(final List<IUsbControlIrp> list) {
    if (list == null) {
      throw new IllegalArgumentException("list must not be null");
    }
    isConnected();
    for (final IUsbControlIrp item : list) {
      asyncSubmit(item);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final IUsbControlIrp createUsbControlIrp(final byte bmRequestType, final byte bRequest, final short wValue, final short wIndex) {
    return new UsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
  }

  /**
   * {@inheritDoc}
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
   * {@inheritDoc}
   */
  @Override
  public final void addUsbDeviceListener(final IUsbDeviceListener listener) {
    this.listeners.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void removeUsbDeviceListener(final IUsbDeviceListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Hash code is based upon the DeviceId.
   *
   * @return object hash code
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.deviceId);
    return hash;
  }

  /**
   * Equals is based upon the DeviceId.
   *
   * @param obj the other object to test
   * @return TRUE if the other object has the same DeviceId.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(this.deviceId, ((IUsbDevice) obj).getDeviceId());
  }

  /**
   * Sort on device ID.
   *
   * @param o the other instance
   * @return the sort order
   */
  @Override
  public int compareTo(IUsbDevice o) {
    return this.deviceId == null ? +1 : this.deviceId.compareTo(o.getDeviceId());
  }

  @Override
  public final String toString() {
    return this.deviceId.toString();
  }

}
