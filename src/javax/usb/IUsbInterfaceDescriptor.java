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
 * Interface for a USB interface descriptor.
 * <p>
 * See the USB 1.1 specification section 9.6.3.
 * <p>
 * @author Dan Streetman
 */
public interface IUsbInterfaceDescriptor extends IUsbDescriptor {

  /**
   * Get this descriptor's bInterfaceNumber.
   * <p>
   * @return This descriptor's bInterfaceNumber.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bInterfaceNumber();

  /**
   * Get this descriptor's bAlternateSetting.
   * <p>
   * @return This descriptor's bAlternateSetting.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bAlternateSetting();

  /**
   * Get this descriptor's bNumEndpoints.
   * <p>
   * @return This descriptor's bNumEndpoints.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bNumEndpoints();

  /**
   * Get this descriptor's bInterfaceClass.
   * <p>
   * @return This descriptor's bInterfaceClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bInterfaceClass();

  /**
   * Get this descriptor's bInterfaceSubClass.
   * <p>
   * @return This descriptor's bInterfaceSubClass.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bInterfaceSubClass();

  /**
   * Get this descriptor's bInterfaceProtocol.
   * <p>
   * @return This descriptor's bInterfaceProtocol.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bInterfaceProtocol();

  /**
   * Get this descriptor's iInterface.
   * <p>
   * @return This descriptor's iInterface.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte iInterface();
}
