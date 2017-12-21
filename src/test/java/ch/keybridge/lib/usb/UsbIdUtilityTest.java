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
package ch.keybridge.lib.usb;

import javax.usb3.database.UsbDeviceDescription;
import javax.usb3.database.UsbRepositoryDatabase;
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
    UsbDeviceDescription ftdi = UsbRepositoryDatabase.lookup("0403", "6001");
    assert ftdi != null;
    System.out.println("usb: " + ftdi);

    UsbDeviceDescription logitech = UsbRepositoryDatabase.lookup("046d:c016");
    assert logitech != null;
    System.out.println("usb: " + logitech);
  }

}
