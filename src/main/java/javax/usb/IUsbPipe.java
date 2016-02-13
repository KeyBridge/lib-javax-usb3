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

import java.util.List;
import javax.usb.event.IUsbPipeListener;
import javax.usb.exception.*;

/**
 * Interface for a USB pipe.
 * <p>
 * See the USB 1.1 specification sec 5.3.2 for details on USB pipes. Data flows
 * in the {@link javax.usb.UsbEndpoint#getDirection() direction} defined by the
 * associated {@link #getUsbEndpoint() endpoint}, except for Control
 * {@link javax.usb.UsbEndpoint#getType() type} pipes.
 * <p>
 * The implementation is not required to be Thread-safe. If a Thread-safe
 * IUsbPipe is required, use a
 * {@link javax.usb.util.UsbUtil#synchronizedUsbPipe(UsbPipe) synchronizedIUsbPipe}.
 * <p>
 * This pipe's configuration and interface setting must be active to use this
 * pipe. Any attempt to use a IUsbPipe belonging to an inactive configuration or
 * interface setting will throw a UsbNotActiveException.
 * <p>
 * A USB pipe is an association between an endpoint on a device and software on
 * the host. Pipes represent the ability to move data between software on the
 * host via a memory buffer and an endpoint on a device. There are two mutually
 * exclusive pipe communication modes: <ul>
 * <li> Stream: Data moving through a pipe has no USB-defined structure </li>
 * <li> Message: Data moving through a pipe has some USB-defined structure
 * </li></ul>
 * The USB does not interpret the content of data it delivers through a pipe.
 * Even though a message pipe requires that data be structured according to USB
 * definitions, the content of the data is not interpreted by the USB.
 * <p>
 * The pipe that consists of the two endpoints with endpoint number zero is
 * called the Default Control Pipe. This pipe is always available once a device
 * is powered and has received a bus reset. Other pipes come into existence when
 * a USB device is configured. The Default Control Pipe is used by the USB
 * System Software to determine device identification and configuration
 * requirements and to configure the device. The Default Control Pipe can also
 * be used by device-specific software after the device is configured.
 * <p>
 * Additionally, pipes have the following associated with them: <ul>
 * <li> A claim on USB bus access and bandwidth usage.</li>
 * <li> A transfer type.</li>
 * <li> The associated endpoint’s characteristics, such as directionality and
 * maximum data payload sizes. The data payload is the data that is carried in
 * the data field of a data packet within a bus transaction (as defined in
 * Chapter 8).</li></ul>
 * A software client normally requests data transfers via I/O Request Packets
 * (IRPs) to a pipe and then either waits or is notified when they are
 * completed. Details about IRPs are defined in an operating system- specific
 * manner.
 * <p>
 * USB bandwidth is allocated among pipes. The USB allocates bandwidth for some
 * pipes when a pipe is established. USB devices are required to provide some
 * buffering of data. It is assumed that USB devices requiring more bandwidth
 * are capable of providing larger buffers. The goal for the USB architecture is
 * to ensure that buffering-induced hardware delay is bounded to within a few
 * milliseconds. The USB’s bandwidth capacity can be allocated among many
 * different data streams. This allows a wide range of devices to be attached to
 * the USB. Further, different device bit rates, with a wide dynamic range, can
 * be concurrently supported. The USB Specification defines the rules for how
 * each transfer type is allowed access to the bus.
 *
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public interface IUsbPipe {

  /**
   * Open this IUsbPipe.
   * <p>
   * The pipe cannot be used for communication until it is open.
   * <p>
   * The implementation should, to whatever extent the platform allows, try to
   * ensure the pipe is usable (not in error) before returning successfully.
   * <p>
   * If the pipe has already been opened, this will not succeed.
   *
   * @throws UsbException             If the IUsbPipe could not be opened.
   * @exception UsbNotActiveException    If the config or interface setting is
   *                                     not active.
   * @exception UsbNotClaimedException   If the interface is not claimed.
   * @exception UsbDisconnectedException If this pipe (device) has been
   *                                     disconnected.
   */
  public void open() throws UsbException, UsbNotActiveException, UsbNotClaimedException, UsbDisconnectedException;

  /**
   * Close this IUsbPipe.
   * <p>
   * The pipe can only be closed while no submissions are pending. All
   * submissions can be aborted by
   * {@link #abortAllSubmissions() abortAllSubmissions}.
   * <p>
   * If the pipe is already closed, this fails.
   *
   * @throws UsbException             If the IUsbPipe could not be closed.
   * @throws UsbNotActiveException    If the IUsbPipe is not active.
   * @throws UsbNotOpenException      If the IUsbPipe is not open.
   * @exception UsbDisconnectedException If this pipe (device) has been
   *                                     disconnected.
   */
  public void close() throws UsbException, UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException;

  /**
   * If this pipe is active.
   * <p>
   * This pipe is active only if it belongs to an
   * {@link javax.usb.UsbConfiguration#isActive() active configuration} and
   * {@link javax.usb.UsbInterface#isActive() interface setting}, otherwise it
   * is inactive. This IUsbPipe cannot be used if inactive.
   *
   * @return If this IUsbPipe is active.
   */
  public boolean isActive();

  /**
   * If this pipe is open.
   * <p>
   * This is true after a sucessful {@link #open() open} until a successful
   * {@link #close() close}.
   * <p>
   * If this pipe is not {@link #isActive() active}, this returns false.
   *
   * @return If this IUsbPipe is open.
   */
  public boolean isOpen();

  /**
   * Get this pipe's IUsbEndpoint.
   *
   * @return The associated endpoint.
   */
  public IUsbEndpoint getUsbEndpoint();

  /**
   * Synchronously submit a byte[] to the IUsbPipe.
   * <p>
   * This can be used for input and output. This may only be called when the
   * pipe is {@link #isOpen() open}. The implementation must support multiple
   * (queued) submissions. There is no maximum size restriction; the
   * implementation will segment the buffer into multiple transactions if
   * required. There may be a minimum size, but it will not be more than the
   * {@link javax.usb.UsbEndpointDescriptor#wMaxPacketSize() maximum packet size}.
   * <p>
   * This will block until either all data is transferred or an error occurs.
   * Short packets indicate either the end of data or an error.
   * <p>
   * The return value will indicate the number of bytes successfully transferred
   * to or from the target endpoint (depending on direction). The return value
   * will never exceed the total size of the provided buffer. If the operation
   * was not successful the UsbException will accurately reflect the cause of
   * the error.
   * <p>
   * Short packets are accepted. There is no way to disable short packet
   * acceptance using this method. See the USB 1.1 specification sec 5.3.2 for
   * details on short packets and short packet detection.
   *
   * @param data The buffer to use.
   * @return The number of bytes actually transferred.
   * @exception UsbException             If an error occurs.
   * @exception UsbNotActiveException    If the pipe is not
   *                                     {@link #isActive() active}.
   * @exception UsbNotOpenException      If the pipe is not
   *                                     {@link #isOpen() open}.
   * @exception IllegalArgumentException If the data is null.
   * @exception UsbDisconnectedException If this pipe (device) has been
   *                                     disconnected.
   */
  public int syncSubmit(byte[] data) throws UsbException, UsbNotActiveException, UsbNotOpenException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Asynchronously submit a byte[] to the IUsbPipe.
   * <p>
   * This can be used for input and output. This may only be called when the
   * pipe is {@link #isOpen() open}. The implementation must support multiple
   * (queued) submissions. There is no maximum size restriction; the
   * implementation will segment the buffer into multiple transactions if
   * required. There may be a minimum size, but it will not be more than the
   * {@link javax.usb.UsbEndpointDescriptor#wMaxPacketSize() maximum packet size}.
   * <p>
   * The implementation should only place this on a queue, or perform whatever
   * minimal processing is required, and then return. This method will not block
   * until the submission is complete.
   * <p>
   * The returned IUsbIrp will represent the submission.
   * <p>
   * Short packets are accepted. There is no way to disable short packet
   * acceptance using this method. See the USB 1.1 specification sec 5.3.2 for
   * details on short packets and short packet detection.
   *
   * @param data The buffer to use.
   * @return A IUsbIrp representing the submission.
   * @exception UsbException             If an error occurs.
   * @exception UsbNotActiveException    If the pipe is not
   *                                     {@link #isActive() active}.
   * @exception UsbNotOpenException      If the pipe is not
   *                                     {@link #isOpen() open}.
   * @exception IllegalArgumentException If the data is null.
   * @exception UsbDisconnectedException If this pipe (device) has been
   *                                     disconnected.
   */
  public IUsbIrp asyncSubmit(byte[] data) throws UsbException, UsbNotActiveException, UsbNotOpenException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Synchronously submit a IUsbIrp to the IUsbPipe.
   * <p>
   * This can be used for input and output. This may only be called when the
   * pipe is {@link #isOpen() open}. The implementation must support multiple
   * (queued) submissions. There is no maximum size restriction; the
   * implementation will segment the buffer into multiple transactions if
   * required. There may be a minimum size, but it will not be more than the
   * {@link javax.usb.UsbEndpointDescriptor#wMaxPacketSize() maximum packet size}.
   * <p>
   * This will block until either all data is transferred or an error occurs.
   * Short packets indicate either the end of data or an error.
   * <p>
   * If this is a Control {@link javax.usb.UsbEndpoint#getType() type} pipe, the
   * IUsbIrp must be a {@link javax.usb.IUsbControlIrp IUsbControlIrp}.
   *
   * @param irp A IUsbIrp to use for the submission.
   * @exception UsbException             If an error occurs.
   * @exception UsbNotActiveException    If the pipe is not
   *                                     {@link #isActive() active}.
   * @exception UsbNotOpenException      If the pipe is not
   *                                     {@link #isOpen() open}.
   * @throws IllegalArgumentException If the IUsbIrp is not valid.
   * @exception UsbDisconnectedException If this pipe (device) has been
   *                                     disconnected.
   */
  public void syncSubmit(IUsbIrp irp) throws UsbException, UsbNotActiveException, UsbNotOpenException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Asynchronously submit a IUsbIrp to the IUsbPipe.
   * <p>
   * This can be used for input and output. This may only be called when the
   * pipe is {@link #isOpen() open}. The implementation must support multiple
   * (queued) submissions. There is no maximum size restriction; the
   * implementation will segment the buffer into multiple transactions if
   * required. There may be a minimum size, but it will not be more than the
   * {@link javax.usb.UsbEndpointDescriptor#wMaxPacketSize() maximum packet size}.
   * <p>
   * The implementation should only place this on a queue, or perform whatever
   * minimal processing is required, and then return. This method will not block
   * until the submission is complete.
   * <p>
   * If this is a Control {@link javax.usb.UsbEndpoint#getType() type} pipe, the
   * IUsbIrp must be a {@link javax.usb.IUsbControlIrp IUsbControlIrp}.
   *
   * @param irp The IUsbIrp to use for the submission.
   * @exception UsbException             If an error occurs.
   * @exception UsbNotActiveException    If the pipe is not
   *                                     {@link #isActive() active}.
   * @exception UsbNotOpenException      If the pipe is not
   *                                     {@link #isOpen() open}.
   * @throws IllegalArgumentException If the IUsbIrp is not valid.
   * @exception UsbDisconnectedException If this pipe (device) has been
   *                                     disconnected.
   */
  public void asyncSubmit(IUsbIrp irp) throws UsbException, UsbNotActiveException, UsbNotOpenException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Synchronously submit a List of IUsbIrps to the IUsbPipe.
   * <p>
   * This is exactly the same as calling {@link #syncSubmit(UsbIrp) syncSubmit}
   * multiple times, except:
   * <ul>
   * <li>The IUsbIrps will be submitted synchronously on the Java level; and if
   * the implementation permits, synchronously on the native level.</li>
   * <li>The implementation may optimize the submissions, especially in the case
   * of Isochronous transfers.</li>
   * <li>If any of the IUsbIrps fails (initial submission or IUsbIrp status),
   * the implementation may either continue with the remaining IUsbIrps, or fail
   * all remaining IUsbIrps.</li>
   * </ul>
   * <p>
   * If this is a Control {@link javax.usb.UsbEndpoint#getType() type} pipe, the
   * IUsbIrps must be {@link javax.usb.IUsbControlIrp IUsbControlIrps}.
   *
   * @param list The List of IUsbIrps.
   * @exception UsbException             If an error occurs.
   * @exception UsbNotActiveException    If the pipe is not
   *                                     {@link #isActive() active}.
   * @exception UsbNotOpenException      If the pipe is not
   *                                     {@link #isOpen() open}.
   * @throws IllegalArgumentException If the list is empty or contains any
   *                                  non-IUsbIrp objects, or those IUsbIrp(s)
   *                                  are invalid.
   * @exception UsbDisconnectedException If this pipe (device) has been
   *                                     disconnected.
   */
  public void syncSubmit(List<IUsbIrp> list) throws UsbException, UsbNotActiveException, UsbNotOpenException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Asynchronously submit a List of IUsbIrps to the IUsbPipe.
   * <p>
   * This is exactly the same as calling
   * {@link #asyncSubmit(UsbIrp) asyncSubmit} multiple times, except:
   * <ul>
   * <li>The IUsbIrps will be submitted synchronously on the Java level; and if
   * the implementation permits, synchronously on the native level.</li>
   * <li>The implementation may optimize the submissions, especially in the case
   * of Isochronous transfers.</li>
   * <li>If any of the IUsbIrps fails (initial submissions only), the
   * implementation may either continue with the remaining IUsbIrps, or fail all
   * remaining IUsbIrps.</li>
   * </ul>
   * <p>
   * If this is a Control {@link javax.usb.UsbEndpoint#getType() type} pipe, the
   * IUsbIrps must be {@link javax.usb.IUsbControlIrp IUsbControlIrps}.
   *
   * @param list The List of IUsbIrps.
   * @exception UsbException             If an error occurs.
   * @exception UsbNotActiveException    If the pipe is not
   *                                     {@link #isActive() active}.
   * @exception UsbNotOpenException      If the pipe is not
   *                                     {@link #isOpen() open}.
   * @throws IllegalArgumentException If the list is empty or contains any
   *                                  non-IUsbIrp objects, or those IUsbIrp(s)
   *                                  are invalid.
   * @exception UsbDisconnectedException If this pipe (device) has been
   *                                     disconnected.
   */
  public void asyncSubmit(List<IUsbIrp> list) throws UsbException, UsbNotActiveException, UsbNotOpenException, IllegalArgumentException, UsbDisconnectedException;

  /**
   * Stop all submissions in progress.
   * <p>
   * This will abort all submission in progress on the pipe, and block until all
   * submissions are complete. There will be no submissions pending after this
   * returns.
   *
   * @exception UsbNotActiveException    If the pipe is not
   *                                     {@link #isActive() active}.
   * @exception UsbNotOpenException      If the pipe is not
   *                                     {@link #isOpen() open}.
   * @exception UsbDisconnectedException If this pipe (device) has been
   *                                     disconnected.
   */
  public void abortAllSubmissions() throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException;

  /**
   * Create a IUsbIrp.
   * <p>
   * This creates a IUsbIrp that may be optimized for use on this IUsbPipe.
   * Using this IUsbIrp instead of a
   * {@link javax.usb.util.DefaultUsbIrp DefaultIUsbIrp} may increase
   * performance or decrease memory requirements.
   * <p>
   * The IUsbPipe cannot require this IUsbIrp to be used, all submit methods
   * <i>must</i> accept any IUsbIrp implementation (or IUsbControlIrp
   * implementation if this is a Control-type IUsbPipe).
   *
   * @return A IUsbIrp ready for use.
   */
  public IUsbIrp createUsbIrp();

  /**
   * Create a IUsbControlIrp.
   * <p>
   * This creates a IUsbControlIrp that may be optimized for use on this
   * IUsbPipe. Using this IUsbControlIrp instead of a
   * {@link javax.usb.util.DefaultUsbControlIrp DefaultIUsbControlIrp} may
   * increase performance or decrease memory requirements.
   * <p>
   * The IUsbPipe cannot require this IUsbControlIrp to be used, all submit
   * methods <i>must</i> accept any IUsbControlIrp implementation.
   * <p>
   * Note that if this is not a Control-type IUsbPipe, none of the setup packet
   * fields will be used.
   *
   * @param bmRequestType The bmRequestType.
   * @param bRequest      The bRequest.
   * @param wValue        The wValue.
   * @param wIndex        The wIndex.
   * @return A IUsbControlIrp ready for use.
   */
  public IUsbControlIrp createUsbControlIrp(byte bmRequestType, byte bRequest, short wValue, short wIndex);

  /**
   * Adds the listener.
   *
   * @param listener The IIUsbPipeListener.
   */
  public void addUsbPipeListener(IUsbPipeListener listener);

  /**
   * Removes the listener.
   *
   * @param listener The IIUsbPipeListener.
   */
  public void removeUsbPipeListener(IUsbPipeListener listener);

}
