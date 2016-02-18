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

import java.util.EventListener;

/**
 * Interface for receiving UsbDeviceEvents.
 *
 * @author E. Michael Maximilien
 * @author Jesse Caulfield
 */
public interface IUsbDeviceListener extends EventListener {

  /**
   * The UsbDevice was detached.
   *
   * @param event The UsbDeviceEvent.
   */
  public void usbDeviceDetached(UsbDeviceEvent event);

  /**
   * An error occurred.
   *
   * @param event The UsbDeviceErrorEvent.
   */
  public void errorEventOccurred(UsbDeviceErrorEvent event);

  /**
   * Data was successfully transferred.
   *
   * @param event The UsbDeviceDataEvent.
   */
  public void dataEventOccurred(UsbDeviceDataEvent event);
}
