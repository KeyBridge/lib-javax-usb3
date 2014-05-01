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
package javax.usb.event;

import javax.usb.exception.UsbException;
import javax.usb.*;

/**
 * Indicates an error occurred on the IUsbPipe.
 * <p>
 This will be fired for all errors on the IUsbPipe.
 <p>
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public class UsbPipeErrorEvent extends UsbPipeEvent {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * <p>
 This should be used only if there is no IUsbIrp associated with this event.
 <p>
   * @param source The IUsbPipe.
   * @param uE     The UsbException.
   */
  public UsbPipeErrorEvent(IUsbPipe source, UsbException uE) {
    super(source);
    usbException = uE;
  }

  /**
   * Constructor.
   * <p>
   * @param source The IUsbPipe.
   * @param uI     The IUsbIrp.
   */
  public UsbPipeErrorEvent(IUsbPipe source, IUsbIrp uI) {
    super(source, uI);
  }

  /**
   * Get the associated UsbException.
   * <p>
   * @return The associated UsbException.
   */
  public UsbException getUsbException() {
    if (hasUsbIrp()) {
      return getUsbIrp().getUsbException();
    } else {
      return usbException;
    }
  }

  private UsbException usbException = null;

}
