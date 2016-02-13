/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package javax.usb;

import java.util.List;
import javax.usb.exception.UsbPlatformException;
import org.usb4java.Device;

/**
 * usb4java implementation of JSR-80 IUsbUsbHub interface.
 * <p>
 * Hubs are a type of USB device that provide additional attachment points to
 * the USB.
 *
 * @author Klaus Reimer (k@ailis.de)
 */
public final class UsbHub extends AUsbDevice implements IUsbHub, IUsbPorts {

  /**
   * The hub ports.
   */
  private final UsbPorts ports = new UsbPorts(this);

  /**
   * Constructs a new USB hub device.
   *
   * @param deviceManager The USB device manager which is responsible for this *
   *                      device.
   * @param id            THe device id. Must not be null.
   * @param parentId      The parent id. may be null if this device has no
   *                      parent.
   * @param speed         The device speed.
   * @param device        The libusb device. This reference is only valid during
   *                      the constructor execution, so don't store it in a
   *                      property or something like that.
   * @throws UsbPlatformException When device configuration could not be read.
   */
  public UsbHub(final UsbDeviceManager deviceManager, final UsbDeviceId id, final UsbDeviceId parentId, final int speed, final Device device) throws UsbPlatformException {
    super(deviceManager, id, parentId, speed, device);
  }

  @Override
  public byte getNumberOfPorts() {
    return this.ports.getNumberOfPorts();
  }

  @Override
  public List<IUsbPort> getUsbPorts() {
    return this.ports.getUsbPorts();
  }

  @Override
  public IUsbPort getUsbPort(final byte number) {
    return this.ports.getUsbPort(number);
  }

  @Override
  public List<IUsbDevice> getAttachedUsbDevices() {
    return this.ports.getAttachedUsbDevices();
  }

  @Override
  public boolean isUsbDeviceAttached(final IUsbDevice device) {
    return this.ports.isUsbDeviceAttached(device);
  }

  @Override
  public boolean isRootUsbHub() {
    return false;
  }

  @Override
  public void connectUsbDevice(final IUsbDevice device) {
    this.ports.connectUsbDevice(device);
  }

  @Override
  public void disconnectUsbDevice(final IUsbDevice device) {
    this.ports.disconnectUsbDevice(device);
  }

  /**
   * TRUE. USB4Java UsbHub instances are always UsbHubs. If the device is not a
   * hub it will be identified as a UsbDevice implementation.
   *
   * @return TRUE
   */
  @Override
  public boolean isUsbHub() {
    return true;
  }
}
