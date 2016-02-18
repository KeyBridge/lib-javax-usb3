/*
 * Copyright (C) 2011 Klaus Reimer 
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
package javax.usb3.descriptor;

import java.util.Objects;
import javax.usb3.IUsbDeviceDescriptor;
import javax.usb3.enumerated.EDescriptorType;
import javax.usb3.enumerated.EUSBClassCode;
import javax.usb3.utility.ByteUtility;

/**
 * 9.6.1 Device Descriptor implementation.
 * <p>
 * Devices report their attributes using descriptors. A descriptor is a data
 * structure with a defined format.
 * <p>
 * A device descriptor describes general information about a USB device. It
 * includes information that applies globally to the device and all of the
 * device’s configurations. A USB device has only one device descriptor.
 *
 * @author Klaus Reimer 
 * @author Jesse Caulfield
 */
public abstract class AUsbDeviceDescriptor extends AUsbDescriptor implements IUsbDeviceDescriptor {

  /**
   * The USB specification version number.
   * <p>
   * USB Specification Release Number in Binary-Coded Decimal (i.e., 2.10 is
   * 210H). This field identifies the release of the USB Specification with
   * which the device and its descriptors are compliant.
   * <p>
   * The bcdUSB field contains a BCD version number. The value of the bcdUSB
   * field is 0xJJMN for version JJ.M.N (JJ – major version number, M – minor
   * version number, N – sub-minor version number), e.g., version 2.1.3 is
   * represented with value 0213H and version 3.0 is represented with a value of
   * 0300H.
   */
  private final short bcdUSB;
  /**
   * The device Class code (assigned by the USB-IF).
   * <p>
   * If this field is reset to zero, each interface within a configuration
   * specifies its own class information and the various interfaces operate
   * independently. If this field is set to a value between 1 and FEH, the
   * device supports different class specifications on different interfaces and
   * the interfaces may not operate independently. This value identifies the
   * class definition used for the aggregate interfaces. If this field is set to
   * FFH, the device class is vendor-specific.
   *
   * @see EUSBClassCode
   */
  private final EUSBClassCode bDeviceClass;
  /**
   * The device sub class. Subclass code (assigned by the USB-IF). These codes
   * are qualified by the value of the bDeviceClass field.
   *
   * @see EUSBClassCode
   */
  private final byte bDeviceSubClass;
  /**
   * The device protocol. Protocol code (assigned by the USB-IF). These codes
   * are qualified by the value of the bDeviceClass and the bDeviceSubClass
   * fields.
   *
   * @see EUSBClassCode
   */
  private final byte bDeviceProtocol;
  /**
   * The maximum packet size for endpoint zero.
   * <p>
   * The maximum packet size of a device’s default control pipe is described in
   * the device descriptor.
   */
  private final byte bMaxPacketSize0;
  /**
   * The Vendor ID (assigned by the USB-IF).
   */
  private final short idVendor;
  /**
   * The Product ID (assigned by the manufacturer)
   */
  private final short idProduct;
  /**
   * The Device release number in binary-coded decimal
   */
  private final short bcdDevice;
  /**
   * The manufacturer string descriptor index.
   */
  private final byte iManufacturer;
  /**
   * The product string descriptor index.
   */
  private final byte iProduct;
  /**
   * The serial number string descriptor index.
   */
  private final byte iSerialNumber;
  /**
   * The Number of possible configurations.
   * <p>
   * The bNumConfigurations field indicates the number of configurations at the
   * current operating speed. Configurations for the other operating speed are
   * not included in the count. If there are specific configurations of the
   * device for specific speeds, the bNumConfigurations field only reflects the
   * number of configurations for a single speed, not the total number of
   * configurations for both speeds.
   */
  private final byte bNumConfigurations;

  /**
   * Construct a new UsbDeviceDescriptor instance.
   * <p>
   * A device descriptor describes general information about a USB device. It
   * includes information that applies globally to the device and all of the
   * device’s configurations. A USB device has only one device descriptor.
   *
   * @param bcdUSB             The USB specification version number.
   * @param bDeviceClass       The device class.
   * @param bDeviceSubClass    The device sub class.
   * @param bDeviceProtocol    The device protocol.
   * @param bMaxPacketSize0    The maximum packet size for endpoint zero.
   * @param idVendor           The vendor ID.
   * @param idProduct          The product ID.
   * @param bcdDevice          The device release number.
   * @param iManufacturer      The manufacturer string descriptor index.
   * @param iProduct           The product string descriptor index.
   * @param iSerialNumber      The serial number string descriptor index.
   * @param bNumConfigurations The number of configurations.
   */
  public AUsbDeviceDescriptor(final short bcdUSB,
                              final EUSBClassCode bDeviceClass,//  final byte bDeviceClass,
                              final byte bDeviceSubClass,
                              final byte bDeviceProtocol,
                              final byte bMaxPacketSize0,
                              final short idVendor,
                              final short idProduct,
                              final short bcdDevice,
                              final byte iManufacturer,
                              final byte iProduct,
                              final byte iSerialNumber,
                              final byte bNumConfigurations) {
    super(EDescriptorType.DEVICE);
    this.bcdUSB = bcdUSB;
    this.bDeviceClass = bDeviceClass;
    this.bDeviceSubClass = bDeviceSubClass;
    this.bDeviceProtocol = bDeviceProtocol;
    this.bMaxPacketSize0 = bMaxPacketSize0;
    this.idVendor = idVendor;
    this.idProduct = idProduct;
    this.bcdDevice = bcdDevice;
    this.iManufacturer = iManufacturer;
    this.iProduct = iProduct;
    this.iSerialNumber = iSerialNumber;
    this.bNumConfigurations = bNumConfigurations;
  }

  /**
   * Get the USB Specification Release Number in Binary-Coded Decimal (i.e.,
   * 2.10 is 210H). This field identifies the release of the USB Specification
   * with which the device and its descriptors are compliant.
   *
   * @return This descriptor's bcdUSB.
   */
  @Override
  public short bcdUSB() {
    return this.bcdUSB;
  }

  /**
   * Class code (assigned by the USB-IF). If this field is reset to zero, each
   * interface within a configuration specifies its own class information and
   * the various interfaces operate independently.
   * <p>
   * If this field is set to a value between 1 and FEH, the device supports
   * different class specifications on different interfaces and the interfaces
   * may not operate independently. This value identifies the class definition
   * used for the aggregate interfaces.
   * <p>
   * If this field is set to FFH, the device class is vendor-specific.
   *
   * @return This descriptor's bDeviceClass.
   */
  @Override
  public EUSBClassCode bDeviceClass() {
    return this.bDeviceClass;
  }

  /**
   * Subclass code (assigned by the USB-IF).
   * <p>
   * These codes are qualified by the value of the bDeviceClass field.
   * <p>
   * If the bDeviceClass field is reset to zero, this field must also be reset
   * to zero.
   * <p>
   * If the bDeviceClass field is not set to FFH, all values are reserved for
   * assignment by the USB-IF.
   *
   *
   * @return This descriptor's bDeviceSubClass.
   */
  @Override
  public byte bDeviceSubClass() {
    return this.bDeviceSubClass;
  }

  /**
   * Protocol code (assigned by the USB-IF). These codes are qualified by the
   * value of the bDeviceClass and the bDeviceSubClass fields. If a device
   * supports class-specific protocols on a device basis as opposed to an
   * interface basis, this code identifies the protocols that the device uses as
   * defined by the specification of the device class.
   * <p>
   * If this field is reset to zero, the device does not use class-specific
   * protocols on a device basis. However, it may use class- specific protocols
   * on an interface basis.
   * <p>
   * If this field is set to FFH, the device uses a vendor-specific protocol on
   * a device basis.
   *
   * @return This descriptor's bDeviceProtocol.
   */
  @Override
  public byte bDeviceProtocol() {
    return this.bDeviceProtocol;
  }

  /**
   * Maximum packet size for endpoint zero (only 8, 16, 32, or 64 are valid)
   *
   * @return This descriptor's bMaxPacketSize.
   */
  @Override
  public byte bMaxPacketSize0() {
    return this.bMaxPacketSize0;
  }

  /**
   * Vendor ID (assigned by the USB-IF)
   *
   * @return This descriptor's idVendor.
   */
  @Override
  public short idVendor() {
    return this.idVendor;
  }

  /**
   * Product ID (assigned by the manufacturer)
   *
   * @return This descriptor's idProduct.
   */
  @Override
  public short idProduct() {
    return this.idProduct;
  }

  /**
   * Device release number in binary-coded decimal
   *
   * @return This descriptor's bcdDevice.
   */
  @Override
  public short bcdDevice() {
    return this.bcdDevice;
  }

  /**
   * Index of string descriptor describing manufacturer
   *
   * @return This descriptor's iManufacturer.
   */
  @Override
  public byte iManufacturer() {
    return this.iManufacturer;
  }

  /**
   * Index of string descriptor describing product
   *
   * @return This descriptor's iProduct.
   */
  @Override
  public byte iProduct() {
    return this.iProduct;
  }

  /**
   * Index of string descriptor describing the device’s serial number
   *
   * @return This descriptor's iSerialNumber.
   */
  @Override
  public byte iSerialNumber() {
    return this.iSerialNumber;
  }

  /**
   * Number of possible configurations
   *
   * @return This descriptor's bNumConfigurations.
   */
  @Override
  public byte bNumConfigurations() {
    return this.bNumConfigurations;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash += 61 * hash + super.hashCode();
    hash += 61 * hash + this.bcdUSB;
    hash += 61 * Objects.hashCode(this.bDeviceClass);
    hash += 61 * hash + this.bDeviceSubClass;
    hash += 61 * hash + this.bDeviceProtocol;
    hash += 61 * hash + this.bMaxPacketSize0;
    hash += 61 * hash + this.idVendor;
    hash += 61 * hash + this.idProduct;
    hash += 61 * hash + this.bcdDevice;
    hash += 61 * hash + this.iManufacturer;
    hash += 61 * hash + this.iProduct;
    hash += 61 * hash + this.iSerialNumber;
    hash += 61 * hash + this.bNumConfigurations;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    return this.hashCode() == obj.hashCode();
  }

  @Override
  public String toString() {
    return String.format("USB Device Descriptor:%n"
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
                         + "  iManufacturer %12d%n"
                         + "  iProduct %17d%n"
                         + "  iSerial %18d%n"
                         + "  bNumConfigurations %7d%n",
                         bLength() & 0xff,
                         bDescriptorType() & 0xff,
                         ByteUtility.decodeBCD(bcdUSB()),
                         bDeviceClass().getByteCode(),
                         bDeviceClass(),
                         bDeviceSubClass() & 0xff,
                         bDeviceProtocol() & 0xff,
                         bMaxPacketSize0() & 0xff,
                         String.format("0x%04x", idVendor() & 0xffff),
                         String.format("0x%04x", idProduct() & 0xffff),
                         ByteUtility.decodeBCD(bcdDevice()),
                         iManufacturer() & 0xff,
                         iProduct() & 0xff,
                         iSerialNumber() & 0xff,
                         bNumConfigurations() & 0xff);
  }
}
