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
package javax.usb.ri.enumerated;

import static javax.usb.ri.enumerated.EUSBClassCode.*;

/**
 * USB Class Codes supplementary information.
 * <p>
 * This class provides a lookup mechanism to identify the SubClass, Protocol,
 * and Meaning (description) of a USB Class code.
 * <p>
 * Information is encoded into this class as follows:
 * <ul>
 * <li>Global byte masks (noted as "xxh" in the USBCodes list) are recorded in
 * this class as NULL.</li>
 * <li>The description field is associated with a Protocol.</li> </ul>
 * <p>
 * Identify an entry by matching as follows: 1) Base Class is always non-null
 * and must match. 2) SubClass matches if not null. 3) Protocol matches if not
 * null.
 * <p>
 * @see <a href="http://www.usb.org/developers/defined_class">USB Class
 * Codes</a>
 * @author Jesse Caulfield
 */
public enum EUSBSubclassCode {

  OTHER(EUSBClassCode.OTHER, (byte) 0x00, (byte) 0x00, (byte) 0x00, "Use class code info from Interface Descriptors "),
  AUDIO_DEVICE(AUDIO, (byte) 0x01, null, null, "Audio device "),
  COMMUNICATION_DEVICE_CLASS(COMMUNICATIONS_AND_CDC_CONTROL, (byte) 0x02, null, null, "Communication device class "),
  HID_DEVICE_CLASS(HID_HUMAN_INTERFACE_DEVICE, (byte) 0x03, null, null, "HID device class "),
  PHYSICAL_DEVICE_CLASS(PHYSICAL, (byte) 0x05, null, null, "Physical device class "),
  STILL_IMAGING_DEVICE(IMAGE, (byte) 0x06, (byte) 0x01, (byte) 0x01, "Still Imaging device "),
  PRINTER_DEVICE(PRINTER, (byte) 0x07, null, null, "Printer device "),
  MASS_STORAGE_DEVICE(MASS_STORAGE, (byte) 0x08, null, null, "Mass Storage device "),
  FULL_SPEED_HUB(HUB, (byte) 0x09, (byte) 0x00, (byte) 0x00, "Full speed Hub "),
  HI_SPEED_HUB_WITH_SINGLE_TT(HUB, (byte) 0x09, (byte) 0x00, (byte) 0x01, "Hi-speed hub with single TT "),
  HI_SPEED_HUB_WITH_MULTIPLE_TTS(HUB, (byte) 0x09, (byte) 0x00, (byte) 0x02, "Hi-speed hub with multiple TTs "),
  CDC_DATA_DEVICE(CDC_DATA, (byte) 0x0A, null, null, "CDC data device "),
  SMART_CARD_DEVICE(SMART_CARD, (byte) 0x0B, null, null, "Smart Card device "),
  CONTENT_SECURITY_DEVICE(CONTENT_SECURITY, (byte) 0x0D, (byte) 0x00, (byte) 0x00, "Content Security device "),
  VIDEO_DEVICE(VIDEO, (byte) 0x0E, null, null, "Video device "),
  PERSONAL_HEALTHCARE_DEVICE(PERSONAL_HEALTHCARE, (byte) 0x0F, null, null, "Personal Healthcare device "),
  AVCONTROL_INTERFACE(AUDIO_VIDEO_DEVICE, (byte) 0x10, (byte) 0x01, (byte) 0x00, "Audio/Video Device – AVControl Interface"),
  AVDATA_VIDEO_STREAMING_INTERFACE(AUDIO_VIDEO_DEVICE, (byte) 0x10, (byte) 0x02, (byte) 0x00, "Audio/Video Device – AVData Video Streaming Interface"),
  AVDATA_AUDIO_STREAMING_INTERFACE(AUDIO_VIDEO_DEVICE, (byte) 0x10, (byte) 0x03, (byte) 0x00, "Audio/Video Device – AVData Audio Streaming Interface"),
  USB2_COMPLIANCE_DEVICE(DIAGNOSTIC_DEVICE, (byte) 0xDC, (byte) 0x01, (byte) 0x01, "USB2 Compliance Device.  Definition for this device can be found at http://www.intel.com/technology/usb/spec.htm"),
  BLUETOOTH_PROGRAMMING_INTERFACE(WIRELESS_CONTROLLER, (byte) 0xE0, (byte) 0x01, (byte) 0x01, "Bluetooth Programming Interface.  Get specific information from www.bluetooth.com. "),
  UWB_RADIO_CONTROL_INTERFACE(WIRELESS_CONTROLLER, (byte) 0xE0, (byte) 0x01, (byte) 0x02, "UWB Radio Control Interface.  Definition for this is found in the Wireless USB Specification in Chapter 8. "),
  REMOTE_NDIS(WIRELESS_CONTROLLER, (byte) 0xE0, (byte) 0x01, (byte) 0x03, "Remote NDIS.  Information can be found at: http://www.microsoft.com/windowsmobile/mobileoperators/default.mspx "),
  BLUETOOTH_AMP_CONTROLLER(WIRELESS_CONTROLLER, (byte) 0xE0, (byte) 0x01, (byte) 0x04, "(Bluetooth AMP Controller. Get specific information from www.bluetooth.com."),
  HOST_WIRE_ADAPTER_CONTROL(WIRELESS_CONTROLLER, (byte) 0xE0, (byte) 0x2, (byte) 0x01, "Host Wire Adapter Control/Data interface.  Definition can be found in the Wireless USB Specification in Chapter 8. "),
  DEVICE_WIRE_ADAPTER_CONTROL_DATA_INTERFACE(WIRELESS_CONTROLLER, (byte) 0xE0, (byte) 0x2, (byte) 0x02, "Device Wire Adapter Control/Data interface.  Definition can be found in the Wireless USB Specification in Chapter 8. "),
  DEVICE_WIRE_ADAPTER_ISOCHRONOUS_INTERFACE(WIRELESS_CONTROLLER, (byte) 0xE0, (byte) 0x2, (byte) 0x03, "Device Wire Adapter Isochronous interface.  Definition can be found in the Wireless USB Specification in Chapter 8. "),
  ACTIVE_SYNC_DEVICERMATION_ON_THIS_CLASS(MISCELLANEOUS, (byte) 0xEF, (byte) 0x01, (byte) 0x01, "Active Sync device. This class code can be used in either Device or Interface Descriptors. Contact Microsoft for more information on this class. "),
  PALM_SYNC(MISCELLANEOUS, (byte) 0xEF, null, (byte) 0x02, "Palm Sync. This class code can be used in either Device or Interface Descriptors. "),
  INTERFACE_ASSOCIATION_DESCRIPTOR(MISCELLANEOUS, (byte) 0xEF, (byte) 0x02, (byte) 0x01, "Interface Association Descriptor. The usage of this class code triple is defined in the Interface Association Descriptor ECN that is provided on www.usb.org . This class code may only be used in Device Descriptors. "),
  WIRE_ADAPTER_MULTIFUNCTION_PERIPHERAL_PROGRAMMING_INTERFACE(MISCELLANEOUS, (byte) 0xEF, null, (byte) 0x02, "Wire Adapter Multifunction Peripheral programming interface. Definition can be found in the Wireless USB Specification in Chapter 8. This class code may only be used in Device Descriptors "),
  CABLE_BASED_ASSOCIATION_FRAMEWORK(MISCELLANEOUS, (byte) 0xEF, (byte) 0x03, (byte) 0x01, "Cable Based Association Framework. This is defined in the Association Model addendum to the Wireless USB specification. This class code may only be used in Interface Descriptors. "),
  RNDIS_OVER_ETHERNET(MISCELLANEOUS, (byte) 0xEF, (byte) 0x04, (byte) 0x01, "RNDIS over Ethernet.  Connecting a host to the Internet via Ethernet mobile device. The device appears to the host as an Ethernet gateway device.  This class code may only be used in Interface Descriptors."),
  RNDIS_OVER_WIFI(MISCELLANEOUS, (byte) 0xEF, (byte) 0x04, (byte) 0x02, "RNDIS over WiFi.  Connecting a host to the Internet via WiFi enabled mobile device.  The device represents itself to the host as an 802.11 compliant network device.  This class code may only be used in Interface Descriptors."),
  RNDIS_OVER_WIMAX(MISCELLANEOUS, (byte) 0xEF, (byte) 0x04, (byte) 0x03, "RNDIS over WiMAX Connecting a host to the Internet via WiMAX enabled mobile device.  The device is represented to the host as an 802.16 network device.  This class code may only be used in Interface Descriptors."),
  RNDIS_OVER_WWAN(MISCELLANEOUS, (byte) 0xEF, (byte) 0x04, (byte) 0x04, "RNDIS over WWAN Connecting a host to the Internet via a device using mobile broadband, i.e. WWAN (GSM/CDMA).  This class code may only be used in Interface Descriptors."),
  RNDIS_FOR_RAW_IPV4(MISCELLANEOUS, (byte) 0xEF, (byte) 0x04, (byte) 0x05, "RNDIS for Raw IPv4 Connecting a host to the Internet using raw IPv4 via non-Ethernet mobile device.  Devices that provide raw IPv4, not in an Ethernet packet, may use this form to in lieu of other stock types.  This class code may only be used in Interface Descriptors."),
  RNDIS_FOR_RAW_IPV6(MISCELLANEOUS, (byte) 0xEF, (byte) 0x04, (byte) 0x06, "RNDIS for Raw IPv6 Connecting a host to the Internet using raw IPv6 via non-Ethernet mobile device.  Devices that provide raw IPv6, not in an Ethernet packet, may use this form to in lieu of other stock types.  This class code may only be used in Interface Descriptors."),
  RNDIS_FOR_GPRS(MISCELLANEOUS, (byte) 0xEF, (byte) 0x04, (byte) 0x07, "RNDIS for GPRS. Connecting a host to the Internet over GPRS mobile device using the device’s cellular radio"),
  USB3_VISION_CONTROL_INTERFACE(MISCELLANEOUS, (byte) 0xEF, (byte) 0x05, (byte) 0x00, "USB3 Vision Control Interface "),
  USB3_VISION_EVENT_INTERFACE(MISCELLANEOUS, (byte) 0xEF, (byte) 0x05, (byte) 0x01, "USB3 Vision Event Interface "),
  USB3_VISION_STREAMING_INTERFACE(MISCELLANEOUS, (byte) 0xEF, (byte) 0x05, (byte) 0x02, "USB3 Vision Streaming Interface"),
  DEVICE_FIRMWARE_UPGRADE(APPLICATION_SPECIFIC, (byte) 0xFE, (byte) 0x01, (byte) 0x01, "Device Firmware Upgrade.  Device class definition provided on www.usb.org. "),
  IRDA_BRIDGE_DEVICE(APPLICATION_SPECIFIC, (byte) 0xFE, (byte) 0x02, (byte) 0x00, "IRDA Bridge device.  Device class definition provided on www.usb.org. "),
  USB_TEST_AND_MEASUREMENT_DEVICE(APPLICATION_SPECIFIC, (byte) 0xFE, (byte) 0x03, (byte) 0x00, "USB Test and Measurement Device.  Definition provided in the USB Test and Measurement Class spec found on www.usb.org. "),
  USB_TEST_AND_MEASUREMENT_DEVICE_USBTMC_USB488(APPLICATION_SPECIFIC, (byte) 0xFE, (byte) 0x03, (byte) 0x01, "USB Test and Measurement Device conforming to the USBTMC USB488 Subclass Specification found on www.usb.org. "),
  VENDOR_SPECIFIC(EUSBClassCode.VENDOR_SPECIFIC, (byte) 0xFF, null, null, "Vendor specific ");
  /**
   * The Base Class.
   */
  private final EUSBClassCode parent;
  /**
   * The Base Class byte code.
   */
  private final Byte bytecodeBaseClass;
  /**
   * The SubClass byte code.
   */
  private final Byte bytecodeSubclass;
  /**
   * The Protocol byte code.
   */
  private final Byte bytecodeProtocol;
  /**
   * The USB Class description.
   */
  private final String description;

  private EUSBSubclassCode(EUSBClassCode parent, Byte bytecodeBaseClass, Byte bytecodeSubclass, Byte bytecodeProtocol, String description) {
    this.parent = parent;
    this.bytecodeBaseClass = bytecodeBaseClass;
    this.bytecodeSubclass = bytecodeSubclass;
    this.bytecodeProtocol = bytecodeProtocol;
    this.description = description;
  }

  /**
   * Get The Base Class byte code.
   * <p>
   * @return The parent byte code. Always defined (never null)
   */
  public Byte getBytecodeBaseClass() {
    return bytecodeBaseClass;
  }

  /**
   * Get The Protocol byte code.
   * <p>
   * @return The Protocol. Null if not defined or only one entry.
   */
  public Byte getBytecodeProtocol() {
    return bytecodeProtocol;
  }

  /**
   * Get The SubClass byte code.
   * <p>
   * @return The SubClass. Null if not defined or only one entry.
   */
  public Byte getBytecodeSubclass() {
    return bytecodeSubclass;
  }

  /**
   * Get The USB Class description. This is a human readable description.
   * <p>
   * @return The description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get the Base Class.
   * <p>
   * @return The parent as an enumerated instance.
   */
  public EUSBClassCode getParent() {
    return parent;
  }

  /**
   * Get an EUSBSubClassCode corresponding to a Base Class , SubClass , Protocol
   * configuration.
   * <p>
   * @param bytecodeBaseClass The Base Class byte code.
   * @param bytecodeSubclass  The SubClass byte code. (may be null)
   * @param bytecodeProtocol  The Protocol byte code. (may be null)
   * @return
   */
  public static EUSBSubclassCode fromBytes(Byte bytecodeBaseClass, Byte bytecodeSubclass, Byte bytecodeProtocol) {
    for (EUSBSubclassCode subclassCode : EUSBSubclassCode.values()) {
      if (subclassCode.getBytecodeBaseClass() == bytecodeBaseClass
        && (subclassCode.getBytecodeSubclass() == null || subclassCode.getBytecodeSubclass() == bytecodeSubclass)
        && (subclassCode.getBytecodeProtocol() == null || subclassCode.getBytecodeProtocol() == bytecodeProtocol)) {
        return subclassCode;
      }
    }
    return null;
  }

}
