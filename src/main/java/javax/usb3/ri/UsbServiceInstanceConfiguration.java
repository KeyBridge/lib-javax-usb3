/*
 * Copyright (C) 2011 Klaus Reimer
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
package javax.usb3.ri;

/**
 * USB Services ServicesInstanceConfigurationuration.
 * <p>
 * This is a container class for the Properties file.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield (set defaults)
 */
public final class UsbServiceInstanceConfiguration {

  /**
   * 5000 ms.
   * <p>
   * The default USB communication timeout in milliseconds.
   */
  public static final int TIMEOUT = 5000;

  /**
   * 500 ms.
   * <p>
   * The default scan interval in milliseconds.
   */
  public static final int SCAN_INTERVAL = 500;

}
