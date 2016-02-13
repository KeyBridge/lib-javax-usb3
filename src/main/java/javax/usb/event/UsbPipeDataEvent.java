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
package javax.usb.event;

import java.util.Arrays;
import javax.usb.IUsbIrp;
import javax.usb.IUsbPipe;

/**
 * Indicates data was successfully transferred over the IUsbPipe.
 * <p>
 * This event will be fired to all listeners for all data that is transferred
 * over the pipe.
 *
 * @author Dan Streetman
 * @author E. Michael Maximilien
 */
public class UsbPipeDataEvent extends UsbPipeEvent {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * <p>
   * This should only be used if there is no IUsbIrp associated with this event.
   *
   * @param source The IUsbPipe.
   * @param d      The data.
   * @param aL     The actual length of data transferred.
   */
  public UsbPipeDataEvent(IUsbPipe source, byte[] d, int aL) {
    super(source);
    data = d != null ? Arrays.copyOf(d, d.length) : null;
    actualLength = aL;
  }

  /**
   * Constructor.
   *
   * @param source The IUsbPipe.
   * @param uI     The IUsbIrp.
   */
  public UsbPipeDataEvent(IUsbPipe source, IUsbIrp uI) {
    super(source, uI);
  }

  /**
   * Get the data.
   * <p>
   * If there is an associated IUsbIrp, this returns a new byte[] containing
   * only the actual transferred data. If there is no associated IUsbIrp, this
   * returns the actual data buffer used.
   *
   * @return The transferred data.
   */
  public byte[] getData() {
    if (hasUsbIrp()) {
      byte[] newData = new byte[getUsbIrp().getActualLength()];
      System.arraycopy(getUsbIrp().getData(), getUsbIrp().getOffset(), newData, 0, newData.length);
      return newData;
    } else {
      return data != null ? Arrays.copyOf(data, data.length) : null;
    }
  }

  /**
   * Get the actual length.
   *
   * @return The actual amount of transferred data.
   */
  public int getActualLength() {
    if (hasUsbIrp()) {
      return getUsbIrp().getActualLength();
    } else {
      return actualLength;
    }
  }

  private byte[] data = null;
  private int actualLength = 0;

}
