/**
 * Original Copyright (c) 1999 - 2001, International Business Machines
 * Corporation. All Rights Reserved. Provided and licensed under the terms and
 * conditions of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 * <p>
 * Modifications and improvements Copyright (c) 2014 Key Bridge Global LLC. All
 * Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javax.usb;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.usb.event.*;
import javax.usb.exception.UsbDisconnectedException;
import javax.usb.exception.UsbException;
import javax.usb.ri.enumerated.EDevicePortSpeed;

/**
 * Interface for a USB device.
 * <p>
 * The submission methods contained in this IUsbDevice operate on the device's
 * Default Control Pipe. The device does not have to be
 * {@link #isConfigured() configured} to use the Default Control Pipe.
 * <p>
 * The implementation is not required to be Thread-safe. If a Thread-safe
 * IUsbDevice is required, use a
 * {@link javax.usb.util.UsbUtil#synchronizedUsbDevice(UsbDevice) synchronizedIUsbDevice}.
 * <p>
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public interface IUsbDevice {

  /**
   * Get the IUsbPort on the parent UsbHub that this device is connected to.
   * <p>
   * @return The port on the parent UsbHub that this is attached to.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  public IUsbPort getParentUsbPort() throws UsbDisconnectedException;

  /**
   * If this is a UsbHub.
   * <p>
   * @return true if this is a UsbHub.
   */
  public boolean isUsbHub();

  /**
   * Get the manufacturer String.
   * <p>
   * This is a convienence method, which uses
   * {@link #getString(byte) getString}.
   * <p>
   * @return The manufacturer String, or null.
   * @throws UsbException                 If there was an error getting the
   *                                      IUsbStringDescriptor.
   * @exception UnsupportedEncodingException If the string encoding is not
   *                                         supported.
   * @exception UsbDisconnectedException     If this device has been
   *                                         disconnected.
   */
  public String getManufacturerString() throws UsbException, UnsupportedEncodingException, UsbDisconnectedException;

  /**
   * Get the serial number String.
   * <p>
   * This is a convienence method, which uses
   * {@link #getString(byte) getString}.
   * <p>
   * @return The serial number String, or null.
   * @throws UsbException                 If there was an error getting the
   *                                      IUsbStringDescriptor.
   * @exception UnsupportedEncodingException If the string encoding is not
   *                                         supported.
   * @exception UsbDisconnectedException     If this device has been
   *                                         disconnected.
   */
  public String getSerialNumberString() throws UsbException, UnsupportedEncodingException, UsbDisconnectedException;

  /**
   * Get the product String.
   * <p>
   * This is a convienence method, which uses
   * {@link #getString(byte) getString}.
   * <p>
   * @return The product String, or null.
   * @throws UsbException                 If there was an error getting the
   *                                      IUsbStringDescriptor.
   * @exception UnsupportedEncodingException If the string encoding is not
   *                                         supported.
   * @exception UsbDisconnectedException     If this device has been
   *                                         disconnected.
   */
  public String getProductString() throws UsbException, UnsupportedEncodingException, UsbDisconnectedException;

  /**
   * Get the speed of the device.
   * <p>
   * The speed will be one of:
   * <ul>
   * <li>{@link javax.usb.UsbConst#DEVICE_SPEED_UNKNOWN UsbConst.DEVICE_SPEED_UNKNOWN}</li>
   * <li>{@link javax.usb.UsbConst#DEVICE_SPEED_LOW UsbConst.DEVICE_SPEED_LOW}</li>
   * <li>{@link javax.usb.UsbConst#DEVICE_SPEED_FULL UsbConst.DEVICE_SPEED_FULL}</li>
   * </ul>
   * <p>
   * @return The speed of this device.
   */
  public EDevicePortSpeed getSpeed();

  /**
   * Get all IUsbConfigurations for this device.
   * <p>
   * The List is unmodifiable.
   * <p>
   * @return All IUsbConfigurations for this device.
   */
  public List<IUsbConfiguration> getUsbConfigurations();

  /**
   * Get the specified IUsbConfiguration.
   * <p>
   * If the specified IUsbConfiguration does not exist, null is returned. Config
   * number 0 is reserved for the Not Configured state (see the USB 1.1
   * specification section 9.4.2). Obviously, no IUsbConfiguration exists for
   * the Not Configured state.
   * <p>
   * @param number the bytecode address of the configuration value
   * @return The specified IUsbConfiguration, or null.
   */
  public IUsbConfiguration getUsbConfiguration(byte number);

  /**
   * If this IUsbDevice contains the specified IUsbConfiguration.
   * <p>
   * This will return false for zero (the Not Configured state).
   * <p>
   * @param number the bytecode address of the configuration value
   * @return If the specified IUsbConfiguration is contained in this IUsbDevice.
   */
  public boolean containsUsbConfiguration(byte number);

  /**
   * Get the number of the active IUsbConfiguration.
   * <p>
   * If the device is in a Not Configured state, this will return zero.
   * <p>
   * @return The active config number.
   */
  public byte getActiveUsbConfigurationNumber();

  /**
   * Get the active IUsbConfiguration.
   * <p>
   * If this device is Not Configured, this returns null.
   * <p>
   * @return The active IUsbConfiguration, or null.
   */
  public IUsbConfiguration getActiveUsbConfiguration();

  /**
   * If this IUsbDevice is configured.
   * <p>
   * This returns true if the device is in the configured state as shown in the
   * USB 1.1 specification table 9.1.
   * <p>
   * @return If this is in the Configured state.
   */
  public boolean isConfigured();

  /**
   * Get the device descriptor.
   * <p>
   * The descriptor may be cached.
   * <p>
   * @return The device descriptor.
   */
  public IUsbDeviceDescriptor getUsbDeviceDescriptor();

  /**
   * Get the specified string descriptor.
   * <p>
   * This is a convienence method. The IUsbStringDescriptor may be cached. If
   * the device does not support strings or does not define the specified string
   * descriptor, this returns null.
   * <p>
   * @param index The index of the string descriptor to get.
   * @return The specified string descriptor.
   * @exception UsbException             If an error occurred while getting the
   *                                     string descriptor.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  public IUsbStringDescriptor getUsbStringDescriptor(byte index) throws UsbException, UsbDisconnectedException;

  /**
   * Get the String from the specified string descriptor.
   * <p>
   * This is a convienence method, which uses
   * {@link #getUsbStringDescriptor(byte) getIUsbStringDescriptor()}.
   * {@link javax.usb.UsbStringDescriptor#getString() getString()}.
   * <p>
   * @param index The index of the string to get.
   * @return The specified String.
   * @exception UsbException                 If an error occurred while getting
   *                                         the String.
   * @exception UnsupportedEncodingException If the string encoding is not
   *                                         supported.
   * @exception UsbDisconnectedException     If this device has been
   *                                         disconnected.
   */
  public String getString(byte index) throws UsbException, UnsupportedEncodingException, UsbDisconnectedException;

  /**
   * Submit a IUsbControlIrp synchronously to the Default Control Pipe.
   * <p>
   * @param irp The IUsbControlIrp.
   * @exception UsbException             If an error occurrs.
   * @throws IllegalArgumentException If the IUsbControlIrp is not valid.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  public void syncSubmit(IUsbControlIrp irp) throws UsbException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Submit a IUsbControlIrp asynchronously to the Default Control Pipe.
   * <p>
   * @param irp The IUsbControlIrp.
   * @exception UsbException             If an error occurrs.
   * @throws IllegalArgumentException If the IUsbControlIrp is not valid.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  public void asyncSubmit(IUsbControlIrp irp) throws UsbException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Submit a List of IUsbControlIrps synchronously to the Default Control Pipe.
   * <p>
   * All IUsbControlIrps are guaranteed to be atomically (with respect to other
   * clients of this API) submitted to the Default Control Pipe. Atomicity on a
   * native level is implementation-dependent.
   * <p>
   * @param list The List of IUsbControlIrps.
   * @exception UsbException             If an error occurrs.
   * @throws IllegalArgumentException If the List contains non-IUsbControlIrp
   *                                  objects or those UsbIrp(s) are invalid.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  public void syncSubmit(List<IUsbControlIrp> list) throws UsbException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Submit a List of IUsbControlIrps asynchronously to the Default Control
   * Pipe.
   * <p>
   * All IUsbControlIrps are guaranteed to be atomically (with respect to other
   * clients of this API) submitted to the Default Control Pipe. Atomicity on a
   * native level is implementation-dependent.
   * <p>
   * @param list The List of IUsbControlIrps.
   * @exception UsbException             If an error occurrs.
   * @throws IllegalArgumentException If the List contains non-IUsbControlIrp
   *                                  objects or those UsbIrp(s) are invalid.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  public void asyncSubmit(List<IUsbControlIrp> list) throws UsbException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Create a IUsbControlIrp.
   * <p>
   * This creates a IUsbControlIrp that may be optimized for use on this
   * IUsbDevice. Using this UsbIrp instead of a
   * {@link javax.usb.util.DefaultUsbControlIrp DefaultIUsbControlIrp} may
   * increase performance or decrease memory requirements.
   * <p>
   * The IUsbDevice cannot require this IUsbControlIrp to be used, all submit
   * methods <i>must</i> accept any IUsbControlIrp implementation.
   * <p>
   * @param bmRequestType The bmRequestType.
   * @param bRequest      The bRequest.
   * @param wValue        The wValue.
   * @param wIndex        The wIndex.
   * @return A IUsbControlIrp ready for use.
   */
  public IUsbControlIrp createUsbControlIrp(byte bmRequestType, byte bRequest, short wValue, short wIndex);

  /**
   * Add a IIUsbDeviceListener to this IUsbDevice.
   * <p>
   * @param listener The IIUsbDeviceListener to add.
   */
  public void addUsbDeviceListener(IUsbDeviceListener listener);

  /**
   * Remove a IIUsbDeviceListener from this IUsbDevice.
   * <p>
   * @param listener The listener to remove.
   */
  public void removeUsbDeviceListener(IUsbDeviceListener listener);

}
