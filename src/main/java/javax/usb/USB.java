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

import java.util.ArrayList;
import java.util.List;
import javax.usb.exception.UsbException;

/**
 * Default programmer entry point for the {@code javax.usb} class library.
 * <p>
 * To get started:
 * <p>
 * <p>
 * {@code IUsbServices USB_SERVICES = UsbHostManager.getUsbServices();
 * IUsbHub usbHub = USB_SERVICES.getRootUsbHub();
 * System.out.println("Number of ports: " + usbHub.getNumberOfPorts());}
 *
 * @author Dan Streetman
 * @author E. Michael Maximilien
 * @author Jesse Caulfield (complete rewrite)
 */
public final class USB {

  /**
   * An instance of the UsbServices implementation (as specified in the
   * JAVAX_USB_PROPERTIES_FILE.
   */
  private static IUsbServices USB_SERVICES = null;
  /**
   * Lock object used during instantiation of the UsbServices implementation.
   */
  private static final Object USB_SERVICES_LOCK = new Object();

  private USB() {
  }

  /**
   * Get the IUsbServices implementation specified in the "javax.usb.PROPERTIES"
   * file.
   *
   * @return The IUsbServices implementation instance.
   * @exception UsbException      If there is an error creating the UsbSerivces
   *                              implementation.
   * @exception SecurityException If the caller does not have security access.
   */
  public static IUsbServices getUsbServices() throws UsbException, SecurityException {
    synchronized (USB_SERVICES_LOCK) {
      if (null == USB_SERVICES) {
        USB_SERVICES = new UsbServices();
      }
    }
    return USB_SERVICES;
  }

  /**
   * Get a List of all devices that match the specified vendor and product id.
   * <p>
   * Set the productID to capture all USB devices with the given vendor id.
   *
   * @param usbDevice The IUsbDevice to check. If null then a new recursive
   *                  search from the ROOT device will be initiated.
   * @param vendorId  The vendor ID to match.
   * @param productId (Optional) The product id to match. Set to MINUS ONE (-1)
   *                  to match all vendor IDs.
   * @return A non-null ArrayList instance containing any matching
   *         IUsbDevice(s).
   * @throws javax.usb.exception.UsbException if the USB bus cannot be accessed
   *                                          (e.g. permission error)
   * @since 3.1
   */
  public static List<IUsbDevice> getUsbDeviceList(short vendorId, short productId) throws UsbException {
    return getUsbDeviceList(getUsbServices().getRootUsbHub(), vendorId, productId);
  }

  /**
   * Get a List of all devices that match the specified vendor and product id.
   * <p>
   * Set the productID to capture all USB devices with the given vendor id.
   *
   * @param usbDevice The IUsbDevice to check. If null then a new recursive
   *                  search from the ROOT device will be initiated.
   * @param vendorId  The vendor ID to match.
   * @param productId (Optional) The product id to match. Set to MINUS ONE (-1)
   *                  to match all vendor IDs.
   * @return A non-null ArrayList instance containing any matching
   *         IUsbDevice(s).
   * @throws javax.usb.exception.UsbException if the USB bus cannot be accessed
   *                                          (e.g. permission error)
   * @since 3.1
   */
  public static List<IUsbDevice> getUsbDeviceList(IUsbDevice usbDevice, short vendorId, short productId) throws UsbException {
    List<IUsbDevice> iUsbDeviceList = new ArrayList<>();
    /**
     * If the usbDevice is null then get initialize the search at the virtual
     * ROOT hub.
     */
    if (usbDevice == null) {
      return getUsbDeviceList(getUsbServices().getRootUsbHub(), vendorId, productId);
    }
    /*
     * A device's descriptor is always available. All descriptor field names and
     * types match exactly what is in the USB specification. Note that Java does
     * not have unsigned numbers, so if you are comparing 'magic' numbers to the
     * fields, you need to handle it correctly. For example if you were checking
     * for Intel (vendor id 0x8086) devices, if (0x8086 ==
     * descriptor.idVendor()) will NOT work. The 'magic' number 0x8086 is a
     * positive integer, while the _short_ vendor id 0x8086 is a negative
     * number! So you need to do either if ((short)0x8086 ==
     * descriptor.idVendor()) or if (0x8086 ==
     * UsbUtil.unsignedInt(descriptor.idVendor())) or short intelVendorId =
     * (short)0x8086; if (intelVendorId == descriptor.idVendor()) Note the last
     * one, if you don't cast 0x8086 into a short, the compiler will fail
     * because there is a loss of precision; you can't represent positive 0x8086
     * as a short; the max value of a signed short is 0x7fff (see
     * Short.MAX_VALUE).
     *
     * See javax.usb.util.UsbUtil.unsignedInt() for some more information.
     */
    if (vendorId == usbDevice.getUsbDeviceDescriptor().idVendor()
        && (productId == -1 || productId == usbDevice.getUsbDeviceDescriptor().idProduct())) {
      iUsbDeviceList.add(usbDevice);
    }
    /*
     * If the device is a HUB then recurse and scan the hub connected devices.
     * This is just normal recursion: Nothing special.
     */
    if (usbDevice.isUsbHub()) {
      for (IUsbDevice usbDeviceTemp : ((IUsbHub) usbDevice).getAttachedUsbDevices()) {
        iUsbDeviceList.addAll(getUsbDeviceList(usbDeviceTemp, vendorId, productId));
      }
    }
    return iUsbDeviceList;
  }

  /**
   * Get a List of all devices that match the specified vendor and product id.
   * <p>
   * Set the productID to capture all USB devices with the given vendor id.
   *
   * @param usbDevice The IUsbDevice to check.
   * @param vendorId  The vendor ID to match.
   * @param productId (Optional) A non-null list of product IDs to match.
   *                  Provide an empty list to match all product IDs for the
   *                  given vendor ID.
   * @return A non-null ArrayList instance containing of any matching
   *         IUsbDevice(s).
   * @since 3.1
   */
  public static List<IUsbDevice> getUsbDeviceList(short vendorId, List<Short> productId) throws UsbException {
    return getUsbDeviceList(getUsbServices().getRootUsbHub(), vendorId, productId);
  }

  /**
   * Get a List of all devices that match the specified vendor and product id.
   * <p>
   * Set the productID to capture all USB devices with the given vendor id.
   *
   * @param usbDevice The IUsbDevice to check.
   * @param vendorId  The vendor ID to match.
   * @param productId (Optional) A non-null list of product IDs to match.
   *                  Provide an empty list to match all product IDs for the
   *                  given vendor ID.
   * @return A non-null ArrayList instance containing of any matching
   *         IUsbDevice(s).
   * @since 3.1
   */
  public static List<IUsbDevice> getUsbDeviceList(IUsbDevice usbDevice, short vendorId, List<Short> productId) {
    List<IUsbDevice> iUsbDeviceList = new ArrayList<>();
    /*
     * A device's descriptor is always available. All descriptor field names and
     * types match exactly what is in the USB specification. Note that Java does
     * not have unsigned numbers, so if you are comparing 'magic' numbers to the
     * fields, you need to handle it correctly. For example if you were checking
     * for Intel (vendor id 0x8086) devices, if (0x8086 ==
     * descriptor.idVendor()) will NOT work. The 'magic' number 0x8086 is a
     * positive integer, while the _short_ vendor id 0x8086 is a negative
     * number! So you need to do either if ((short)0x8086 ==
     * descriptor.idVendor()) or if (0x8086 ==
     * UsbUtil.unsignedInt(descriptor.idVendor())) or short intelVendorId =
     * (short)0x8086; if (intelVendorId == descriptor.idVendor()) Note the last
     * one, if you don't cast 0x8086 into a short, the compiler will fail
     * because there is a loss of precision; you can't represent positive 0x8086
     * as a short; the max value of a signed short is 0x7fff (see
     * Short.MAX_VALUE).
     *
     * See javax.usb.util.UsbUtil.unsignedInt() for some more information.
     */
    if (vendorId == usbDevice.getUsbDeviceDescriptor().idVendor()
        && (productId.isEmpty() || productId.contains(usbDevice.getUsbDeviceDescriptor().idProduct()))) {
      iUsbDeviceList.add(usbDevice);
    }
    /*
     * If the device is a HUB then recurse and scan the hub connected devices.
     * This is just normal recursion: Nothing special.
     */
    if (usbDevice.isUsbHub()) {
      for (IUsbDevice usbDeviceTemp : ((IUsbHub) usbDevice).getAttachedUsbDevices()) {
        iUsbDeviceList.addAll(getUsbDeviceList(usbDeviceTemp, vendorId, productId));
      }
    }
    return iUsbDeviceList;
  }

}
