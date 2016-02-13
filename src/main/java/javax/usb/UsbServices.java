/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package javax.usb;

import javax.usb.event.IUsbServicesListener;
import javax.usb.event.UsbServicesEvent;
import javax.usb.exception.UsbException;
import javax.usb.utility.JNINativeLibraryLoader;

/**
 * usb4java implementation of JSR-80 IUsbServices.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class UsbServices implements IUsbServices {

  /**
   * The implementation description.
   */
  private static final String IMP_DESCRIPTION = "javax.usb";
  /**
   * The implementation version. This is the Java source code version.
   */
  private static final String IMP_VERSION = "1.3.0";
  /**
   * The API version. This is the usb4java JNI source code version.
   */
  private static final String API_VERSION = "1.0.2";

  /**
   * The USB services listeners.
   */
  private final UsbServicesListener listeners = new UsbServicesListener();

  /**
   * The USB device scanner.
   */
  private final UsbDeviceManager deviceManager;

  /**
   * If devices should be scanned by hierarchy.
   */
  private final UsbServiceInstanceConfiguration config;

  /**
   * The virtual USB root hub.
   */
  private final UsbRootHub rootUsbHub;

  /**
   * Constructor.
   *
   * @throws UsbException     When properties could not be loaded.
   * @throws RuntimeException When the native library corresponding to the host
   *                          operating system fails to load
   */
  public UsbServices() throws UsbException {
    /**
     * Load configurations from the "javax.usb.properties" file, then load the
     * native libusb wrapper (usb4java JNI) library.
     */
    this.config = new UsbServiceInstanceConfiguration();
    JNINativeLibraryLoader.load();
    /**
     * Scan the USB tree to identify the system ROOT USB hub.
     */
    this.rootUsbHub = new UsbRootHub();
    this.deviceManager = new UsbDeviceManager(this.rootUsbHub,
                                              UsbServiceInstanceConfiguration.SCAN_INTERVAL);
    this.deviceManager.start();
  }

  @Override
  public IUsbHub getRootUsbHub() {
    this.deviceManager.firstScan();
    return this.rootUsbHub;
  }

  /**
   * Add a UsbServicesListener implementation to the listener list. The
   * UsbServicesListener will be notified for Device ATTACH and DETACH events.
   *
   * @param listener a UsbServicesListener implementation
   */
  @Override
  public void addUsbServicesListener(final IUsbServicesListener listener) {
    this.listeners.add(listener);
  }

  /**
   * Remove a UsbServicesListener implementation to the listener list.
   *
   * @param listener a UsbServicesListener implementation
   */
  @Override
  public void removeUsbServicesListener(final IUsbServicesListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * The API version. This is the usb4java JNI source code version.
   *
   * @return TFhe usb4java JNI source code version
   */
  @Override
  public String getApiVersion() {
    return API_VERSION;
  }

  /**
   * The implementation version. This is the Java source code version.
   *
   * @return The API implementation version
   */
  @Override
  public String getImpVersion() {
    return IMP_VERSION;
  }

  /**
   * The implementation description. e.g. "javax.usb + usb4java"
   *
   * @return The current implementation description.
   */
  @Override
  public String getImpDescription() {
    return IMP_DESCRIPTION;
  }

  /**
   * Informs listeners about a new attached device.
   *
   * @param device The new attached device.
   */
  void usbDeviceAttached(final IUsbDevice device) {
    this.listeners.usbDeviceAttached(new UsbServicesEvent(this, device));
  }

  /**
   * Informs listeners about a detached device.
   *
   * @param device The detached device.
   */
  void usbDeviceDetached(final IUsbDevice device) {
    this.listeners.usbDeviceDetached(new UsbServicesEvent(this, device));
  }

  /**
   * Returns the configuration.
   *
   * @return The configuration.
   */
  public UsbServiceInstanceConfiguration getConfig() {
    return this.config;
  }

  /**
   * Returns the usb4java services.
   *
   * @return The usb4java services.
   */
  static UsbServices getInstance() {
    try {
      return (UsbServices) USB.getUsbServices();
    } catch (final ClassCastException e) {
      throw new RuntimeException("usb4java is not the configured USB services implementation: " + e, e);
    } catch (final UsbException e) {
      throw new RuntimeException("Unable to create USB services: " + e, e);
    }
  }

  /**
   * Manually scans for USB device connection changes.
   */
  public void scan() {
    this.deviceManager.scan();
  }
}
