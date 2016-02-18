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
package javax.usb3.request;

import javax.usb3.enumerated.EDataFlowtype;
import javax.usb3.enumerated.EEndpointSynchronizationType;
import javax.usb3.enumerated.EEndpointUsageType;

/**
 * Helper class to encode and decode the Standard Endpoint Descriptor
 * bmAttributes field. This class encodes/decodes the bmAttributes field
 * according to Table 9-13. Standard Endpoint Descriptor.
 * <p>
 * This contains information encoded into the {@code bmAttributes} (Bitmap)
 * field.
 *
 * @author Jesse Caulfield
 */
public class BPEndpointAttributes {

  /**
   * The Endpoint Descriptor Type encoded into the endpoint.
   */
  private final EDataFlowtype transferType;
  /**
   * The Endpoint Synchronization Type encoded into the endpoint.
   */
  private final EEndpointSynchronizationType synchronizationType;
  /**
   * The Endpoint Usage Types encoded into the endpoint.
   */
  private final EEndpointUsageType usageType;

  /**
   * Construct a new BMAttributes instance.
   *
   * @param transferType        The Transfer Type
   * @param synchronizationType The Synchronization Type.
   * @param usageType           The Usage Type.
   */
  public BPEndpointAttributes(EDataFlowtype transferType, EEndpointSynchronizationType synchronizationType, EEndpointUsageType usageType) {
    this.transferType = transferType;
    this.synchronizationType = synchronizationType;
    this.usageType = usageType;
  }

  /**
   * Construct a BMAttributes instance from a byte code value.
   *
   * @param bmAttributes This field describes the endpoint’s attributes when it
   *                     is configured using the bConfigurationValue.
   */
  public BPEndpointAttributes(byte bmAttributes) {
    this.transferType = EDataFlowtype.fromByte(bmAttributes);
    this.synchronizationType = EEndpointSynchronizationType.fromByte(bmAttributes);
    this.usageType = EEndpointUsageType.fromByte(bmAttributes);
  }

  /**
   * Get a BPEndpointAttributes instance from the bmAttributes byte code. This
   * is a shortcut to the "new" constructor.
   *
   * @param bmAttributes the USB descriptor bmAttributes byte code
   * @return a BPEndpointAttributes instance
   */
  public static BPEndpointAttributes getInstance(byte bmAttributes) {
    return new BPEndpointAttributes(bmAttributes);
  }

  /**
   * Get the BPEndpointAttributes as a byte.
   *
   * @return the BPEndpointAttributes encoded as a byte.
   */
  public byte asByte() {
    /**
     * OR mask the byte codes from all attributes.
     */
    return (byte) (usageType.getByteCode() | synchronizationType.getByteCode() | usageType.getByteCode());
  }

  /**
   * Get the Endpoint Synchronization Type encoded into the endpoint on the USB
   * device described by this descriptor bmAttributes field.
   *
   * @return the Endpoint Synchronization Type
   */
  public EEndpointSynchronizationType getSynchronizationType() {
    return synchronizationType;
  }

  /**
   * Get the Endpoint Descriptor Type encoded into the endpoint on the USB
   * device described by this descriptor bmAttributes field.
   *
   * @return the Endpoint Descriptor Type
   */
  public EDataFlowtype getTransferType() {
    return transferType;
  }

  /**
   * Get the Endpoint Usage Types encoded into the endpoint on the USB device
   * described by this descriptor bmAttributes field.
   *
   * @return the Endpoint Usage Type
   */
  public EEndpointUsageType getUsageType() {
    return usageType;
  }
}
