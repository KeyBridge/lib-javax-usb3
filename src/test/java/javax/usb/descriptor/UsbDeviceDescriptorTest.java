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
package javax.usb.descriptor;

import javax.usb.database.UsbDeviceDescription;
import javax.usb.database.UsbRepositoryDatabase;
import javax.usb.enumerated.EUSBClassCode;
import javax.usb.enumerated.EUSBSubclassCode;
import org.junit.Test;

/**
 *
 * @author Key Bridge LLC
 */
public class UsbDeviceDescriptorTest {

  public UsbDeviceDescriptorTest() {
  }

  @Test
  public void testVirtualRootHub() throws Exception {

    UsbDeviceDescriptor d = new UsbDeviceDescriptor(
            (byte) 0x0300,
            EUSBClassCode.HUB,
            EUSBSubclassCode.FULL_SPEED_HUB.getBytecodeSubclass(),
            EUSBSubclassCode.FULL_SPEED_HUB.getBytecodeProtocol(),
            (byte) 0xffff,
            (short) 0x1d6b,
            (short) 0x0003,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00);

    System.out.println("UsbDeviceDescriptor\n" + d);

    UsbDeviceDescription id = UsbRepositoryDatabase.lookup(d.idVendor(), d.idProduct());

    System.out.println("ID: " + id);

  }

}
