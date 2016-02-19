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

import javax.usb3.IUsbDevice;
import javax.usb3.IUsbHub;
import javax.usb3.IUsbServices;
import javax.usb3.event.IUsbServicesListener;
import javax.usb3.event.UsbServicesEvent;
import javax.usb3.exception.UsbException;
import javax.usb3.utility.JNINativeLibraryLoader;

/**
 * Implementation of JSR-80 IUsbServices interface.
 * <p>
 * This is instantiated by the USB. The implementation must include a
 * no-parameter constructor.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class UsbServices implements IUsbServices {

  /**
   * The implementation description.
   */
  private static final String IMP_DESCRIPTION = "javax.usb3";
  /**
   * The implementation version. This is the Java source code version.
   */
  private static final String IMP_VERSION = "1.4.x";
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
   * @inherit
   */
  @Override
  public void addUsbServicesListener(final IUsbServicesListener listener) {
    this.listeners.add(listener);
  }

  /**
   * @inherit
   */
  @Override
  public void removeUsbServicesListener(final IUsbServicesListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * @inherit
   *
   * @return {@value #API_VERSION}
   */
  @Override
  public String getApiVersion() {
    return API_VERSION;
  }

  /**
   * @inherit
   *
   * @return {@value #IMP_VERSION}
   */
  @Override
  public String getImpVersion() {
    return IMP_VERSION;
  }

  /**
   * @inherit
   *
   * @return {@value #IMP_DESCRIPTION}
   */
  @Override
  public String getImpDescription() {
    return IMP_DESCRIPTION;
  }

  /**
   * @inherit
   */
  @Override
  public void usbDeviceAttached(final IUsbDevice device) {
    this.listeners.usbDeviceAttached(new UsbServicesEvent(this, device));
  }

  /**
   * @inherit
   */
  @Override
  public void usbDeviceDetached(final IUsbDevice device) {
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
   * Manually scans for USB device connection changes.
   */
  public void scan() {
    this.deviceManager.scan();
  }
}
