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

import javax.usb.enumerated.EUSBClassCode;

/**
 * Interface for a USB device descriptor.
 * <p>
 * A device descriptor describes general information about a USB device. It
 * includes information that applies globally to the device and all of the
 * device’s configurations. A USB device has only one device descriptor.
 * <p>
 * A high-speed capable device that has different device information for
 * full-speed and high-speed must also have a device_qualifier descriptor.
 * <p>
 * The DEVICE descriptor of a high-speed capable device has a version number of
 * 2.0 (0200H). If the device is full-speed only or low-speed only, this version
 * number indicates that it will respond correctly to a request for the
 * device_qualifier desciptor (i.e., it will respond with a request error).
 * <p>
 * <p>
 * See the USB 1.1 specification section 9.6.1.
 *
 * @author Dan Streetman
 */
public interface IUsbDeviceDescriptor extends IUsbDescriptor {

  /**
   * Get the USB Specification Release Number in Binary-Coded Decimal (i.e.,
   * 2.10 is 210H). This field identifies the release of the USB Specification
   * with which the device and its descriptors are compliant.
   *
   * @return This descriptor's bcdUSB.
   * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
   */
  public short bcdUSB();

  /**
   * Class code (assigned by the USB-IF). If this field is reset to zero, each
   * interface within a configuration specifies its own class information and
   * the various interfaces operate independently.
   * <p>
   * If this field is set to a value between 1 and FEH, the device supports
   * different class specifications on different interfaces and the interfaces
   * may not operate independently. This value identifies the class definition
   * used for the aggregate interfaces.
   * <p>
   * If this field is set to FFH, the device class is vendor-specific.
   *
   * @return This descriptor's bDeviceClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   * @see EUSBClassCode
   */
  public EUSBClassCode bDeviceClass();

  /**
   * Subclass code (assigned by the USB-IF).
   * <p>
   * These codes are qualified by the value of the bDeviceClass field.
   * <p>
   * If the bDeviceClass field is reset to zero, this field must also be reset
   * to zero.
   * <p>
   * A bDeviceClass field value of 0xFF indicates the device SubClass and
   * Protocol fields are vendor specific. If the bDeviceClass field is not set
   * to 0xFF then all values are reserved for assignment by the USB-IF.
   *
   * @return This descriptor's bDeviceSubClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bDeviceSubClass();

  /**
   * Protocol code (assigned by the USB-IF). These codes are qualified by the
   * value of the bDeviceClass and the bDeviceSubClass fields. If a device
   * supports class-specific protocols on a device basis as opposed to an
   * interface basis, this code identifies the protocols that the device uses as
   * defined by the specification of the device class.
   * <p>
   * If this field is reset to zero, the device does not use class-specific
   * protocols on a device basis. However, it may use class- specific protocols
   * on an interface basis.
   * <p>
   * If this field is set to FFH, the device uses a vendor-specific protocol on
   * a device basis.
   *
   * @return This descriptor's bDeviceProtocol.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bDeviceProtocol();

  /**
   * Maximum packet size for endpoint zero (only 8, 16, 32, or 64 are valid)
   *
   * @return This descriptor's bMaxPacketSize.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bMaxPacketSize0();

  /**
   * Vendor ID (assigned by the USB-IF)
   *
   * @return This descriptor's idVendor.
   * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
   */
  public short idVendor();

  /**
   * Product ID (assigned by the manufacturer)
   *
   * @return This descriptor's idProduct.
   * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
   */
  public short idProduct();

  /**
   * Device release number in binary-coded decimal
   *
   * @return This descriptor's bcdDevice.
   * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
   */
  public short bcdDevice();

  /**
   * Index of string descriptor describing manufacturer
   *
   * @return This descriptor's iManufacturer.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte iManufacturer();

  /**
   * Index of string descriptor describing product
   *
   * @return This descriptor's iProduct.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte iProduct();

  /**
   * Index of string descriptor describing the device’s serial number
   *
   * @return This descriptor's iSerialNumber.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte iSerialNumber();

  /**
   * Number of possible configurations
   *
   * @return This descriptor's bNumConfigurations.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bNumConfigurations();
}
