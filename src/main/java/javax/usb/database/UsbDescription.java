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
package javax.usb.database;

import java.util.Objects;

/**
 * Simple container for USB ID information.
 * <p>
 * This class contains descriptive information about a USB device to facilitate
 * the lookup of USB device information.
 *
 * @author Key Bridge LLC 02/11/16
 */
public class UsbDescription {

  /**
   * The USB vendor ID. This is a four character byte code. e.g. "03eb"
   */
  private String vendorId;
  /**
   * The USB vendor name. This is a human readable name. e.g. "Atmel Corp."
   */
  private String vendorName;
  /**
   * The USB device ID. This is a four character byte code. e.g. "0902"
   */
  private String deviceId;
  /**
   * The USB device name. This is a human readable name. e.g. "4-Port Hub"
   */
  private String deviceName;

  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public boolean hasVendorId() {
    return this.vendorId != null && !this.vendorId.isEmpty();
  }

  public String getVendorName() {
    return vendorName;
  }

  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public boolean hasDeviceId() {
    return this.deviceId != null && !this.deviceId.isEmpty();
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + Objects.hashCode(this.vendorId);
    hash = 59 * hash + Objects.hashCode(this.deviceId);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final UsbDescription other = (UsbDescription) obj;
    if (!Objects.equals(this.vendorId, other.vendorId)) {
      return false;
    }
    return Objects.equals(this.deviceId, other.deviceId);
  }

  /**
   * Get the USB ids plus vendor and device names. The output is formatted as
   * {@code [vendorId]:[deviceID] vendorName deviceName}. For example,
   * {@code 0403:6001 Future Technology Devices International, Ltd FT232 USB-Serial (UART) IC}
   *
   * @return the USB ids plus vendor and device names.
   */
  @Override
  public String toString() {
    return vendorId + ":" + deviceId + " " + vendorName + " " + deviceName;
  }

}
