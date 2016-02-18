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

import java.util.ArrayList;
import java.util.List;
import javax.usb.descriptor.UsbInterfaceDescriptor;
import javax.usb.enumerated.EUSBClassCode;
import javax.usb.exception.UsbException;

/**
 * The virtual USB interfaces used by the virtual USB root hub.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class UsbRootHubInterface implements IUsbInterface {

  /**
   * The list of endpoints.
   */
  private final List<IUsbEndpoint> endpoints = new ArrayList<>(0);

  /**
   * The list of alternate settings.
   */
  private final List<IUsbInterface> settings = new ArrayList<>(0);

  /**
   * The USB configuration.
   */
  private final IUsbConfiguration configuration;

  /**
   * The interface descriptor.
   */
  private final IUsbInterfaceDescriptor descriptor = new UsbInterfaceDescriptor((byte) 0,
                                                                                (byte) 0,
                                                                                (byte) 0,
                                                                                EUSBClassCode.HUB,
                                                                                (byte) 0,
                                                                                (byte) 0,
                                                                                (byte) 0);

  /**
   * Constructor.
   *
   * @param configuration The USB configuration.
   */
  public UsbRootHubInterface(final IUsbConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void claim() throws UsbException {
    throw new UsbException("Virtual interfaces can't be claimed");
  }

  @Override
  public void claim(final IUsbInterfacePolicy policy) throws UsbException {
    throw new UsbException("Virtual interfaces can't be claimed");
  }

  @Override
  public void release() throws UsbException {
    throw new UsbException("Virtual interfaces can't be released");
  }

  @Override
  public boolean isClaimed() {
    return true;
  }

  @Override
  public boolean isActive() {
    return true;
  }

  @Override
  public int getNumSettings() {
    return 0;
  }

  @Override
  public byte getActiveSettingNumber() {
    return 0;
  }

  @Override
  public IUsbInterface getActiveSetting() {
    return this;
  }

  @Override
  public IUsbInterface getSetting(final byte number) {
    return this;
  }

  @Override
  public boolean containsSetting(final byte number) {
    return false;
  }

  @Override
  public List<IUsbInterface> getSettings() {
    return this.settings;
  }

  @Override
  public List<IUsbEndpoint> getUsbEndpoints() {
    return this.endpoints;
  }

  @Override
  public IUsbEndpoint getUsbEndpoint(final byte address) {
    return null;
  }

  @Override
  public boolean containsUsbEndpoint(final byte address) {
    return false;
  }

  @Override
  public IUsbConfiguration getUsbConfiguration() {
    return this.configuration;
  }

  @Override
  public IUsbInterfaceDescriptor getUsbInterfaceDescriptor() {
    return this.descriptor;
  }

  @Override
  public String getInterfaceString() {
    return null;
  }
}
