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
package javax.usb3.ri;

import javax.usb3.IUsbControlIrp;
import javax.usb3.request.BMRequestType;
import javax.usb3.request.BRequest;
import javax.usb3.utility.ByteUtility;

/**
 * IUsbControlIrp default implementation.
 * <p>
 * This extends UsbIrp with the Control-specific methods.
 *
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
@SuppressWarnings("ProtectedField")
public class UsbControlIrp extends AUsbIrp implements IUsbControlIrp {

  /**
   * Identifies the characteristics of the specific request. In particular, this
   * field identifies the direction of data transfer in the second phase of the
   * control transfer. The state of the Direction bit is ignored if the wLength
   * field is zero, signifying there is no Data stage.
   * <p>
   * The USB Specification defines a series of standard requests that all
   * devices must support. In addition, a device class may define additional
   * requests. A device vendor may also define requests supported by the device.
   * <p>
   * Requests may be directed to the device, an interface on the device, or a
   * specific endpoint on a device. This field also specifies the intended
   * recipient of the request. When an interface or endpoint is specified, the
   * wIndex field identifies the interface or endpoint.
   * <p>
   * Characteristics of request:
   * <p>
   * D7: Data transfer direction 0 = Host-to-device 1 = Device-to-host<br/>
   * D6...5: Type 0 = Standard 1 = Class 2 = Vendor 3 = Reserved<br/>
   * D4...0: Recipient 0 = Device 1 = Interface 2 = Endpoint 3 = Other 4...31 =
   * Reserved
   * <p>
   * Size is one byte.
   */
  protected byte bmRequestType = 0x00;
  /**
   * Specifies the particular request.
   * <p>
   * The Type bits in the bmRequestType field modify the meaning of this field.
   * This specification defines values for the bRequest field only when the bits
   * are reset to zero, indicating a standard request.
   * <p>
   * Size is one byte.
   */
  protected byte bRequest = 0x00;
  /**
   * The request value. The contents of this field vary according to the
   * request. It is used to pass a parameter to the device, specific to the
   * request.
   * <p>
   * Size is two bytes.
   */
  protected short wValue = 0x0000;
  /**
   * The wIndex field is often used in requests to specify an endpoint or an
   * interface.
   * <p>
   * The contents of this field vary according to the request. It is used to
   * pass a parameter to the device, specific to the request.
   * <p>
   * Size is two bytes.
   */
  protected short wIndex = 0x0000;

  /**
   * Constructor. The data array must be set before use.
   *
   * @param bmRequestType The bmRequestType.
   * @param bRequest      The bRequest.
   * @param wValue        The wValue.
   * @param wIndex        The wIndex.
   */
  public UsbControlIrp(byte bmRequestType, byte bRequest, short wValue, short wIndex) {
    super();
    this.bmRequestType = bmRequestType;
    this.bRequest = bRequest;
    this.wValue = wValue;
    this.wIndex = wIndex;
  }

  /**
   * Constructor.
   *
   * @param bmRequestType The bmRequestType.
   * @param bRequest      The bRequest.
   * @param wValue        The wValue.
   * @param wIndex        The wIndex.
   * @param data          The I/O Request Packet data buffer.
   */
  public UsbControlIrp(byte bmRequestType, byte bRequest, short wValue, short wIndex, byte[] data) {
    super();
    this.bmRequestType = bmRequestType;
    this.bRequest = bRequest;
    this.wValue = wValue;
    this.wIndex = wIndex;
    this.data = data;
  }

  /**
   * Constructor.
   *
   * @param data          The data.
   * @param offset        The offset.
   * @param length        The length.
   * @param shortPacket   The Short Packet policy.
   * @param bmRequestType The bmRequestType.
   * @param bRequest      The bRequest.
   * @param wValue        The wValue.
   * @param wIndex        The wIndex.
   */
  public UsbControlIrp(byte[] data, int offset, int length, boolean shortPacket, byte bmRequestType, byte bRequest, short wValue, short wIndex) {
    super(data, offset, length, shortPacket);
    this.bmRequestType = bmRequestType;
    this.bRequest = bRequest;
    this.wValue = wValue;
    this.wIndex = wIndex;
  }

  /**
   * Get the bmRequestType.
   *
   * @return The bmRequestType.
   */
  @Override
  public byte bmRequestType() {
    return bmRequestType;
  }

  /**
   * Get the bRequest.
   *
   * @return The bRequest.
   */
  @Override
  public byte bRequest() {
    return bRequest;
  }

  /**
   * Get the wValue.
   *
   * @return The wValue.
   */
  @Override
  public short wValue() {
    return wValue;
  }

  /**
   * Get the wIndex.
   *
   * @return The wIndex.
   */
  @Override
  public short wIndex() {
    return wIndex;
  }

  /**
   * Get the wLength.
   *
   * @return The wLength.
   */
  public short wLength() {
    return (short) getLength();
  }

  /**
   * Get a pretty-print string output for this UsbIrp implementation.
   *
   * @return the bean configuration
   */
  @Override
  public String toString() {
    BMRequestType requestType = new BMRequestType(bmRequestType);
    BRequest request = new BRequest(bRequest);

    return "UsbControlIrp"
           + " bmRequestType " + requestType
           + " bRequest [" + (requestType.getType().equals(BMRequestType.EType.STANDARD)
                              ? request
                              : ByteUtility.toString(bRequest, false))
           + "] wValue [" + wValue
           + "] wIndex [" + wIndex
           + "] "
           + super.toString();
  }

}
