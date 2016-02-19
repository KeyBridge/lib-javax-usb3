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

import java.util.ArrayList;
import java.util.List;
import javax.usb3.exception.UsbException;
import javax.usb3.ri.UsbServices;

/**
 * Default programmer entry point for the {@code javax.usb} class library.
 * <p>
 * This class implements the JSR80 defined USB class to (perhaps unnecessarily)
 * emphasize the differentiation in this implementation.
 * <p>
 * This class instantiates the platform-specific instance of the UsbServices
 * interface. From the UsbServices instance, the virtual root UsbHub is
 * available.
 * <p>
 * To get started: null {@code IUsbServices USB_SERVICES = USB.getUsbServices();
 * IUsbHub usbHub = USB_SERVICES.getRootUsbHub();
 * System.out.println("Number of ports: " + usbHub.getNumberOfPorts());}
 *
 * @author Dan Streetman
 * @author E. Michael Maximilien
 * @author Jesse Caulfield (complete rewrite)
 */
public final class USB {

  /**
   * An instance of the IUsbServices interface specification.
   */
  private static IUsbServices usbServices = null;
  /**
   * Lock object used during instantiation of the UsbServices implementation.
   */
  private static final Object USB_SERVICES_LOCK = new Object();

  private USB() {
  }

  /**
   * Get the system IUsbServices implementation.
   *
   * @return The IUsbServices implementation instance.
   * @exception UsbException      If there is an error creating the UsbSerivces
   *                              implementation.
   * @exception SecurityException If the caller does not have security access.
   */
  public static IUsbServices getUsbServices() throws UsbException, SecurityException {
    synchronized (USB_SERVICES_LOCK) {
      if (null == usbServices) {
        usbServices = new UsbServices();
      }
    }
    return usbServices;
  }

  /**
   * Get the virtual IUsbHub to which all physical Host Controller IUsbHubs are
   * attached.
   * <p>
   * The USB root hub is a special hub at the top of the topology tree. The USB
   * 1.1 specification mentions root hubs in sec 5.2.3, where it states that
   * 'the host includes an embedded hub called the root hub'. The implication of
   * this seems to be that the (hardware) Host Controller device is the root
   * hub, since the Host Controller device 'emulates' a USB hub, and in systems
   * with only one physical Host Controller device, its emulated hub is in
   * effect the root hub. However when multiple Host Controller devices are
   * considered, there are two (2) options that were considered:
   * <ol>
   * <li>Have an array or list of the available topology trees, with each
   * physical Host Controller's emulated root hub as the root IUsbHub of that
   * particular topology tree. This configuration could be compared to the
   * MS-DOS/Windows decision to assign drive letters to different physical
   * drives (partitions).
   * </li>
   * <li>Have a 'virtual' root hub, which is completely virtual (not associated
   * with any physical device) and is created and managed solely by the
   * javax.usb implementation. This configuration could be compared to the UNIX
   * descision to put all physical drives on 'mount points' under a single
   * 'root' (/) directory filesystem.
   * </li>
   * </ol>
   * <p>
   * The first configuration results in having to maintain a list of different
   * and completely unconnected device topologies. This means a search for a
   * particular device must be performed on all the device topologies. Since a
   * IUsbHub already has a list of UsbDevices, and a IUsbHub <i>is</i> a
   * UsbDevice, introducing a new, different list is not a desirable action,
   * since it introduces extra unnecessary steps in performing actions, like
   * searching.
   * <p>
   * As an example, a recursive search for a certain device in the first
   * configuration involves getting the first root IUsbHub, getting all its
   * attached UsbDevices, and checking each device; any of those devices which
   * are IUsbHubs can be also searched recursively. Then, the entire operation
   * must be performed on the next root IUsbHub, and this is repeated for all
   * the root IUsbHubs in the array/list. In the second configuration, the
   * virtual root IUsbHub is recursively searched in a single operation.
   * <p>
   * The second configuration is what is used in this API. The implementation is
   * responsible for creating a single root IUsbHub which is completely virtual
   * (and available through the IUsbServices object). Every IUsbHub attached to
   * this virtual root IUsbHub corresponds to a physical Host Controller's
   * emulated hub. I.e., the first level of UsbDevices under the virtual root
   * IUsbHub are all IUsbHubs corresponding to a particular Host Controller on
   * the system. Note that since the root IUsbHub is a virtual hub, the number
   * of ports is not posible to specify; so all that is guaranteed is the number
   * of ports is at least equal to the number of IUsbHubs attached to the root
   * IUsbHub. The number of ports on the virtual root IUsbHub may change if
   * IUsbHubs are attached or detached (e.g., if a Host Controller is physically
   * hot-removed from the system or hot-plugged, or if its driver is dynamically
   * loaded, or for any other reason a top-level Host Controller's hub is
   * attached/detached). This API specification suggests that the number of
   * ports for the root IUsbHub equal the number of directly attached IUsbHubs.
   *
   * @return The virtual IUsbHub object.
   * @exception UsbException      If there is an error accessing javax.usb.
   * @exception SecurityException If current client not configured to access
   *                              javax.usb.
   */
  public static IUsbHub getRootUsbHub() throws UsbException {
    return getUsbServices().getRootUsbHub();
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
   * @throws javax.usb3.exception.UsbException if the USB bus cannot be accessed
   *                                           (e.g. permission error)
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
   * @throws javax.usb3.exception.UsbException if the USB bus cannot be accessed
   *                                           (e.g. permission error)
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
     * types match exactly what is in the USB specification.
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
     * types match exactly what is in the USB specification.
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
