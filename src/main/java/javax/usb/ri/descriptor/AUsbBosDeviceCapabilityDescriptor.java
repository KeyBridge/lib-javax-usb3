/*
 * Copyright (C) 2014 Jesse Caulfield <jesse@caulfield.org>
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
package javax.usb.ri.descriptor;

import java.util.Arrays;
import javax.usb.ri.enumerated.EBosDeviceCapabilityType;
import javax.usb.ri.enumerated.EDescriptorType;
import javax.usb.ri.enumerated.EDevicePortSpeed;
import javax.usb.util.ByteUtil;

/**
 * 9.6.2 Binary Device Object Store (BOS) Device Descriptor Definition.
 * <p>
 * Device Capability descriptors are always returned as part of the BOS
 * information returned by a GetDescriptor(BOS) request. A Device Capability
 * cannot be directly accessed with a GetDescriptor() or SetDescriptor()
 * request.
 * <p>
 * It is advised to check bDevCapabilityType and call the matching
 * get*Descriptor method to get a structure fully matching the type.
 * <p>
 * Individual technology-specific or generic device-level capabilities are
 * reported via Device Capability descriptors. The format of the Device
 * Capability descriptor is defined in Table 9-13.
 * <p>
 * See Table 9-13. Format of a Device Capability Descriptor of the USB 3.1 spec.
 * <p>
 * @author Jesse Caulfield
 */
public abstract class AUsbBosDeviceCapabilityDescriptor extends AUsbDescriptor {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1L;

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  /**
   * The Device Capability Type
   */
  private final EBosDeviceCapabilityType capabilityType;

  /**
   * The device capability data. This is in a Capability-specific format defined
   * by the identified capabilityType.
   */
  private final byte[] capabilityData;
  /**
   * 9.6.2.1 USB 2.0 Extension. An Enhanced SuperSpeed device shall include the
   * USB 2.0 Extension descriptor and shall support LPM when operating in USB
   * 2.0 High-Speed mode.
   */
  private boolean linkPowerManagement;
  /**
   * 9.6.2.2 SuperSpeed USB Device Capability. LTM Capable. Indicates that this
   * device has is capable of generating Latency Tolerance Messages.
   */
  private boolean latencyToleranceMessages;
  /**
   * 9.6.2.2 SuperSpeed USB Device Capability. The speed supported by this
   * device.
   */
  private EDevicePortSpeed wSpeedsSupported;
  /**
   * 9.6.2.2 SuperSpeed USB Device Capability. The lowest speed at which all the
   * functionality supported by the device is available to the user. For example
   * if the device supports all its functionality when connected at full speed
   * and above then it sets this value to 1.
   */
  private EDevicePortSpeed bFunctionalitySupport;
  /**
   * 9.6.2.2 SuperSpeed USB Device Capability. U1 Device Exit Latency
   * (microseconds). Worst-case latency to transition from U1 to U0, assuming
   * the latency is limited only by the device and not the device’s link
   * partner.
   * <p>
   * This field applies only to the exit latency associated with an individual
   * port, and does not apply to the total latency through a hub (e.g., from
   * downstream port to upstream port).
   * <p>
   * Range is from zero to 10 microsecond.
   */
  private int bU1DevExitLat;
  /**
   * 9.6.2.2 SuperSpeed USB Device Capability. U2 Device Exit Latency.
   * Worst-case latency to transition from U2 to U0, assuming the latency is
   * limited only by the device and not the device’s link partner. Applies to
   * all ports on a device.
   * <p>
   * Range is from Zero to 2047 microsecond.
   */
  private int wU2DevExitLat;
  /**
   * 9.6.2.3 Container ID. Implemented by all USB hubs, and optionally for other
   * devices.
   * <p>
   * If this descriptor is provided when operating in one mode, it shall be
   * provided when operating in any mode. This descriptor may be used by a host
   * in order to identify a unique device instance across all operating modes.
   * If a device can also connect to a host through other technologies, the same
   * Container ID value contained in this descriptor should also be provided
   * over those other technologies in a technology specific manner.
   * <p>
   * Developer note: This is a UUID.
   */
  private String containerId;
  /**
   * 9.6.2.4 Platform Descriptor. The Platform Descriptor contains a 128-bit
   * UUID value that is defined and published independently by the
   * platform/operating system vendor, and is used to identify a unique platform
   * specific device capability.
   * <p>
   */
  private String platformCapabilityUUID;
  /**
   * 9.6.2.4 Platform Descriptor. The descriptor may also contain one or more
   * bytes of data associated with the capability.
   */
  private byte[] platformCapabilityData;

  /**
   * 9.6.2.2 SuperSpeed USB Device Capability. This section defines the required
   * device-level capabilities descriptor which shall be implemented by all
   * Enhanced SuperSpeed devices.
   */
  /**
   * Construct a new UsbBosDeviceCapabilityDescriptor by reading the descriptor
   * data from the specified byte buffer.
   * <p>
   * @param bDevCapabilityType The Device Capability Type
   * @param capabilityData     The device capability Data
   */
  public AUsbBosDeviceCapabilityDescriptor(byte bDevCapabilityType,
                                           byte[] capabilityData) {
    super(EDescriptorType.DEVICE_CAPABILITY);
    this.capabilityType = EBosDeviceCapabilityType.fromByte(bDevCapabilityType);
    this.capabilityData = capabilityData;
    switch (this.capabilityType) {
      case WIRELESS_USB:
        break;
      case USB_20_EXTENSION:
        /**
         * Bit 1. A value of one in this bit location indicates that this device
         * supports the Link Power Management protocol. Enhanced SuperSpeed
         * devices shall set this bit to one.
         */
        this.linkPowerManagement = (capabilityData[0] & 0x02) != 0;
        break;
      case SUPERSPEED_USB:
        this.latencyToleranceMessages = (capabilityData[0] & 0x02) != 0;
        this.wSpeedsSupported = EDevicePortSpeed.speedSupported(capabilityData[1]); // Only first 4 bits are coded
        this.bFunctionalitySupport = EDevicePortSpeed.speedSupported(capabilityData[3]);
        this.bU1DevExitLat = capabilityData[4];
        this.wU2DevExitLat = ByteUtil.toShort(Arrays.copyOfRange(capabilityData, 5, 2));
        break;
      case CONTAINER_ID:
        /**
         * This is a 128-bit number that is unique to a device instance that is
         * used to uniquely identify the device instance across all modes of
         * operation. This same value may be provided over other technologies as
         * well to allow the host to identify the device independent of means of
         * connectivity. Refer to IETF RFC 4122 for details on generation of a
         * UUID.
         * <p>
         * Developer note: capabilityData[0] is reserved and shall be set to
         * zero.
         */
        this.containerId = new String(capabilityData, 1, 16);
        break;
      case PLATFORM:
        /**
         * Developer note: capabilityData[0] is reserved and shall be set to
         * zero.
         */
        this.platformCapabilityUUID = new String(capabilityData, 1, 16);
        this.platformCapabilityData = Arrays.copyOfRange(capabilityData, 17, capabilityData.length - 17);
        break;
      case POWER_DELIVERY_CAPABILITY:
        break;
      case BATTERY_INFO_CAPABILITY:
        break;
      case PD_CONSUMER_PORT_CAPABILITY:
        break;
      case PD_PROVIDER_PORT_CAPABILITY:
        break;
      case SUPERSPEED_PLUS:
        /**
         * @TODO: 9.6.2.5 SuperSpeedPlus USB Device Capability
         */
        break;
      case PRECISION_TIME_MEASUREMENT:
        /**
         * No data described in the USB spec.
         */
        break;
      case WIRELESS_USB_EXT:
        break;
      case RESERVED:
        break;
      default:
        throw new AssertionError(this.capabilityType.name());

    }
  }

  //<editor-fold defaultstate="collapsed" desc="Getter Methods">
  /**
   * Get The Device Capability Type represented by this descriptor instance
   * <p>
   * @return The Device Capability Type
   */
  public EBosDeviceCapabilityType getCapabilityType() {
    return capabilityType;
  }

  /**
   * Get the RAW capability data field provided by the device.
   * <p>
   * @return the byte array provided by the device.
   */
  public byte[] getCapabilityData() {
    return capabilityData;
  }

  /**
   * Get the Container ID. Implemented by all USB hubs, and optionally for other
   * devices.
   * <p>
   * @return the Container ID.
   */
  public String getContainerId() {
    return containerId;
  }

  /**
   * Get the data associated with a Platform Descriptor capability
   * <p>
   * @return
   */
  public byte[] getPlatformCapabilityData() {
    return platformCapabilityData;
  }

  /**
   * Get the Platform Descriptor UUID value
   * <p>
   * @return the Platform Descriptor UUID
   */
  public String getPlatformCapabilityUUID() {
    return platformCapabilityUUID;
  }

  /**
   * SuperSpeed USB Device Capability. The lowest speed at which all the
   * functionality supported by the device is available to the user
   * <p>
   * @return The lowest speed supported by the device
   */
  public EDevicePortSpeed getbFunctionalitySupport() {
    return bFunctionalitySupport;
  }

  /**
   * SuperSpeed USB Device Capability. U1 Device Exit Latency (microseconds).
   * Worst-case latency.
   * <p>
   * @return the Worst-case Device Exit latency (microseconds)
   */
  public int getbU1DevExitLat() {
    return bU1DevExitLat;
  }

  /**
   * SuperSpeed USB Device Capability. The speed supported by this device.
   * <p>
   * @return The speed supported by this device.
   */
  public EDevicePortSpeed getwSpeedsSupported() {
    return wSpeedsSupported;
  }

  /**
   * SuperSpeed USB Device Capability. U2 Device Exit Latency. Worst-case
   * latency
   * <p>
   * @return the Worst-case Device Exit latency (microseconds)
   */
  public int getwU2DevExitLat() {
    return wU2DevExitLat;
  }

  /**
   * SuperSpeed USB Device Capability. LTM Capable. Indicates that this device
   * has is capable of generating Latency Tolerance Messages
   * <p>
   * @return whether this device can generate Latency Tolerance Messages
   */
  public boolean isLatencyToleranceMessages() {
    return latencyToleranceMessages;
  }

  /**
   * USB 2.0 Extension. An Enhanced SuperSpeed device shall include the USB 2.0
   * Extension descriptor and shall support LPM.
   * <p>
   * @return whether this device supports LPM
   */
  public boolean isLinkPowerManagement() {
    return linkPowerManagement;
  }//</editor-fold>

}
