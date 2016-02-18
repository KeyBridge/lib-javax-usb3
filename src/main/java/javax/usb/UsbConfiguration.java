/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
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
package javax.usb;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.usb.descriptor.UsbConfigurationDescriptor;
import javax.usb.exception.UsbDisconnectedException;
import javax.usb.exception.UsbException;
import javax.usb.utility.UsbExceptionFactory;
import org.usb4java.ConfigDescriptor;
import org.usb4java.InterfaceDescriptor;
import org.usb4java.LibUsb;

/**
 * usb4java implementation of JSR-80 IUsbConfiguration.
 *
 * @author Klaus Reimer (k@ailis.de)
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
  private final AUsbDevice device;

  /**
   * The interfaces. This is a map from interface number to a map of alternate
   * settings which maps setting numbers to actual interfaces.
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
  public UsbConfiguration(final AUsbDevice device, final ConfigDescriptor descriptor) {
    this.device = device;
    this.descriptor = new UsbConfigurationDescriptor(descriptor);
    for (org.usb4java.Interface iface : descriptor.iface()) {
      for (InterfaceDescriptor ifaceDescriptor : iface.altsetting()) {
        final int ifaceNumber = ifaceDescriptor.bInterfaceNumber() & 0xff;
        final int settingNumber = ifaceDescriptor.bAlternateSetting() & 0xff;

        Map<Integer, IUsbInterface> settings = this.interfaces.get(ifaceNumber);
        if (settings == null) {
          settings = new HashMap<>();
          this.interfaces.put(ifaceNumber, settings);
        }
        final UsbInterface usbInterface = new UsbInterface(this, ifaceDescriptor);

        // If we have no active setting for current interface number
        // yet or the alternate setting number is 0 (which marks the
        // default alternate setting) then set current interface as
        // the active setting.
        if (!this.activeSettings.containsKey(ifaceNumber) || ifaceDescriptor.bAlternateSetting() == 0) {
          this.activeSettings.put(ifaceNumber, usbInterface);
        }

        // Add the interface to the settings list
        settings.put(settingNumber, usbInterface);
      }
    }
  }

  /**
   * Ensures that the device is connected.
   *
   * @throws UsbDisconnectedException When device has been disconnected.
   */
  private void checkConnected() {
    this.device.checkConnected();
  }

  @Override
  public boolean isActive() {
    return this.device.getActiveUsbConfigurationNumber() == this.descriptor
            .bConfigurationValue();
  }

  @Override
  public List<IUsbInterface> getUsbInterfaces() {
    return Collections.unmodifiableList(new ArrayList<>(this.activeSettings.values()));
  }

  /**
   * Returns the alternate settings for the specified interface.
   *
   * @param number The interface number.
   * @return The alternate settings for the specified interface.
   */
  Map<Integer, IUsbInterface> getSettings(final byte number) {
    return this.interfaces.get(number & 0xff);
  }

  /**
   * Returns the number of alternate settings of the specified interface.
   *
   * @param number The interface number.
   * @return The number of alternate settings.
   */
  int getNumSettings(final byte number) {
    return this.interfaces.get(number & 0xff).size();
  }

  @Override
  public IUsbInterface getUsbInterface(final byte number) {
    return this.activeSettings.get(number & 0xff);
  }

  /**
   * Sets the active USB interface setting.
   *
   * @param number THe interface number.
   * @param iface  The interface setting to activate.
   * @throws UsbException When interface setting could not be set.
   */
  void setUsbInterface(final byte number, final UsbInterface iface)
          throws UsbException {
    if (this.activeSettings.get(number & 0xff) != iface) {
      final int result = LibUsb.setInterfaceAltSetting(
              this.device.open(), number,
              iface.getUsbInterfaceDescriptor().bAlternateSetting());
      if (result < 0) {
        throw UsbExceptionFactory.createPlatformException("Unable to set alternate interface", result);
      }
      this.activeSettings.put(number & 0xff, iface);
    }
  }

  @Override
  public boolean containsUsbInterface(final byte number) {
    return this.activeSettings.containsKey(number & 0xff);
  }

  @Override
  public AUsbDevice getUsbDevice() {
    return this.device;
  }

  @Override
  public IUsbConfigurationDescriptor getUsbConfigurationDescriptor() {
    return this.descriptor;
  }

  @Override
  public String getConfigurationString() throws UsbException,
                                                UnsupportedEncodingException {
    checkConnected();
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
