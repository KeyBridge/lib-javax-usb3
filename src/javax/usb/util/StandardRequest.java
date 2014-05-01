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
package javax.usb.util;

import javax.usb.*;
import javax.usb.exception.UsbException;

/**
 * Utility to easily allow Standard Device Requests.
 * <p>
 * These methods are defined in the USB 1.1 specification section 9.4. The table
 * 9.4 lists all Standard Requests.
 * <p>
 * Many of these methods have parameters whose type is short, but should never
 * be passed more than a byte; e.g. all the interface number and endpoint
 * address parameters have a type of short, but interface numbers and endpoint
 * addresses are byte-sized.
 * <p>
 * @author Dan Streetman
 */
public class StandardRequest {

  protected IUsbDevice usbDevice = null;

  protected static final byte REQUESTTYPE_CLEAR_FEATURE = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD;
  protected static final byte REQUESTTYPE_GET_CONFIGURATION = IUsbConst.REQUESTTYPE_DIRECTION_IN | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
  protected static final byte REQUESTTYPE_GET_DESCRIPTOR = IUsbConst.REQUESTTYPE_DIRECTION_IN | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
  protected static final byte REQUESTTYPE_GET_INTERFACE = IUsbConst.REQUESTTYPE_DIRECTION_IN | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;
  protected static final byte REQUESTTYPE_GET_STATUS = IUsbConst.REQUESTTYPE_DIRECTION_IN | IUsbConst.REQUESTTYPE_TYPE_STANDARD;
  @SuppressWarnings("PointlessBitwiseExpression")
  protected static final byte REQUESTTYPE_SET_ADDRESS = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
  @SuppressWarnings("PointlessBitwiseExpression")
  protected static final byte REQUESTTYPE_SET_CONFIGURATION = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
  @SuppressWarnings("PointlessBitwiseExpression")
  protected static final byte REQUESTTYPE_SET_DESCRIPTOR = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
  protected static final byte REQUESTTYPE_SET_FEATURE = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD;
  @SuppressWarnings("PointlessBitwiseExpression")
  protected static final byte REQUESTTYPE_SET_INTERFACE = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;
  protected static final byte REQUESTTYPE_SYNCH_FRAME = IUsbConst.REQUESTTYPE_DIRECTION_IN | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;

  /**
   * Constructor.
   * <p>
   * The specified IUsbDevice will be used on the instance methods which do not
   * include a IUsbDevice parameter. The class methods which do include a
   * IUsbDevice parameter will use that IUsbDevice.
   * <p>
   * @param usbDevice The IUsbDevice to use.
   */
  public StandardRequest(IUsbDevice usbDevice) {
    this.usbDevice = usbDevice;
  }

  //**************************************************************************
  // Instance methods/fields
  /**
   * Clear Feature.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @param recipient       The recipient.
   * @param featureSelector The Feature Selector.
   * @param target          The target interface number or endpoint address.
   * @exception UsbException             If unsuccessful.
   * @exception IllegalArgumentException If the recipient or target is invalid.
   */
  public void clearFeature(byte recipient, short featureSelector, short target) throws UsbException, IllegalArgumentException {
    clearFeature(usbDevice, recipient, featureSelector, target);
  }

  /**
   * Get Configuration.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @return The configuration number.
   * @exception UsbException If unsuccessful.
   */
  public byte getConfiguration() throws UsbException {
    return getConfiguration(usbDevice);
  }

  /**
   * Get Descriptor.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @param type   The Descriptor Type.
   * @param index  The Descriptor Index.
   * @param langid The String Descriptor Language ID.
   * @param data   The data to fill with the Descriptor.
   * @return The actual length of transferred data.
   * @exception UsbException If unsuccessful.
   */
  public int getDescriptor(byte type, byte index, short langid, byte[] data) throws UsbException {
    return getDescriptor(usbDevice, type, index, langid, data);
  }

  /**
   * Get Interface.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @param interfaceNumber The interface number.
   * @return The active alternate setting for the specified interface.
   * @exception UsbException If unsuccessful.
   */
  public byte getInterface(short interfaceNumber) throws UsbException {
    return getInterface(usbDevice, interfaceNumber);
  }

  /**
   * Get Status.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @param recipient The recipient.
   * @param target    The target interface number or endpoint address.
   * @return The status of the specified recipient.
   * @exception UsbException             If unsuccessful.
   * @exception IllegalArgumentException If the recipient or target is invalid.
   */
  public short getStatus(byte recipient, short target) throws UsbException, IllegalArgumentException {
    return getStatus(usbDevice, recipient, target);
  }

  /**
   * Set Address.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @param deviceAddress The new device address.
   * @exception UsbException If unsuccessful.
   */
  public void setAddress(short deviceAddress) throws UsbException {
    setAddress(usbDevice, deviceAddress);
  }

  /**
   * Set Configuration.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @param configurationValue The new configuration value.
   * @exception UsbException If unsuccessful.
   */
  public void setConfiguration(short configurationValue) throws UsbException {
    setConfiguration(usbDevice, configurationValue);
  }

  /**
   * Set Descriptor.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @param type   The Descriptor Type.
   * @param index  The Descriptor Index.
   * @param langid The String Descriptor Language ID.
   * @param data   The Descriptor.
   * @return The actual length of transferred data.
   * @exception UsbException If unsuccessful.
   */
  public int setDescriptor(byte type, byte index, short langid, byte[] data) throws UsbException {
    return setDescriptor(usbDevice, type, index, langid, data);
  }

  /**
   * Set Feature.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @param recipient       The recipient.
   * @param featureSelector The Feature Selector.
   * @param target          The target interface number or endpoint address.
   * @exception UsbException             If unsuccessful.
   * @exception IllegalArgumentException If the recipient or target is invalid.
   */
  public void setFeature(byte recipient, short featureSelector, short target) throws UsbException, IllegalArgumentException {
    setFeature(usbDevice, recipient, featureSelector, target);
  }

  /**
   * Set Interface.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @param interfaceNumber  The interface number.
   * @param alternateSetting The alternate setting number.
   * @exception UsbException If unsuccessful.
   */
  public void setInterface(short interfaceNumber, short alternateSetting) throws UsbException {
    setInterface(usbDevice, interfaceNumber, alternateSetting);
  }

  /**
   * Synch Frame.
   * <p>
   * This only calls the corresponding class method using the IUsbDevice
   * specified in the constructor.
   * <p>
   * @param endpointAddress The endpoint address.
   * @return The frame number.
   * @exception UsbException If unsuccessful.
   */
  public short synchFrame(short endpointAddress) throws UsbException {
    return synchFrame(usbDevice, endpointAddress);
  }

  //**************************************************************************
  // Class methods/fields
  /**
   * Clear Feature.
   * <p>
   * Valid recipients are {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_DEVICE device},
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_INTERFACE interface}, and
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_ENDPOINT endpoint}. If the
   * recipient is device, the target must be 0. If the recipient is interface or
   * endpoint, the target is the interface number or endpoint address,
   * respectively.
   * <p>
   * @param usbDevice       The IUsbDevice.
   * @param recipient       The recipient.
   * @param featureSelector The Feature Selector.
   * @param target          The target interface number or endpoint address.
   * @exception UsbException             If unsuccessful.
   * @exception IllegalArgumentException If the recipient or target is invalid.
   */
  public static void clearFeature(IUsbDevice usbDevice, byte recipient, short featureSelector, short target) throws UsbException, IllegalArgumentException {
    checkRecipient(recipient);
    if (IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE == recipient && 0 != target) {
      throw new IllegalArgumentException("If the recipient is device, the target must be 0");
    }

    byte bmRequestType = (byte) (REQUESTTYPE_CLEAR_FEATURE | recipient);
    byte bRequest = IUsbConst.REQUEST_CLEAR_FEATURE;
    short wValue = featureSelector;
    short wIndex = target;

    usbDevice.syncSubmit(usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex));
  }

  /**
   * Get Configuration.
   * <p>
   * @param usbDevice The IUsbDevice.
   * @return The configuration number.
   * @exception UsbException If unsuccessful.
   */
  public static byte getConfiguration(IUsbDevice usbDevice) throws UsbException {
    byte bmRequestType = REQUESTTYPE_GET_CONFIGURATION;
    byte bRequest = IUsbConst.REQUEST_GET_CONFIGURATION;
    short wValue = 0;
    short wIndex = 0;
    byte[] data = new byte[1];

    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
    usbControlIrp.setData(data);
    usbControlIrp.setAcceptShortPacket(false);

    usbDevice.syncSubmit(usbControlIrp);

    return data[0];
  }

  /**
   * Get Descriptor.
   * <p>
   * The parameters correspond to the setup packet:
   * <ul>
   * <li>type is the wValue MSB</li>
   * <li>index is the wValue LSB</li>
   * <li>langid is the wIndex</li>
   * <li>data.length is the wLength</li>
   * </ul>
   * <p>
   * This method does not restrict the Descriptor type, but the device is only
   * required to support those Descriptors defined in the USB 1.1 specification
   * table 9.5, which includes {@link javax.usb.UsbConst#DESCRIPTOR_TYPE_DEVICE device},
   * {@link javax.usb.UsbConst#DESCRIPTOR_TYPE_CONFIGURATION configuration}, and
   * {@link javax.usb.UsbConst#DESCRIPTOR_TYPE_STRING string} descriptor types.
   * Note that devices normally do not support requests for interface or
   * endpoint descriptors; the configuration descriptor contains all its
   * interface and endpoint descriptors.
   * <p>
   * If the type is string or configuration, the index is used to select the
   * specific descriptor; for other descriptor types is should be 0 (this
   * implementation does not enforce this).
   * <p>
   * If the type is string, the langid indicates what language to use. For other
   * types it should be 0 (but this is not enforced).
   * <p>
   * The data is filled with the actual descriptor.
   * <p>
   * @param usbDevice The IUsbDevice.
   * @param type      The Descriptor Type.
   * @param index     The Descriptor Index.
   * @param langid    The String Descriptor Language ID.
   * @param data      The data to fill with the Descriptor.
   * @return The actual length of transferred data.
   * @exception UsbException If unsuccessful.
   */
  public static int getDescriptor(IUsbDevice usbDevice, byte type, byte index, short langid, byte[] data) throws UsbException {
    byte bmRequestType = REQUESTTYPE_GET_DESCRIPTOR;
    byte bRequest = IUsbConst.REQUEST_GET_DESCRIPTOR;
    short wValue = (short) ((type << 8) | ((int) index) & 0xff);
    short wIndex = langid;

    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
    usbControlIrp.setData(data);

    usbDevice.syncSubmit(usbControlIrp);

    return usbControlIrp.getActualLength();
  }

  /**
   * Get Interface.
   * <p>
   * @param usbDevice       The IUsbDevice.
   * @param interfaceNumber The interface number.
   * @return The active alternate setting for the specified interface.
   * @exception UsbException If unsuccessful.
   */
  public static byte getInterface(IUsbDevice usbDevice, short interfaceNumber) throws UsbException {
    byte bmRequestType = REQUESTTYPE_GET_INTERFACE;
    byte bRequest = IUsbConst.REQUEST_GET_INTERFACE;
    short wValue = 0;
    short wIndex = interfaceNumber;
    byte[] data = new byte[1];

    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
    usbControlIrp.setData(data);
    usbControlIrp.setAcceptShortPacket(false);

    usbDevice.syncSubmit(usbControlIrp);

    return data[0];
  }

  /**
   * Get Status.
   * <p>
   * Valid recipients are {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_DEVICE device},
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_INTERFACE interface}, and
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_ENDPOINT endpoint}. If the
   * recipient is device, the target must be 0. If the recipient is interface or
   * endpoint, the target is the interface number or endpoint address,
   * respectively.
   * <p>
   * @param usbDevice The IUsbDevice.
   * @param recipient The recipient.
   * @param target    The target interface number or endpoint address.
   * @return The status of the specified recipient.
   * @exception UsbException             If unsuccessful.
   * @exception IllegalArgumentException If the recipient or target are invalid.
   */
  public static short getStatus(IUsbDevice usbDevice, byte recipient, short target) throws UsbException, IllegalArgumentException {
    checkRecipient(recipient);
    if (IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE == recipient && 0 != target) {
      throw new IllegalArgumentException("If the recipient is device, the target must be 0");
    }

    byte bmRequestType = (byte) (REQUESTTYPE_GET_STATUS | recipient);
    byte bRequest = IUsbConst.REQUEST_GET_STATUS;
    short wValue = 0;
    short wIndex = target;
    byte[] data = new byte[2];

    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
    usbControlIrp.setData(data);
    usbControlIrp.setAcceptShortPacket(false);

    usbDevice.syncSubmit(usbControlIrp);

    return (short) ((data[1] << 8) | (data[0] & 0xff));
  }

  /**
   * Set Address.
   * <p>
   * Since this is normally only used by the low-level Host Controller Driver,
   * this quite likely will either fail or cause serious problems. This should
   * not be used unless you know what you are doing and know that the Operating
   * System's Host Controller Driver can handle this.
   * <p>
   * @param usbDevice     The IUsbDevice.
   * @param deviceAddress The new device address.
   * @exception UsbException If unsuccessful.
   */
  public static void setAddress(IUsbDevice usbDevice, short deviceAddress) throws UsbException {
    byte bmRequestType = REQUESTTYPE_SET_ADDRESS;
    byte bRequest = IUsbConst.REQUEST_SET_ADDRESS;
    short wValue = deviceAddress;
    short wIndex = 0;

    usbDevice.syncSubmit(usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex));
  }

  /**
   * Set Configuration.
   * <p>
   * @param usbDevice          The IUsbDevice.
   * @param configurationValue The new configuration value.
   * @exception UsbException If unsuccessful.
   */
  public static void setConfiguration(IUsbDevice usbDevice, short configurationValue) throws UsbException {
    byte bmRequestType = REQUESTTYPE_SET_CONFIGURATION;
    byte bRequest = IUsbConst.REQUEST_SET_CONFIGURATION;
    short wValue = configurationValue;
    short wIndex = 0;

    usbDevice.syncSubmit(usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex));
  }

  /**
   * Set Descriptor.
   * <p>
   * The parameters correspond to the setup packet:
   * <ul>
   * <li>type is the wValue MSB</li>
   * <li>index is the wValue LSB</li>
   * <li>langid is the wIndex</li>
   * <li>data.length is the wLength</li>
   * </ul>
   * <p>
   * This method does not restrict the Descriptor type, but the device is only
   * required to support those Descriptors defined in the USB 1.1 specification
   * table 9.5, which includes {@link javax.usb.UsbConst#DESCRIPTOR_TYPE_DEVICE device},
   * {@link javax.usb.UsbConst#DESCRIPTOR_TYPE_CONFIGURATION configuration}, and
   * {@link javax.usb.UsbConst#DESCRIPTOR_TYPE_STRING string} descriptor types.
   * Note that devices normally do not support requests for interface or
   * endpoint descriptors; the configuration descriptor contains all its
   * interface and endpoint descriptors.
   * <p>
   * If the type is string or configuration, the index is used to select the
   * specific descriptor; for other descriptor types is should be 0 (this
   * implementation does not enforce this).
   * <p>
   * If the type is string, the langid indicates what language to use. For other
   * types it should be 0 (but this is not enforced).
   * <p>
   * The data should contain the actual Descriptor. The entire length is used,
   * i.e. wLength is set to data.length.
   * <p>
   * @param usbDevice The IUsbDevice.
   * @param type      The Descriptor Type.
   * @param index     The Descriptor Index.
   * @param langid    The String Descriptor Language ID.
   * @param data      The Descriptor.
   * @return The actual length of transferred data.
   * @exception UsbException If unsuccessful.
   */
  public static int setDescriptor(IUsbDevice usbDevice, byte type, byte index, short langid, byte[] data) throws UsbException {
    byte bmRequestType = REQUESTTYPE_SET_DESCRIPTOR;
    byte bRequest = IUsbConst.REQUEST_SET_DESCRIPTOR;
    short wValue = (short) ((type << 8) | (index & 0xff));
    short wIndex = langid;

    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
    usbControlIrp.setData(data);

    usbDevice.syncSubmit(usbControlIrp);

    return usbControlIrp.getActualLength();
  }

  /**
   * Set Feature.
   * <p>
   * Valid recipients are {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_DEVICE device},
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_INTERFACE interface}, and
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_ENDPOINT endpoint}. If the
   * recipient is device, the target is either 0 or the Test Selector. If the
   * recipient is interface or endpoint, the target is the interface number or
   * endpoint address, respectively.
   * <p>
   * @param usbDevice       The IUsbDevice.
   * @param recipient       The recipient.
   * @param featureSelector The Feature Selector.
   * @param target          The target interface number or endpoint address.
   * @exception UsbException             If unsuccessful.
   * @exception IllegalArgumentException If the recipient or target is invalid.
   */
  public static void setFeature(IUsbDevice usbDevice, byte recipient, short featureSelector, short target) throws UsbException, IllegalArgumentException {
    checkRecipient(recipient);

    byte bmRequestType = (byte) (REQUESTTYPE_SET_FEATURE | recipient);
    byte bRequest = IUsbConst.REQUEST_SET_FEATURE;
    short wValue = featureSelector;
    short wIndex = target;

    usbDevice.syncSubmit(usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex));
  }

  /**
   * Set Interface.
   * <p>
   * @param usbDevice        The IUsbDevice.
   * @param interfaceNumber  The interface number.
   * @param alternateSetting The alternate setting number.
   * @exception UsbException If unsuccessful.
   */
  public static void setInterface(IUsbDevice usbDevice, short interfaceNumber, short alternateSetting) throws UsbException {
    byte bmRequestType = REQUESTTYPE_SET_INTERFACE;
    byte bRequest = IUsbConst.REQUEST_SET_INTERFACE;
    short wValue = alternateSetting;
    short wIndex = interfaceNumber;

    usbDevice.syncSubmit(usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex));
  }

  /**
   * Synch Frame.
   * <p>
   * @param usbDevice       The IUsbDevice.
   * @param endpointAddress The endpoint address.
   * @return The frame number.
   * @exception UsbException If unsuccessful.
   */
  public static short synchFrame(IUsbDevice usbDevice, short endpointAddress) throws UsbException {
    byte bmRequestType = REQUESTTYPE_SYNCH_FRAME;
    byte bRequest = IUsbConst.REQUEST_SYNCH_FRAME;
    short wValue = 0;
    short wIndex = endpointAddress;
    byte[] data = new byte[2];

    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
    usbControlIrp.setData(data);
    usbControlIrp.setAcceptShortPacket(false);

    usbDevice.syncSubmit(usbControlIrp);

    return (short) ((data[1] << 8) | (data[0] & 0xff));
  }

  //**************************************************************************
  // Protected methods
  /**
   * Check the recipient.
   * <p>
   * Valid recipients are {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_DEVICE device},
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_INTERFACE interface}, and
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_ENDPOINT endpoint}.
   * <p>
   * @param recipient The recipient.
   * @exception IllegalArgumentException If the recipient is not valid.
   */
  protected static void checkRecipient(byte recipient) throws IllegalArgumentException {
    switch (recipient) {
      case IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE:
      case IUsbConst.REQUESTTYPE_RECIPIENT_INTERFACE:
      case IUsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT:
        break;
      default:
        throw new IllegalArgumentException("Recipient must be device (0x00), interface (0x01), or endpoint (0x02)");
    }
  }
}
