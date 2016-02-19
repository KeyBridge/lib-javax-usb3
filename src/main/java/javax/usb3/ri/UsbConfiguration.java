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

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.usb3.IUsbConfiguration;
import javax.usb3.IUsbConfigurationDescriptor;
import javax.usb3.IUsbDevice;
import javax.usb3.IUsbInterface;
import javax.usb3.descriptor.UsbConfigurationDescriptor;
import javax.usb3.descriptor.UsbInterfaceDescriptor;
import javax.usb3.exception.UsbException;
import javax.usb3.utility.UsbExceptionFactory;
import org.usb4java.ConfigDescriptor;
import org.usb4java.InterfaceDescriptor;
import org.usb4java.LibUsb;

/**
 * Implementation of JSR-80 IUsbConfiguration.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class UsbConfiguration implements IUsbConfiguration {

  /**
   * The configurationDescriptor.
   */
  private final IUsbConfigurationDescriptor descriptor;

  /**
   * The USB device this configuration belongs to.
   */
  private final IUsbDevice device;

  /**
   * The interfaces.
   * <p>
   * This is a map of the interface number to a sub-map of alternate settings
   * which maps setting numbers to actual interfaces.
   */
  private final Map<Integer, Map<Integer, IUsbInterface>> interfaces = new HashMap<>();

  /**
   * This map contains the active USB interfaces.
   */
  private final Map<Integer, IUsbInterface> activeSettings = new HashMap<>();

  /**
   * Constructor.
   *
   * @param device     The device this configuration belongs to.
   * @param descriptor The libusb configuration descriptor.
   */
  public UsbConfiguration(final IUsbDevice device, final ConfigDescriptor descriptor) {
    this.device = device;
    this.descriptor = new UsbConfigurationDescriptor(descriptor);
    for (org.usb4java.Interface jniInterface : descriptor.iface()) {
      for (InterfaceDescriptor ifDescriptor : jniInterface.altsetting()) {
        final int interfaceNumber = ifDescriptor.bInterfaceNumber() & 0xff;
        final int settingNumber = ifDescriptor.bAlternateSetting() & 0xff;

        final UsbInterface usbInterface = new UsbInterface(this, new UsbInterfaceDescriptor(ifDescriptor));
        /**
         * If we have no active setting for current interface number yet or the
         * alternate setting number is 0 (which marks the default alternate
         * setting) then set current interface as the active setting.
         */
        if (!this.activeSettings.containsKey(interfaceNumber) || ifDescriptor.bAlternateSetting() == 0) {
          this.activeSettings.put(interfaceNumber, usbInterface);
        }
        /**
         * Add the interface to the settings list
         */
        Map<Integer, IUsbInterface> settings = this.interfaces.get(interfaceNumber);
        if (settings == null) {
          settings = new HashMap<>();
          this.interfaces.put(interfaceNumber, settings);
        }
        settings.put(settingNumber, usbInterface);
      }
    }
  }

  /**
   * Ensures that the device is connected.
   *
   * @throws UsbDisconnectedException When device has been disconnected.
   */
//  private boolean isConnected() {    return this.device.isConnected();  }
  /**
   * @inherit
   */
  @Override
  public boolean isActive() {
    return this.device.getActiveUsbConfigurationNumber() == this.descriptor.bConfigurationValue();
  }

  /**
   * @inherit
   */
  @Override
  public List<IUsbInterface> getUsbInterfaces() {
    return Collections.unmodifiableList(new ArrayList<>(this.activeSettings.values()));
  }

  /**
   * @inherit
   */
  @Override
  public Map<Integer, IUsbInterface> getSettings(final byte number) {
    return this.interfaces.get(number & 0xff);
  }

  /**
   * @inherit
   */
  @Override
  public int getNumSettings(final byte number) {
    return this.interfaces.get(number & 0xff).size();
  }

  /**
   * @inherit
   */
  @Override
  public IUsbInterface getUsbInterface(final byte number) {
    return this.activeSettings.get(number & 0xff);
  }

  /**
   * @inherit
   */
  @Override
  public void setUsbInterface(final byte number, final IUsbInterface usbInterface) throws UsbException {
    if (this.activeSettings.get(number & 0xff) != usbInterface) {
      final int result = LibUsb.setInterfaceAltSetting(((AUsbDevice) this.device).open(),
                                                       number,
                                                       usbInterface.getUsbInterfaceDescriptor().bAlternateSetting());
      if (result < 0) {
        throw UsbExceptionFactory.createPlatformException("Unable to set alternate interface", result);
      }
      this.activeSettings.put(number & 0xff, usbInterface);
    }
  }

  /**
   * @inherit
   */
  @Override
  public boolean containsUsbInterface(final byte number) {
    return this.activeSettings.containsKey(number & 0xff);
  }

  /**
   * @inherit
   */
  @Override
  public IUsbDevice getUsbDevice() {
    return this.device;
  }

  /**
   * @inherit
   */
  @Override
  public IUsbConfigurationDescriptor getUsbConfigurationDescriptor() {
    return this.descriptor;
  }

  /**
   * @inherit
   */
  @Override
  public String getConfigurationString() throws UsbException, UnsupportedEncodingException {
    /**
     * Ensure the device is still connected.
     */
    this.device.isConnected();
    final byte iConfiguration = this.descriptor.iConfiguration();
    if (iConfiguration == 0) {
      return null;
    }
    return this.device.getString(iConfiguration);
  }

  @Override
  public String toString() {
    return String.format("USB configuration %02x",
                         this.descriptor.bConfigurationValue());
  }
}
