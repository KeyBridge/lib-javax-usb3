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

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.usb.*;
import javax.usb.event.*;
import javax.usb.exception.UsbException;
import javax.usb.exception.UsbNotActiveException;
import javax.usb.exception.UsbNotClaimedException;
import javax.usb.exception.UsbNotOpenException;

/**
 * General utility methods.
 * <p>
 * @author Dan Streetman
 */
@SuppressWarnings("AssignmentToMethodParameter")
public class UsbUtil {


  /**
   * Get a String description of the specified device-speed Object.
   * <p>
   * The String will be one of:
   * <ul>
   * <li>Low</li>
   * <li>Full</li>
   * <li>Unknown</li>
   * <li>null</li>
   * <li>Invalid</li>
   * </ul>
   * The string "null" is used for a null Object. The string "Invalid" is used
   * for an Object that does not correspond to any of those defined in
   * {@link javax.usb.IUsbConst IUsbConst}.
   * <p>
   * @param object The device-speed Object.
   * @return A String representing the speed Object.
   * @see UsbConst#DEVICE_SPEED_LOW Low Speed.
   * @see UsbConst#DEVICE_SPEED_FULL Full Speed.
   * @see UsbConst#DEVICE_SPEED_UNKNOWN Unknown Speed.
   */
  public static String getSpeedString(Object object) {
    if (IUsbConst.DEVICE_SPEED_LOW == object) {
      return "Low";
    }
    if (IUsbConst.DEVICE_SPEED_FULL == object) {
      return "Full";
    }
    if (IUsbConst.DEVICE_SPEED_UNKNOWN == object) {
      return "Unknown";
    }
    if (null == object) {
      return "null";
    }

    return "Invalid";
  }

  /**
   * Create a synchronized IUsbDevice.
   * <p>
   * @param usbDevice The unsynchronized IUsbDevice.
   * @return A synchronized IUsbDevice.
   */
  public static IUsbDevice synchronizedUsbDevice(IUsbDevice usbDevice) {
    return new UsbUtil.SynchronizedUsbDevice(usbDevice);
  }

  /**
   * Create a synchronized IUsbPipe.
   * <p>
   * @param usbPipe The unsynchronized IUsbPipe.
   * @return A synchronized IUsbPipe.
   */
  public static IUsbPipe synchronizedUsbPipe(IUsbPipe usbPipe) {
    return new UsbUtil.SynchronizedUsbPipe(usbPipe);
  }

  /**
   * A synchronized IUsbDevice wrapper implementation.
   */
  public static class SynchronizedUsbDevice implements IUsbDevice {

    private final IUsbDevice usbDevice;
    private final Object submitLock = new Object();
    private final Object listenerLock = new Object();

    public SynchronizedUsbDevice(IUsbDevice usbDevice) {
      this.usbDevice = usbDevice;
    }

    @Override
    public IUsbPort getParentUsbPort() {
      return usbDevice.getParentUsbPort();
    }

    @Override
    public boolean isUsbHub() {
      return usbDevice.isUsbHub();
    }

    @Override
    public String getManufacturerString() throws UsbException, UnsupportedEncodingException {
      synchronized (submitLock) {
        return usbDevice.getManufacturerString();
      }
    }

    @Override
    public String getSerialNumberString() throws UsbException, UnsupportedEncodingException {
      synchronized (submitLock) {
        return usbDevice.getSerialNumberString();
      }
    }

    @Override
    public String getProductString() throws UsbException, UnsupportedEncodingException {
      synchronized (submitLock) {
        return usbDevice.getProductString();
      }
    }

    @Override
    public Object getSpeed() {
      return usbDevice.getSpeed();
    }

    @Override
    public List<IUsbConfiguration> getUsbConfigurations() {
      return usbDevice.getUsbConfigurations();
    }

    @Override
    public IUsbConfiguration getUsbConfiguration(byte number) {
      return usbDevice.getUsbConfiguration(number);
    }

    @Override
    public boolean containsUsbConfiguration(byte number) {
      return usbDevice.containsUsbConfiguration(number);
    }

    @Override
    public byte getActiveUsbConfigurationNumber() {
      return usbDevice.getActiveUsbConfigurationNumber();
    }

    @Override
    public IUsbConfiguration getActiveUsbConfiguration() {
      return usbDevice.getActiveUsbConfiguration();
    }

    @Override
    public boolean isConfigured() {
      return usbDevice.isConfigured();
    }

    @Override
    public IUsbDeviceDescriptor getUsbDeviceDescriptor() {
      return usbDevice.getUsbDeviceDescriptor();
    }

    @Override
    public IUsbStringDescriptor getUsbStringDescriptor(byte index) throws UsbException {
      synchronized (submitLock) {
        return usbDevice.getUsbStringDescriptor(index);
      }
    }

    @Override
    public String getString(byte index) throws UsbException, UnsupportedEncodingException {
      synchronized (submitLock) {
        return usbDevice.getString(index);
      }
    }

    @Override
    public void syncSubmit(IUsbControlIrp irp) throws UsbException {
      synchronized (submitLock) {
        usbDevice.syncSubmit(irp);
      }
    }

    @Override
    public void asyncSubmit(IUsbControlIrp irp) throws UsbException {
      synchronized (submitLock) {
        usbDevice.asyncSubmit(irp);
      }
    }

    @Override
    public void syncSubmit(List<IUsbControlIrp> list) throws UsbException {
      synchronized (submitLock) {
        usbDevice.syncSubmit(list);
      }
    }

    @Override
    public void asyncSubmit(List<IUsbControlIrp> list) throws UsbException {
      synchronized (submitLock) {
        usbDevice.asyncSubmit(list);
      }
    }

    @Override
    public IUsbControlIrp createUsbControlIrp(byte bmRequestType, byte bRequest, short wValue, short wIndex) {
      return usbDevice.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
    }

    @Override
    public void addUsbDeviceListener(IUsbDeviceListener listener) {
      synchronized (listenerLock) {
        usbDevice.addUsbDeviceListener(listener);
      }
    }

    @Override
    public void removeUsbDeviceListener(IUsbDeviceListener listener) {
      synchronized (listenerLock) {
        usbDevice.removeUsbDeviceListener(listener);
      }
    }
  }

  /**
   * A synchronized IUsbPipe wrapper implementation.
   * <p>
   * Not all methods are synchronized; the open/close methods are synchronized
   * to each other, and the submission and abort methods are synchronized to
   * each other.
   */
  public static class SynchronizedUsbPipe implements IUsbPipe {

    private final IUsbPipe usbPipe;
    private final Object openLock = new Object();
    private final Object submitLock = new Object();

    public SynchronizedUsbPipe(IUsbPipe usbPipe) {
      this.usbPipe = usbPipe;
    }

    @Override
    public void open() throws UsbException, UsbNotActiveException, UsbNotClaimedException {
      synchronized (openLock) {
        usbPipe.open();
      }
    }

    @Override
    public void close() throws UsbException, UsbNotOpenException {
      synchronized (openLock) {
        usbPipe.close();
      }
    }

    @Override
    public boolean isActive() {
      return usbPipe.isActive();
    }

    @Override
    public boolean isOpen() {
      return usbPipe.isOpen();
    }

    @Override
    public IUsbEndpoint getUsbEndpoint() {
      return usbPipe.getUsbEndpoint();
    }

    @Override
    public int syncSubmit(byte[] data) throws UsbException, UsbNotOpenException {
      synchronized (submitLock) {
        return usbPipe.syncSubmit(data);
      }
    }

    @Override
    public IUsbIrp asyncSubmit(byte[] data) throws UsbException, UsbNotOpenException {
      synchronized (submitLock) {
        return usbPipe.asyncSubmit(data);
      }
    }

    @Override
    public void syncSubmit(IUsbIrp irp) throws UsbException, UsbNotOpenException {
      synchronized (submitLock) {
        usbPipe.syncSubmit(irp);
      }
    }

    @Override
    public void asyncSubmit(IUsbIrp irp) throws UsbException, UsbNotOpenException {
      synchronized (submitLock) {
        usbPipe.asyncSubmit(irp);
      }
    }

    @Override
    public void syncSubmit(List<IUsbIrp> list) throws UsbException, UsbNotOpenException {
      synchronized (submitLock) {
        usbPipe.syncSubmit(list);
      }
    }

    @Override
    public void asyncSubmit(List<IUsbIrp> list) throws UsbException, UsbNotOpenException {
      synchronized (submitLock) {
        usbPipe.asyncSubmit(list);
      }
    }

    @Override
    public void abortAllSubmissions() throws UsbNotOpenException {
      synchronized (submitLock) {
        usbPipe.abortAllSubmissions();
      }
    }

    @Override
    public IUsbIrp createUsbIrp() {
      return usbPipe.createUsbIrp();
    }

    @Override
    public IUsbControlIrp createUsbControlIrp(byte bmRequestType, byte bRequest, short wValue, short wIndex) {
      return usbPipe.createUsbControlIrp(bmRequestType, bRequest, wValue, wIndex);
    }

    @Override
    public void addUsbPipeListener(IUsbPipeListener listener) {
      usbPipe.addUsbPipeListener(listener);
    }

    @Override
    public void removeUsbPipeListener(IUsbPipeListener listener) {
      usbPipe.removeUsbPipeListener(listener);
    }
  }

}
