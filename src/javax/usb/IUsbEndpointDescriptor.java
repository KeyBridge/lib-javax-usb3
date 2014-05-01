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
 * Interface for a USB endpoint descriptor.
 * <p>
 * See the USB 1.1 specification section 9.6.4.
 * <p>
 * @author Dan Streetman
 */
public interface IUsbEndpointDescriptor extends IUsbDescriptor {

  /**
   * Get this descriptor's bEndpointAddress.
   * <p>
   * @return This descriptor's bEndpointAddress.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bEndpointAddress();

  /**
   * Get this descriptor's bmAttributes.
   * <p>
   * @return This descriptor's bmAttributes.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bmAttributes();

  /**
   * Get this descriptor's wMaxPacketSize.
   * <p>
   * @return This descriptor's wMaxPacketSize.
   * @see javax.usb.util.UsbUtil#unsignedInt(short) This is unsigned.
   */
  public short wMaxPacketSize();

  /**
   * Get this descriptor's bInterval.
   * <p>
   * @return This descriptor's bInterval.
   * @see javax.usb.util.UsbUtil#unsignedInt(byte) This is unsigned.
   */
  public byte bInterval();
}
