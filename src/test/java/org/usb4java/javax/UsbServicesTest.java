/*
 * Copyright (C) 2014 Jesse Caulfield 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
    IUsbServices services = UsbHostManager.getUsbServices();
    IUsbHub rootHub = services.getRootUsbHub();
    System.out.println("--------------------------------------------");
    System.out.println("ROOT");
    System.out.println(rootHub);
    System.out.println("--------------------------------------------");
    System.out.println("PORTS");
    for (IUsbPort usbPort : rootHub.getUsbPorts()) {
      System.out.println("USB Port " + usbPort.getPortNumber() + " with attached device(s): " + usbPort.isUsbDeviceAttached());
    }
    System.out.println("--------------------------------------------");
    System.out.println("DEVICES");
    printTree(rootHub);
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
