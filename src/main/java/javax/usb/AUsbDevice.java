/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import javax.usb.descriptor.UsbStringDescriptor;
import javax.usb.enumerated.EDescriptorType;
import javax.usb.enumerated.EDevicePortSpeed;
import javax.usb.event.IUsbDeviceListener;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.exception.*;
import org.usb4java.ConfigDescriptor;
import org.usb4java.Device;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;

/**
 * A general, abstract USB Device implementation.
 * <p>
 * This implements all required functionality of the IUsbDevice interface plus
 * additional functionality required to interface with the native LIBUSB
 * library.
 * <p>
 * This abstract class is extended by: <ul>
 * <li>UsbHub extends AUsbDevice implements IUsbUsbHub, IUsbPorts</li>
 * <li>UsbDevice extends AUsbDevice</li>
 * </ul>
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
@SuppressWarnings("ProtectedField")
public abstract class AUsbDevice implements IUsbDevice {

  /**
   * The USB device deviceManager.
   */
  protected final UsbDeviceManager deviceManager;
  /**
   * The device deviceId.
   */
  protected final UsbDeviceId deviceId;
  /**
   * The parent deviceId. Null if no parent exists.
   */
  protected final UsbDeviceId parentId;
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
   * Constructs a new device.
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
   * @throws UsbPlatformException When device configuration could not be read.
   */
  public AUsbDevice(final UsbDeviceManager deviceManager,
                    final UsbDeviceId deviceId,
                    final UsbDeviceId parentId,
                    final int speed,
                    final Device device) throws UsbPlatformException {
    if (deviceManager == null) {
      throw new IllegalArgumentException("DeviceManager must be set");
    }
    if (deviceId == null) {
      throw new IllegalArgumentException("DeviceId must be set");
    }
    this.deviceManager = deviceManager;
    this.deviceId = deviceId;
    this.parentId = parentId;
    this.speed = speed;
    /**
     * Read the device configurations
     */
    final int numConfigurations = deviceId.getDeviceDescriptor().bNumConfigurations() & 0xff;
    final List<IUsbConfiguration> configurationTemp = new ArrayList<>(numConfigurations);
    for (int i = 0; i < numConfigurations; i += 1) {
      final ConfigDescriptor configDescriptor = new ConfigDescriptor();
      final int result = LibUsb.getConfigDescriptor(device, (byte) i, configDescriptor);
      if (result < 0) {
        throw ExceptionUtility.createPlatformException("Unable to get configuation " + i + " for device " + deviceId, result);
      }
      try {
        final UsbConfiguration config = new UsbConfiguration(this, configDescriptor);
        configurationTemp.add(config);
        this.configMapping.put(configDescriptor.bConfigurationValue(), config);
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
     * it may return INVALID_PARAM in this case because of a bug in libusb.
     */
    if (result == LibUsb.ERROR_NOT_FOUND || result == LibUsb.ERROR_INVALID_PARAM) {
      this.activeConfigurationNumber = 0;
    } else if (result < 0) {
      throw ExceptionUtility.createPlatformException("Unable to read active config descriptor from device " + deviceId, result);
    } else {
      this.activeConfigurationNumber = configDescriptor.bConfigurationValue();
      LibUsb.freeConfigDescriptor(configDescriptor);
    }
  }

  /**
   * Returns the device deviceId.
   *
   * @return The device deviceId.
   */
  public final UsbDeviceId getId() {
    return this.deviceId;
  }

  /**
   * Returns the parent device deviceId.
   *
   * @return The parent device deviceId or null of there is no parent.
   */
  public final UsbDeviceId getParentId() {
    return this.parentId;
  }

  /**
   * Ensures the device is connected.
   *
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
          throw ExceptionUtility.createPlatformException("Can't open device " + this.deviceId, result);
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

  @Override
  public final IUsbPort getParentUsbPort() {
    checkConnected();
    return this.port;
  }

  /**
   * Sets the parent USB port. If port is unset then a usbDeviceDetached event
   * is send.
   *
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
      final UsbHub hub = (UsbHub) this;
      for (final IUsbDevice device : hub.getAttachedUsbDevices()) {
        hub.disconnectUsbDevice(device);
      }
    }

    this.port = port;

    final UsbServices services = UsbServices.getInstance();

    if (port == null) {
      this.listeners.usbDeviceDetached(new UsbDeviceEvent(this));
      services.usbDeviceDetached(this);
    } else {
      services.usbDeviceAttached(this);
    }
  }

  /**
   * Get the manufacturer String.
   * <p>
   * This is a convienence method, which uses
   * {@link #getString(byte) getString}.
   *
   * @return The manufacturer String, or null.
   * @throws UsbException                 If there was an error getting the
   *                                      IUsbStringDescriptor.
   * @exception UnsupportedEncodingException If the string encoding is not
   *                                         supported.
   * @exception UsbDisconnectedException     If this device has been
   *                                         disconnected.
   */
  @Override
  public final String getManufacturerString() throws UsbException, UnsupportedEncodingException {
    checkConnected();
    final byte index = getUsbDeviceDescriptor().iManufacturer();
    if (index == 0) {
      return null;
    }
    return getString(index);
  }

  /**
   * Get the serial number String.
   * <p>
   * This is a convienence method, which uses
   * {@link #getString(byte) getString}.
   *
   * @return The serial number String, or null.
   * @throws UsbException                 If there was an error getting the
   *                                      IUsbStringDescriptor.
   * @exception UnsupportedEncodingException If the string encoding is not
   *                                         supported.
   * @exception UsbDisconnectedException     If this device has been
   *                                         disconnected.
   */
  @Override
  public final String getSerialNumberString() throws UsbException, UnsupportedEncodingException {
    checkConnected();
    final byte index = getUsbDeviceDescriptor().iSerialNumber();
    if (index == 0) {
      return null;
    }
    return getString(index);
  }

  /**
   * Get the product String.
   * <p>
   * This is a convienence method, which uses
   * {@link #getString(byte) getString}.
   *
   * @return The product String, or null.
   * @throws UsbException                 If there was an error getting the
   *                                      IUsbStringDescriptor.
   * @exception UnsupportedEncodingException If the string encoding is not
   *                                         supported.
   * @exception UsbDisconnectedException     If this device has been
   *                                         disconnected.
   */
  @Override
  public final String getProductString() throws UsbException, UnsupportedEncodingException {
    checkConnected();
    final byte index = getUsbDeviceDescriptor().iProduct();
    if (index == 0) {
      return null;
    }
    return getString(index);
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
   *
   * @return The speed of this device.
   */
  @Override
  public final EDevicePortSpeed getSpeed() {
    return EDevicePortSpeed.speedSupported((short) this.speed);
  }

  /**
   * Get all IUsbConfigurations for this device.
   * <p>
   * The List is unmodifiable.
   *
   * @return All IUsbConfigurations for this device.
   */
  @Override
  public final List<IUsbConfiguration> getUsbConfigurations() {
    return this.configurations;
  }

  /**
   * Get the specified IUsbConfiguration.
   * <p>
   * If the specified IUsbConfiguration does not exist, null is returned. Config
   * number 0 is reserved for the Not Configured state (see the USB 1.1
   * specification section 9.4.2). Obviously, no IUsbConfiguration exists for
   * the Not Configured state.
   *
   * @param number the bytecode address of the configuration value
   * @return The specified IUsbConfiguration, or null.
   */
  @Override
  public final IUsbConfiguration getUsbConfiguration(final byte number) {
    return this.configMapping.get(number);
  }

  /**
   * If this IUsbDevice contains the specified IUsbConfiguration.
   * <p>
   * This will return false for zero (the Not Configured state).
   *
   * @param number the bytecode address of the configuration value
   * @return If the specified IUsbConfiguration is contained in this IUsbDevice.
   */
  @Override
  public final boolean containsUsbConfiguration(final byte number) {
    return this.configMapping.containsKey(number);
  }

  /**
   * Get the number of the active IUsbConfiguration.
   * <p>
   * If the device is in a Not Configured state, this will return zero.
   *
   * @return The active config number.
   */
  @Override
  public final byte getActiveUsbConfigurationNumber() {
    return this.activeConfigurationNumber;
  }

  /**
   * Sets the active USB configuration.
   *
   * @param number The number of the USB configuration to activate.
   * @throws UsbException When configuration could not be activated.
   */
  final void setActiveUsbConfigurationNumber(final byte number) throws UsbException {
    if (number != this.activeConfigurationNumber) {
      if (!this.claimedInterfaceNumbers.isEmpty()) {
        throw new UsbException("Can't change configuration while an "
                               + "interface is still claimed");
      }

      final int result = LibUsb.setConfiguration(open(), number & 0xff);
      if (result < 0) {
        throw ExceptionUtility.createPlatformException(
                "Unable to set configuration", result);
      }
      this.activeConfigurationNumber = number;
    }
  }

  /**
   * Claims the specified interface.
   *
   * @param number The number of the interface to claim.
   * @param force  If claim should be forces if possible.
   * @throws UsbException When interface could not be claimed.
   */
  final void claimInterface(final byte number, final boolean force) throws UsbException {
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
          throw ExceptionUtility.createPlatformException(
                  "Unable to detach kernel driver", result);
        }
        this.detachedKernelDriver = true;
      }
    }

    final int result = LibUsb.claimInterface(deviceHandle, number & 0xff);
    if (result < 0) {
      throw ExceptionUtility.createPlatformException(
              "Unable to claim interface", result);
    }
    this.claimedInterfaceNumbers.add(number);
  }

  /**
   * Releases a claimed interface.
   *
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
      throw ExceptionUtility.createPlatformException("Unable to release interface", result);
    }

    if (this.detachedKernelDriver) {
      result = LibUsb.attachKernelDriver(deviceHandle, number & 0xff);
      if (result < 0) {
        throw ExceptionUtility.createPlatformException("Uanble to re-attach kernel driver", result);
      }
    }

    this.claimedInterfaceNumbers.remove(number);
  }

  /**
   * Checks if the specified interface is claimed.
   *
   * @param number The number of the interface to check.
   * @return True if interface is claimed, false if not.
   */
  final boolean isInterfaceClaimed(final byte number) {
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
   * Get the device descriptor.
   * <p>
   * The descriptor may be cached.
   *
   * @return The device descriptor.
   */
  @Override
  public final IUsbDeviceDescriptor getUsbDeviceDescriptor() {
    return this.deviceId.getDeviceDescriptor();
  }

  /**
   * Get the specified string descriptor.
   * <p>
   * This is a convienence method. The IUsbStringDescriptor may be cached. If
   * the device does not support strings or does not define the specified string
   * descriptor, this returns null.
   *
   * @param index The index of the string descriptor to get.
   * @return The specified string descriptor.
   * @exception UsbException             If an error occurred while getting the
   *                                     string descriptor.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  @Override
  public final IUsbStringDescriptor getUsbStringDescriptor(final byte index) throws UsbException {
    checkConnected();
    final short[] languages = getLanguages();
    final DeviceHandle deviceHandle = open();
    final short langId = languages.length == 0 ? 0 : languages[0];
    final ByteBuffer data = ByteBuffer.allocateDirect(256);
    final int result = LibUsb.getStringDescriptor(deviceHandle, index, langId, data);
    if (result < 0) {
      throw ExceptionUtility.createPlatformException("Unable to get string descriptor " + index + " from device " + this, result);
    }
    return new UsbStringDescriptor(data);
  }

  /**
   * Get the String from the specified string descriptor.
   * <p>
   * This is a convienence method, which uses
   * {@link #getUsbStringDescriptor(byte) getIUsbStringDescriptor()}.
   * {@link javax.usb.UsbStringDescriptor#getString() getString()}.
   *
   * @param index The index of the string to get.
   * @return The specified String.
   * @exception UsbException                 If an error occurred while getting
   *                                         the String.
   * @exception UnsupportedEncodingException If the string encoding is not
   *                                         supported.
   * @exception UsbDisconnectedException     If this device has been
   *                                         disconnected.
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
      throw ExceptionUtility.createPlatformException("Unable to get string descriptor languages", result);
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
   * Submit a IUsbControlIrp synchronously to the Default Control Pipe.
   *
   * @param irp The IUsbControlIrp.
   * @exception UsbException             If an error occurs.
   * @throws IllegalArgumentException If the IUsbControlIrp is not valdeviceId.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  @Override
  public final void syncSubmit(final IUsbControlIrp irp) throws UsbException {
    if (irp == null) {
      throw new IllegalArgumentException("irp must not be null");
    }
    checkConnected();
    this.controlIrpQueue.add(irp);
    irp.waitUntilComplete();
    if (irp.isUsbException()) {
      throw irp.getUsbException();
    }
  }

  /**
   * Submit a IUsbControlIrp asynchronously to the Default Control Pipe.
   *
   * @param irp The IUsbControlIrp.
   * @throws IllegalArgumentException If the IUsbControlIrp is not valdeviceId.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  @Override
  public final void asyncSubmit(final IUsbControlIrp irp) {
    if (irp == null) {
      throw new IllegalArgumentException("irp must not be null");
    }
    checkConnected();
    this.controlIrpQueue.add(irp);
  }

  /**
   * Submit a List of IUsbControlIrps synchronously to the Default Control Pipe.
   * <p>
   * All IUsbControlIrps are guaranteed to be atomically (with respect to other
   * clients of this API) submitted to the Default Control Pipe. Atomicity on a
   * native level is implementation-dependent.
   *
   * @param list The List of IUsbControlIrps.
   * @exception UsbException             If an error occurs.
   * @throws IllegalArgumentException If the List contains non-IUsbControlIrp
   *                                  objects or those UsbIrp(s) are
   *                                  invaldeviceId.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
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

  /**
   * Submit a List of IUsbControlIrps asynchronously to the Default Control
   * Pipe.
   * <p>
   * All IUsbControlIrps are guaranteed to be atomically (with respect to other
   * clients of this API) submitted to the Default Control Pipe. Atomicity on a
   * native level is implementation-dependent.
   *
   * @param list The List of IUsbControlIrps.
   * @exception UsbException             If an error occurs.
   * @throws IllegalArgumentException If the List contains non-IUsbControlIrp
   *                                  objects or those UsbIrp(s) are
   *                                  invaldeviceId.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
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
   *
   * @param bmRequestType The bmRequestType.
   * @param bRequest      The bRequest.
   * @param wValue        The wValue.
   * @param wIndex        The wIndex.
   * @return A IUsbControlIrp ready for use.
   */
  @Override
  public final IUsbControlIrp createUsbControlIrp(final byte bmRequestType, final byte bRequest, final short wValue, final short wIndex) {
    return new UsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
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
   * The {@code wLength} field must be automatically calculated by the
   * implementation. The {@code timeout} timeout (in millseconds) value that
   * this function should wait before giving up due to no response being
   * received should be set to a default value.
   *
   * @param bmRequestType The bmRequestType field for the setup packet
   * @param bRequest      The bRequest field for the setup packet
   * @param wValue        The wValue field for the setup packet
   * @param wIndex        The wIndex field for the setup packet
   * @param data          a suitably-sized data buffer for either input or
   *                      output (depending on direction bits within
   *                      bmRequestType)
   * @return A IUsbControlIrp ready for use.
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
   * Add a IIUsbDeviceListener to this IUsbDevice.
   *
   * @param listener The IIUsbDeviceListener to add.
   */
  @Override
  public final void addUsbDeviceListener(final IUsbDeviceListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Remove a IIUsbDeviceListener from this IUsbDevice.
   *
   * @param listener The listener to remove.
   */
  @Override
  public final void removeUsbDeviceListener(final IUsbDeviceListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Hash code is based upon the DeviceId.
   *
   * @return
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
    return Objects.equals(this.deviceId, ((AUsbDevice) obj).getId());
  }

  @Override
  public final String toString() {
    return this.deviceId.toString();
  }

}
