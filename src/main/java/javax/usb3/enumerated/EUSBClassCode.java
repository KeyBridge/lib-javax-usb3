/*
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
package javax.usb3.enumerated;

import java.util.ArrayList;
import java.util.List;

/**
 * USB Class Codes.
 * <p>
 * USB defines class code information that is used to identify a device’s
 * functionality and to nominally load a device driver based on that
 * functionality.
 * <p>
 * The information is contained in three bytes with the names Base Class,
 * SubClass, and Protocol. (Note that ‘Base Class’ is used in this description
 * to identify the first byte of the Class Code triple. That terminology is not
 * used in the USB Specification).
 * <p>
 * There are two places on a device where class code information can be
 * placed.One place is in the Device Descriptor, and the other is in Interface
 * Descriptors. Some defined class codes are allowed to be used only in a Device
 * Descriptor, others can be used in both Device and Interface Descriptors, and
 * some can only be used in Interface Descriptors.
 *
 * @see <a href="http://www.usb.org/developers/defined_class">USB Class
 * Codes</a>
 * @author Jesse Caulfield
 * @TODO: Code the SubClass and Protocol enumerated types ... ?
 */
public enum EUSBClassCode {

  /**
   * Base Class 00h (Device)
   * <p>
   * This base class is defined to be used in Device Descriptors to indicate
   * that class information should be determined from the Interface Descriptors
   * in the device. There is one class code definition in this base class. All
   * other values are reserved.
   * <p>
   * This value is also used in Interface Descriptors to indicate a null class
   * code triple.
   */
  OTHER("Device", (byte) 0x00),
  /**
   * Base Class 01h (Audio)
   * <p>
   * This base class is defined for Audio capable devices that conform to the
   * Audio Device Class Specification found on the USB-IF website. That
   * specification defines the usable set of SubClass and Protocol values.
   * Values outside of that defined spec are reserved. These class codes may
   * only be used in Interface Descriptors.
   */
  AUDIO("Interface", (byte) 0x01),
  /**
   * Base Class 02h (Communications and CDC Control)
   * <p>
   * <p>
   * This base class is defined for devices that conform to the Communications
   * Device Class Specification found on the USB-IF website. That specification
   * defines the usable set of SubClass and Protocol values. Values outside of
   * that defined spec are reserved. Note that the Communication Device Class
   * spec requires some class code values (triples) to be used in Device
   * Descriptors and some to be used in Interface Descriptors.
   */
  COMMUNICATIONS_AND_CDC_CONTROL("Both", (byte) 0x02),
  /**
   * Base Class 03h (HID – Human Interface Device)
   * <p>
   * This base class is defined for devices that conform to the HID Device Class
   * Specification found on the USB-IF website. That specification defines the
   * usable set of SubClass and Protocol values. Values outside of that defined
   * spec are reserved. These class codes can only be used in Interface
   * Descriptors.
   */
  HID_HUMAN_INTERFACE_DEVICE("Interface", (byte) 0x03),
  /**
   * Base Class 05h (Physical)
   * <p>
   * This base class is defined for devices that conform to the Physical Device
   * Class Specification found on the USB-IF website. That specification defines
   * the usable set of SubClass and Protocol values. Values outside of that
   * defined spec are reserved. These class codes can only be used in Interface
   * Descriptors.
   */
  PHYSICAL("Interface", (byte) 0x05),
  /**
   * Base Class 06h (Still Imaging)
   * <p>
   * This base class is defined for devices that conform to the Imaging Device
   * Class Specification found on the USB-IF website. That specification defines
   * the usable set of SubClass and Protocol values. Values outside of that
   * defined spec are reserved.
   */
  IMAGE("Interface", (byte) 0x06),
  /**
   * Base Class 07h (Printer)
   * <p>
   * This base class is defined for devices that conform to the Printer Device
   * Class Specification found on the USB-IF website. That specification defines
   * the usable set of SubClass and Protocol values. Values outside of that
   * defined spec are reserved. These class codes can only be used in Interface
   * Descriptors.
   */
  PRINTER("Interface", (byte) 0x07),
  /**
   * Base Class 08h (Mass Storage)
   * <p>
   * This base class is defined for devices that conform to the Mass Storage
   * Device Class Specification found on the USB-IF website. That specification
   * defines the usable set of SubClass and Protocol values. Values outside of
   * that defined spec are reserved. These class codes can only be used in
   * Interface Descriptors.
   */
  MASS_STORAGE("Interface", (byte) 0x08),
  /**
   * Base Class 09h (Hub)
   * <p>
   * This base class is defined for devices that are USB hubs and conform to the
   * definition in the USB specification. That specification defines the
   * complete triples as shown below. All other values are reserved. These class
   * codes can only be used in Device Descriptors.
   * <p>
   * Protocol: Meaning<br/>
   * 00h : Full speed Hub <br/>
   * 01h : Hi-speed hub with single TT<br/>
   * 02h : Hi-speed hub with multiple TTs
   */
  HUB("Device", (byte) 0x09),
  /**
   * Base Class 0Ah (CDC-Data)
   * <p>
   * This base class is defined for devices that conform to the Communications
   * Device Class Specification found on the USB-IF website. That specification
   * defines the usable set of SubClass and Protocol values.Values outside of
   * that defined spec are reserved. These class codes can only be used in
   * Interface Descriptors.
   */
  CDC_DATA("Interface", (byte) 0x0A),
  /**
   * Base Class 0Bh (Smart Card)
   * <p>
   * This base class is defined for devices that conform to the Smart Card
   * Device Class Specification found on the USB-IF website. That specification
   * defines the usable set of SubClass and Protocol values.Values outside of
   * that defined spec are reserved. These class codes can only be used in
   * Interface Descriptors.
   */
  SMART_CARD("Interface", (byte) 0x0B),
  /**
   * Base Class 0Dh (Content Security)
   * <p>
   * This base class is defined for devices that conform to the Content Security
   * Device Class Specification found on the USB-IF website. That specification
   * defines the usable set of SubClass and Protocol values. Values outside of
   * that defined spec are reserved. These class codes can only be used in
   * Interface Descriptors.
   */
  CONTENT_SECURITY("Interface", (byte) 0x0D),
  /**
   * Base Class 0Eh (Video)
   * <p>
   * This base class is defined for devices that conform to the Video Device
   * Class Specification found on the USB-IF website. That specification defines
   * the usable set of SubClass and Protocol values. Values outside of that
   * defined spec are reserved. These class codes can only be used in Interface
   * Descriptors.
   */
  VIDEO("Interface", (byte) 0x0E),
  /**
   * Base Class 0Fh (Personal Healthcare)
   * <p>
   * This base class is defined for devices that conform to the Personal
   * Healthcare Device Class Specification found on the USB-IF website. That
   * specification defines the usable set of SubClass and Protocol values.
   * Values outside of that defined spec are reserved. These class codes should
   * only be used in Interface Descriptors.
   */
  PERSONAL_HEALTHCARE("Interface", (byte) 0x0F),
  /**
   * Base Class 10h (Audio/Video Devices)
   * <p>
   * The USB Audio/Video (AV) Device Class Definition describes the methods used
   * to communicate with devices or functions embedded in composite devices that
   * are used to manipulate audio, video, voice, and all image- and
   * sound-related functionality. That specification defines the usable set of
   * SubClass and Protocol values. Values outside of that defined spec are
   * reserved. These class codes can only be used in Interface Descriptors.
   *
   * @TODO: Code the 3 sub classes.
   */
  AUDIO_VIDEO_DEVICE("Interface", (byte) 0x10),
  /**
   * Base Class DCh (Diagnostic Device)
   * <p>
   * This base class is defined for devices that diagnostic devices. There is
   * currently only one value defined. All other values are reserved. This class
   * code can be used in Device or Interface Descriptors.
   * <p>
   * USB2 Compliance Device. Definition for this device can be found at
   * http://www.intel.com/technology/usb/spec.htm
   */
  DIAGNOSTIC_DEVICE("Both", (byte) 0xDC),
  /**
   * Base Class E0h (Wireless Controller)
   * <p>
   * This base class is defined for devices that are Wireless controllers.
   * Values not shown in the table below are reserved. These class codes are to
   * be used in Interface Descriptors, with the exception of the Bluetooth class
   * code which can also be used in a Device Descriptor.
   * <pre>
   * Base Class
   *    Sub Class
   *        Protocol
   * ---------------------------------------------------------------------------
   * E0h
   *    01h
   *        01h Bluetooth Programming Interface.  Get specific information from www.bluetooth.com.
   *        02h UWB Radio Control Interface.  Definition for this is found in the Wireless USB Specification in Chapter 8.
   *        03h Remote NDIS.  Information can be found at: http://www.microsoft.com/windowsmobile/mobileoperators/default.mspx
   *        04h	Bluetooth AMP Controller. Get specific information from www.bluetooth.com.
   *    2h
   *        01h Host Wire Adapter Control/Data interface.  Definition can be found in the Wireless USB Specification in Chapter 8.
   *        02h Device Wire Adapter Control/Data interface.  Definition can be found in the Wireless USB Specification in Chapter 8.
   *        03h Device Wire Adapter Isochronous interface.  Definition can be found in the Wireless USB Specification in Chapter 8.
   * </pre>
   */
  WIRELESS_CONTROLLER("Interface", (byte) 0xE0),
  /**
   * Base Class EFh (Miscellaneous)
   * <p>
   * This base class is defined for miscellaneous device definitions. Values not
   * shown in the table below are reserved. The use of these class codes (Device
   * or Interface descriptor) are specifically annotated in each entry below.
   *
   * @TODO: Code the 5 SubClasses and numerous protocols.
   */
  MISCELLANEOUS("Both", (byte) 0xEF),
  /**
   * Base Class FEh (Application Specific)
   * <p>
   * This base class is defined for devices that conform to several class
   * specifications found on the USB-IF website. That specification defines the
   * usable set of SubClass and Protocol values. Values outside of that defined
   * spec are reserved. These class codes can only be used in Interface
   * Descriptors.
   * <pre>
   * Base Class
   *    Sub Class
   *        Protocol
   * ---------------------------------------------------------------------------
   * EFh
   *    01h
   *        01h Active Sync device. This class code can be used in either Device or Interface Descriptors. Contact Microsoft for more information on this class.
   *        02h Palm Sync. This class code can be used in either Device or Interface Descriptors.
   *    02h
   *        01h Interface Association Descriptor. The usage of this class code triple is defined in the Interface Association Descriptor ECN that is provided on www.usb.org . This class code may only be used in Device Descriptors.
   *        02h Wire Adapter Multifunction Peripheral programming interface. Definition can be found in the Wireless USB Specification in Chapter 8. This class code may only be used in Device Descriptors
   *    03h
   *        01h Cable Based Association Framework. This is defined in the Association Model addendum to the Wireless USB specification. This class code may only be used in Interface Descriptors.
   *    04h
   *        01h RNDIS over Ethernet.  Connecting a host to the Internet via Ethernet mobile device. The device appears to the host as an Ethernet gateway device.  This class code may only be used in Interface Descriptors.
   *        02h RNDIS over WiFi.  Connecting a host to the Internet via WiFi enabled mobile device.  The device represents itself to the host as an 802.11 compliant network device.  This class code may only be used in Interface Descriptors.
   *        03h RNDIS over WiMAX Connecting a host to the Internet via WiMAX enabled mobile device.  The device is represented to the host as an 802.16 network device.  This class code may only be used in Interface Descriptors.
   *        04h RNDIS over WWAN Connecting a host to the Internet via a device using mobile broadband, i.e. WWAN (GSM/CDMA).  This class code may only be used in Interface Descriptors.
   *        05h RNDIS for Raw IPv4 Connecting a host to the Internet using raw IPv4 via non-Ethernet mobile device.  Devices that provide raw IPv4, not in an Ethernet packet, may use this form to in lieu of other stock types.  This class code may only be used in Interface Descriptors.
   *        06h RNDIS for Raw IPv6 Connecting a host to the Internet using raw IPv6 via non-Ethernet mobile device.  Devices that provide raw IPv6, not in an Ethernet packet, may use this form to in lieu of other stock types.  This class code may only be used in Interface Descriptors.
   *        07h RNDIS for GPRS Connecting a host to the Internet over GPRS mobile device using the device’s cellular radio
   *    05h
   *        00h USB3 Vision Control Interface Machine Vision Device conforming to the USB3 Vision specification. This standard covers cameras and other related devices that are typically used in machine vision, industrial, and embedded applications.  Reference: http://visiononline.org/  This class code may only be used in Interface Descriptors.
   * </pre>
   */
  APPLICATION_SPECIFIC("Interface", (byte) 0xFE),
  /**
   * Base Class FFh (Vendor Specific)
   * <p>
   * This base class is defined for vendors to use as they please. These class
   * codes can be used in both Device and Interface Descriptors.
   *    * <pre>
   * Base Class
   *    Sub Class
   *        Protocol
   * ---------------------------------------------------------------------------
   * FEh
   *    01h
   *        01h Device Firmware Upgrade.  Device class definition provided on www.usb.org .
   *    02h
   *        00h IRDA Bridge device.  Device class definition provided on www.usb.org .
   *    03h
   *        00h USB Test and Measurement Device.  Definition provided in the USB Test and Measurement Class spec found on www.usb.org .
   *        01h USB Test and Measurement Device conforming to the USBTMC USB488 Subclass Specification found on www.usb.org.
   * </pre>
   */
  VENDOR_SPECIFIC("Both", (byte) 0xFF);
  /**
   * The usage descriptor. e.g. "Interface, Device, Both).
   */
  private final String description;
  /**
   * The USB Class Codes. This is returned by a USB device as the bDeviceClass,
   * bInterfaceClass fields.
   */
  private final byte byteCode;

  private EUSBClassCode(String description, byte byteCode) {
    this.description = description;
    this.byteCode = byteCode;
  }

  /**
   * The Class code (assigned by the USB-IF).
   *
   * @return the USB Class code
   */
  public byte getByteCode() {
    return byteCode;
  }

  /**
   * Get the usage descriptor. e.g. "Interface, Device, Both).
   *
   * @return a String describing the USB class usage.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get an EUSBClassCode corresponding to the a bDeviceClass (or
   * bInterfaceClass) byte
   *
   * @param bDeviceClass a bDeviceClass (or bInterfaceClass) byte
   * @return the corresponding USBClassCode
   */
  public static EUSBClassCode fromByteCode(byte bDeviceClass) {
    for (EUSBClassCode code : EUSBClassCode.values()) {
      if (code.getByteCode() == bDeviceClass) {
        return code;
      }
    }
    return null;
  }

  /**
   * Get a list of sub-classes belonging to this class type. Typically only one
   * entry.
   *
   * @return a non-null ArrayList with at minimum one entry.
   */
  public List<EUSBSubclassCode> getChildren() {
    List<EUSBSubclassCode> subclasses = new ArrayList<>();
    for (EUSBSubclassCode subclass : EUSBSubclassCode.values()) {
      if (subclass.getParent().equals(this)) {
        subclasses.add(subclass);
      }
    }
    return subclasses;
  }

}
