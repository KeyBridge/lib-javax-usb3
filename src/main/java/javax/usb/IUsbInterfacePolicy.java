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
package javax.usb;

/**
 * Interface describing the policy to claim an interface with.
 *
 * @author Dan Streetman
 */
public interface IUsbInterfacePolicy {

  /**
   * If the claim should be forced.
   * <p>
   * This will try to forcibly claim the IUsbInterface. This is only intended as
   * a flag to the implementation to try everything possible to allow a
   * successful claim. The implementation may try to override any other
   * driver(s) that have the interface claimed.
   * <p>
   * The implementation is not required to use this flag.
   * <p>
   * <strong>WARNING</strong>: This should <i>only</i> be used if you are
   * <i>absolutely sure</i> you want to drive the interface.
   *
   * @param usbInterface The IUsbInterface being claimed.
   * @return If the interface should be forcibly claimed.
   */
  public boolean forceClaim(IUsbInterface usbInterface);

}
