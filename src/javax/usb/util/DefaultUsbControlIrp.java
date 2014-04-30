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

/**
 * UsbControlIrp default implementation.
 * <p>
 * This extends DefaultUsbIrp with the Control-specific methods.
 * <p>
 * @author Dan Streetman
 */
@SuppressWarnings("ProtectedField")
public class DefaultUsbControlIrp extends DefaultUsbIrp implements UsbControlIrp {

  protected byte bmRequestType = 0x00;
  protected byte bRequest = 0x00;
  protected short wValue = 0x0000;
  protected short wIndex = 0x0000;

  /**
   * Constructor.
   * <p>
   * @param bmRequestType The bmRequestType.
   * @param bRequest      The bRequest.
   * @param wValue        The wValue.
   * @param wIndex        The wIndex.
   */
  public DefaultUsbControlIrp(byte bmRequestType, byte bRequest, short wValue, short wIndex) {
    super();
    this.bmRequestType = bmRequestType;
    this.bRequest = bRequest;
    this.wValue = wValue;
    this.wIndex = wIndex;
  }

  /**
   * Constructor.
   * <p>
   * @param data          The data.
   * @param offset        The offset.
   * @param length        The length.
   * @param shortPacket   The Short Packet policy.
   * @param bmRequestType The bmRequestType.
   * @param bRequest      The bRequest.
   * @param wValue        The wValue.
   * @param wIndex        The wIndex.
   */
  public DefaultUsbControlIrp(byte[] data, int offset, int length, boolean shortPacket, byte bmRequestType, byte bRequest, short wValue, short wIndex) {
    super(data, offset, length, shortPacket);
    this.bmRequestType = bmRequestType;
    this.bRequest = bRequest;
    this.wValue = wValue;
    this.wIndex = wIndex;
  }

  /**
   * Get the bmRequestType.
   * <p>
   * @return The bmRequestType.
   */
  @Override
  public byte bmRequestType() {
    return bmRequestType;
  }

  /**
   * Get the bRequest.
   * <p>
   * @return The bRequest.
   */
  @Override
  public byte bRequest() {
    return bRequest;
  }

  /**
   * Get the wValue.
   * <p>
   * @return The wValue.
   */
  @Override
  public short wValue() {
    return wValue;
  }

  /**
   * Get the wIndex.
   * <p>
   * @return The wIndex.
   */
  @Override
  public short wIndex() {
    return wIndex;
  }

  /**
   * Get the wLength.
   * <p>
   * @return The wLength.
   */
  public short wLength() {
    return (short) getLength();
  }

}
