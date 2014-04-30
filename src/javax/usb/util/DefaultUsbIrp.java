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
package javax.usb.util;

import java.util.Arrays;
import javax.usb.*;

/**
 * UsbIrp default implementation.
 * <p>
 * The behavior and defaults follow those defined in the
 * {@link javax.usb.UsbIrp interface}. Any of the fields may be updated if the
 * default is not appropriate; in most cases the {@link #getData() data} will be
 * the only field that needs to be {@link #setData(byte[]) set}.
 * <p>
 * @author Dan Streetman
 */
@SuppressWarnings("ProtectedField")
public class DefaultUsbIrp implements UsbIrp {

  /**
   * Constructor.
   */
  public DefaultUsbIrp() {
  }

  /**
   * Constructor.
   * <p>
   * @param data The data.
   * @exception IllegalArgumentException If the data is null.
   */
  public DefaultUsbIrp(byte[] data) {
    setData(data);
  }

  /**
   * Constructor.
   * <p>
   * @param data        The data.
   * @param offset      The offset.
   * @param length      The length.
   * @param shortPacket The Short Packet policy.
   * @exception IllegalArgumentException If the data is null, or the offset
   *                                     and/or length is negative.
   */
  public DefaultUsbIrp(byte[] data, int offset, int length, boolean shortPacket) {
    setData(data, offset, length);
    setAcceptShortPacket(shortPacket);
  }

  /**
   * Get the data.
   * <p>
   * @return The data.
   */
  @Override
  public byte[] getData() {
    return data != null ? Arrays.copyOf(data, data.length) : null;
  }

  /**
   * Get the offset.
   * <p>
   * @return The offset.
   */
  @Override
  public int getOffset() {
    return offset;
  }

  /**
   * Get the length.
   * <p>
   * @return The length.
   */
  @Override
  public int getLength() {
    return length;
  }

  /**
   * Get the actual length.
   * <p>
   * @return The actual length.
   */
  @Override
  public int getActualLength() {
    return actualLength;
  }

  /**
   * Set the data, offset, and length.
   * <p>
   * @param d The data.
   * @param o The offset.
   * @param l The length.
   * @exception IllegalArgumentException If the data is null, or the offset
   *                                     and/or length is negative.
   */
  @Override
  public final void setData(byte[] d, int o, int l) throws IllegalArgumentException {
    if (null == d) {
      throw new IllegalArgumentException("Data cannot be null.");
    }

    data = Arrays.copyOf(d, d.length);
    setOffset(o);
    setLength(l);
  }

  /**
   * Set the data.
   * <p>
   * @param d The data.
   * @exception IllegalArgumentException If the data is null.
   */
  @Override
  public final void setData(byte[] d) throws IllegalArgumentException {
    if (null == d) {
      throw new IllegalArgumentException("Data cannot be null.");
    }

    setData(d, 0, d.length);
  }

  /**
   * Set the offset.
   * <p>
   * @param o The offset.
   * @exception IllegalArgumentException If the offset is negative.
   */
  @Override
  public void setOffset(int o) throws IllegalArgumentException {
    if (0 > o) {
      throw new IllegalArgumentException("Offset cannot be negative.");
    }

    offset = o;
  }

  /**
   * Set the length.
   * <p>
   * @param l The length.
   * @exception IllegalArgumentException If the length is negative.
   */
  @Override
  public void setLength(int l) throws IllegalArgumentException {
    if (0 > l) {
      throw new IllegalArgumentException("Length cannot be negative");
    }

    length = l;
  }

  /**
   * Set the actual length.
   * <p>
   * @param l The actual length.
   * @exception IllegalArgumentException If the length is negative.
   */
  @Override
  public void setActualLength(int l) throws IllegalArgumentException {
    if (0 > l) {
      throw new IllegalArgumentException("Actual length cannot be negative");
    }

    actualLength = l;
  }

  /**
   * If a UsbException occurred.
   * <p>
   * @return If a UsbException occurred.
   */
  @Override
  public boolean isUsbException() {
    return (null != getUsbException());
  }

  /**
   * Get the UsbException.
   * <p>
   * @return The UsbException, or null.
   */
  @Override
  public UsbException getUsbException() {
    return usbException;
  }

  /**
   * Set the UsbException.
   * <p>
   * @param exception The UsbException.
   */
  @Override
  public void setUsbException(UsbException exception) {
    usbException = exception;
  }

  /**
   * Get the Short Packet policy.
   * <p>
   * @return The Short Packet policy.
   */
  @Override
  public boolean getAcceptShortPacket() {
    return acceptShortPacket;
  }

  /**
   * Set the Short Packet policy.
   * <p>
   * @param accept The Short Packet policy.
   */
  @Override
  public final void setAcceptShortPacket(boolean accept) {
    acceptShortPacket = accept;
  }

  /**
   * If this is complete.
   * <p>
   * @return If this is complete.
   */
  @Override
  public boolean isComplete() {
    return complete;
  }

  /**
   * Set this as complete (or not).
   * <p>
   * @param b If this is complete (or not).
   */
  @Override
  public void setComplete(boolean b) {
    complete = b;
  }

  /**
   * Complete this submission.
   * <p>
   * This will:
   * <ul>
   * <li>{@link #setComplete(boolean) Set} this
   * {@link #isComplete() complete}.</li>
   * <li>Notify all {@link #waitUntilComplete() waiting Threads}.</li>
   * </ul>
   */
  @Override
  public void complete() {
    setComplete(true);
    synchronized (waitLock) {
      waitLock.notifyAll();
    }
  }

  /**
   * Wait until {@link #isComplete() complete}.
   * <p>
   * This will block until this is {@link #isComplete() complete}.
   */
  @Override
  public void waitUntilComplete() {
    synchronized (waitLock) {
      while (!isComplete()) {
        try {
          waitLock.wait();
        } catch (InterruptedException iE) {
        }
      }
    }
  }

  /**
   * Wait until {@link #isComplete() complete}, or the timeout has expired.
   * <p>
   * This will block until this is {@link #isComplete() complete}, or the
   * timeout has expired. If the timeout is 0 or less, this behaves as the
   * {@link #waitUntilComplete() no-timeout method}.
   * <p>
   * @param timeout The maximum number of milliseconds to wait.
   */
  @Override
  public void waitUntilComplete(long timeout) {
    if (0 >= timeout) {
      waitUntilComplete();
      return;
    }

    long startTime = System.currentTimeMillis();

    synchronized (waitLock) {
      if (!isComplete() && ((System.currentTimeMillis() - startTime) < timeout)) {
        try {
          waitLock.wait(timeout);
        } catch (InterruptedException iE) {
        }
      }
    }
  }

  protected byte[] data = new byte[0];
  protected boolean complete = false;
  protected boolean acceptShortPacket = true;
  protected int offset = 0;
  protected int length = 0;
  protected int actualLength = 0;
  protected UsbException usbException = null;
  private final Object waitLock = new Object();
}
