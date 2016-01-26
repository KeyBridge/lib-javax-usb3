/*
 * Copyright 2016 Caulfield IP Holdings (Caulfield) and affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * Software Code is protected by copyright. Caulfield hereby
 * reserves all rights and copyrights and no license is
 * granted under said copyrights in this Software License Agreement.
 * Caulfield generally licenses software for commercialization
 * pursuant to the terms of either a Standard Software Source Code
 * License Agreement or a Standard Product License Agreement.
 * A copy of these agreements may be obtained by sending a request
 * via email to info@caufield.org.
 */
package org.usb4java.javax;

import java.util.Iterator;
import javax.usb.*;
import javax.usb.exception.UsbException;
import org.junit.Test;

/**
 *
 * @author Key Bridge LLC
 */
public class UsbServicesTest {

  public UsbServicesTest() {
  }

  @Test
  public void testUSBServices() throws UsbException {
    IUsbServices services = UsbHostManager.getUsbServices();
    IUsbHub root = services.getRootUsbHub();
    for (IUsbPort usbPort : root.getUsbPorts()) {
      System.out.println("usb port " + usbPort);
      createTree(root, 0);
    }
  }

  private void createTree(IUsbHub hub, int level) {
    Iterator iterator = hub.getUsbPorts().iterator();
    while (iterator.hasNext()) {
      IUsbPort port = (IUsbPort) iterator.next();

      if (port.isUsbDeviceAttached()) {
        IUsbDevice device = port.getUsbDevice();

        for (int i = 0; i < level; i++) {
          System.out.print("  ");
        }
        if (device.isUsbHub()) {
//          child = getHubNode((IUsbHub) device);
          System.out.println("Hub: " + device);
          createTree((IUsbHub) device, level++);
        } else {
//          child = getDeviceNode(device);
//          createDevice(device, child);
          System.out.println("Device: " + device);
        }

//        deviceTable.put(device, child);
      } else {
//        child = getPortNode(port);
        for (int i = 0; i < level; i++) {
          System.out.print("  ");
        }
        System.out.println("Node: " + port);
      }

    }

  }

}
