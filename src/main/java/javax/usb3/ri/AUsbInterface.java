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

import java.util.*;
import javax.usb3.*;

/**
 * Abstract Implementation of IUsbUsbInterface.
 * <p>
 * The interface descriptor describes a specific interface within a
 * configuration. A configuration provides one or more interfaces, each with
 * zero or more endpoint descriptors describing a unique set of endpoints within
 * the configuration. When a configuration supports more than one interface, the
 * endpoint descriptors for a particular interface follow the interface
 * descriptor in the data returned by the GetConfiguration() request. An
 * interface descriptor is always returned as part of a configuration
 * descriptor. Interface descriptors cannot be directly accessed with a
 * GetDescriptor() or SetDescriptor() request.
 * <p>
 * An interface may include alternate settings that allow the endpoints and/or
 * their characteristics to be varied after the device has been configured. The
 * default setting for an interface is always alternate setting zero. The
 * SetInterface() request is used to select an alternate setting or to return to
 * the default setting. The GetInterface() request returns the selected
 * alternate setting.
 * <p>
 * Alternate settings allow a portion of the device configuration to be varied
 * while other interfaces remain in operation. If a configuration has alternate
 * settings for one or more of its interfaces, a separate interface descriptor
 * and its associated endpoints are included for each setting.
 * <p>
 * If a device configuration supported a single interface with two alternate
 * settings, the configuration descriptor would be followed by an interface
 * descriptor with the bInterfaceNumber and bAlternateSetting fields set to zero
 * and then the endpoint descriptors for that setting, followed by another
 * interface descriptor and its associated endpoint descriptors. The second
 * interface descriptor’s bInterfaceNumber field would also be set to zero, but
 * the bAlternateSetting field of the second interface descriptor would be set
 * to one. If an interface uses only endpoint zero, no endpoint descriptors
 * follow the interface descriptor. In this case, the bNumEndpoints field must
 * be set to zero. An interface descriptor never includes endpoint zero in the
 * number of endpoints. Table 9-12 shows the
 * <p>
 * See USB 2.0 sec 9.6.5 Interface
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public abstract class AUsbInterface implements IUsbInterface {

  /**
   * The interface descriptor.
   */
  protected final IUsbInterfaceDescriptor descriptor;
  /**
   * The configuration this interface belongs to.
   */
  protected final IUsbConfiguration configuration;
  /**
   * The endpoints of this interface.
   */
  protected final Map<Byte, IUsbEndpoint> endpoints;

  /**
   * Construct a new UsbInterface.
   *
   * @param configuration The USB configuration this interface belongs to.
   */
  public AUsbInterface(IUsbConfiguration configuration) {
    this.configuration = configuration;
    this.endpoints = new HashMap<>();
    this.descriptor = null;
  }

  /**
   * Construct a new UsbInterface.
   *
   * @param configuration The USB configuration this interface belongs to.
   * @param descriptor    The USB interface descriptor.
   */
  public AUsbInterface(final IUsbConfiguration configuration, final IUsbInterfaceDescriptor descriptor) {
    this(configuration);
    /**
     * The USB (virtual) Root hub has not endpoint.
     */
    for (IUsbEndpointDescriptor iUsbEndpointDescriptor : descriptor.endpoint()) {
      endpoints.put(iUsbEndpointDescriptor.endpointAddress().getByteCode(), new UsbEndpoint(this, iUsbEndpointDescriptor));
    }
  }

  /**
   * @inherit
   */
  @Override
  public boolean isClaimed() {
    return this.configuration.getUsbDevice().isInterfaceClaimed(this.descriptor.bInterfaceNumber());
  }

  /**
   * @inherit
   */
  @Override
  public boolean isActive() {
    return this.configuration.getUsbInterface(this.descriptor.bInterfaceNumber()) == this;
  }

  /**
   * @inherit
   */
  @Override
  public int getNumSettings() {
    return this.configuration.getNumSettings(this.descriptor.bInterfaceNumber());
  }

  /**
   * @inherit
   */
  @Override
  public IUsbInterface getSetting(final byte number) {
    return this.configuration.getSettings(this.descriptor.bInterfaceNumber()).get(number & 0xff);
  }

  /**
   * @inherit
   */
  @Override
  public boolean containsSetting(final byte number) {
    return this.configuration.getSettings(this.descriptor.bInterfaceNumber()).containsKey(number & 0xff);
  }

  /**
   * @inherit
   */
  @Override
  public List<IUsbInterface> getSettings() {
    return Collections.unmodifiableList(new ArrayList<>(this.configuration.getSettings(this.descriptor.bInterfaceNumber()).values()));
  }

  /**
   * @inherit
   */
  @Override
  public List<IUsbEndpoint> getUsbEndpoints() {
    return Collections.unmodifiableList(new ArrayList<>(this.endpoints.values()));
  }

  /**
   * @inherit
   */
  @Override
  public IUsbEndpoint getUsbEndpoint(final byte address) {
    return this.endpoints.get(address);
  }

  /**
   * @inherit
   */
  @Override
  public boolean containsUsbEndpoint(final byte address) {
    return this.endpoints.containsKey(address);
  }

  /**
   * @inherit
   */
  @Override
  public IUsbConfiguration getUsbConfiguration() {
    return this.configuration;
  }

  /**
   * @inherit
   */
  @Override
  public IUsbInterfaceDescriptor getUsbInterfaceDescriptor() {
    return this.descriptor;
  }

  /**
   * Sort order is based upon the interface number in the descriptor.
   *
   * @param o the other instance
   * @return the sort order
   */
  @Override
  public int compareTo(IUsbInterface o) {
    return Integer.compare(this.descriptor.bInterfaceNumber(), o.getUsbInterfaceDescriptor().bInterfaceNumber());
  }

  @Override
  public String toString() {
    return String.format("USB interface %02x", this.descriptor.bInterfaceNumber());
  }

}
