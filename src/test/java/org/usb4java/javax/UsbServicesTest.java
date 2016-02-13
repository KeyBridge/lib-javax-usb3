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
    System.out.println("UsbServicesTest");
    IUsbServices services = USB.getUsbServices();
    IUsbHub root = services.getRootUsbHub();
    System.out.println("--------------------------------------------");
    System.out.println("ROOT");
    System.out.println(root);
    System.out.println("--------------------------------------------");
    System.out.println("PORTS");
    for (IUsbPort usbPort : root.getUsbPorts()) {
      System.out.println("USB Port " + usbPort.getPortNumber() + " with attached device(s): " + usbPort.isUsbDeviceAttached());
    }
    System.out.println("--------------------------------------------");
    System.out.println("DEVICES");
    printTree(root);
  }

  private void printTree(IUsbHub hub) {
    Iterator iterator = hub.getUsbPorts().iterator();
    while (iterator.hasNext()) {
      IUsbPort port = (IUsbPort) iterator.next();
      if (port.isUsbDeviceAttached()) {

        IUsbDevice device = port.getUsbDevice();
        if (device.isUsbHub()) {
          System.out.println("Hub: Port " + port.getPortNumber() + " " + device);
          printTree((IUsbHub) device);
        } else {
          System.out.println("  Device: Port " + port.getPortNumber() + " " + device);
        }
      } else {
        System.out.println("Empty: Port " + port.getPortNumber());
      }

    }

  }

}
