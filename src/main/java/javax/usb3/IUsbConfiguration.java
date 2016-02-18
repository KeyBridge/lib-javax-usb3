/*
 * Copyright (C) 1999 - 2001, International Business Machines
 * Corporation. All Rights Reserved. Provided and licensed under the terms and
 * conditions of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 *
 * Copyright (C) 2014 Key Bridge LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javax.usb3;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import javax.usb3.exception.UsbDisconnectedException;
import javax.usb3.exception.UsbException;

/**
 * 9.6.3 Standard USB Descriptor Configuration
 * <p>
 * Interface for a USB configuration. This represents a configuration of a USB
 * device. The device may have multiple configurations, and must have at least
 * one configuration; only one configuration (if any) can be currently active.
 * <p>
 * If the device is in an unconfigured state none of its configurations are
 * active. If this configuration is not active, its device model
 * (IUsbInterfaces, UsbEndpoints, and UsbPipes) may be browsed, but no action
 * can be taken.
 * <p>
 * See the USB 2.0 sec 9.6.3 for more information on USB device configurations.
 *
 * @author Dan Streetman
 * @author Jesse Caulfield
 * @author E. Michael Maximilien
 */
public interface IUsbConfiguration {

  /**
   * If this IUsbConfiguration is active.
   *
   * @return if this IUsbConfiguration is active.
   */
  public boolean isActive();

  /**
   * Get all IUsbInterfaces for this configuration.
   * <p>
   * The returned IUsbInterface settings depend on whether this configuration
   * (and by association its contained interfaces) is active or not:
   * <ul>
   * <li>If this configuration is active, all IUsbInterfaces will be the active
   * alternate setting for that interface.</li>
   * <li>If this configuration is not active, no contained interfaces are
   * active, so they have no active alternate settings. The IUsbInterfaces will
   * then be an implementation-dependent alternate setting IUsbInterface for
   * each interface. To get a specific alternate setting, use
   * {@link javax.usb.UsbInterface#getSetting(byte) IUsbInterface.getSetting(byte number)}.</li>
   * </ul>
   *
   * @return All IUsbInterfaces for this configuration.
   */
  public Collection<IUsbInterface> getUsbInterfaces();

  /**
   * Get the IUsbInterface with the specified interface number.
   * <p>
   * The returned interface setting will be the current active alternate setting
   * if this configuration (and thus the contained interface) is
   * {@link #isActive() active}. If this configuration is not active, the
   * returned interface setting will be an implementation-dependent alternate
   * setting. To get a specific alternate setting, use
   * {@link javax.usb.UsbInterface#getSetting(byte) IUsbInterface.getSetting(byte number)}.
   * <p>
   * If the specified IUsbInterface does not exist, this returns null.
   *
   * @param number The number of the interface to get.
   * @return The specified IUsbInterface, or null.
   */
  public IUsbInterface getUsbInterface(byte number);

  /**
   * If the specified IUsbInterface is contained in this IUsbConfiguration.
   *
   * @param number The interface number.
   * @return If this configuration contains the specified IUsbInterface.
   */
  public boolean containsUsbInterface(byte number);

  /**
   * Get the parent IUsbDevice that this IUsbConfiguration belongs to.
   *
   * @return the IUsbDevice that this IUsbConfiguration belongs to.
   */
  public IUsbDevice getUsbDevice();

  /**
   * Get the configuration descriptor.
   * <p>
   * The descriptor may be cached.
   *
   * @return The configuration descriptor.
   */
  public IUsbConfigurationDescriptor getUsbConfigurationDescriptor();

  /**
   * Get the configuration String.
   * <p>
   * This is a convenience method. The String may be cached. If the device does
   * not support strings or does not define the configuration string, this
   * returns null.
   *
   * @return The configuration String, or null.
   * @exception UsbException                 If there was an error getting the
   *                                         UsbStringDescriptor.
   * @exception UnsupportedEncodingException If the string encoding is not
   *                                         supported.
   * @exception UsbDisconnectedException     If the device has been
   *                                         disconnected.
   */
  public String getConfigurationString() throws UsbException, UnsupportedEncodingException, UsbDisconnectedException;

  /**
   * Returns the number of alternate settings on the specified interface.
   *
   * @param number The interface number.
   * @return The number of alternate settings.
   */
  public int getNumSettings(final byte number);

  /**
   * Returns the alternate settings for the specified interface.
   *
   * @param number The interface number.
   * @return The alternate settings for the specified interface.
   */
  public Map<Integer, IUsbInterface> getSettings(final byte number);
}
