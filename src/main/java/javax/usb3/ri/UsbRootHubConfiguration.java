/*
 * Copyright (C) 2011 Klaus Reimer
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
package javax.usb3.ri;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.usb3.IUsbConfiguration;
import javax.usb3.IUsbConfigurationDescriptor;
import javax.usb3.IUsbDevice;
import javax.usb3.IUsbInterface;
import javax.usb3.descriptor.UsbConfigurationDescriptor;
import javax.usb3.enumerated.EDescriptorType;
import javax.usb3.exception.UsbException;
import javax.usb3.request.BMConfigurationAttributes;

/**
 * Virtual USB configuration used by the virtual USB root hub.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public final class UsbRootHubConfiguration implements IUsbConfiguration {

  /**
   * The virtual interfaces. Contains a single UsbRootHubInterface instance.
   */
  private final List<IUsbInterface> interfaces;

  /**
   * The upstream device (e.g. HOST system) to which this configuration belongs.
   */
  private final IUsbDevice device;

  /**
   * The default USB configuration descriptor. bmAttributes is 0x80 [1000 0000],
   * which sets D7 to one (required) and the other parameters to false
   * (Self-powered, Remote wakeup).
   */
  private final IUsbConfigurationDescriptor descriptor;

  /**
   * Constructor.
   *
   * @param device The device this configuration belongs to.
   */
  public UsbRootHubConfiguration(final IUsbDevice device) {
    this.device = device;
    this.interfaces = Arrays.asList(new UsbRootHubInterface(this));
    this.descriptor = new UsbConfigurationDescriptor(
      (short) (EDescriptorType.CONFIGURATION.getLength() + EDescriptorType.INTERFACE.getLength()), // wTotalLength
      (byte) 1, // bNumInterfaces
      (byte) 1, // bConfigurationValue
      (byte) 0, // iConfiguration
      BMConfigurationAttributes.getInstance(),//  (byte) 0x80, // bmAttributes
      (byte) 0); // bMaxPower

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isActive() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IUsbInterface> getUsbInterfaces() {
    return this.interfaces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IUsbInterface getUsbInterface(final byte number) {
    if (number != 0) {
      return null;
    }
    return this.interfaces.get(0);
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated Not supported on Root Hub
   */
  @Override
  public void setUsbInterface(byte number, IUsbInterface usbInterface) throws UsbException {
    throw new UnsupportedOperationException("Not supported on Root Hub.");
  }

  /**
   * {@inheritDoc}
   *
   * @return TRUE if the input number is zero.
   */
  @Override
  public boolean containsUsbInterface(final byte number) {
    return number == 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IUsbDevice getUsbDevice() {
    return this.device;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IUsbConfigurationDescriptor getUsbConfigurationDescriptor() {
    return this.descriptor;
  }

  /**
   * {@inheritDoc}
   *
   * @return null;
   */
  @Override
  public String getConfigurationString() {
    return null;
  }

  /**
   * {@inheritDoc}
   *
   * @return 1
   */
  @Override
  public int getNumSettings(byte number) {
    return 1;
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated Root Hub has no settings
   */
  @Override
  public Map<Integer, IUsbInterface> getSettings(byte number) {
    throw new UnsupportedOperationException("Root Hub has no settings.");
  }

}
