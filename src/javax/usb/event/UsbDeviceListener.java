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

import java.util.EventListener;

/**
 * Interface for receiving UsbDeviceEvents.
 * <p>
 * @author E. Michael Maximilien
 */
public interface UsbDeviceListener extends EventListener {

  /**
   * The UsbDevice was detached.
   * <p>
   * @param event The UsbDeviceEvent.
   */
  public void usbDeviceDetached(UsbDeviceEvent event);

  /**
   * An error occurred.
   * <p>
   * @param event The UsbDeviceErrorEvent.
   */
  public void errorEventOccurred(UsbDeviceErrorEvent event);

  /**
   * Data was successfully transferred.
   * <p>
   * @param event The UsbDeviceDataEvent.
   */
  public void dataEventOccurred(UsbDeviceDataEvent event);
}
