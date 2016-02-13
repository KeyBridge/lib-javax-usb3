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
package ch.keybridge.lib.usb;

import javax.usb.database.UsbDescription;
import javax.usb.database.UsbDescriptionUtility;
import org.junit.Test;

/**
 *
 * @author Key Bridge LLC
 */
public class UsbIdUtilityTest {

  public UsbIdUtilityTest() {
  }

  @Test
  public void testLookup() throws Exception {
    System.out.println("Test USB database lookup");
    UsbDescription ftdi = UsbDescriptionUtility.lookup("0403", "6001");
    assert ftdi != null;
    System.out.println("usb: " + ftdi);

    UsbDescription logitech = UsbDescriptionUtility.lookup("046d:c016");
    assert logitech != null;
    System.out.println("usb: " + logitech);
  }

}
