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
package javax.usb3;

/**
 * Interface for a USB port.
 * <p>
 * USB ports belong to a USB hub. In this API, they represent downstream ports.
 * Upstream and (non-declared) internal ports are not represented in this API.
 * Internal ports that are reported as downstream ports by the USB hub (in the
 * USB hub descriptor) are represented.
 * <p>
 * See the USB 2.0 specification sec 11.5; USB 3.1 sec 10.3 Hub Downstream
 * Facing Ports.
 *
 * @author E. Michael Maximilien
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
public interface IUsbPort extends Comparable<IUsbPort> {

  /**
   * Get the number of this port.
   * <p>
   * Port numbers are 1-based, not 0-based; the first port on a hub has port
   * number 1. There is a maximum of 255 ports on a single hub (so the maximum
   * port number is 255).
   *
   * @return The number of this port.
   */
  public int getPortNumber();

  /**
   * Get the parent IUsbHub.
   *
   * @return The parent IUsbHub.
   */
  public IUsbHub getUsbHub();

  /**
   * Get the IUsbDevice attached to this IUsbPort.
   * <p>
   * If no IUsbDevice is attached, this returns null.
   *
   * @return The attached device, or null.
   */
  public IUsbDevice getUsbDevice();

  /**
   * If a device is attached to this port.
   *
   * @return If a device is attached to this port.
   */
  public boolean isUsbDeviceAttached();
}
