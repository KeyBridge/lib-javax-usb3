/*
 * Copyright (C) 2014 Jesse Caulfield 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY), without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package javax.usb3.enumerated;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration of recognized speeds encoded intoa SuperSpeed USB Device
 * Capability descriptor.
 * <p>
 * See 9.6.2.2 SuperSpeed USB Device Capability, wSpeedsSupported of the USB 3.1
 * spec.
 *
 * @author Jesse Caulfield
 */
public enum EDevicePortSpeed {

  /**
   * Developer note: The order of appearance should be sorted highest to lowest.
   * This is to ensure that the maximum supported speed is returned from the
   * speedSupported() method, which scans the list of EDevicePortSpeed values
   * and returns the first match.
   */
  /**
   * SuperSpeed operation supported (5000MBit/s).
   */
  SUPER((byte) 0x04),
  /**
   * High speed operation supported (480MBit/s).
   */
  HIGH((byte) 0x02),
  /**
   * Full speed operation supported (12MBit/s).
   */
  FULL((byte) 0x01),
  /**
   * Low speed operation supported (1.5MBit/s). Default if not otherwise
   * specified..
   */
  LOW((byte) 0x00);
  /**
   * Device speed configuration was not recognized by the host operating system.
   */
//  UNKNOWN((byte) 0x00);

  /**
   * The bimap encoding of the speed supported by this device.
   */
  private final byte byteCode;

  private EDevicePortSpeed(byte byteCode) {
    this.byteCode = byteCode;
  }

  public byte getByteCode() {
    return byteCode;
  }

  /**
   * Get the supported speed from the wSpeedsSupported value returned by the
   * operating system.
   *
   * @param wSpeedsSupported
   * @return
   */
  public static EDevicePortSpeed speedSupported(short wSpeedsSupported) {
    for (EDevicePortSpeed eDevicePortSpeed : EDevicePortSpeed.values()) {
      if ((eDevicePortSpeed.getByteCode() & wSpeedsSupported) == 1) {
        return eDevicePortSpeed;
      }
    }
    return null;
  }

  /**
   * Get a list of supported speeds from the wSpeedsSupported value returned by
   * the operating system.
   *
   * @param wSpeedsSupported Bitmap encoding of the speed supported by this
   *                         device. (Expect 1, 2, 3, 4 from the operating
   *                         system.)
   * @return a non-null but possibly empty ArrayList instance
   */
  public static List<EDevicePortSpeed> speedsSupported(short wSpeedsSupported) {
    List<EDevicePortSpeed> speeds = new ArrayList<>();
    for (EDevicePortSpeed eDevicePortSpeed : EDevicePortSpeed.values()) {
      if ((eDevicePortSpeed.getByteCode() & wSpeedsSupported) != 0) {
        speeds.add(eDevicePortSpeed);
      }
    }
    return speeds;
  }
}
