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

/**
 * An enumerated list of Endpoint Synchronization Types encoded into the
 * endpoint on the USB device described by this descriptor bmAttributes field.
 * <p>
 * If not an isochronous endpoint, bits 5..2 are reserved and must be set to
 * zero. If isochronous, they are defined as Bits 3..2: Synchronization Type.
 */
public enum EEndpointSynchronizationType {

  NO_SYNCHRONIZATION((byte) 0x00),
  ASYNCHRONOUS((byte) 0x04),
  ADAPTIVE((byte) 0x08),
  SYNCHRONOUS((byte) 0x0c);
  private final byte byteCode;
  private static final byte MASK = (byte) 0x0c;

  private EEndpointSynchronizationType(byte byteCode) {
    this.byteCode = byteCode;
  }

  /**
   * Get the byte code corresponding to this instance.
   * <p>
   * @return the byte code
   */
  public byte getByteCode() {
    return byteCode;
  }

  /**
   * Get the byte mask used to identify the values from a bmAttributes byte.
   * <p>
   * @return The bit mask value.
   */
  public static byte getMASK() {
    return MASK;
  }

  /**
   * Get a EEndpointSynchronizationType instance from a Standard Endpoint
   * Descriptor bmAttributes byte.
   * <p>
   * @param bmAttributes the bmAttributes byte
   * @return the corresponding EEndpointSynchronizationType instance
   */
  public static EEndpointSynchronizationType fromByte(byte bmAttributes) {
    for (EEndpointSynchronizationType type : EEndpointSynchronizationType.values()) {
      if ((bmAttributes & MASK) == type.getByteCode()) {
        return type;
      }
    }
    return null;
  }

}
