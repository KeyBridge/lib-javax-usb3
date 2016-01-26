/*
 * Copyright (C) 2014 Jesse Caulfield <jesse@caulfield.org>
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
package javax.usb.ri;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple class to read the USB ID Repository and identify a USB Vendor and
 * Product ID.
 * <p>
 * This class scans the <code>usb.ids</code> file to describe the provided
 * vendor and product IDs. The <code>usb.ids</code> file is from the public
 * repository of all known ID's used in USB devices: ID's of vendors, devices,
 * subsystems and device classes. It is used in various programs (e.g., The USB
 * Utilities) to display full human-readable names instead of cryptic numeric
 * codes.
 * <p>
 * @see <a href="http://www.linux-usb.org/usb-ids.html">The USB ID
 * Repository<a/>
 * @author Jesse Caulfield
 */
public class USBDeviceInfo {

  /**
   * Developer note: The usb.ids file contains a ton of useful lookup
   * information including known device classes, subclasses and protocols,
   * terminal types, descriptor types etc. Basically the last 3rd of the file is
   * a ClassCode lookup database.
   * <p>
   * @TODO: decode additional information in usb.ids
   */
  /**
   * Scan the USB ID file to identify the indicated vendor ID and product ID.
   * <p>
   * Results are placed as String values in a simple map and may be retrieved
   * with the following keys:
   * <ul><li>vendorId</li>
   * <li>vendorName</li>
   * <li>productId</li>
   * <li>productName</li></ul>
   * <p>
   * @param vendorId  The vendor ID. Match is not case sensitive.
   * @param productId The product ID. Match is not case sensitive.
   * @return a non-null HashMap instance
   */
  public static Map<String, String> findUsbDeviceInfo(String vendorId, String productId) {
    /**
     * Initialize the result map.
     */
    Map<String, String> resultMap = new HashMap<>();
    /**
     * Load and scan the usb.ids file.
     */
    InputStream stream = USBDeviceInfo.class.getResourceAsStream("/resources/usb.ids");
    try (Scanner scanner = new Scanner(stream)) {
      Pattern vendorPattern = Pattern.compile("^(\\w*)\\s{2,}(.*)$");
      Pattern productPattern = Pattern.compile("^\\s{1,}(\\w*)\\s{2,}(.*)$");
      /**
       * Boolean indicator that we are looking for a vendor (not a product yet)
       */
      boolean vendorSeek = true;
      /**
       * Scan the file line by line.
       */
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        /**
         * Skip comments
         */
        if (line.startsWith("#")) {
          continue;
        }
        /**
         * All entries begin with a vendor definition. First scan each line to
         * look for a vendor match.
         */
        if (vendorSeek) {
          Matcher vendorMatcher = vendorPattern.matcher(line);
          if (vendorMatcher.find()) {
            if (vendorId.equalsIgnoreCase(vendorMatcher.group(1))) {
              resultMap.put("vendorId", vendorId);
              resultMap.put("vendorName", vendorMatcher.group(2));
              /**
               * Note that the vendor has been found. This triggers a product
               * search on subsequent lines.
               */
              vendorSeek = false;
            }
          }
        } else {
          /**
           * Only scan product lines if a vendor line has already been
           * identified.
           */
          Matcher producMatcher = productPattern.matcher(line);
          if (producMatcher.find()) {
            if (productId.equalsIgnoreCase(producMatcher.group(1))) {
              resultMap.put("productId", productId);
              resultMap.put("productName", producMatcher.group(2));
              break;
            }
          }
        }
      }
    }
    return resultMap;
  }
}
