/**
 * Original Copyright (c) 1999 - 2001, International Business Machines
 * Corporation. All Rights Reserved. Provided and licensed under the terms and
 * conditions of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 * <p>
 * Modifications and improvements Copyright (c) 2014 Key Bridge Global LLC. All
 * Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javax.usb;

import javax.usb.exception.UsbException;

/**
 * Interface for a USB I/O Request Packet (IRP).
 * <p>
 * A software client normally requests data transfers via I/O Request Packets
 * (IRPs) to a pipe and then either waits or is notified when they are
 * completed. Details about IRPs are defined in an operating system- specific
 * manner.
 * <p>
 * A UsbIrp has methods that can be divided into four different categories; the
 * data buffer, status, short packets, and the UsbException.
 * <p>
 * An IRP may require multiple data payloads to move the client data over the
 * bus. The data payloads for such a multiple data payload IRP are expected to
 * be of the maximum packet size until the last data payload that contains the
 * remainder of the overall IRP.
 * <p>
 * Some USB communication requires additional metadata that describes how the
 * actual data should be handled when being transferred. This IUsbIrp
 * encapsulates the actual data buffer, as well as other metadata that gives the
 * user more control and knowledge over how the data is handled.
 * <p>
 * Before submitting this, at least some of these (depending on IUsbIrp
 * implementation) must be performed:
 * <ul>
 * <li>The {@link #getData() data} must be {@link #setData(byte[]) set}.</li>
 * <li>The {@link #getOffset() data offset}, may be
 * {@link #setOffset(int) set}.</li>
 * <li>The {@link #getLength() data length} may be
 * {@link #setLength(int) set}.</li>
 * <li>The {@link #getAcceptShortPacket() Short Packet policy} may be
 * {@link #setAcceptShortPacket(boolean) set}.</li>
 * <li>The {@link #getUsbException() UsbException} must be null (and
 * {@link #isUsbException() isUsbException} must be false).</li>
 * <li>The {@link #isComplete() complete state} must be false.</li>
 * </ul>
 * Any IUsbIrp implementation must behave as specified in this interface's
 * documentation, including the specified defaults. Note that
 * {@link #setData(byte[]) setData} also sets the offset to 0 and the length to
 * data.length; if other values should be used, use the
 * {@link #setData(byte[],int,int) 3-parameter setData} or set the
 * {@link #setOffset(int) offset} and {@link #setLength(int) length} with their
 * setters <i>after</i> setting the data.
 * <p>
 * The javax.usb implementation will set the
 * {@link #getActualLength() data length} or, if unsuccessful, the
 * {@link #getUsbException() UsbException}, after processing. Finally, it will
 * call {@link #complete() complete}.
 * <p>
 * See the USB 1.1 specification section 5.3.2 for details on USB IRPs. The IRP
 * defined in this API has more than is mentioned in the USB 1.1 specification.
 * <p>
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
public interface IUsbIrp {

  /**
   * Get the data. The UsbIrp data buffer is simply a byte[] array.
   * <p>
   * This defaults to an empty byte[]. This will never be null.
   * <p>
   * @return The data. A non-null byte[] array.
   */
  public byte[] getData();

  /**
   * Set the data.
   * <p>
   * This {@link #setOffset(int) sets the offset} to 0, and
   * {@link #setLength(int) sets the length} to data.length; if those values are
   * inappropriate, use the {@link #setData(byte[],int,int) other setData}.
   * <p>
   * @param data The data.
   * @exception IllegalArgumentException If the data is null.
   */
  public void setData(byte[] data);

  /**
   * Set the data.
   * <p>
   * This sets the data, offset, and length to the specified values.
   * <p>
   * @param data   The data.
   * @param offset The offset. Indicates what offset into the data buffer the
   *               implementation should use when transferring data. If the
   *               offset is zero, data will be transferred starting at the
   *               beginning of the byte[], if the offset is above zero, data
   *               starting at the offset into the byte[] will be used when
   *               communicating with the device.
   * @param length The length of data to transfer with the device.
   * @exception IllegalArgumentException If the data is null, or offset and/or
   *                                     length is negative.
   */
  public void setData(byte[] data, int offset, int length);

  /**
   * Get the starting offset of the data.
   * <p>
   * This indicates the starting byte in the data, or what offset into the data
   * buffer the implementation should use when transferring data. If the offset
   * is zero, data will be transferred starting at the beginning of the byte[],
   * if the offset is above zero, data starting at the offset into the byte[]
   * will be used when communicating with the device.
   * <p>
   * This defaults to 0, and this is set to 0 by
   * {@link #setData(byte[]) the 1-parameter setData}. This will never be
   * negative.
   * <p>
   * @return The offset.
   */
  public int getOffset();

  /**
   * The amount of data to transfer.
   * <p>
   * This indicates the amount of data to transfer.
   * <p>
   * This defaults to 0, and this is set to data.length by
   * {@link #setData(byte[]) the 1-parameter setData}. This will never be
   * negative.
   * <p>
   * @return The amount of data to transfer.
   */
  public int getLength();

  /**
   * Set the amount of data to transfer.
   * <p>
   * @param length The amount of data to transfer.
   * @exception IllegalArgumentException If the length is negative.
   */
  public void setLength(int length);

  /**
   * Set the offset.
   * <p>
   * @param offset The offset.
   * @exception IllegalArgumentException If the offset is negative.
   */
  public void setOffset(int offset);

  /**
   * The amount of data that was transferred.
   * <p>
   * This defaults to 0, and is set by the implementation during/after
   * submission (if successful). This will never be negative. If
   * {@link #isUsbException() isUsbException} is true, this value is undefined.
   * <p>
   * @return The amount of data that was transferred.
   */
  public int getActualLength();

  /**
   * Set the amount of data that was transferred.
   * <p>
   * The implementation will set this to the amount of data actually
   * transferred. The implementation will set this before calling
   * {@link #complete() complete}, regardless of whether the submission was
   * successful or not.
   * <p>
   * @param length The amount of data that was transferred.
   * @exception IllegalArgumentException If the length is negative.
   */
  public void setActualLength(int length);

  /**
   * If a UsbException occured.
   * <p>
   * If this is true, the {@link #getActualLength() actual length} is undefined.
   * <p>
   * @return If a UsbException occurred.
   */
  public boolean isUsbException();

  /**
   * Get the UsbException.
   * <p>
   * If no UsbException occurred, this returns null.
   * <p>
   * @return The UsbException, or null.
   */
  public UsbException getUsbException();

  /**
   * Set the UsbException.
   * <p>
   * @param usbException The UsbException.
   */
  public void setUsbException(UsbException usbException);

  /**
   * If short packets should be accepted.
   * <p>
   * See the USB 1.1 specification sec 5.3.2 for details on short packets and
   * short packet detection. If short packets are accepted (true), a short
   * packet indicates the end of data. If short packets are not accepted
   * (false), a short packet will generate an UsbException. The default is true.
   * <p>
   * @return If short packects should be accepted.
   */
  public boolean getAcceptShortPacket();

  /**
   * Set if short packets should be accepted. Sets the policy either accept or
   * reject short packets.
   * <p>
   * This should be set by the application.
   * <p>
   * @param accept If short packets should be accepted.
   */
  public void setAcceptShortPacket(boolean accept);

  /**
   * If this has completed.
   * <p>
   * This must be false before use.
   * <p>
   * @return If this IUsbIrp has completed.
   */
  public boolean isComplete();

  /**
   * Set this as complete or not.
   * <p>
   * This is what {@link #isComplete() isComplete} returns.
   * <p>
   * @param complete If this is complete or not.
   */
  public void setComplete(boolean complete);

  /**
   * Set this as complete.
   * <p>
   * This is the last method the implementation calls; it indicates the IUsbIrp
   * has completed. The implementation will
   * {@link #setActualLength(int) set the actual length}, even if the submission
   * was unsuccessful, before calling this. The implementation will
   * {@link #setUsbException(UsbException) set the UsbException}, if
   * appropriate, before calling this.
   * <p>
   * After calling this {@link #isComplete() isComplete} will return true.
   */
  public void complete();

  /**
   * Wait until {@link #isComplete() complete}.
   * <p>
   * This will block until this is {@link #isComplete() complete}.
   */
  public void waitUntilComplete();

  /**
   * Wait until {@link #isComplete() complete}, or the timeout has expired.
   * <p>
   * This will block until this is {@link #isComplete() complete}, or the
   * timeout has expired. The timeout is ignored if it is 0 or less, i.e. this
   * will behave as the {@link #waitUntilComplete() no-timeout method}.
   * <p>
   * @param timeout The maximum number of milliseconds to wait.
   */
  public void waitUntilComplete(long timeout);
}
