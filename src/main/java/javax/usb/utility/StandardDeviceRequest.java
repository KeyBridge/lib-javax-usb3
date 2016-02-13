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
package javax.usb.utility;

import javax.usb.IUsbControlIrp;
import javax.usb.IUsbDevice;
import javax.usb.enumerated.EDescriptorType;
import javax.usb.enumerated.EDeviceRequest;
import javax.usb.enumerated.EEndpointDirection;
import javax.usb.enumerated.EFeatureSelector;
import javax.usb.exception.UsbException;
import javax.usb.request.BMRequestType;
import javax.usb.request.BMRequestType.ERecipient;
import javax.usb.request.BRequest;

/**
 * Utility to simplify the creation and exchange of Standard Device Requests
 * with USB Devices.
 * <p>
 * These methods are defined in Table 9-4 of the USB 3.1 specification 9.4.
 * <p>
 * Many of these methods have parameters whose type is short, but should never
 * be passed more than a byte; e.g. all the interface number and endpoint
 * address parameters have a type of short, but interface numbers and endpoint
 * addresses are byte-sized.
 *
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
public class StandardDeviceRequest {

//  protected IUsbDevice usbDevice = null;
//  protected static final byte REQUESTTYPE_CLEAR_FEATURE = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD;
//  protected static final byte REQUESTTYPE_GET_CONFIGURATION = IUsbConst.REQUESTTYPE_DIRECTION_IN | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
//  protected static final byte REQUESTTYPE_GET_DESCRIPTOR = IUsbConst.REQUESTTYPE_DIRECTION_IN | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
//  protected static final byte REQUESTTYPE_GET_INTERFACE = IUsbConst.REQUESTTYPE_DIRECTION_IN | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;
//  protected static final byte REQUESTTYPE_GET_STATUS = IUsbConst.REQUESTTYPE_DIRECTION_IN | IUsbConst.REQUESTTYPE_TYPE_STANDARD;
//  protected static final byte REQUESTTYPE_SET_ADDRESS = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
//  protected static final byte REQUESTTYPE_SET_CONFIGURATION = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
//  protected static final byte REQUESTTYPE_SET_DESCRIPTOR = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_DEVICE;
//  protected static final byte REQUESTTYPE_SET_FEATURE = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD;
//  protected static final byte REQUESTTYPE_SET_INTERFACE = IUsbConst.REQUESTTYPE_DIRECTION_OUT | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_INTERFACE;
//  protected static final byte REQUESTTYPE_SYNCH_FRAME = IUsbConst.REQUESTTYPE_DIRECTION_IN | IUsbConst.REQUESTTYPE_TYPE_STANDARD | IUsbConst.REQUESTTYPE_RECIPIENT_ENDPOINT;
  /**
   * Private Constructor.
   * <p>
   * The specified IUsbDevice will be used on the instance methods which do not
   * include a IUsbDevice parameter. The class methods which do include a
   * IUsbDevice parameter will use that IUsbDevice.
   *
   * @param usbDevice The IUsbDevice to use.
   */
  private StandardDeviceRequest() {
  }

  /**
   * 9.4.1 Clear Feature
   * <p>
   * This request is used to clear or disable a specific feature.
   * <p>
   * Feature selector values in wValue shall be appropriate to the recipient.
   * Only device feature selector values may be used when the recipient is a
   * device, only interface feature selector values may be used when the
   * recipient is an interface, and only endpoint feature selector values may be
   * used when the recipient is an endpoint.
   * <p>
   * A ClearFeature() request that references a feature that cannot be cleared,
   * that does not exist, or that references an interface or an endpoint that
   * does not exist, will cause the device to respond with a Request Error.
   * <p>
   * <p>
   * Valid recipients are {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_DEVICE device},
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_INTERFACE interface}, and
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_ENDPOINT endpoint}. If the
   * recipient is device, the target must be 0. If the recipient is interface or
   * endpoint, the target is the interface number or endpoint address,
   * respectively.
   *
   * @param usbDevice       The IUsbDevice instance.
   * @param recipient       The recipient [DEVICE, INTERFACE, ENDPOINT].
   *                        Requests may be directed to a device, an interface
   *                        on the device, or to a specific endpoint on a
   *                        device.
   * @param featureSelector The Feature Selector.
   * @param wIndex          The target interface number or endpoint address.
   * @exception UsbException             If unsuccessful.
   * @exception IllegalArgumentException If the recipient or target is invalid.
   */
  public static void clearFeature(IUsbDevice usbDevice, ERecipient recipient, EFeatureSelector featureSelector, short wIndex) throws UsbException, IllegalArgumentException {
    checkRecipient(recipient);
    if (ERecipient.DEVICE.equals(recipient) && 0 != wIndex) {
      throw new IllegalArgumentException("If the recipient is device, the target must be 0");
    }
//    byte bmRequestType = (byte) (REQUESTTYPE_CLEAR_FEATURE | recipient);
//    byte bRequest = IUsbConst.REQUEST_CLEAR_FEATURE;
//    short wValue = featureSelector;
//    short wIndex = wIndex;
    usbDevice.syncSubmit(usbDevice.createUsbControlIrp(new BMRequestType(EEndpointDirection.HOST_TO_DEVICE, BMRequestType.EType.STANDARD, recipient).getByteCode(),
                                                       BRequest.getInstance(EDeviceRequest.CLEAR_FEATURE),
                                                       featureSelector.asByte(),
                                                       wIndex));
  }

  /**
   * 9.4.2 Get Configuration.
   * <p>
   * This request returns the current device configuration value.
   *
   * @param usbDevice The IUsbDevice.
   * @return The configuration number. If the returned value is zero, the device
   *         is not configured.
   * @exception UsbException If unsuccessful.
   */
  public static byte getConfiguration(IUsbDevice usbDevice) throws UsbException {
    short wValue = 0;
    short wIndex = 0;
    byte[] data = new byte[1];
    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(BMRequestType.getInstanceStandardRead(),
                                                                 BRequest.getInstance(EDeviceRequest.GET_CONFIGURATION),
                                                                 wValue,
                                                                 wIndex);
    usbControlIrp.setData(data);
    usbControlIrp.setAcceptShortPacket(false);
    usbDevice.syncSubmit(usbControlIrp);
    return data[0];
  }

  /**
   * 9.4.3 Get Descriptor
   * <p>
   * This request returns the specified descriptor if the descriptor exists. The
   * standard request to a device supports four types of descriptors: device,
   * configuration, BOS (Binary device Object Store), and string. A request for
   * a configuration descriptor returns the configuration descriptor, all
   * interface descriptors, endpoint descriptors and endpoint companion
   * descriptors (when operating at Gen X speed) for all of the interfaces in a
   * single request.
   * <p>
   * The first interface descriptor follows the configuration descriptor. The
   * endpoint descriptors for the first interface follow the first interface
   * descriptor. In addition, Enhanced SuperSpeed devices shall return Endpoint
   * Companion descriptors for each of the endpoints in that interface to return
   * the endpoint capabilities required for Enhanced SuperSpeed devices, which
   * would not fit inside the existing endpoint descriptor footprint. If there
   * are additional interfaces, their interface descriptor, endpoint
   * descriptors, and endpoint companion descriptors (when operating at Gen X
   * speed) follow the first interface’s endpoint and endpoint companion (when
   * operating at Gen X speed) descriptors.
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
   *
   * @param usbDevice The IUsbDevice.
   * @param type      The Descriptor Type.
   * @param index     The Descriptor Index.
   * @param langid    Zero or The String Descriptor Language ID.
   * @param data      The data to fill with the Descriptor.
   * @return The actual length of transferred data.
   * @exception UsbException If unsuccessful.
   */
  public static int getDescriptor(IUsbDevice usbDevice, EDescriptorType type, byte index, short langid, byte[] data) throws UsbException {
//    byte bmRequestType = REQUESTTYPE_GET_DESCRIPTOR;
//    byte bRequest = IUsbConst.REQUEST_GET_DESCRIPTOR;
    /**
     * The wValue field specifies the descriptor type in the high byte (refer to
     * Table 9-5) and the descriptor index in the low byte. The descriptor index
     * is used to select a specific descriptor (only for configuration and
     * string descriptors) when several descriptors of the same type are
     * implemented in a device.
     */
//    short wValue = (short) ((type << 8) | ((int) index) & 0xff);
//    short wIndex = langid;
    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(BMRequestType.getInstanceStandardRead(),
                                                                 BRequest.getInstance(EDeviceRequest.GET_DESCRIPTOR),
                                                                 type.getWValue(index), //     wValue,
                                                                 langid);
    usbControlIrp.setData(data);
    usbDevice.syncSubmit(usbControlIrp);
    return usbControlIrp.getActualLength();
  }

  /**
   * 9.4.4 Get Interface
   * <p>
   * This request returns the selected alternate setting for the specified
   * interface.
   * <p>
   * Some devices have configurations with interfaces that have mutually
   * exclusive settings. This request allows the host to determine the currently
   * selected alternate setting.
   *
   * @param usbDevice       The IUsbDevice.
   * @param interfaceNumber The interface number.
   * @return The active alternate setting for the specified interface.
   * @exception UsbException If unsuccessful.
   */
  public static byte getInterface(IUsbDevice usbDevice, short interfaceNumber) throws UsbException {
//    byte bmRequestType = REQUESTTYPE_GET_INTERFACE;
//    byte bRequest = IUsbConst.REQUEST_GET_INTERFACE;
    short wValue = 0;
    short wIndex = interfaceNumber;
    byte[] data = new byte[1];

    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(BMRequestType.getInstanceStandardRead(ERecipient.INTERFACE),
                                                                 BRequest.getInstance(EDeviceRequest.GET_INTERFACE),
                                                                 wValue,
                                                                 wIndex);
    usbControlIrp.setData(data);
    usbControlIrp.setAcceptShortPacket(false);
    usbDevice.syncSubmit(usbControlIrp);
    return data[0];
  }

  /**
   * 9.4.5 Get Status
   * <p>
   * This request returns status for the specified recipient.
   * <p>
   * The data returned is the current status of the specified recipient.
   * <p>
   * See Figure 9-4. Information Returned by a Standard GetStatus() Request to a
   * Device for returned coding details.
   * <p>
   * Valid recipients are {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_DEVICE device},
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_INTERFACE interface}, and
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_ENDPOINT endpoint}. If the
   * recipient is device, the target must be 0. If the recipient is interface or
   * endpoint, the target is the interface number or endpoint address,
   * respectively.
   *
   * @param usbDevice The IUsbDevice.
   * @param recipient The recipient. Requests may be directed to a device, an
   *                  interface on the device, or to a specific endpoint on a
   *                  device.
   * @param target    The target interface number or endpoint address.
   * @return The status of the specified recipient.
   * @exception UsbException             If unsuccessful.
   * @exception IllegalArgumentException If the recipient or target are invalid.
   */
  public static short getStatus(IUsbDevice usbDevice, ERecipient recipient, short target) throws UsbException, IllegalArgumentException {
    checkRecipient(recipient);
    if (ERecipient.DEVICE.equals(recipient) && 0 != target) {
      throw new IllegalArgumentException("If the recipient is device, the target must be 0");
    }

//    byte bmRequestType = (byte) (REQUESTTYPE_GET_STATUS | recipient);
//    byte bRequest = IUsbConst.REQUEST_GET_STATUS;
    short wValue = 0;
    short wIndex = target;
    byte[] data = new byte[2];

    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(BMRequestType.getInstanceStandardRead(recipient),
                                                                 BRequest.getInstance(EDeviceRequest.GET_STATUS),
                                                                 wValue,
                                                                 wIndex);
    usbControlIrp.setData(data);
    usbControlIrp.setAcceptShortPacket(false);

    usbDevice.syncSubmit(usbControlIrp);

    return (short) ((data[1] << 8) | (data[0] & 0xff));
  }

  /**
   * 9.4.6 Set Address
   * <p>
   * This request sets the device address for all future device accesses.
   * <p>
   * The Status stage after the initial Setup packet assumes the same device
   * address as the Setup packet. The device does not change its device address
   * until after the Status stage of this request is completed successfully.
   * Note that this is a difference between this request and all other requests.
   * For all other requests, the operation indicated shall be completed before
   * the Status stage.
   * <p>
   * If the specified device address is greater than 127, or if wIndex or
   * wLength is non-zero, then the behavior of the device is not specified.
   * <p>
   * Developer note: Since this is normally only used by the low-level Host
   * Controller Driver, this quite likely will either fail or cause serious
   * problems. This should not be used unless you know what you are doing and
   * know that the Operating System's Host Controller Driver can handle this.
   *
   * @param usbDevice     The IUsbDevice.
   * @param deviceAddress The new device address.
   * @exception UsbException If unsuccessful.
   */
  public static void setAddress(IUsbDevice usbDevice, short deviceAddress) throws UsbException {
//    byte bmRequestType = REQUESTTYPE_SET_ADDRESS;
//    byte bRequest = IUsbConst.REQUEST_SET_ADDRESS;
    short wValue = deviceAddress;
    short wIndex = 0;

    usbDevice.syncSubmit(usbDevice.createUsbControlIrp(BMRequestType.getInstanceStandardWrite(),
                                                       BRequest.getInstance(EDeviceRequest.SET_ADDRESS),
                                                       wValue,
                                                       wIndex));
  }

  /**
   * 9.4.7 Set Configuration
   * <p>
   * This request sets the device configuration.
   * <p>
   * If the configuration value is zero, the device is placed in its Address
   * state. If the specified configuration value matches the configuration value
   * from a configuration descriptor, then that configuration is selected and
   * the device remains in the Configured state. Otherwise, the device responds
   * with a Request Error.
   *
   * @param usbDevice          The IUsbDevice.
   * @param configurationValue The new configuration value.
   * @exception UsbException If unsuccessful.
   */
  public static void setConfiguration(IUsbDevice usbDevice, short configurationValue) throws UsbException {
//    byte bmRequestType = REQUESTTYPE_SET_CONFIGURATION;
//    byte bRequest = IUsbConst.REQUEST_SET_CONFIGURATION;
    short wValue = configurationValue;
    short wIndex = 0;

    usbDevice.syncSubmit(usbDevice.createUsbControlIrp(BMRequestType.getInstanceStandardWrite(),
                                                       BRequest.getInstance(EDeviceRequest.SET_CONFIGURATION),
                                                       wValue,
                                                       wIndex));
  }

  /**
   * 9.4.8 Set Descriptor.
   * <p>
   * This request is optional and may be used to update existing descriptors or
   * new descriptors may be added.
   * <p>
   * The descriptor index is used to select a specific descriptor (only for
   * configuration and string descriptors) when several descriptors of the same
   * type are implemented in a device.
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
   *
   * @param usbDevice The IUsbDevice.
   * @param type      The Descriptor Type.
   * @param index     The Descriptor Index.
   * @param langid    The String Descriptor Language ID.
   * @param data      The Descriptor.
   * @return The actual length of transferred data.
   * @exception UsbException If unsuccessful.
   */
  public static int setDescriptor(IUsbDevice usbDevice, EDescriptorType type, byte index, short langid, byte[] data) throws UsbException {
//    byte bmRequestType = REQUESTTYPE_SET_DESCRIPTOR;
//    byte bRequest = IUsbConst.REQUEST_SET_DESCRIPTOR;
//    short wValue = (short) ((type << 8) | (index & 0xff));
    short wIndex = langid;

    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(BMRequestType.getInstanceStandardWrite(),
                                                                 BRequest.getInstance(EDeviceRequest.SET_DESCRIPTOR),
                                                                 type.getWValue(index),
                                                                 wIndex);
    usbControlIrp.setData(data);

    usbDevice.syncSubmit(usbControlIrp);

    return usbControlIrp.getActualLength();
  }

  /**
   * 9.4.9 Set Feature
   * <p>
   * This request is used to set or enable a specific feature.
   * <p>
   * Only device feature selector values may be used when the recipient is a
   * device; only interface feature selector values may be used when the
   * recipient is an interface; and only endpoint feature selector values may be
   * used when the recipient is an endpoint.
   * <p>
   * <p>
   * Valid recipients are {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_DEVICE device},
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_INTERFACE interface}, and
   * {@link javax.usb.UsbConst#REQUESTTYPE_RECIPIENT_ENDPOINT endpoint}. If the
   * recipient is device, the target is either 0 or the Test Selector. If the
   * recipient is interface or endpoint, the target is the interface number or
   * endpoint address, respectively.
   *
   * @param usbDevice       The IUsbDevice.
   * @param recipient       The recipient. Requests may be directed to a device,
   *                        an interface on the device, or to a specific
   *                        endpoint on a device.
   * @param featureSelector The Feature Selector.
   * @param target          The target interface number or endpoint address.
   * @exception UsbException             If unsuccessful.
   * @exception IllegalArgumentException If the recipient or target is invalid.
   */
  public static void setFeature(IUsbDevice usbDevice, ERecipient recipient, EFeatureSelector featureSelector, short target) throws UsbException, IllegalArgumentException {
    checkRecipient(recipient);

//    byte bmRequestType = (byte) (REQUESTTYPE_SET_FEATURE | recipient);
//    byte bRequest = IUsbConst.REQUEST_SET_FEATURE;
//    short wValue = featureSelector;
    short wIndex = target;

    usbDevice.syncSubmit(usbDevice.createUsbControlIrp(BMRequestType.getInstanceStandardWrite(recipient),
                                                       BRequest.getInstance(EDeviceRequest.SET_FEATURE),
                                                       featureSelector.asByte(),
                                                       wIndex));
  }

  /**
   * Set Interface.
   *
   * @param usbDevice        The IUsbDevice.
   * @param interfaceNumber  The interface number.
   * @param alternateSetting The alternate setting number.
   * @exception UsbException If unsuccessful.
   */
  public static void setInterface(IUsbDevice usbDevice, short interfaceNumber, short alternateSetting) throws UsbException {
//    byte bmRequestType = REQUESTTYPE_SET_INTERFACE;
//    byte bRequest = IUsbConst.REQUEST_SET_INTERFACE;
    short wValue = alternateSetting;
    short wIndex = interfaceNumber;

    usbDevice.syncSubmit(usbDevice.createUsbControlIrp(BMRequestType.getInstanceStandardWrite(ERecipient.INTERFACE),
                                                       BRequest.getInstance(EDeviceRequest.SET_INTERFACE),
                                                       wValue,
                                                       wIndex));
  }

  /**
   * 9.4.13 Synch Frame.
   * <p>
   * This request is used to set and then report an endpoint’s synchronization
   * frame.
   * <p>
   * When an endpoint supports isochronous transfers, the endpoint may also
   * require per-frame transfers to vary in size according to a specific
   * pattern. The host and the endpoint must agree on which frame the repeating
   * pattern begins. The number of the frame in which the pattern began is
   * returned to the host.
   *
   * @param usbDevice       The IUsbDevice.
   * @param endpointAddress The endpoint address.
   * @return The frame number.
   * @exception UsbException If unsuccessful.
   */
  public static short synchFrame(IUsbDevice usbDevice, short endpointAddress) throws UsbException {
//    byte bmRequestType = REQUESTTYPE_SYNCH_FRAME;
//    byte bRequest = IUsbConst.REQUEST_SYNCH_FRAME;
    short wValue = 0;
    short wIndex = endpointAddress;
    byte[] data = new byte[2];

    IUsbControlIrp usbControlIrp = usbDevice.createUsbControlIrp(BMRequestType.getInstanceStandardRead(ERecipient.ENDPOINT),
                                                                 BRequest.getInstance(EDeviceRequest.SYNCH_FRAME),
                                                                 wValue,
                                                                 wIndex);
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
   *
   * @param recipient The recipient.
   * @exception IllegalArgumentException If the recipient is not valid.
   */
  protected static void checkRecipient(ERecipient recipient) throws IllegalArgumentException {
    switch (recipient) {
      case DEVICE:
      case INTERFACE:
      case ENDPOINT:
        break;
      default:
        throw new IllegalArgumentException("Recipient must be device (0x00), interface (0x01), or endpoint (0x02)");
    }
  }
}
