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
import java.io.*;
import java.util.*;

/**
 * Entry point for javax.usb.
 * <p>
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public final class UsbHostManager {

  private UsbHostManager() {
  }

  /**
   * Get the UsbServices implementation.
   * <p>
   * @return The UsbServices implementation instance.
   * @exception UsbException      If there is an error creating the UsbSerivces
   *                              implementation.
   * @exception SecurityException If the caller does not have security access.
   */
  public static UsbServices getUsbServices() throws UsbException, SecurityException {
    synchronized (servicesLock) {
      if (null == usbServices) {
        usbServices = createUsbServices();
      }
    }

    return usbServices;
  }

  /**
   * Get the Properties loaded from the properties file.
   * <p>
   * If the properties have not yet been loaded, this loads them.
   * <p>
   * @return An copy of the Properties.
   * @exception UsbException      If an error occurs while loading the
   *                              properties.
   * @exception SecurityException If the caller does not have security access.
   */
  public static Properties getProperties() throws UsbException, SecurityException {
    synchronized (propertiesLock) {
      if (!propertiesLoaded) {
        setupProperties();
      }
    }

    return (Properties) properties.clone();
  }

  /**
   * Create the UsbServices implementation instance.
   * <p>
   * This creates the UsbServices implementation instance based on the class
   * named in the properties file.
   * <p>
   * @return The UsbServices implementation instance.
   * @exception UsbException      If the UsbServices class could not be
   *                              instantiated.
   * @exception SecurityException If the caller does not have security access.
   */
  private static UsbServices createUsbServices() throws UsbException, SecurityException {
    String className = getProperties().getProperty(JAVAX_USB_USBSERVICES_PROPERTY);

    if (null == className) {
      throw new UsbException(USBSERVICES_PROPERTY_NOT_DEFINED());
    }

    try {
      return (UsbServices) Class.forName(className).newInstance();
    } catch (ClassNotFoundException cnfE) {
      throw new UsbException(USBSERVICES_CLASSNOTFOUNDEXCEPTION(className) + " : " + cnfE.getMessage());
    } catch (ExceptionInInitializerError eiiE) {
      throw new UsbException(USBSERVICES_EXCEPTIONININITIALIZERERROR(className) + " : " + eiiE.getMessage());
    } catch (InstantiationException iE) {
      throw new UsbException(USBSERVICES_INSTANTIATIONEXCEPTION(className) + " : " + iE.getMessage());
    } catch (IllegalAccessException iaE) {
      throw new UsbException(USBSERVICES_ILLEGALACCESSEXCEPTION(className) + " : " + iaE.getMessage());
    } catch (ClassCastException ccE) {
      throw new UsbException(USBSERVICES_CLASSCASTEXCEPTION(className) + " : " + ccE.getMessage());
    }
  }

  /**
   * Set up the Properties using the properties file.
   * <p>
   * This populates the Properties using the key-values listed in the properties
   * file.
   * <p>
   * @exception UsbException      If an error occurs.
   * @exception SecurityException If the caller does not have security access.
   */
  private static void setupProperties() throws UsbException, SecurityException {
    InputStream i = null;

    // First look in 'java.home'/lib
    String h = System.getProperty("java.home");
    String s = System.getProperty("file.separator");
    if (null != h && null != s) {
      File usbPropertiesFile = new File(h + s + "lib" + s + JAVAX_USB_PROPERTIES_FILE);
      if (usbPropertiesFile.exists()) {
        try {
          i = new FileInputStream(usbPropertiesFile);
        } catch (FileNotFoundException ex) {
        }
      }
    }

    // Now check the normal CLASSPATH
    if (null == i) {
      i = UsbHostManager.class.getClassLoader().getResourceAsStream(JAVAX_USB_PROPERTIES_FILE);
    }

    if (null == i) {
      throw new UsbException(PROPERTIES_FILE_NOT_FOUND);
    }

    try {
      properties.load(i);
    } catch (IOException ioE) {
      throw new UsbException(PROPERTIES_FILE_IOEXCEPTION_READING + " : " + ioE.getMessage());
    }

    propertiesLoaded = true;

    try {
      i.close();
    } catch (IOException ioE) {
//FIXME - handle this better than System.err
      System.err.println(PROPERTIES_FILE_IOEXCEPTION_CLOSING + " : " + ioE.getMessage());
    }
  }

  public static final String JAVAX_USB_PROPERTIES_FILE = "javax.usb.properties";
  public static final String JAVAX_USB_USBSERVICES_PROPERTY = "javax.usb.services";

  private static final String PROPERTIES_FILE_NOT_FOUND = "Properties file " + JAVAX_USB_PROPERTIES_FILE + " not found.";
  private static final String PROPERTIES_FILE_IOEXCEPTION_READING = "IOException while reading properties file " + JAVAX_USB_PROPERTIES_FILE;
  private static final String PROPERTIES_FILE_IOEXCEPTION_CLOSING = "IOException while closing properties file " + JAVAX_USB_PROPERTIES_FILE;

  private static String USBSERVICES_PROPERTY_NOT_DEFINED() {
    return "The property " + JAVAX_USB_USBSERVICES_PROPERTY + " is not defined as the implementation class of UsbServices";
  }

  private static String USBSERVICES_CLASSNOTFOUNDEXCEPTION(String c) {
    return "The UsbServices implementation class " + c + " was not found";
  }

  private static String USBSERVICES_EXCEPTIONININITIALIZERERROR(String c) {
    return "an Exception occurred during initialization of the UsbServices Class " + c;
  }

  private static String USBSERVICES_INSTANTIATIONEXCEPTION(String c) {
    return "An Exception occurred during instantiation of the UsbServices implementation " + c;
  }

  private static String USBSERVICES_ILLEGALACCESSEXCEPTION(String c) {
    return "An IllegalAccessException occurred while creating the UsbServices implementation " + c;
  }

  private static String USBSERVICES_CLASSCASTEXCEPTION(String c) {
    return "The class " + c + " does not implement UsbServices";
  }

  private static boolean propertiesLoaded = false;
  private static final Properties properties = new Properties();
  private static final Object propertiesLock = new Object();

  private static UsbServices usbServices = null;
  private static final Object servicesLock = new Object();
}
