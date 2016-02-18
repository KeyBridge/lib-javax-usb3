/*
 * Copyright 2013 Klaus Reimer 
 * Copyright (C) 2014 Key Bridge LLC. All Rights Reserved.
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
package javax.usb.utility;

import java.nio.ByteBuffer;
import javax.usb.enumerated.EUSBClassCode;
import org.usb4java.*;

/**
 * Utility methods used for descriptor dumps.
 * <p>
 * JMC: Reduced functionality to only dump descriptors. USB class codes are
 * handled via the USB Database (and USB Description) utility class.
 *
 * @author Klaus Reimer 
 * @author Jesse Caulfield
 */
public final class DescriptorDumpUtility {

  /**
   * Private constructor to prevent instantiation.
   */
  private DescriptorDumpUtility() {
    // Empty
  }

  /**
   * Decodes a binary-coded decimal into a string and returns it.
   *
   * @param bcd The binary-coded decimal to decode.
   * @return The decoded binary-coded decimal.
   */
  private static String decodeBCD(final short bcd) {
    return String.format("%x.%02x", (bcd & 0xFF00) >> 8, bcd & 0x00FF);
  }

  /**
   * Dumps the specified byte buffer into a hex string and returns it.
   *
   * @param bytes The bytes to dump.
   * @return The hex dump.
   */
  public static String dump(final ByteBuffer bytes) {
    bytes.rewind();
    final int columns = 16;
    final StringBuilder builder = new StringBuilder();
    int i = 0;
    while (bytes.hasRemaining()) {
      if ((i % columns) != 0) {
        builder.append(' ');
      } else if (i >= columns) {
        builder.append(String.format("%n"));
      }
      builder.append(String.format("%02x", bytes.get()));
      i++;
    }
    return builder.toString();
  }

  /**
   * Dumps the specified USB device descriptor into a string and returns it.
   *
   * @param descriptor The USB device descriptor to dump.
   * @return The descriptor dump.
   * @deprecated not used, phase out.
   */
  private static String dump(final DeviceDescriptor descriptor) {
    return dump(descriptor, null, null, null);
  }

  /**
   * Dumps the specified USB device descriptor into a string and returns it.
   *
   * @param descriptor   The USB device descriptor to dump.
   * @param manufacturer The manufacturer string or null if unknown.
   * @param product      The product string or null if unknown.
   * @param serial       The serial number strsing or null if unknown.
   * @return The descriptor dump.
   */
  public static String dump(final DeviceDescriptor descriptor,
                            final String manufacturer, final String product, final String serial) {
    return String.format(
            "Device Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bcdUSB %19s%n"
            + "  bDeviceClass %13d %s%n"
            + "  bDeviceSubClass %10d%n"
            + "  bDeviceProtocol %10d%n"
            + "  bMaxPacketSize0 %10d%n"
            + "  idVendor %17s%n"
            + "  idProduct %16s%n"
            + "  bcdDevice %16s%n"
            + "  iManufacturer %12d%s%n"
            + "  iProduct %17d%s%n"
            + "  iSerial %18d%s%n"
            + "  bNumConfigurations %7d%n",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            decodeBCD(descriptor.bcdUSB()),
            descriptor.bDeviceClass() & 0xff,
            EUSBClassCode.fromByteCode(descriptor.bDeviceClass()),
            descriptor.bDeviceSubClass() & 0xff,
            descriptor.bDeviceProtocol() & 0xff,
            descriptor.bMaxPacketSize0() & 0xff,
            String.format("0x%04x", descriptor.idVendor() & 0xffff),
            String.format("0x%04x", descriptor.idProduct() & 0xffff),
            decodeBCD(descriptor.bcdDevice()),
            descriptor.iManufacturer() & 0xff,
            (manufacturer == null) ? "" : (" " + manufacturer),
            descriptor.iProduct() & 0xff,
            (product == null) ? "" : (" " + product),
            descriptor.iSerialNumber() & 0xff,
            (serial == null) ? "" : (" " + serial),
            descriptor.bNumConfigurations() & 0xff);
  }

  /**
   * Dumps the specified USB configuration descriptor into a string and returns
   * it.
   *
   * @param descriptor The USB configuration descriptor to dump.
   * @return The descriptor dump.
   */
  public static String dump(final ConfigDescriptor descriptor) {
    return String.format(
            "Configuration Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  wTotalLength %13d%n"
            + "  bNumInterfaces %11d%n"
            + "  bConfigurationValue %6d%n"
            + "  iConfiguration %11d%n"
            + "  bmAttributes %13s%n"
            + "    %s%n"
            + "%s"
            + "  bMaxPower %16smA%n",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            descriptor.wTotalLength() & 0xffff,
            descriptor.bNumInterfaces() & 0xff,
            descriptor.bConfigurationValue() & 0xff,
            descriptor.iConfiguration() & 0xff,
            String.format("0x%02x", descriptor.bmAttributes() & 0xff),
            ((descriptor.bmAttributes() & 64) == 0) ? "(Bus Powered)"
            : "Self Powered",
            ((descriptor.bmAttributes() & 32) == 0) ? ""
            : String.format("    Remote Wakeup%n"),
            (descriptor.bMaxPower() & 0xff) * 2);
  }

  /**
   * Dumps the specified USB interface descriptor into a string and returns it.
   *
   * @param descriptor The USB interface descriptor to dump.
   * @return The descriptor dump.
   */
  public static String dump(final InterfaceDescriptor descriptor) {
    return String.format(
            "Interface Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bInterfaceNumber %9d%n"
            + "  bAlternateSetting %8d%n"
            + "  bNumEndpoints %12d%n"
            + "  bInterfaceClass %10d %s%n"
            + "  bInterfaceSubClass %7d%n"
            + "  bInterfaceProtocol %7d%n"
            + "  iInterface %15d%n",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            descriptor.bInterfaceNumber() & 0xff,
            descriptor.bAlternateSetting() & 0xff,
            descriptor.bNumEndpoints() & 0xff,
            descriptor.bInterfaceClass() & 0xff,
            EUSBClassCode.fromByteCode(descriptor.bInterfaceClass()),
            descriptor.bInterfaceSubClass() & 0xff,
            descriptor.bInterfaceProtocol() & 0xff,
            descriptor.iInterface() & 0xff);
  }

  /**
   * Dumps the specified USB endpoint descriptor into a string and returns it.
   *
   * @param descriptor The USB endpoint descriptor to dump.
   * @return The descriptor dump.
   */
  public static String dump(final EndpointDescriptor descriptor) {
    return String.format(
            "Endpoint Descriptor:%n"
            + "  bLength %18d%n"
            + "  bDescriptorType %10d%n"
            + "  bEndpointAddress %9s  EP %d %s%n"
            + "  bmAttributes %13d%n"
            + "    Transfer Type             %s%n"
            + "    Synch Type                %s%n"
            + "    Usage Type                %s%n"
            + "  wMaxPacketSize %11d%n"
            + "  bInterval %16d%n",
            descriptor.bLength(),
            descriptor.bDescriptorType(),
            String.format("0x%02x", descriptor.bEndpointAddress() & 0xff),
            descriptor.bEndpointAddress() & 0x0f,
            getDirectionName(descriptor.bEndpointAddress()),
            descriptor.bmAttributes() & 0xff,
            getTransferTypeName(descriptor.bmAttributes()),
            getSynchTypeName(descriptor.bmAttributes()),
            getUsageTypeName(descriptor.bmAttributes()),
            descriptor.wMaxPacketSize() & 0xffff,
            descriptor.bInterval() & 0xff);
  }

  /**
   * Returns the name for the transfer type in the specified endpoint
   * attributes.
   *
   * @param bmAttributes The endpoint attributes value.
   * @return The transfer type name.
   */
  public static String getTransferTypeName(final byte bmAttributes) {
    switch (bmAttributes & LibUsb.TRANSFER_TYPE_MASK) {
      case LibUsb.TRANSFER_TYPE_CONTROL:
        return "Control";
      case LibUsb.TRANSFER_TYPE_ISOCHRONOUS:
        return "Isochronous";
      case LibUsb.TRANSFER_TYPE_BULK:
        return "Bulk";
      case LibUsb.TRANSFER_TYPE_INTERRUPT:
        return "Interrupt";
      default:
        return "Unknown";
    }
  }

  /**
   * Returns the name for the synchronization type in the specified endpoint
   * attributes.
   *
   * @param bmAttributes The endpoint attributes value.
   * @return The synch type name.
   */
  public static String getSynchTypeName(final byte bmAttributes) {
    switch ((bmAttributes & LibUsb.ISO_SYNC_TYPE_MASK) >> 2) {
      case LibUsb.ISO_SYNC_TYPE_NONE:
        return "None";
      case LibUsb.ISO_SYNC_TYPE_ASYNC:
        return "Asynchronous";
      case LibUsb.ISO_SYNC_TYPE_ADAPTIVE:
        return "Adaptive";
      case LibUsb.ISO_SYNC_TYPE_SYNC:
        return "Synchronous";
      default:
        return "Unknown";
    }
  }

  /**
   * Returns the name for the usage type in the specified endpoint attributes.
   *
   * @param bmAttributes The endpoint attributes value.
   * @return The usage type name.
   */
  public static String getUsageTypeName(final byte bmAttributes) {
    switch ((bmAttributes & LibUsb.ISO_USAGE_TYPE_MASK) >> 4) {
      case LibUsb.ISO_USAGE_TYPE_DATA:
        return "Data";
      case LibUsb.ISO_USAGE_TYPE_FEEDBACK:
        return "Feedback";
      case LibUsb.ISO_USAGE_TYPE_IMPLICIT:
        return "Implicit Feedback Data";
      case 3:
        // b11 is considered "Reserved" according to USB 3.0 spec.
        return "Reserved";
      default:
        return "Unknown";
    }
  }

  /**
   * Returns the name of the USB traffic direction for the specified endpoint
   * address.
   *
   * @param bEndpointAddress The endpoint address.
   * @return The direction name.
   */
  public static String getDirectionName(final byte bEndpointAddress) {
    return ((bEndpointAddress & LibUsb.ENDPOINT_IN) == 0) ? "OUT" : "IN";
  }
}
