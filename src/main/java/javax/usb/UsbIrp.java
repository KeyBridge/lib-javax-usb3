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
 * A basic, abstract USB I/O Request Packet (IRP) implementation (IUsbIrp). This
 * class implements minimum required functionality for the IUsbIrp interface.
 * <p>
 * The behavior and defaults follow those defined in the
 * {@link javax.usb.IUsbIrp interface}. Any of the fields may be updated if the
 * default is not appropriate; in most cases the {@link #getData() data} will be
 * the only field that needs to be {@link #setData(byte[]) set}.
 *
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
public class UsbIrp extends AUsbIrp {

  /**
   * Empty constructor. The data array must be set before use.
   */
  public UsbIrp() {
  }

  /**
   * Constructor.
   *
   * @param data The data.
   * @exception IllegalArgumentException If the data is null.
   */
  public UsbIrp(byte[] data) {
    super(data);
  }

  /**
   * Constructor.
   *
   * @param data        The data.
   * @param offset      The offset.
   * @param length      The length.
   * @param shortPacket The Short Packet policy.
   * @exception IllegalArgumentException If the data is null, or the offset
   *                                     and/or length is negative.
   */
  public UsbIrp(byte[] data, int offset, int length, boolean shortPacket) {
    super(data, offset, length, shortPacket);
  }

  /**
   * Get a pretty-print string output for this UsbIrp implementation.
   *
   * @return the bean configuration
   */
  @Override
  public String toString() {
    return "UsbIrp " + super.toString();
  }

}
