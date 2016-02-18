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

import javax.usb3.IUsbInterface;
import javax.usb3.IUsbInterfacePolicy;
import javax.usb3.IUsbEndpoint;
import javax.usb3.IUsbInterfaceDescriptor;
import javax.usb3.IUsbConfiguration;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.usb3.exception.UsbDisconnectedException;
import javax.usb3.exception.UsbException;
import javax.usb3.exception.UsbNotActiveException;

/**
 * Implementation of IUsbUsbInterface.
 *
 * @author Jesse Caulfield
 */
public final class UsbInterface extends AUsbInterface {

  /**
   * Constructor.
   *
   * @param configuration The USB configuration this interface belongs to.
   * @param descriptor    The libusb interface descriptor.
   */
  public UsbInterface(final IUsbConfiguration configuration, final IUsbInterfaceDescriptor descriptor) {
    super(configuration, descriptor);
  }

  /**
   * Ensures this setting and configuration is active.
   *
   * @throws UsbNotActiveException When the setting or the configuration is not
   *                               active.
   */
  private void checkActive() {
    if (!this.configuration.isActive()) {
      throw new UsbNotActiveException("Configuration is not active");
    }
    if (!isActive()) {
      throw new UsbNotActiveException("Setting is not active");
    }
  }

  /**
   * Ensures that the device is connected.
   *
   * @throws UsbDisconnectedException When device has been disconnected.
   */
  private void checkConnected() {
    this.configuration.getUsbDevice().isConnected();
  }

  /**
   * @inherit
   */
  @Override
  public void claim() throws UsbException {
    claim(null);
  }

  /**
   * @inherit
   */
  @Override
  public void claim(final IUsbInterfacePolicy policy) throws UsbException {
    checkActive();
    checkConnected();
    final AUsbDevice device = (AUsbDevice) this.configuration.getUsbDevice();
    device.claimInterface(this.descriptor.bInterfaceNumber(), policy != null && policy.forceClaim(this));
    this.configuration.setUsbInterface(this.descriptor.bInterfaceNumber(), this);
  }

  /**
   * @inherit
   */
  @Override
  public void release() throws UsbException {
    checkActive();
    checkConnected();
    ((AUsbDevice) this.configuration.getUsbDevice()).releaseInterface(this.descriptor.bInterfaceNumber());
  }

  /**
   * @inherit
   */
  @Override
  public boolean isClaimed() {
    return this.configuration.getUsbDevice().isInterfaceClaimed(this.descriptor.bInterfaceNumber());
  }

  /**
   * @inherit
   */
  @Override
  public boolean isActive() {
    return this.configuration.getUsbInterface(this.descriptor.bInterfaceNumber()) == this;
  }

  /**
   * @inherit
   */
  @Override
  public int getNumSettings() {
    return this.configuration.getNumSettings(this.descriptor.bInterfaceNumber());
  }

  /**
   * @inherit
   */
  @Override
  public byte getActiveSettingNumber() {
    checkActive();
    return this.configuration
            .getUsbInterface(this.descriptor.bInterfaceNumber())
            .getUsbInterfaceDescriptor().bAlternateSetting();
  }

  /**
   * @inherit
   */
  @Override
  public IUsbInterface getActiveSetting() {
    checkActive();
    return this.configuration.getUsbInterface(this.descriptor.bInterfaceNumber());
  }

  /**
   * @inherit
   */
  @Override
  public IUsbInterface getSetting(final byte number) {
    return (this.configuration).getSettings(
            this.descriptor.bInterfaceNumber()).get(number & 0xff);
  }

  /**
   * @inherit
   */
  @Override
  public boolean containsSetting(final byte number) {
    return (this.configuration).getSettings(
            this.descriptor.bInterfaceNumber()).containsKey(number & 0xff);
  }

  /**
   * @inherit
   */
  @Override
  public List<IUsbInterface> getSettings() {
    return Collections.unmodifiableList(new ArrayList<>(this.configuration.getSettings(this.descriptor.bInterfaceNumber()).values()));
  }

  /**
   * @inherit
   */
  @Override
  public List<IUsbEndpoint> getUsbEndpoints() {
    return Collections.unmodifiableList(new ArrayList<>(this.endpoints.values()));
  }

  /**
   * @inherit
   */
  @Override
  public IUsbEndpoint getUsbEndpoint(final byte address) {
    return this.endpoints.get(address);
  }

  /**
   * @inherit
   */
  @Override
  public boolean containsUsbEndpoint(final byte address) {
    return this.endpoints.containsKey(address);
  }

  /**
   * @inherit
   */
  @Override
  public IUsbConfiguration getUsbConfiguration() {
    return this.configuration;
  }

  /**
   * @inherit
   */
  @Override
  public IUsbInterfaceDescriptor getUsbInterfaceDescriptor() {
    return this.descriptor;
  }

  /**
   * @inherit
   */
  @Override
  public String getInterfaceString() throws UsbException,
                                            UnsupportedEncodingException {
    checkConnected();
    final byte iInterface = this.descriptor.iInterface();
    if (iInterface == 0) {
      return null;
    }
    return this.configuration.getUsbDevice().getString(iInterface);
  }

  @Override
  public String toString() {
    return String.format("USB interface %02x",
                         this.descriptor.bInterfaceNumber());
  }
}
