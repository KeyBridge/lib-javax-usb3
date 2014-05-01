/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import org.usb4java.javax.exception.ExceptionUtils;
import org.usb4java.javax.exception.ScanException;
import org.usb4java.javax.exception.DeviceNotFoundException;
import org.usb4java.libusbutil.DeviceList;
import java.util.*;
import javax.usb.IUsbDevice;
import javax.usb.IUsbHub;
import javax.usb.exception.UsbException;
import javax.usb.exception.UsbPlatformException;
import org.usb4java.*;
import org.usb4java.javax.descriptors.SimpleUsbDeviceDescriptor;

/**
 * Manages the USB devices.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class DeviceManager {

  /**
   * The virtual USB root hub.
   */
  private final UsbRootHub rootHub;

  /**
   * The libusb context.
   */
  private final Context context;

  /**
   * If scanner already scanned for devices.
   */
  private boolean scanned = false;

  /**
   * The scan interval in milliseconds.
   */
  private final int scanInterval;

  /**
   * The currently connected devices.
   */
  private final Map<DeviceId, AUsbDevice> devices = Collections.synchronizedMap(new HashMap<DeviceId, AUsbDevice>());

  /**
   * Constructs a new device manager.
   * <p>
   * @param rootHub      The root hub. Must not be null.
   * @param scanInterval The scan interval in milliseconds.
   * @throws UsbException When USB initialization fails.
   */
  public DeviceManager(final UsbRootHub rootHub, final int scanInterval) throws UsbException {
    if (rootHub == null) {
      throw new IllegalArgumentException("rootHub must be set");
    }
    this.scanInterval = scanInterval;
    this.rootHub = rootHub;
    this.context = new Context();
    final int result = LibUsb.init(this.context);
    if (result != 0) {
      throw ExceptionUtils.createPlatformException("Unable to initialize libusb", result);
    }
  }

  /**
   * Dispose the USB device manager. This exits the USB context opened by the
   * constructor.
   */
  public void dispose() {
    LibUsb.exit(this.context);
  }

  /**
   * Creates a device ID from the specified device.
   * <p>
   * @param device The libusb device. Must not be null.
   * @return The device id.
   * @throws UsbPlatformException When device descriptor could not be read from
   *                              the specified device.
   */
  private DeviceId createId(final Device device) throws UsbPlatformException {
    if (device == null) {
      throw new IllegalArgumentException("device must be set");
    }
    final int busNumber = LibUsb.getBusNumber(device);
    final int addressNumber = LibUsb.getDeviceAddress(device);
    final int portNumber = LibUsb.getPortNumber(device);
    final DeviceDescriptor deviceDescriptor = new DeviceDescriptor();
    final int result = LibUsb.getDeviceDescriptor(device, deviceDescriptor);
    if (result < 0) {
      throw ExceptionUtils.createPlatformException("Unable to get device descriptor for device " + addressNumber + " at bus " + busNumber, result);
    }
    return new DeviceId(busNumber, addressNumber, portNumber, new SimpleUsbDeviceDescriptor(deviceDescriptor));
  }

  /**
   * Scans the specified ports for removed devices.
   * <p>
   * @param ports The ports to scan for removals.
   */
  private void scanRemovedDevices(final IUsbPorts ports) {
    for (IUsbDevice device : ports.getAttachedUsbDevices()) {
      // Scan for removed child devices if current device is a hub
      if (device.isUsbHub()) {
        scanRemovedDevices((IUsbPorts) device);
      }

      // If device is no longer present then remove it
      if (!this.devices.containsKey(((AUsbDevice) device).getId())) {
        ports.disconnectUsbDevice(device);
      }
    }
  }

  /**
   * Scans the specified ports for new devices.
   * <p>
   * @param ports The ports to scan for new devices.
   * @param hubId The hub ID. Null if scanned hub is the root hub.
   */
  private void scanNewDevices(final IUsbPorts ports, final DeviceId hubId) {
    for (AUsbDevice device : this.devices.values()) {
      // Get parent ID from device and reset it to null if we don't
      // know this parent device (This happens on Windows because some
      // devices/hubs can't be fully enumerated.)
      DeviceId parentId = device.getParentId();
      if (!this.devices.containsKey(parentId)) {
        parentId = null;
      }

      if (DeviceId.equals(parentId, hubId)) {
        if (!ports.isUsbDeviceAttached(device)) {
          // Connect new devices to the ports of the current hub.
          ports.connectUsbDevice(device);
        }

        // Scan for removed child devices if current device is a hub
        if (device.isUsbHub()) {
          scanNewDevices((IUsbPorts) device, device.getId());
        }
      }
    }

  }

  /**
   * Scans the specified hub for changes.
   * <p>
   * @param hub The hub to scan.
   */
  public void scan(final IUsbHub hub) {
    try {
      updateDeviceList();
    } catch (UsbException e) {
      throw new ScanException("Unable to scan for USB devices: " + e, e);
    }

    if (hub.isRootUsbHub()) {
      final UsbRootHub rootHubTemp = (UsbRootHub) hub;
      scanRemovedDevices(rootHubTemp);
      scanNewDevices(rootHubTemp, null);
    } else {
      final UsbHub nonRootHub = (UsbHub) hub;
      scanRemovedDevices(nonRootHub);
      scanNewDevices(nonRootHub, nonRootHub.getId());
    }
  }

  /**
   * Updates the device list by adding newly connected devices to it and by
   * removing no longer connected devices.
   * <p>
   * @throws UsbPlatformException When libusb reported an error which we can't
   *                              ignore during scan.
   */
  private void updateDeviceList() throws UsbPlatformException {
    final List<DeviceId> current = new ArrayList<>();

    // Get device list from libusb and abort if it failed
    final DeviceList deviceList = new DeviceList();
    final int result = LibUsb.getDeviceList(this.context, deviceList);
    if (result < 0) {
      throw ExceptionUtils.createPlatformException(
        "Unable to get USB device list", result);
    }

    try {
      // Iterate over all currently connected devices
      for (final Device libUsbDevice : deviceList) {
        try {
          final DeviceId id = createId(libUsbDevice);

          AUsbDevice device = this.devices.get(id);
          if (device == null) {
            final Device parent = LibUsb.getParent(libUsbDevice);
            final DeviceId parentId = parent == null ? null : createId(parent);
            final int speed = LibUsb.getDeviceSpeed(libUsbDevice);
            final boolean isHub = id.getDeviceDescriptor().bDeviceClass() == LibUsb.CLASS_HUB;
            if (isHub) {
              device = new UsbHub(this, id, parentId, speed, libUsbDevice);
            } else {
              device = new UsbDevice(this, id, parentId, speed, libUsbDevice);
            }

            // Add new device to global device list.
            this.devices.put(id, device);
          }

          // Remember current device as "current"
          current.add(id);
        } catch (UsbPlatformException e) {
          // Devices which can't be enumerated are ignored
        }
      }

      this.devices.keySet().retainAll(current);
    } finally {
      LibUsb.freeDeviceList(deviceList, true);
    }
  }

  /**
   * Scans the USB busses for new or removed devices.
   */
  public synchronized void scan() {
    scan(this.rootHub);
    this.scanned = true;
  }

  /**
   * Returns the libusb device for the specified id. The device must be freed
   * after use.
   * <p>
   * @param id The id of the device to return. Must not be null.
   * @return device The libusb device. Never null.
   * @throws DeviceNotFoundException When the device was not found.
   * @throws UsbPlatformException    When libusb reported an error while
   *                                 enumerating USB devices.
   */
  public Device getLibUsbDevice(final DeviceId id) throws UsbPlatformException {
    if (id == null) {
      throw new IllegalArgumentException("id must be set");
    }

    final DeviceList deviceList = new DeviceList();
    final int result = LibUsb.getDeviceList(this.context, deviceList);
    if (result < 0) {
      throw ExceptionUtils.createPlatformException("Unable to get USB device list", result);
    }
    try {
      for (Device device : deviceList) {
        try {
          if (id.equals(createId(device))) {
            LibUsb.refDevice(device);
            return device;
          }
        } catch (UsbPlatformException e) {
          // Devices for which no ID can be created are ignored
        }
      }
    } finally {
      LibUsb.freeDeviceList(deviceList, true);
    }

    throw new DeviceNotFoundException(id);
  }

  /**
   * Releases the specified device.
   * <p>
   * @param device The device to release. Must not be null.
   */
  public void releaseDevice(final Device device) {
    if (device == null) {
      throw new IllegalArgumentException("device must be set");
    }
    LibUsb.unrefDevice(device);
  }

  /**
   * Starts scanning in the background.
   */
  public void start() {
    // Do not start the scan thread when interval is set to 0
    final int scanIntervalTemp = this.scanInterval;
    if (scanIntervalTemp == 0) {
      return;
    }

    final Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            Thread.sleep(scanIntervalTemp);
          } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
          }
          scan();
        }
      }
    });
    thread.setDaemon(true);
    thread.setName("usb4java Device Scanner");
    thread.start();
  }

  /**
   * Scans for devices but only if this was not already done.
   */
  public void firstScan() {
    if (!this.scanned) {
      scan();
    }
  }
}
