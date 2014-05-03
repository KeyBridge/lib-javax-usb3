/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import javax.usb.*;
import javax.usb.event.IUsbServicesListener;
import javax.usb.event.UsbServicesEvent;
import javax.usb.exception.UsbException;
import org.usb4java.libusbutil.NativeLibraryLoader;

/**
 * usb4java implementation of JSR-80 IUsbServices.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Services implements IUsbServices {

  /**
   * The implementation description.
   */
  private static final String IMP_DESCRIPTION = "usb4java";

  /**
   * The implementation version. This is the Java source code version.
   */
  private static final String IMP_VERSION = "1.3.0-javax.usb";

  /**
   * The API version. This is the JNI source code version.
   */
  private static final String API_VERSION = "1.0.2";

  /**
   * The USB services listeners.
   */
  private final ServicesListenerList listeners = new ServicesListenerList();

  /**
   * The virtual USB root hub.
   */
  private final UsbRootHub rootHub;

  /**
   * The USB device scanner.
   */
  private final DeviceManager deviceManager;

  /**
   * If devices should be scanned by hierarchy.
   */
  private final ServicesInstanceConfiguration config;

  /**
   * Constructor.
   * <p>
   * @throws UsbException     When properties could not be loaded.
   * @throws RuntimeException When the native library corresponding to the host
   *                          operating system fails to load
   */
  public Services() throws UsbException {
    this.config = new ServicesInstanceConfiguration(UsbHostManager.getProperties());
    NativeLibraryLoader.load();
    this.rootHub = new UsbRootHub();
    this.deviceManager = new DeviceManager(this.rootHub,
                                           this.config.getScanInterval());
    this.deviceManager.start();
  }

  @Override
  public IUsbHub getRootUsbHub() {
    this.deviceManager.firstScan();
    return this.rootHub;
  }

  @Override
  public void addUsbServicesListener(final IUsbServicesListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void removeUsbServicesListener(final IUsbServicesListener listener) {
    this.listeners.remove(listener);
  }

  @Override
  public String getApiVersion() {
    return API_VERSION;
  }

  @Override
  public String getImpVersion() {
    return IMP_VERSION;
  }

  @Override
  public String getImpDescription() {
    return IMP_DESCRIPTION;
  }

  /**
   * Informs listeners about a new attached device.
   * <p>
   * @param device The new attached device.
   */
  void usbDeviceAttached(final IUsbDevice device) {
    this.listeners.usbDeviceAttached(new UsbServicesEvent(this, device));
  }

  /**
   * Informs listeners about a detached device.
   * <p>
   * @param device The detached device.
   */
  void usbDeviceDetached(final IUsbDevice device) {
    this.listeners.usbDeviceDetached(new UsbServicesEvent(this, device));
  }

  /**
   * Returns the configuration.
   * <p>
   * @return The configuration.
   */
  public ServicesInstanceConfiguration getConfig() {
    return this.config;
  }

  /**
   * Returns the usb4java services.
   * <p>
   * @return The usb4java services.
   */
  static Services getInstance() {
    try {
      return (Services) UsbHostManager.getUsbServices();
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
