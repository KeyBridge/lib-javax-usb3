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

import javax.usb.ri.enumerated.EUSBClassCode;

/**
 * Interface for a USB interface descriptor.
 * <p>
 * The interface descriptor describes a specific interface within a
 * configuration. A configuration provides one or more interfaces, each with
 * zero or more endpoint descriptors describing a unique set of endpoints within
 * the configuration. When a configuration supports more than one interface, the
 * endpoint descriptors for a particular interface follow the interface
 * descriptor in the data returned by the GetConfiguration() request. An
 * interface descriptor is always returned as part of a configuration
 * descriptor. Interface descriptors cannot be directly accessed with a
 * GetDescriptor() or SetDescriptor() request.
 * <p>
 * An interface may include alternate settings that allow the endpoints and/or
 * their characteristics to be varied after the device has been configured. The
 * default setting for an interface is always alternate setting zero. The
 * SetInterface() request is used to select an alternate setting or to return to
 * the default setting. The GetInterface() request returns the selected
 * alternate setting.
 * <p>
 * Alternate settings allow a portion of the device configuration to be varied
 * while other interfaces remain in operation. If a configuration has alternate
 * settings for one or more of its interfaces, a separate interface descriptor
 * and its associated endpoints are included for each setting.
 * <p>
 * If a device configuration supported a single interface with two alternate
 * settings, the configuration descriptor would be followed by an interface
 * descriptor with the bInterfaceNumber and bAlternateSetting fields set to zero
 * and then the endpoint descriptors for that setting, followed by another
 * interface descriptor and its associated endpoint descriptors. The second
 * interface descriptor’s bInterfaceNumber field would also be set to zero, but
 * the bAlternateSetting field of the second interface descriptor would be set
 * to one.
 * <p>
 * If an interface uses only endpoint zero, no endpoint descriptors follow the
 * interface descriptor. In this case, the bNumEndpoints field must be set to
 * zero. An interface descriptor never includes endpoint zero in the number of
 * endpoints.
 * <p>
 * See the USB 1.1 specification section 9.6.3.
 * <p>
 * @author Dan Streetman
 */
public interface IUsbInterfaceDescriptor extends IUsbDescriptor {

  /**
   * Number of this interface. Zero-based value identifying the index in the
   * array of concurrent interfaces supported by this configuration.
   * <p>
   * @return This descriptor's bInterfaceNumber.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bInterfaceNumber();

  /**
   * Value used to select this alternate setting for the interface identified in
   * the prior field
   * <p>
   * @return This descriptor's bAlternateSetting.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bAlternateSetting();

  /**
   * Number of endpoints used by this interface (excluding endpoint zero). If
   * this value is zero, this interface only uses the Default Control Pipe.
   * <p>
   * @return This descriptor's bNumEndpoints.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bNumEndpoints();

  /**
   * Class code (assigned by the USB-IF).
   * <p>
   * A value of zero is reserved for future standardization.
   * <p>
   * If this field is set to FFH, the interface class is vendor-specific. All
   * other values are reserved for assignment by the USB-IF.
   * <p>
   * @return This descriptor's bInterfaceClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   * @see EUSBClassCode
   */
  public EUSBClassCode bInterfaceClass();

  /**
   * Subclass code (assigned by the USB-IF). These codes are qualified by the
   * value of the bInterfaceClass field.
   * <p>
   * If the bInterfaceClass field is reset to zero, this field must also be
   * reset to zero. If the bInterfaceClass field is not set to FFH, all values
   * are reserved for assignment by the USB-IF.
   * <p>
   * @return This descriptor's bInterfaceSubClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   * @see EUSBClassCode
   */
  public byte bInterfaceSubClass();

  /**
   * Protocol code (assigned by the USB). These codes are qualified by the value
   * of the bInterfaceClass and the bInterfaceSubClass fields. If an interface
   * supports class-specific requests, this code identifies the protocols that
   * the device uses as defined by the specification of the device class.
   * <p>
   * If this field is reset to zero, the device does not use a class-specific
   * protocol on this interface.
   * <p>
   * If this field is set to FFH, the device uses a vendor-specific protocol for
   * this interface.
   * <p>
   * @return This descriptor's bInterfaceProtocol.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   * @see EUSBClassCode
   */
  public byte bInterfaceProtocol();

  /**
   * Index of string descriptor describing this interface
   * <p>
   * @return This descriptor's iInterface.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte iInterface();
}
