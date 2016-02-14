/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb;

import java.util.*;
import javax.usb.descriptor.UsbDeviceDescriptor;
import javax.usb.enumerated.EUSBClassCode;
import javax.usb.exception.*;
import org.usb4java.*;

/**
 * Manages the USB devices.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Key Bridge (keybridge.ch)
 */
public final class UsbDeviceManager {

  /**
   * The virtual USB root hub.
   */
  private final UsbRootHub usbRootHub;

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
  private final Map<UsbDeviceId, AUsbDevice> devices = Collections.synchronizedMap(new HashMap<UsbDeviceId, AUsbDevice>());

  /**
   * Constructs a new device manager.
   *
   * @param usbRootHub   The root hub. Must not be null.
   * @param scanInterval The scan interval in milliseconds.
   * @throws UsbException When USB initialization fails.
   */
  public UsbDeviceManager(final UsbRootHub usbRootHub, final int scanInterval) throws UsbException {
    if (usbRootHub == null) {
      throw new IllegalArgumentException("rootHub must be set");
    }
    this.scanInterval = scanInterval;
    this.usbRootHub = usbRootHub;
    this.context = new Context();
    final int result = LibUsb.init(this.context);
    if (result != 0) {
      throw ExceptionUtility.createPlatformException("Unable to initialize libusb", result);
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
   * Creates a DeviceId from the specified (JNI) Device instance.
   *
   * @param device The libusb device. Must not be null.
   * @return The device id.
   * @throws UsbPlatformException When device descriptor could not be read from
   *                              the specified device.
   */
  private UsbDeviceId createDeviceId(final Device device) throws UsbPlatformException {
    if (device == null) {
      throw new IllegalArgumentException("device must be set");
    }
    final int busNumber = LibUsb.getBusNumber(device);
    final int addressNumber = LibUsb.getDeviceAddress(device);
    final int portNumber = LibUsb.getPortNumber(device);
    final DeviceDescriptor deviceDescriptor = new DeviceDescriptor();
    final int result = LibUsb.getDeviceDescriptor(device, deviceDescriptor);
    if (result < 0) {
      throw ExceptionUtility.createPlatformException("Unable to get device descriptor for device " + addressNumber + " at bus " + busNumber, result);
    }
    return new UsbDeviceId(busNumber, addressNumber, portNumber, new UsbDeviceDescriptor(deviceDescriptor));
  }

  /**
   * Scans the specified ports for removed devices.
   *
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
   *
   * @param ports The ports to scan for new devices.
   * @param hubId The hub ID. Null if scanned hub is the root hub.
   */
  private void scanNewDevices(final IUsbPorts ports, final UsbDeviceId hubId) {
    for (AUsbDevice device : this.devices.values()) {
      /**
       * Get parent ID from device and reset it to null if we don't know this
       * parent device (This happens on Windows because some devices/hubs can't
       * be fully enumerated.)
       */
      UsbDeviceId parentId = device.getParentId();
      if (!this.devices.containsKey(parentId)) {
        parentId = null;
      }

      if (UsbDeviceId.equals(parentId, hubId)) {
        if (!ports.isUsbDeviceAttached(device)) {
          /**
           * Connect new devices to the ports of the current hub.
           */
          ports.connectUsbDevice(device);
        }
        /**
         * Scan for removed child devices if current device is a hub
         */
        if (device.isUsbHub()) {
          scanNewDevices((IUsbPorts) device, device.getId());
        }
      }
    }

  }

  /**
   * Scans the specified hub for changes.
   *
   * @param usbHub The hub to scan.
   */
  public void scan(final IUsbHub usbHub) {
    try {
      updateDeviceList();
    } catch (UsbException e) {
      throw new UsbScanException("Unable to scan for USB devices: " + e, e);
    }

    if (usbHub.isRootUsbHub()) {
      final UsbRootHub rootHubTemp = (UsbRootHub) usbHub;
      scanRemovedDevices(rootHubTemp);
      scanNewDevices(rootHubTemp, null);
    } else {
      final UsbHub nonRootHub = (UsbHub) usbHub;
      scanRemovedDevices(nonRootHub);
      scanNewDevices(nonRootHub, nonRootHub.getId());
    }
  }

  /**
   * Updates the device list by adding newly connected devices to it and by
   * removing no longer connected devices.
   *
   * @throws UsbPlatformException When libusb reported an error which we can't
   *                              ignore during scan.
   */
  private void updateDeviceList() throws UsbPlatformException {
    final List<UsbDeviceId> current = new ArrayList<>();

    // Get device list from libusb and abort if it failed
    final DeviceList deviceList = new DeviceList();
    final int result = LibUsb.getDeviceList(this.context, deviceList);
    if (result < 0) {
      throw ExceptionUtility.createPlatformException("Unable to get USB device list", result);
    }

    try {
      // Iterate over all currently connected devices
      for (final Device libUsbDevice : deviceList) {
        try {
          final UsbDeviceId deviceId = createDeviceId(libUsbDevice);

          AUsbDevice device = this.devices.get(deviceId);
          if (device == null) {
            final Device parent = LibUsb.getParent(libUsbDevice);
            final UsbDeviceId parentId = parent == null ? null : createDeviceId(parent);
            final int speed = LibUsb.getDeviceSpeed(libUsbDevice);

            if (EUSBClassCode.HUB.equals(deviceId.getDeviceDescriptor().bDeviceClass())) {
              device = new UsbHub(this, deviceId, parentId, speed, libUsbDevice);
            } else {
              device = new UsbDevice(this, deviceId, parentId, speed, libUsbDevice);
            }
            // Add new device to global device list.
            this.devices.put(deviceId, device);
          }
          // Remember current device as "current"
          current.add(deviceId);
        } catch (UsbPlatformException e) {
          // Devices which can't be enumerated are ignored
        }
      }
      /**
       * Retains only the elements in this set that are contained in the
       * specified collection (optional operation). In other words, removes from
       * this set all of its elements that are not contained in the specified
       * collection.
       */
      this.devices.keySet().retainAll(current);
    } finally {
      LibUsb.freeDeviceList(deviceList, true);
    }
  }

  /**
   * Scans the USB busses for new or removed devices.
   */
  public synchronized void scan() {
    scan(this.usbRootHub);
    this.scanned = true;
  }

  /**
   * Returns the libusb device for the specified id. The device must be freed
   * after use.
   *
   * @param id The id of the device to return. Must not be null.
   * @return device The libusb device. Never null.
   * @throws UsbDeviceNotFoundException When the device was not found.
   * @throws UsbPlatformException       When libusb reported an error while
   *                                    enumerating USB devices.
   */
  public Device getLibUsbDevice(final UsbDeviceId id) throws UsbPlatformException {
    if (id == null) {
      throw new IllegalArgumentException("id must be set");
    }

    final DeviceList deviceList = new DeviceList();
    final int result = LibUsb.getDeviceList(this.context, deviceList);
    if (result < 0) {
      throw ExceptionUtility.createPlatformException("Unable to get USB device list", result);
    }
    try {
      for (Device device : deviceList) {
        try {
          if (id.equals(createDeviceId(device))) {
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

    throw new UsbDeviceNotFoundException(id);
  }

  /**
   * Releases the specified device.
   *
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
