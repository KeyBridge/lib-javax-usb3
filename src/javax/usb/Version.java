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

/**
 * Version class that prints the current version numbers.
 * <p>
 * This maintains the version number of the current javax.usb API specification
 * and the supported USB specification version number.
 * <p>
 * @author Dan Streetman
 * @author E. Michael Maximilien
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
public class Version {

  /**
   * Prints out text to stdout (with appropriate version numbers).
   * <p>
   * The specific text printed is:
   * <pre>
   * javax.usb API version &lt;getApiVersion()>
   * USB specification version &lt;getUsbVersion()>
   * </pre>
   * <p>
   * @param args a String[] of arguments.
   */
  public static void main(String[] args) {
    System.out.println("javax.usb API version " + getApiVersion());
    System.out.println("USB specification version " + getUsbVersion());
  }

  /**
   * Get the version number of this API.
   * <p>
   * The format of this is &lt;major>.&lt;minor>[.&lt;revision>]
   * <p>
   * The revision number is optional; a missing revision number (i.e., version
   * X.X) indicates the revision number is zero (i.e., version X.X.0).
   * <p>
   * @return the version number of this API.
   */
  public static String getApiVersion() {
    return VERSION_JAVAX_USB;
  }

  /**
   * Get the version number of the USB specification this API implements.
   * <p>
   * The formt of this is &lt;major>.&lt;minor>[.&lt;revision>]
   * <p>
   * This should correspond with a released USB specification hosted by
   * <a href="http://www.usb.org">the USB organization website</a>. The revision
   * number will only be present if the USB specification contains it.
   * <p>
   * @return the version number of the implemented USB specification version.
   */
  public static String getUsbVersion() {
    return VERSION_USB_SPECIFICATION;
  }

  //--------------------------------------------------------------------------
  // Class variables
  //
  private static final String VERSION_USB_SPECIFICATION = "3.1";
  private static final String VERSION_JAVAX_USB = "2.0.r01+jni";
}
