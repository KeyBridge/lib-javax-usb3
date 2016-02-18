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
package javax.usb.event;

import javax.usb.IUsbControlIrp;
import javax.usb.IUsbDevice;
import javax.usb.exception.UsbException;

/**
 * Indicates an error occurred on the Default Control Pipe.
 * <p>
 * This will be fired for all errors on the Default Control Pipe.
 *
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public class UsbDeviceErrorEvent extends UsbDeviceEvent {

  private static final long serialVersionUID = 1L;

  private transient IUsbControlIrp usbControlIrp = null;

  /**
   * Constructor.
   *
   * @param source The IUsbDevice.
   * @param irp    The IUsbControlIrp associated with this error.
   */
  public UsbDeviceErrorEvent(IUsbDevice source, IUsbControlIrp irp) {
    super(source);
    usbControlIrp = irp;
  }

  /**
   * Get the associated UsbException.
   *
   * @return The associated UsbException.
   */
  public UsbException getUsbException() {
    return getUsbControlIrp().getUsbException();
  }

  /**
   * Get the IUsbControlIrp associated with this event.
   *
   * @return The IUsbControlIrp.
   */
  public IUsbControlIrp getUsbControlIrp() {
    return usbControlIrp;
  }

}
