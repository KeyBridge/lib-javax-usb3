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

import java.util.*;
import javax.usb.*;

/**
 * Class for USB pipe events.
 * <p>
 * @author E. Michael Maximilien
 * @author Dan Streetman
 */
public class UsbPipeEvent extends EventObject {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * <p>
   * This should only be used if there is no UsbIrp associated with this event.
   * <p>
   * @param source The source UsbPipe.
   */
  public UsbPipeEvent(UsbPipe source) {
    super(source);
  }

  /**
   * Constructor.
   * <p>
   * @param source The source UsbPipe.
   * @param uI     The UsbIrp.
   */
  public UsbPipeEvent(UsbPipe source, UsbIrp uI) {
    super(source);
    usbIrp = uI;
  }

  /**
   * Get the UsbPipe.
   * <p>
   * @return The associated UsbPipe.
   */
  public UsbPipe getUsbPipe() {
    return (UsbPipe) getSource();
  }

  /**
   * If this has an associated UsbIrp.
   * <p>
   * Note that even if a byte[] was submitted to a UsbPipe, if the
   * implementation used/created a UsbIrp to manage the submission, it may (or
   * may not) provide that UsbIrp in any event generated from the submission.
   * <p>
   * @return If this has an associated UsbIrp.
   */
  public boolean hasUsbIrp() {
    return null != getUsbIrp();
  }

  /**
   * Get the UsbIrp associated with this event.
   * <p>
   * If there is no UsbIrp associated with this event, this returns null.
   * <p>
   * @return The associated UsbIrp, or null.
   */
  public UsbIrp getUsbIrp() {
    return usbIrp;
  }

  private transient UsbIrp usbIrp = null;

}
