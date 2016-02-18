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
package javax.usb;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.usb.enumerated.EDevicePortSpeed;
import javax.usb.event.IUsbDeviceListener;
import javax.usb.exception.UsbDisconnectedException;
import javax.usb.exception.UsbException;

/**
 * Interface for a USB device.
 * <p>
 * The submission methods contained in this IUsbDevice operate on the device's
 * Default Control Pipe. The device does not have to be
 * {@link #isConfigured() configured} to use the Default Control Pipe.
 * <p>
 * USB devices are divided into device classes such as hub, human interface,
 * printer, imaging, or mass storage device. The hub device class indicates a
 * specially designated USB device that provides additional USB attachment
 * points (refer to Chapter 11). USB devices are required to carry information
 * for self- identification and generic configuration. They are also required at
 * all times to display behavior consistent with defined USB device states.
 * <p>
 * All USB devices are accessed by a USB address that is assigned when the
 * device is attached and enumerated. Each USB device additionally supports one
 * or more pipes through which the host may communicate with the device. All USB
 * devices must support a specially designated pipe at endpoint zero to which
 * the USB device’s USB control pipe will be attached. All USB devices support a
 * common access mechanism for accessing information through this control pipe.
 * <p>
 * Associated with the control pipe at endpoint zero is the information required
 * to completely describe the USB device. This information falls into the
 * following categories:
 * <ul>
 * <li>Standard: This is information whose definition is common to all USB
 * devices and includes items such as vendor identification, device class, and
 * power management capability. Device, configuration, interface, and endpoint
 * descriptions carry configuration-related information about the device.</li>
 * <li> Class: The definition of this information varies, depending on the
 * device class of the USB device.</li>
 * <li>USB Vendor: The vendor of the USB device is free to put any information
 * desired here. The format, however, is not determined by this
 * specification.</li></ul>
 * <p>
 * Two major divisions of device classes exist: hubs and functions. Only hubs
 * have the ability to provide additional USB attachment points. Functions
 * provide additional capabilities to the host. In this library USB
 * {@code functions} are represented by {@linkplain #IUsbDevice} while Hubs are
 * represented by {@linkplain #IUsbHub}, which extends IUsbDevice.
 * <p>
 * <p>
 * Additionally, each USB device carries USB control and status information.
 *
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public interface IUsbDevice {

  /**
   * Get the IUsbPort on the parent UsbHub that this device is connected to.
   *
   * @return The port on the parent UsbHub that this is attached to.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  public IUsbPort getParentUsbPort() throws UsbDisconnectedException;

  /**
   * If this is a UsbHub.
   *
   * @return true if this is a UsbHub.
   */
  public boolean isUsbHub();

  /**
   * Get the manufacturer String.
   * <p>
   * This is a convienence method, which uses
   * {@link #getString(byte) getString}.
   *
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
   *
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
   *
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
   * <li>{@link javax.usb.enumerated.EDevicePortSpeed#UNKNOWN }</li>
   * <li>{@link javax.usb.enumerated.EDevicePortSpeed#LOW }</li>
   * <li>{@link javax.usb.enumerated.EDevicePortSpeed#FULL }</li>
   * <li>{@link javax.usb.enumerated.EDevicePortSpeed#HIGH }</li>
   * <li>{@link javax.usb.enumerated.EDevicePortSpeed#SUPER }</li>
   * </ul>
   *
   * @return The speed of this device.
   */
  public EDevicePortSpeed getSpeed();

  /**
   * Get all IUsbConfigurations for this device.
   * <p>
   * The List is unmodifiable.
   *
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
   *
   * @param number the bytecode address of the configuration value
   * @return The specified IUsbConfiguration, or null.
   */
  public IUsbConfiguration getUsbConfiguration(byte number);

  /**
   * If this IUsbDevice contains the specified IUsbConfiguration.
   * <p>
   * This will return false for zero (the Not Configured state).
   *
   * @param number the bytecode address of the configuration value
   * @return If the specified IUsbConfiguration is contained in this IUsbDevice.
   */
  public boolean containsUsbConfiguration(byte number);

  /**
   * Get the number of the active IUsbConfiguration.
   * <p>
   * If the device is in a Not Configured state, this will return zero.
   *
   * @return The active config number.
   */
  public byte getActiveUsbConfigurationNumber();

  /**
   * Get the active IUsbConfiguration.
   * <p>
   * If this device is Not Configured, this returns null.
   *
   * @return The active IUsbConfiguration, or null.
   */
  public IUsbConfiguration getActiveUsbConfiguration();

  /**
   * If this IUsbDevice is configured.
   * <p>
   * This returns true if the device is in the configured state as shown in the
   * USB 1.1 specification table 9.1.
   *
   * @return If this is in the Configured state.
   */
  public boolean isConfigured();

  /**
   * Get the device descriptor.
   * <p>
   * The descriptor may be cached.
   *
   * @return The device descriptor.
   */
  public IUsbDeviceDescriptor getUsbDeviceDescriptor();

  /**
   * Get the specified string descriptor.
   * <p>
   * This is a convienence method. The IUsbStringDescriptor may be cached. If
   * the device does not support strings or does not define the specified string
   * descriptor, this returns null.
   *
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
   *
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
   *
   * @param irp The IUsbControlIrp.
   * @exception UsbException             If an error occurs.
   * @throws IllegalArgumentException If the IUsbControlIrp is not valid.
   * @exception UsbDisconnectedException If this device has been disconnected.
   */
  public void syncSubmit(IUsbControlIrp irp) throws UsbException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Submit a IUsbControlIrp asynchronously to the Default Control Pipe.
   *
   * @param irp The IUsbControlIrp.
   * @exception UsbException             If an error occurs.
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
   *
   * @param list The List of IUsbControlIrps.
   * @exception UsbException             If an error occurs.
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
   *
   * @param list The List of IUsbControlIrps.
   * @exception UsbException             If an error occurs.
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
   * The data field is initialized to an empty array.. The {@code wLength} field
   * is automatically calculated by the implementation. The {@code timeout}
   * timeout (in millseconds) value that this function should wait before giving
   * up due to no response being received should be set to a default value.
   *
   * @param bmRequestType The bmRequestType field for the setup packet
   * @param bRequest      The bRequest field for the setup packet
   * @param wValue        The wValue field for the setup packet
   * @param wIndex        The wIndex field for the setup packet
   * @return A IUsbControlIrp ready for use.
   */
  public IUsbControlIrp createUsbControlIrp(byte bmRequestType,
                                            byte bRequest,
                                            short wValue,
                                            short wIndex);

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
   * The {@code wLength} field must be automatically calculated by the
   * implementation. The {@code timeout} timeout (in millseconds) value that
   * this function should wait before giving up due to no response being
   * received should be set to a default value.
   *
   * @param bmRequestType The bmRequestType field for the setup packet
   * @param bRequest      The bRequest field for the setup packet
   * @param wValue        The wValue field for the setup packet
   * @param wIndex        The wIndex field for the setup packet
   * @param data          a suitably-sized data buffer for either input or
   *                      output (depending on direction bits within
   *                      bmRequestType)
   * @return A IUsbControlIrp ready for use.
   */
  public IUsbControlIrp createUsbControlIrp(byte bmRequestType,
                                            byte bRequest,
                                            short wValue,
                                            short wIndex,
                                            byte[] data);

  /**
   * Add a IIUsbDeviceListener to this IUsbDevice.
   *
   * @param listener The IIUsbDeviceListener to add.
   */
  public void addUsbDeviceListener(IUsbDeviceListener listener);

  /**
   * Remove a IIUsbDeviceListener from this IUsbDevice.
   *
   * @param listener The listener to remove.
   */
  public void removeUsbDeviceListener(IUsbDeviceListener listener);

}
