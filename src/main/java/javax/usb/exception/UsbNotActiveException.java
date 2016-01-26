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
package javax.usb.exception;

/**
 * Exception indicating an operation was attempted on an null {@link javax.usb.UsbConfiguration#isActive() inactive UsbConfiguration},
 * {@link javax.usb.UsbInterface#isActive() inactive UsbInterface}, and/or
 * {@link javax.usb.UsbPipe#isActive() inactive UsbPipe}.
 * <p>
 * @author Dan Streetman
 */
public class UsbNotActiveException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   */
  public UsbNotActiveException() {
    super();
  }

  /**
   * Constructor.
   * <p>
   * @param s The detail message.
   */
  public UsbNotActiveException(String s) {
    super(s);
  }

}
