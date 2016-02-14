/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb;

import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.usb.descriptor.UsbInterfaceDescriptor;
import javax.usb.exception.UsbDisconnectedException;
import javax.usb.exception.UsbException;
import javax.usb.exception.UsbNotActiveException;
import org.usb4java.EndpointDescriptor;
import org.usb4java.InterfaceDescriptor;

/**
 * usb4java implementation of IUsbUsbInterface.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class UsbInterface implements IUsbInterface {

  /**
   * The configuration this interface belongs to.
   */
  private final UsbConfiguration configuration;

  /**
   * The interface descriptor.
   */
  private final IUsbInterfaceDescriptor descriptor;

  /**
   * The endpoints of this interface.
   */
  private final Map<Byte, IUsbEndpoint> endpoints = new HashMap<>();

  /**
   * Constructor.
   *
   * @param configuration The USB configuration this interface belongs to.
   * @param descriptor    The libusb interface descriptor.
   */
  public UsbInterface(final UsbConfiguration configuration, final InterfaceDescriptor descriptor) {
    this.configuration = configuration;
    this.descriptor = new UsbInterfaceDescriptor(descriptor);
    for (EndpointDescriptor endpointDescriptor : descriptor.endpoint()) {
      final UsbEndpoint endpoint = new UsbEndpoint(this, endpointDescriptor);
      this.endpoints.put(endpointDescriptor.bEndpointAddress(), endpoint);
    }
  }

  /**
   * Ensures this setting and configuration is active.
   *
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
   *
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
  public void claim(final IUsbInterfacePolicy policy) throws UsbException {
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
  public IUsbInterface getActiveSetting() {
    checkActive();
    return this.configuration.getUsbInterface(this.descriptor
            .bInterfaceNumber());
  }

  @Override
  public IUsbInterface getSetting(final byte number) {
    return (this.configuration).getSettings(
            this.descriptor.bInterfaceNumber()).get(number & 0xff);
  }

  @Override
  public boolean containsSetting(final byte number) {
    return (this.configuration).getSettings(
            this.descriptor.bInterfaceNumber()).containsKey(number & 0xff);
  }

  @Override
  public List<IUsbInterface> getSettings() {
    return Collections.unmodifiableList(new ArrayList<>(this.configuration.getSettings(this.descriptor.bInterfaceNumber()).values()));
  }

  @Override
  public List<IUsbEndpoint> getUsbEndpoints() {
    return Collections.unmodifiableList(new ArrayList<>(this.endpoints.values()));
  }

  @Override
  public IUsbEndpoint getUsbEndpoint(final byte address) {
    return this.endpoints.get(address);
  }

  @Override
  public boolean containsUsbEndpoint(final byte address) {
    return this.endpoints.containsKey(address);
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
