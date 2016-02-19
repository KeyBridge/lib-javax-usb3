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

import javax.usb3.IUsbConfiguration;
import javax.usb3.IUsbEndpoint;
import javax.usb3.IUsbInterface;
import javax.usb3.IUsbInterfacePolicy;
import javax.usb3.exception.UsbException;

/**
 * The virtual USB interfaces used by the virtual USB root hub.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class UsbRootHubInterface extends AUsbInterface {

  /**
   * Construct a new USB root hub interface. A static UsbInterfaceDescriptor is
   * automatically created.
   *
   * @param configuration The USB configuration.
   */
  public UsbRootHubInterface(final IUsbConfiguration configuration) {
    super(configuration);
//    super(configuration, new UsbInterfaceDescriptor((byte) 0x00, // bInterfaceNumber
//                                                    (byte) 0x00,
//                                                    (byte) 0x00,
//                                                    EUSBClassCode.HUB,
//                                                    EUSBSubclassCode.INTERFACE_ASSOCIATION_DESCRIPTOR.getBytecodeSubclass(),
//                                                    EUSBSubclassCode.INTERFACE_ASSOCIATION_DESCRIPTOR.getBytecodeProtocol(),
//                                                    (byte) 0x00,
//                                                    null));
  }

  /**
   * @inherit
   *
   * @deprecated Virtual interfaces can't be claimed
   */
  @Override
  public void claim() throws UsbException {
    throw new UsbException("Virtual interfaces can't be claimed");
  }

  /**
   * @inherit
   *
   * @deprecated Virtual interfaces can't be claimed
   */
  @Override
  public void claim(final IUsbInterfacePolicy policy) throws UsbException {
    throw new UsbException("Virtual interfaces can't be claimed");
  }

  /**
   * @inherit
   *
   * @deprecated Virtual interfaces can't be claimed
   */
  @Override
  public void release() throws UsbException {
    throw new UsbException("Virtual interfaces can't be released");
  }

  /**
   * @inherit
   *
   * @return true
   */
  @Override
  public boolean isClaimed() {
    return true;
  }

  /**
   * @inherit
   *
   * @return true
   */
  @Override
  public boolean isActive() {
    return true;
  }

  /**
   * @inherit
   *
   * @return 0
   */
  @Override
  public int getNumSettings() {
    return 0;
  }

  /**
   * @inherit
   *
   * @return 0
   */
  @Override
  public byte getActiveSettingNumber() {
    return 0;
  }

  /**
   * @inherit
   *
   * @return this
   */
  @Override
  public IUsbInterface getActiveSetting() {
    return this;
  }

  /**
   * @inherit
   *
   * @return this
   */
  @Override
  public IUsbInterface getSetting(final byte number) {
    return this;
  }

  /**
   * @inherit
   *
   * @return FALSE
   */
  @Override
  public boolean containsSetting(final byte number) {
    return false;
  }

  /**
   * @inherit
   *
   * @return null
   */
  @Override
  public IUsbEndpoint getUsbEndpoint(final byte address) {
    return null;
  }

  /**
   * @inherit
   *
   * @return false
   */
  @Override
  public boolean containsUsbEndpoint(final byte address) {
    return false;
  }

  /**
   * @inherit
   *
   * @return null
   */
  @Override
  public String getInterfaceString() {
    return null;
  }
}
