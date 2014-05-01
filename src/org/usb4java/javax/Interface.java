/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.usb.*;
import javax.usb.exception.UsbException;
import javax.usb.exception.UsbNotActiveException;
import org.usb4java.EndpointDescriptor;
import org.usb4java.InterfaceDescriptor;
import org.usb4java.javax.descriptors.SimpleUsbInterfaceDescriptor;

/**
 * usb4java implementation of UsbInterface.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Interface implements UsbInterface {

  /**
   * The configuration this interface belongs to.
   */
  private final Configuration configuration;

  /**
   * The interface descriptor.
   */
  private final UsbInterfaceDescriptor descriptor;

  /**
   * The endpoints of this interface.
   */
  private final Map<Byte, UsbEndpoint> endpoints = new HashMap<>();

  /**
   * Constructor.
   * <p>
   * @param configuration The USB configuration this interface belongs to.
   * @param descriptor    The libusb interface descriptor.
   */
  public Interface(final Configuration configuration, final InterfaceDescriptor descriptor) {
    this.configuration = configuration;
    this.descriptor = new SimpleUsbInterfaceDescriptor(descriptor);
    for (EndpointDescriptor endpointDescriptor : descriptor.endpoint()) {
      final Endpoint endpoint = new Endpoint(this, endpointDescriptor);
      this.endpoints.put(endpointDescriptor.bEndpointAddress(), endpoint);
    }
  }

  /**
   * Ensures this setting and configuration is active.
   * <p>
   * @throws UsbNotActiveException When the setting or the configuration is not
   *                               active.
   */
  private void checkActive() {
    if (!this.configuration.isActive()) {
      throw new UsbNotActiveException("Configuration is not active");
    }
    if (!isActive()) {
      throw new UsbNotActiveException("Setting is not active");
    }
  }

  /**
   * Ensures that the device is connected.
   * <p>
   * @throws UsbDisconnectedException When device has been disconnected.
   */
  private void checkConnected() {
    this.configuration.getUsbDevice().checkConnected();
  }

  @Override
  public void claim() throws UsbException {
    claim(null);
  }

  @Override
  public void claim(final UsbInterfacePolicy policy) throws UsbException {
    checkActive();
    checkConnected();
    final AUsbDevice device = this.configuration.getUsbDevice();
    device.claimInterface(this.descriptor.bInterfaceNumber(), policy != null && policy.forceClaim(this));
    this.configuration.setUsbInterface(this.descriptor.bInterfaceNumber(), this);
  }

  @Override
  public void release() throws UsbException {
    checkActive();
    checkConnected();
    this.configuration.getUsbDevice().releaseInterface(this.descriptor.bInterfaceNumber());
  }

  @Override
  public boolean isClaimed() {
    return this.configuration.getUsbDevice().isInterfaceClaimed(
      this.descriptor.bInterfaceNumber());
  }

  @Override
  public boolean isActive() {
    return this.configuration.getUsbInterface(this.descriptor
      .bInterfaceNumber()) == this;
  }

  @Override
  public int getNumSettings() {
    return this.configuration.getNumSettings(this.descriptor
      .bInterfaceNumber());
  }

  @Override
  public byte getActiveSettingNumber() {
    checkActive();
    return this.configuration
      .getUsbInterface(this.descriptor.bInterfaceNumber())
      .getUsbInterfaceDescriptor().bAlternateSetting();
  }

  @Override
  public UsbInterface getActiveSetting() {
    checkActive();
    return this.configuration.getUsbInterface(this.descriptor
      .bInterfaceNumber());
  }

  @Override
  public UsbInterface getSetting(final byte number) {
    return (this.configuration).getSettings(
      this.descriptor.bInterfaceNumber()).get(number & 0xff);
  }

  @Override
  public boolean containsSetting(final byte number) {
    return (this.configuration).getSettings(
      this.descriptor.bInterfaceNumber()).containsKey(number & 0xff);
  }

  @Override
  public List<UsbInterface> getSettings() {
    return Collections.unmodifiableList(new ArrayList<>(this.configuration.getSettings(this.descriptor.bInterfaceNumber()).values()));
  }

  @Override
  public List<UsbEndpoint> getUsbEndpoints() {
    return Collections.unmodifiableList(new ArrayList<>(this.endpoints.values()));
  }

  @Override
  public UsbEndpoint getUsbEndpoint(final byte address) {
    return this.endpoints.get(address);
  }

  @Override
  public boolean containsUsbEndpoint(final byte address) {
    return this.endpoints.containsKey(address);
  }

  @Override
  public UsbConfiguration getUsbConfiguration() {
    return this.configuration;
  }

  @Override
  public UsbInterfaceDescriptor getUsbInterfaceDescriptor() {
    return this.descriptor;
  }

  @Override
  public String getInterfaceString() throws UsbException,
                                            UnsupportedEncodingException {
    checkConnected();
    final byte iInterface = this.descriptor.iInterface();
    if (iInterface == 0) {
      return null;
    }
    return this.configuration.getUsbDevice().getString(iInterface);
  }

  @Override
  public String toString() {
    return String.format("USB interface %02x",
                         this.descriptor.bInterfaceNumber());
  }
}
