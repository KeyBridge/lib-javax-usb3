/*
 * Copyright (C) 1999 - 2001, International Business Machines
 * Corporation. All Rights Reserved. Provided and licensed under the terms and
 * conditions of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 *
 * Copyright (C) 2014 Key Bridge LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package javax.usb3;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.usb3.exception.UsbClaimException;
import javax.usb3.exception.UsbDisconnectedException;
import javax.usb3.exception.UsbException;
import javax.usb3.exception.UsbNotActiveException;

/**
 * Interface for a USB interface.
 * <p>
 * This object actually represents a specific 'alternate' setting for a USB
 * interface. Interfaces must have at least one alternate setting, and one and
 * only one setting is active per interface. All settings share the same
 * interface number.
 * <p>
 * If this interface setting is not active, it cannot be claimed or released;
 * the active interface setting should be used for claiming and releasing
 * ownership of the interface; also no action may be taken on any parts of this
 * interface setting, if the setting is not active. Any attempt to perform
 * action on objects belonging to an inactive interface setting will throw a
 * UsbNotActiveException.
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
 * @author Dan Streetman
 * @author Jesse Caulfield
 */
public interface IUsbInterface extends Comparable<IUsbInterface> {

  /**
   * Claim this interface.
   * <p>
   * This will attempt whatever claiming the native implementation provides, if
   * any. If the interface is already claimed, or the native claim fails, this
   * will fail.
   * <p>
   * This must be done before opening and/or using any UsbPipes.
   *
   * @exception UsbClaimException        If the interface is already claimed.
   * @exception UsbException             If the interface could not be claimed.
   * @exception UsbNotActiveException    If this interface setting is not
   *                                     {@link #isActive() active}.
   * @exception UsbDisconnectedException If this interface (device) has been
   *                                     disconnected.
   */
  public void claim() throws UsbClaimException, UsbException, UsbNotActiveException, UsbDisconnectedException;

  /**
   * Claim this interface using a IIUsbInterfacePolicy.
   * <p>
   * This will attempt whatever claiming the native implementation provides, if
   * any. If the native claim fails, this will fail. If the interface is already
   * claimed, this may fail depending on the value of the
   * {@link javax.usb.UsbInterfacePolicy#forceClaim(UsbInterface) IIUsbInterfacePolicy.forceClaim()}.
   *
   * @param policy The IIUsbInterfacePolicy to use.
   * @exception UsbClaimException        If the interface is already claimed.
   * @exception UsbException             If the interface could not be claimed.
   * @exception UsbNotActiveException    If this interface setting is not
   *                                     {@link #isActive() active}.
   * @exception UsbDisconnectedException If this interface (device) has been
   *                                     disconnected.
   */
  public void claim(IUsbInterfacePolicy policy) throws UsbClaimException, UsbException, UsbNotActiveException, UsbDisconnectedException;

  /**
   * Release this interface.
   * <p>
   * This will only succeed if the interface has been properly claimed. If the
   * native release fails, this will fail.
   * <p>
   * This should be done after the interface is no longer being used. All pipes
   * must be closed before this can be released.
   *
   * @exception UsbClaimException        If the interface is not claimed.
   * @exception UsbException             If the interface could not be released.
   * @exception UsbNotActiveException    If this interface setting is not
   *                                     {@link #isActive() active}.
   * @exception UsbDisconnectedException If this interface (device) has been
   *                                     disconnected.
   */
  public void release() throws UsbClaimException, UsbException, UsbNotActiveException, UsbDisconnectedException;

  /**
   * If this interface is claimed.
   * <p>
   * This will return true if claimed in Java. This may, depending on
   * implementation, return true if claimed natively (outside of Java)
   * <p>
   * If this IUsbInterface is not {@link #isActive() active}, this will return
   * if the active alternate setting is active.
   *
   * @return If this interface is claimed (in Java).
   */
  public boolean isClaimed();

  /**
   * If this interface alternate setting is active.
   * <p>
   * The interface itself is active if and only if its parent configuration is
   * {@link javax.usb.UsbConfiguration#isActive() active}. If the interface
   * itself is not active, none of its alternate settings are active.
   *
   * @return if this interface alternate setting is active.
   */
  public boolean isActive();

  /**
   * Get the number of alternate settings for this interface.
   *
   * @return the number of alternate settings for this interface.
   */
  public int getNumSettings();

  /**
   * Get the number of the active alternate setting.
   *
   * @return The active setting number for this interface.
   * @exception UsbNotActiveException If the interface (and parent config) is
   *                                  not {@link #isActive() active}.
   */
  public byte getActiveSettingNumber() throws UsbNotActiveException;

  /**
   * Get the active alternate setting.
   *
   * @return The active setting for this interface.
   * @exception UsbNotActiveException If this interface (and parent config) is
   *                                  not {@link #isActive() active}.
   */
  public IUsbInterface getActiveSetting() throws UsbNotActiveException;

  /**
   * Get the specified alternate setting.
   * <p>
   * If the specified setting does not exist, this returns null.
   *
   * @param number
   * @return The specified alternate setting, or null.
   */
  public IUsbInterface getSetting(byte number);

  /**
   * If the specified alternate setting exists.
   *
   * @param number The alternate setting number.
   * @return If the alternate setting exists.
   */
  public boolean containsSetting(byte number);

  /**
   * Get all alternate settings for this interface.
   * <p>
   * This returns all alternate settings, including this one.
   *
   * @return All alternate settings for this interface.
   */
  public List<IUsbInterface> getSettings();

  /**
   * Get all endpoints for this interface setting.
   *
   * @return All endpoints for this setting.
   */
  public List<IUsbEndpoint> getUsbEndpoints();

  /**
   * Get a specific IUsbEndpoint.
   * <p>
   * If this does not contain the specified endpoint, this returns null.
   *
   * @param address The address of the IUsbEndpoint to get.
   * @return The specified IUsbEndpoint, or null.
   */
  public IUsbEndpoint getUsbEndpoint(byte address);

  /**
   * If the specified IUsbEndpoint is contained in this IUsbInterface.
   *
   * @param address The endpoint address.
   * @return If this IUsbInterface contains the specified IUsbEndpoint.
   */
  public boolean containsUsbEndpoint(byte address);

  /**
   * Get the parent IUsbConfiguration that this IUsbInterface belongs to.
   *
   * @return The IUsbConfiguration that this interface belongs to.
   */
  public IUsbConfiguration getUsbConfiguration();

  /**
   * Get the interface descriptor.
   * <p>
   * The descriptor may be cached.
   *
   * @return The interface descriptor.
   */
  public IUsbInterfaceDescriptor getUsbInterfaceDescriptor();

  /**
   * Get the interface String.
   * <p>
   * This is a convenience method. The String may be cached. If the device does
   * not support strings or does not define the interface string, this returns
   * null.
   *
   * @return The interface String, or null.
   * @exception UsbException                 If there was an error getting the
   *                                         UsbStringDescriptor.
   * @exception UnsupportedEncodingException If the string encoding is not
   *                                         supported.
   * @exception UsbDisconnectedException     If this interface (device) has been
   *                                         disconnected.
   */
  public String getInterfaceString() throws UsbException, UnsupportedEncodingException, UsbDisconnectedException;
}
