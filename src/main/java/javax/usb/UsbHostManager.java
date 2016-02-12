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

import javax.usb.exception.UsbException;
import org.usb4java.javax.UsbServices;

/**
 * Default programmer entry point for javax.usb.
 * <p>
 * This class reads the "javax.usb.PROPERTIES" file and instantiates and
 * instance of the UsbServices implementation specified in that file.
 * <p>
 * To get started: {@code IUsbServices USB_SERVICES = UsbHostManager.getUsbServices();
 * IUsbHub usbHub = USB_SERVICES.getRootUsbHub();
 * System.out.println("Number of ports: " + usbHub.getNumberOfPorts());}
 * <p>
 * @author Dan Streetman
 * @author E. Michael Maximilien
 * @author Jesse Caulfield (complete rewrite)
 */
public final class UsbHostManager {

  /**
   * An instance of the UsbServices implementation (as specified in the
   * JAVAX_USB_PROPERTIES_FILE.
   */
  private static IUsbServices USB_SERVICES = null;
  /**
   * Lock object used during instantiation of the UsbServices implementation.
   */
  private static final Object USB_SERVICES_LOCK = new Object();

  private UsbHostManager() {
  }

  /**
   * Get the IUsbServices implementation specified in the "javax.usb.PROPERTIES"
   * file.
   * <p>
   * @return The IUsbServices implementation instance.
   * @exception UsbException      If there is an error creating the UsbSerivces
   *                              implementation.
   * @exception SecurityException If the caller does not have security access.
   */
  public static IUsbServices getUsbServices() throws UsbException, SecurityException {
    synchronized (USB_SERVICES_LOCK) {
      if (null == USB_SERVICES) {
        USB_SERVICES = new UsbServices();
//        USB_SERVICES = createUsbServices();
      }
    }

    return USB_SERVICES;
  }

}
