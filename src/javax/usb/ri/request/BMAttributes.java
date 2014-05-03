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
package javax.usb.ri.request;

import javax.usb.ri.enumerated.EEndpointTransferType;
import javax.usb.ri.enumerated.EEndpointUsageType;
import javax.usb.ri.enumerated.EEndpointSynchronizationType;

/**
 * Helper class to encode and decode the Standard Endpoint Descriptor
 * bmAttributes field.
 * <p>
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
public class BMAttributes {

  private final EEndpointTransferType transferType;
  private final EEndpointSynchronizationType synchronizationType;
  private final EEndpointUsageType usageType;

  /**
   * Construct a new BMAttributes instance.
   * <p>
   * @param transferType        The Transfer Type
   * @param synchronizationType The Synchronization Type.
   * @param usageType           The Usage Type.
   */
  public BMAttributes(EEndpointTransferType transferType, EEndpointSynchronizationType synchronizationType, EEndpointUsageType usageType) {
    this.transferType = transferType;
    this.synchronizationType = synchronizationType;
    this.usageType = usageType;
  }

  /**
   * Construct a BMAttributes instance from a byte code value.
   * <p>
   * @param bmAttributes This field describes the endpoint’s attributes when it
   *                     is configured using the bConfigurationValue.
   */
  public BMAttributes(byte bmAttributes) {
    this.transferType = EEndpointTransferType.fromByte(bmAttributes);
    this.synchronizationType = EEndpointSynchronizationType.fromByte(bmAttributes);
    this.usageType = EEndpointUsageType.fromByte(bmAttributes);
  }

  /**
   * Get the BMAttributes as a byte.
   * <p>
   * @return the BMAttributes encoded as a byte.
   */
  public byte asByte() {
    /**
     * OR mask the byte codes from all attributes.
     */
    return (byte) (usageType.getByteCode() | synchronizationType.getByteCode() | usageType.getByteCode());
  }
}
