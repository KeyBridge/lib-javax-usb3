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
 * USB constants.
 * <p>
 * @author Dan Streetman
 */
public interface IUsbConst {

  //**************************************************************************
  // Hub constants
// From 11.23.1 Standard Descriptors for Hub Class
  // Full-/Low-speed Operating Hub
//  bInterfaceClass HUB_CLASSCODE (09H)
  public static final byte HUB_CLASSCODE = (byte) 0x09;

  //**************************************************************************
  // Device constants
  /**
   * Unknown device speed. Either the speed could not be detected or the speed
   * was invalid (not USB 1.1 speed).
   */
  public static final Object DEVICE_SPEED_UNKNOWN = new Object();
  /**
   * Low speed device.
   */
  public static final Object DEVICE_SPEED_LOW = new Object();
  /**
   * Full speed device.
   */
  public static final Object DEVICE_SPEED_FULL = new Object();

  //**************************************************************************
  // Configuration constants
  public static final byte CONFIGURATION_POWERED_MASK = (byte) 0x60;
  public static final byte CONFIGURATION_SELF_POWERED = (byte) 0x40;
  public static final byte CONFIGURATION_REMOTE_WAKEUP = (byte) 0x20;

  //**************************************************************************
  // Endpoint constants
//  public static final byte ENDPOINT_NUMBER_MASK = (byte) 0x0f;
// Table 9-13 bEndpointAddress 
//  public static final byte ENDPOINT_DIRECTION_MASK = (byte) 0x80;
//  public static final byte ENDPOINT_DIRECTION_OUT = (byte) 0x00;
//  public static final byte ENDPOINT_DIRECTION_IN = (byte) 0x80;
// Table 9-13 bmAttributes
//  public static final byte ENDPOINT_TYPE_MASK = (byte) 0x03;
//  public static final byte ENDPOINT_TYPE_CONTROL = (byte) 0x00;
//  public static final byte ENDPOINT_TYPE_ISOCHRONOUS = (byte) 0x01;
//  public static final byte ENDPOINT_TYPE_BULK = (byte) 0x02;
//  public static final byte ENDPOINT_TYPE_INTERRUPT = (byte) 0x03;
// Table 9-13 bmAttributes
//  public static final byte ENDPOINT_SYNCHRONIZATION_TYPE_MASK = (byte) 0x0c;
//  public static final byte ENDPOINT_SYNCHRONIZATION_TYPE_NONE = (byte) 0x00;
//  public static final byte ENDPOINT_SYNCHRONIZATION_TYPE_ASYNCHRONOUS = (byte) 0x04;
//  public static final byte ENDPOINT_SYNCHRONIZATION_TYPE_ADAPTIVE = (byte) 0x08;
//  public static final byte ENDPOINT_SYNCHRONIZATION_TYPE_SYNCHRONOUS = (byte) 0x0c;
  // Table 9-13 bmAttributes
//  public static final byte ENDPOINT_USAGE_TYPE_MASK = (byte) 0x30;
//  public static final byte ENDPOINT_USAGE_TYPE_DATA = (byte) 0x00;
//  public static final byte ENDPOINT_USAGE_TYPE_FEEDBACK = (byte) 0x10;
//  public static final byte ENDPOINT_USAGE_TYPE_IMPLICIT_FEEDBACK_DATA = (byte) 0x20;
//  public static final byte ENDPOINT_USAGE_TYPE_RESERVED = (byte) 0x30;
  //**************************************************************************
  // Request constants Table 9-2. Format of Setup Data
  // D7: Data transfer direction
//  public static final byte REQUESTTYPE_DIRECTION_MASK = (byte) 0x80;
//  public static final byte REQUESTTYPE_DIRECTION_IN = (byte) 0x80;
//  public static final byte REQUESTTYPE_DIRECTION_OUT = (byte) 0x00;
// D6...5: Type
//  public static final byte REQUESTTYPE_TYPE_MASK = (byte) 0x60;
//  public static final byte REQUESTTYPE_TYPE_STANDARD = (byte) 0x00;
//  public static final byte REQUESTTYPE_TYPE_CLASS = (byte) 0x20;
//  public static final byte REQUESTTYPE_TYPE_VENDOR = (byte) 0x40;
//  public static final byte REQUESTTYPE_TYPE_RESERVED = (byte) 0x60;
// D4...0: Recipient
//  public static final byte REQUESTTYPE_RECIPIENT_MASK = (byte) 0x1f;
//  public static final byte REQUESTTYPE_RECIPIENT_DEVICE = (byte) 0x00;
//  public static final byte REQUESTTYPE_RECIPIENT_INTERFACE = (byte) 0x01;
//  public static final byte REQUESTTYPE_RECIPIENT_ENDPOINT = (byte) 0x02;
//  public static final byte REQUESTTYPE_RECIPIENT_OTHER = (byte) 0x03;
  // Table 9-4 Standard Request Code VALUE
//  public static final byte REQUEST_GET_STATUS = (byte) 0x00;
//  public static final byte REQUEST_CLEAR_FEATURE = (byte) 0x01;
//  public static final byte REQUEST_SET_FEATURE = (byte) 0x03;
//  public static final byte REQUEST_SET_ADDRESS = (byte) 0x05;
//  public static final byte REQUEST_GET_DESCRIPTOR = (byte) 0x06;
//  public static final byte REQUEST_SET_DESCRIPTOR = (byte) 0x07;
//  public static final byte REQUEST_GET_CONFIGURATION = (byte) 0x08;
//  public static final byte REQUEST_SET_CONFIGURATION = (byte) 0x09;
//  public static final byte REQUEST_GET_INTERFACE = (byte) 0x0a;
//  public static final byte REQUEST_SET_INTERFACE = (byte) 0x0b;
//  public static final byte REQUEST_SYNCH_FRAME = (byte) 0x0c;
  //**************************************************************************
  // Feature selectors
//  public static final byte FEATURE_SELECTOR_DEVICE_REMOTE_WAKEUP = (byte) 0x01;
//  public static final byte FEATURE_SELECTOR_ENDPOINT_HALT = (byte) 0x00;
  //**************************************************************************
  // Descriptor constants Table 9-5. Descriptor Types
//  public static final byte DESCRIPTOR_TYPE_DEVICE = (byte) 0x01;
//  public static final byte DESCRIPTOR_TYPE_CONFIGURATION = (byte) 0x02;
//  public static final byte DESCRIPTOR_TYPE_STRING = (byte) 0x03;
//  public static final byte DESCRIPTOR_TYPE_INTERFACE = (byte) 0x04;
//  public static final byte DESCRIPTOR_TYPE_ENDPOINT = (byte) 0x05;
  /**
   * From 9.6 Standard USB Descriptor Definitions this is just the byte count
   * for the various descriptor messages
   */
//  public static final byte DESCRIPTOR_MIN_LENGTH = (byte) 0x02;
//  public static final byte DESCRIPTOR_MIN_LENGTH_DEVICE = (byte) 0x12;
//  public static final byte DESCRIPTOR_MIN_LENGTH_CONFIGURATION = (byte) 0x09;
//  public static final byte DESCRIPTOR_MIN_LENGTH_INTERFACE = (byte) 0x09;
//  public static final byte DESCRIPTOR_MIN_LENGTH_ENDPOINT = (byte) 0x07;
//  public static final byte DESCRIPTOR_MIN_LENGTH_STRING = (byte) 0x02;
}
