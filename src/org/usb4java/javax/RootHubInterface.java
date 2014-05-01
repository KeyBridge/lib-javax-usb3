/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.util.ArrayList;
import java.util.List;
import javax.usb.*;
import javax.usb.exception.UsbException;
import org.usb4java.javax.descriptors.SimpleUsbInterfaceDescriptor;

/**
 * The virtual USB interfaces used by the virtual USB root hub.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class RootHubInterface implements IUsbInterface {

  /**
   * The list of endpoints.
   */
  private final List<IUsbEndpoint> endpoints = new ArrayList<>(0);

  /**
   * The list of alternate settings.
   */
  private final List<IUsbInterface> settings = new ArrayList<>(0);

  /**
   * The USB configuration.
   */
  private final IUsbConfiguration configuration;

  /**
   * The interface descriptor.
   */
  private final IUsbInterfaceDescriptor descriptor = new SimpleUsbInterfaceDescriptor(IUsbConst.DESCRIPTOR_MIN_LENGTH_INTERFACE,
                                                                                     IUsbConst.DESCRIPTOR_TYPE_INTERFACE,
                                                                                     (byte) 0,
                                                                                     (byte) 0,
                                                                                     (byte) 0,
                                                                                     IUsbConst.HUB_CLASSCODE,
                                                                                     (byte) 0,
                                                                                     (byte) 0,
                                                                                     (byte) 0);

  /**
   * Constructor.
   * <p>
   * @param configuration The USB configuration.
   */
  public RootHubInterface(final IUsbConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void claim() throws UsbException {
    throw new UsbException("Virtual interfaces can't be claimed");
  }

  @Override
  public void claim(final IUsbInterfacePolicy policy) throws UsbException {
    throw new UsbException("Virtual interfaces can't be claimed");
  }

  @Override
  public void release() throws UsbException {
    throw new UsbException("Virtual interfaces can't be released");
  }

  @Override
  public boolean isClaimed() {
    return true;
  }

  @Override
  public boolean isActive() {
    return true;
  }

  @Override
  public int getNumSettings() {
    return 0;
  }

  @Override
  public byte getActiveSettingNumber() {
    return 0;
  }

  @Override
  public IUsbInterface getActiveSetting() {
    return this;
  }

  @Override
  public IUsbInterface getSetting(final byte number) {
    return this;
  }

  @Override
  public boolean containsSetting(final byte number) {
    return false;
  }

  @Override
  public List<IUsbInterface> getSettings() {
    return this.settings;
  }

  @Override
  public List<IUsbEndpoint> getUsbEndpoints() {
    return this.endpoints;
  }

  @Override
  public IUsbEndpoint getUsbEndpoint(final byte address) {
    return null;
  }

  @Override
  public boolean containsUsbEndpoint(final byte address) {
    return false;
  }

  @Override
  public IUsbConfiguration getUsbConfiguration() {
    return this.configuration;
  }

  @Override
  public IUsbInterfaceDescriptor getUsbInterfaceDescriptor() {
    return this.descriptor;
  }

  @Override
  public String getInterfaceString() {
    return null;
  }
}
