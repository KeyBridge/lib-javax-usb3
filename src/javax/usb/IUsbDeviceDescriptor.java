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

/**
 * Interface for a USB device descriptor.
 * <p>
 * See the USB 1.1 specification section 9.6.1.
 * <p>
 * @author Dan Streetman
 */
public interface IUsbDeviceDescriptor extends IUsbDescriptor {

  /**
   * Get this descriptor's bcdUSB.
   * <p>
   * @return This descriptor's bcdUSB.
   * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
   */
  public short bcdUSB();

  /**
   * Get this descriptor's bDeviceClass.
   * <p>
   * @return This descriptor's bDeviceClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bDeviceClass();

  /**
   * Get this descriptor's bDeviceSubClass.
   * <p>
   * @return This descriptor's bDeviceSubClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bDeviceSubClass();

  /**
   * Get this descriptor's bDeviceProtocol.
   * <p>
   * @return This descriptor's bDeviceProtocol.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bDeviceProtocol();

  /**
   * Get this descriptor's bMaxPacketSize.
   * <p>
   * @return This descriptor's bMaxPacketSize.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bMaxPacketSize0();

  /**
   * Get this descriptor's idVendor.
   * <p>
   * @return This descriptor's idVendor.
   * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
   */
  public short idVendor();

  /**
   * Get this descriptor's idProduct.
   * <p>
   * @return This descriptor's idProduct.
   * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
   */
  public short idProduct();

  /**
   * Get this descriptor's bcdDevice.
   * <p>
   * @return This descriptor's bcdDevice.
   * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
   */
  public short bcdDevice();

  /**
   * Get this descriptor's iManufacturer.
   * <p>
   * @return This descriptor's iManufacturer.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte iManufacturer();

  /**
   * Get this descriptor's iProduct.
   * <p>
   * @return This descriptor's iProduct.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte iProduct();

  /**
   * Get this descriptor's iSerialNumber.
   * <p>
   * @return This descriptor's iSerialNumber.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte iSerialNumber();

  /**
   * Get this descriptor's bNumConfigurations.
   * <p>
   * @return This descriptor's bNumConfigurations.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bNumConfigurations();
}
