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
package javax.usb.enumerated;

/**
 * 4.7 Data Flow Types
 * <p>
 * An enumerated list of Endpoint Descriptor Types encoded into the endpoint on
 * the USB device described by this descriptor bmAttributes field.
 * <p>
 * This field describes the endpoint’s attributes when it is configured using
 * the bConfigurationValue. Bits 1..0: Transfer Type.
 * <p>
 * The USB supports functional data and control exchange between the USB host
 * and a USB device as a set of either uni-directional or bi-directional pipes.
 * USB data transfers take place between host software and a particular endpoint
 * on a USB device. Such associations between the host software and a USB device
 * endpoint are called pipes. In general, data movement though one pipe is
 * independent from the data flow in any other pipe. A given USB device may have
 * many pipes. As an example, a given USB device could have an endpoint that
 * supports a pipe for transporting data to the USB device and another endpoint
 * that supports a pipe for transporting data from the USB device.
 * <p>
 * The USB 2.0 architecture comprehends four basic types of data transfers:
 * [Control, Bulk Data, Interrupt Data, Isochronous Data]
 *
 * @see "4.7 of the USB 2.0 specification"
 * @see "4.4 of the USB 3.1 specification"
 * @since 3.1
 * @author Jesse Caulfield
 */
public enum EDataFlowtype {

  /**
   * Control Transfers: Used to configure a device at attach time and can be
   * used for other device-specific purposes, including control of other pipes
   * on the device.
   * <p>
   * Control data is used by the USB System Software to configure devices when
   * they are first attached. Other driver software can choose to use control
   * transfers in implementation-specific ways. Data delivery is lossless.
   * <p>
   * Each device is required to implement the default control pipe as a message
   * pipe. This pipe is intended for device initialization and management. This
   * pipe is used to access device descriptors and to make requests of the
   * device to manipulate its behavior (at a device-level).
   */
  CONTROL((byte) 0x00),
  /**
   * Isochronous (constant time) Data Transfers: Occupy a pre-negotiated amount
   * of USB bandwidth with a pre-negotiated delivery latency. (Also called
   * streaming real time transfers).
   * <p>
   * Isochronous data is continuous and real-time in creation, delivery, and
   * consumption. Timing-related information is implied by the steady rate at
   * which isochronous data is received and transferred. Isochronous data must
   * be delivered at the rate received to maintain its timing. In addition to
   * delivery rate, isochronous data may also be sensitive to delivery delays.
   * For isochronous pipes, the bandwidth required is typically based upon the
   * sampling characteristics of the associated function. The latency required
   * is related to the buffering available at each endpoint.
   * <p>
   * A typical example of isochronous data is voice.
   * <p>
   * The timely delivery of isochronous data is ensured at the expense of
   * potential transient losses in the data stream. In other words, any error in
   * electrical transmission is not corrected by hardware mechanisms such as
   * retries. In practice, the core bit error rate of the USB is expected to be
   * small enough not to be an issue. USB isochronous data streams are allocated
   * a dedicated portion of USB bandwidth to ensure that data can be delivered
   * at the desired rate.
   */
  ISOCHRONOUS((byte) 0x01),
  /**
   * Bulk Data Transfers: Generated or consumed in relatively large and bursty
   * quantities and have wide dynamic latitude in transmission constraints.
   * <p>
   * Bulk data typically consists of larger amounts of data, such as that used
   * for printers or scanners. Bulk data is sequential. Reliable exchange of
   * data is ensured at the hardware level by using error detection in hardware
   * and invoking a limited number of retries in hardware. Also, the bandwidth
   * taken up by bulk data can vary, depending on other bus activities.
   */
  BULK((byte) 0x02),
  /**
   * Interrupt Data Transfers: Used for timely but reliable delivery of data,
   * for example, characters or coordinates with human-perceptible echo or
   * feedback response characteristics.
   * <p>
   * A limited-latency transfer to or from a device is referred to as interrupt
   * data. Such data may be presented for transfer by a device at any time and
   * is delivered by the USB at a rate no slower than is specified by the
   * device.
   * <p>
   * Interrupt data typically consists of event notification, characters, or
   * coordinates that are organized as one or more bytes. An example of
   * interrupt data is the coordinates from a pointing device. Although an
   * explicit timing rate is not required, interactive data may have response
   * time bounds that the USB must support.
   */
  INTERRUPT((byte) 0x03);
  private final byte byteCode;
  private static final byte MASK = (byte) 0x03;

  private EDataFlowtype(byte byteCode) {
    this.byteCode = byteCode;
  }

  /**
   * Get the byte code corresponding to this instance.
   *
   * @return the byte code
   */
  public byte getByteCode() {
    return byteCode;
  }

  /**
   * Get the byte mask used to identify the values from a bmAttributes byte.
   *
   * @return The bit mask value.
   */
  public static byte getMASK() {
    return MASK;
  }

  /**
   * Get a EDataFlowtype instance from a Standard Endpoint Descriptor
   * bmAttributes byte.
   *
   * @param bmAttributes the bmAttributes byte
   * @return the corresponding EDataFlowtype instance
   */
  public static EDataFlowtype fromByte(byte bmAttributes) {
    for (EDataFlowtype type : EDataFlowtype.values()) {
      if ((bmAttributes & MASK) == type.getByteCode()) {
        return type;
      }
    }
    return null;
  }

}
