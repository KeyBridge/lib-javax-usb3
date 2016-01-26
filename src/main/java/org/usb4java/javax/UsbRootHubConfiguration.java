/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.util.ArrayList;
import java.util.List;
import javax.usb.*;
import javax.usb.ri.enumerated.EDescriptorType;
import javax.usb.ri.request.ConfigurationAttributes;
import org.usb4java.javax.descriptors.UsbConfigurationDescriptor;

/**
 * Virtual USB configuration used by the virtual USB root hub.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class UsbRootHubConfiguration implements IUsbConfiguration {

  /**
   * The virtual interfaces.
   */
  private final List<IUsbInterface> interfaces = new ArrayList<>();

  /**
   * The upstream device (e.g. HOST system) this configuration belongs to.
   */
  private final IUsbDevice device;

  /**
   * The default USB configuration descriptor. bmAttributes is 0x80 [1000 0000],
   * which sets D7 to one (required) and the other parameters to false
   * (Self-powered, Remote wakeup).
   */
  private final IUsbConfigurationDescriptor descriptor = new UsbConfigurationDescriptor(
    (byte) (EDescriptorType.CONFIGURATION.getLength() + EDescriptorType.INTERFACE.getLength()), // wTotalLength
    (byte) 1, // bNumInterfaces
    (byte) 1, // bConfigurationValue
    (byte) 0, // iConfiguration
    ConfigurationAttributes.getInstance(),//  (byte) 0x80, // bmAttributes
    (byte) 0); // bMaxPower

  /**
   * Constructor.
   * <p>
   * @param device The device this configuration belongs to.
   */
  public UsbRootHubConfiguration(final IUsbDevice device) {
    this.device = device;
    this.interfaces.add(new UsbRootHubInterface(this));

  }

  /**
   * If this IUsbConfiguration is active.
   * <p>
   * @return if this IUsbConfiguration is active.
   */
  @Override
  public boolean isActive() {
    return true;
  }

  /**
   * Get all IUsbInterfaces for this configuration.
   * <p>
   * The returned IUsbInterface settings depend on whether this configuration
   * (and by association its contained interfaces) is active or not:
   * <ul>
   * <li>If this configuration is active, all IUsbInterfaces will be the active
   * alternate setting for that interface.</li>
   * <li>If this configuration is not active, no contained interfaces are
   * active, so they have no active alternate settings. The IUsbInterfaces will
   * then be an implementation-dependent alternate setting IUsbInterface for
   * each iterface. To get a specific alternate setting, use
   * {@link javax.usb.UsbInterface#getSetting(byte) IUsbInterface.getSetting(byte number)}.</li>
   * </ul>
   * <p>
   * @return All IUsbInterfaces for this configuration.
   */
  @Override
  public List<IUsbInterface> getUsbInterfaces() {
    return this.interfaces;
  }

  /**
   * Get the IUsbInterface with the specified interface number.
   * <p>
   * The returned interface setting will be the current active alternate setting
   * if this configuration (and thus the contained interface) is
   * {@link #isActive() active}. If this configuration is not active, the
   * returned interface setting will be an implementation-dependent alternate
   * setting. To get a specific alternate setting, use
   * {@link javax.usb.UsbInterface#getSetting(byte) IUsbInterface.getSetting(byte number)}.
   * <p>
   * If the specified IUsbInterface does not exist, this returns null.
   * <p>
   * @param number The number of the interface to get.
   * @return The specified IUsbInterface, or null.
   */
  @Override
  public IUsbInterface getUsbInterface(final byte number) {
    if (number != 0) {
      return null;
    }
    return this.interfaces.get(0);
  }

  /**
   * If the specified IUsbInterface is contained in this IUsbConfiguration.
   * <p>
   * @param number The interface number.
   * @return If this configuration contains the specified IUsbInterface.
   */
  @Override
  public boolean containsUsbInterface(final byte number) {
    return number == 0;
  }

  /**
   * Get the parent IUsbDevice that this IUsbConfiguration belongs to.
   * <p>
   * @return the IUsbDevice that this IUsbConfiguration belongs to.
   */
  @Override
  public IUsbDevice getUsbDevice() {
    return this.device;
  }

  /**
   * Get the configuration descriptor.
   * <p>
   * The descriptor may be cached.
   * <p>
   * @return The configuration descriptor.
   */
  @Override
  public IUsbConfigurationDescriptor getUsbConfigurationDescriptor() {
    return this.descriptor;
  }

  /**
   * Get the configuration String.
   * <p>
   * This is a convienence method. The String may be cached. If the device does
   * not support strings or does not define the configuration string, this
   * returns null.
   * <p>
   * @return The configuration String, or null.
   */
  @Override
  public String getConfigurationString() {
    return null;
  }
}
