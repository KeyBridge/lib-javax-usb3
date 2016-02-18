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
package javax.usb3.event;

import java.util.EventObject;
import javax.usb3.IUsbIrp;
import javax.usb3.IUsbPipe;

/**
 * Class for USB pipe events.
 *
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
public class UsbPipeEvent extends EventObject {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * <p>
   * This should only be used if there is no IUsbIrp associated with this event.
   *
   * @param source The source IUsbPipe.
   */
  public UsbPipeEvent(IUsbPipe source) {
    super(source);
  }

  /**
   * Constructor.
   *
   * @param source The source IUsbPipe.
   * @param uI     The IUsbIrp.
   */
  public UsbPipeEvent(IUsbPipe source, IUsbIrp uI) {
    super(source);
    usbIrp = uI;
  }

  /**
   * Get the IUsbPipe.
   *
   * @return The associated IUsbPipe.
   */
  public IUsbPipe getUsbPipe() {
    return (IUsbPipe) getSource();
  }

  /**
   * If this has an associated IUsbIrp.
   * <p>
   * Note that even if a byte[] was submitted to a IUsbPipe, if the
   * implementation used/created a IUsbIrp to manage the submission, it may (or
   * may not) provide that IUsbIrp in any event generated from the submission.
   *
   * @return If this has an associated IUsbIrp.
   */
  public boolean hasUsbIrp() {
    return null != getUsbIrp();
  }

  /**
   * Get the IUsbIrp associated with this event.
   * <p>
   * If there is no IUsbIrp associated with this event, this returns null.
   *
   * @return The associated IUsbIrp, or null.
   */
  public IUsbIrp getUsbIrp() {
    return usbIrp;
  }

  private transient IUsbIrp usbIrp = null;

}
