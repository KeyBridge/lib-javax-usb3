/*
 * Copyright (C) 2016 Jesse Caulfield
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
package javax.usb.database;

import java.io.InputStream;
import java.util.Scanner;

/**
 * A utility class to read and parse the USB ID Repository database and identify
 * a USB Vendor and Product ID.
 * <p>
 * This class scans the {@code usb.ids} database file to identify the provided
 * vendor and product IDs. The {@code usb.ids} file is a publicly available
 * repository containing all known ID's used in USB devices: ID's of vendors,
 * devices, subsystems and device classes.
 * <p>
 * The {@code usb.ids} database contains useful lookup information including
 * known device classes, subclasses and protocols, terminal types, descriptor
 * types etc. It is used in various programs to display full human-readable
 * names instead of cryptic numeric codes.
 *
 * @see <a href="http://www.linux-usb.org/usb-ids.html">The USB ID
 * Repository<a/>
 * @author Jesse Caulfield
 */
public class UsbRepositoryDatabase {

  /**
   *
   * Lookup a USB vendor + product id in the database.
   *
   * @param vendorId  The USB vendor ID. This is a four character byte code.
   *                  e.g. "03eb"
   * @param productId The USB device ID. This is a four character byte code.
   *                  e.g. "0902"
   * @return the USB ID database entry corresponding to the vendor and device
   *         ID.
   * @throws Exception if the vendor and device ID are not found in the
   *                   database.
   */
  public static UsbDeviceDescription lookup(String vendorId, String productId) throws Exception {
    InputStream stream = UsbRepositoryDatabase.class.getClassLoader().getResourceAsStream("META-INF/database/usb.ids");
    try (Scanner scanner = new Scanner(stream)) {
      UsbDeviceDescription usbId = new UsbDeviceDescription();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        /**
         * Skip comments
         */
        if (line.startsWith("#")) {
          continue;
        }
        /**
         * First match the vendor. Only after (and if) the vendor matches, then
         * try to match the device. Return as soon as the device ID is found.
         */
        if (line.startsWith(vendorId)) {
          usbId.setVendorId(line.substring(0, 4));
          usbId.setVendorName(line.substring(5, line.trim().length()).trim());
        } else if (usbId.hasVendorId() && line.trim().startsWith(productId)) {
          usbId.setDeviceId(line.trim().substring(0, 4));
          usbId.setDeviceName(line.trim().substring(5, line.trim().length()).trim());
          return usbId;
        }
      }
    }
    throw new Exception(vendorId + ":" + productId + " not found in the USB database.");
  }

  /**
   * Lookup a USB vendor + product id in the database.
   *
   * @param vendorId  The USB vendor ID. This is a four-byte code. e.g. "03eb"
   * @param productId The USB device ID. This is a four-byte code code. e.g.
   *                  "0902"
   * @return the USB ID database entry corresponding to the vendor and device
   *         ID.
   * @throws Exception if the vendor and device ID are not found in the
   *                   database.
   */
  public static UsbDeviceDescription lookup(short vendorId, short productId) throws Exception {
    return lookup(String.format("%04x", vendorId), String.format("%04x", productId));
  }

  /**
   * Shortcut to {@link #lookup(java.lang.String, java.lang.String)} accepting a
   * concatenated USB ID.
   *
   * @param usbID the device USB id, which must be formatted as a colon (:)
   *              delimited string.
   * @return the device USB id. e.g. "046d:c016"
   * @throws IllegalArgumentException if the inpt usbID is malformed
   * @throws Exception                if the vendor and device ID are not found
   *                                  in the database.
   * @throws Exception
   */
  public static UsbDeviceDescription lookup(String usbID) throws IllegalArgumentException, Exception {
    if (!usbID.contains(":")) {
      throw new IllegalArgumentException("Invalid USB ID. Must contain a colon (:) delimiter.");
    }
    return lookup(usbID.split(":")[0], usbID.split(":")[1]);
  }
}
