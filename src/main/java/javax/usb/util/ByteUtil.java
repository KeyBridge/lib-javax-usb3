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
package javax.usb.util;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Byte and Bit manipulation utilities. This utility class provides a number of
 * convenience methods to simplify byte conversion, bit shifting and byte array
 * handling.
 * <p>
 * @author Jesse Caulfield
 */
@SuppressWarnings("ValueOfIncrementOrDecrementUsed")
public class ByteUtil {

  /**
   * All methods are static. Hide the constructor to prevent bad code.
   */
  private ByteUtil() {
  }

  /**
   * Returns a byte array containing the two's-complement representation of the
   * integer. The byte array will be in big-endian byte-order with a fixes
   * length of 4 (the least significant byte is in the 4th element).
   * <p>
   * <b>Example:</b>
   * <code>intToByteArray(258)</code> will return { 0, 0, 1, 2 },
   * <code>BigInteger.valueOf(258).toBytes()</code> returns { 1, 2 }.
   * <p>
   * @param integer The integer to be converted.
   * @return The byte array of length 4.
   */
  public static byte[] intToByteArray(final int integer) {
    int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
    byte[] byteArray = new byte[4];

    for (int n = 0; n < byteNum; n++) {
      byteArray[3 - n] = (byte) (integer >>> (n * 8));
    }
    return (byteArray);
  }

  /**
   * Tests whether two byte arrays are equal
   * <p>
   * @param left
   * @param right
   * @return
   */
  public static boolean equals(byte[] left, byte[] right) {
    if ((left == null) || (right == null)) {
      return false;
    }
    if (left.length != right.length) {
      return false;
    }
    for (int i = 0; i < left.length; i++) {
      if (left[i] != right[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * Get one bit back from a bit string within a multi-byte array at the
   * specified position:
   * <p>
   * @param data the Byte to study
   * @param pos  the bit position to get [0-7] (zero to seven)
   * @return returns zero or one
   */
  public static int getBit(byte[] data, int pos) {
    int posByte = pos / 8;
    int posBit = pos % 8;
    byte valByte = data[posByte];
    int valInt = valByte >> (8 - (posBit + 1)) & 0x0001;
    return valInt;
  }

  /**
   * Get one bit back from a bit string within a single byte at the specified
   * position:
   * <p>
   * @param data the Byte to study
   * @param pos  the bit position to get [0-7] (zero to seven)
   * @return returns zero or one
   */
  public static int getBit(byte data, int pos) {
    //    int posByte = pos / 8;
    int posBit = pos % 8;
    //    byte valByte = data;
    int valInt = data >> (8 - (posBit + 1)) & 0x0001;
    return valInt;
  }

  /**
   * Set one bit in a byte at the specified position with the specified bit
   * value. The original data byte is not touched. It is copied, and a new byte
   * with the bit set is returned.
   * <p>
   * @param data the Byte to study
   * @param pos  the bit position to get [0-7] (zero to seven)
   * @param val  the value to set (zero or one)
   * @return the converted data byte (copy of)
   */
  public static byte setBit(byte data, int pos, int val) {
    int posBit = pos % 8;
    return (byte) ((val << (8 - (posBit + 1))) | data);
  }

  /**
   * Set one bit to a bit string at the specified position with the specified
   * bit value.
   * <p>
   * @param data the Byte to study
   * @param pos  the bit position to get [0-7] (zero to seven)
   * @param val  the value to set (zero or one)
   */
  public static void setBit(byte[] data, int pos, int val) {
    int posByte = pos / 8;
    int posBit = pos % 8;
    byte oldByte = data[posByte];
    oldByte = (byte) (((0xFF7F >> posBit) & oldByte) & 0x00FF);
    byte newByte = (byte) ((val << (8 - (posBit + 1))) | oldByte);
    data[posByte] = newByte;
  }

  /**
   * Unsigned int from two bytes
   * <p>
   * @param bytes a two-byte array
   * @param index the index offset into the byte array to sample
   * @return a two-byte integer
   */
  public static int twoByteIntFromBytes(byte[] bytes, int index) {
    int idx = index;
    int i = bytes[idx++] << 8 & 0x0000FF00;
    i |= bytes[idx];
    return i;
  }

  /**
   * Get an unsigned integer from a 4-byte word
   * <p>
   * @param bytes a 4-byte array
   * @param index the index offset into the byte array to sample
   * @return a 4-byte long
   */
  public static long intFrom4Bytes(byte[] bytes, int index) {
    return intFrom4Bytes(bytes, index, false);
  }

  /**
   * Get a signed or unsigned integer from a 4-byte word.
   * <p>
   * @param bytes  a 4-byte array
   * @param index  the index offset into the byte array to sample
   * @param signed indicator for signed/unsigned integer
   * @return a signed or unsigned integer from a 4-byte word
   */
  public static long intFrom4Bytes(byte[] bytes, int index, boolean signed) {
    int idx = index;
    long val = 0;
    /*
     * byte high = bytes[idx++]; i |= (high << 24) & 0x00000000FF000000; i |=
     * (bytes[idx++] << 16) & 0x0000000000FF0000; i |= (bytes[idx++] << 8) &
     * 0x000000000000FF00; i |= bytes[idx++] & 0x00000000000000FF; if(signed) {
     * if((high & 0x80) == 1) i*=-1; }
     */
    for (int i = 0; i < 4; i++) {
      if (i < 3) {
        val |= (((long) bytes[idx + i] & 0x000000ff) << (32 - ((i + 1) * 8)));
      } else {
        val |= ((long) bytes[idx + i] & 0x000000ff);
      }

    }
    return val;
  }

  /**
   * Get an unsigned integer from a single byte.
   * <p>
   * Java assumes all Byte integers are signed and range from -128 to 128. This
   * returns an integer from 0 to 255
   * <p>
   * @param byteValue a single byte value
   * @return an integer from 0 to 255
   */
  public static int intFromByte(byte byteValue) {
    int val = 0;
    val |= byteValue & 0x000000ff;
    return val;
  }

  /**
   * Get an unsigned integer from two bytes sampled from within a byte stream,
   * starting at the specified index
   * <p>
   * @param bytes a 2-byte array
   * @param index the index offset into the byte array to sample
   * @return an unsigned integer
   */
  public static int intFrom2Bytes(byte[] bytes, int index) {
    return ((bytes[index] << 8) + bytes[index + 1]);
  }

  //----------------------------------------------------------------------------
  /**
   * Get the specified byte's value as an unsigned short.
   * <p>
   * This converts the specified byte into a short. The least significant byte
   * (8 bits) of the short will be identical to the byte (8 bits) provided, and
   * the most significant byte (8 bits) of the short will be zero.
   * <p>
   * For many of the values in this USB API, unsigned bytes are used. However,
   * since Java does not include unsigned bytes in the language, those unsigned
   * bytes must be converted to a larger storage type before being used in
   * unsigned calculations.
   * <p>
   * @param b the byte to convert.
   * @return An unsigned short representing the specified byte.
   */
  public static short unsignedShort(byte b) {
    return (short) (0x00ff & b);
  }

  /**
   * Get the specified byte's value as an unsigned integer.
   * <p>
   * This converts the specified byte into an integer. The least significant
   * byte (8 bits) of the integer will be identical to the byte (8 bits)
   * provided, and the most significant 3 bytes (24 bits) of the integer will be
   * zero.
   * <p>
   * For many of the values in this USB API, unsigned bytes are used. However,
   * since Java does not include unsigned bytes in the language, those unsigned
   * bytes must be converted to a larger storage type before being used in
   * unsigned calculations.
   * <p>
   * @param b the byte to convert.
   * @return An unsigned int representing the specified byte.
   */
  public static int unsignedInt(byte b) {
    return 0x000000ff & b;
  }

  /**
   * Get the specified short's value as an unsigned integer.
   * <p>
   * This converts the specified byte into an integer. The least significant
   * short (16 bits) of the integer will be identical to the short (16 bits)
   * provided, and the most significant 2 bytes (16 bits) of the integer will be
   * zero.
   * <p>
   * For many of the values in this USB API, unsigned shorts are used. However,
   * since Java does not include unsigned short in the language, those unsigned
   * shorts must be converted to a larger storage type before being used in
   * unsigned calculations.
   * <p>
   * @param s the short to convert.
   * @return An unsigned int representing the specified short.
   */
  public static int unsignedInt(short s) {
    return 0x0000ffff & s;
  }

  /**
   * Get the specified byte's value as an unsigned long.
   * <p>
   * This converts the specified byte into a long. The least significant byte (8
   * bits) of the long will be identical to the byte (8 bits) provided, and the
   * most significant 7 bytes (56 bits) of the long will be zero.
   * <p>
   * For many of the values in this USB API, unsigned bytes are used. However,
   * since Java does not include unsigned bytes in the language, those unsigned
   * bytes must be converted to a larger storage type before being used in
   * unsigned calculations.
   * <p>
   * @param b the byte to convert.
   * @return An unsigned long representing the specified byte.
   */
  public static long unsignedLong(byte b) {
    return 0x00000000000000ff & b;
  }

  /**
   * Get the specified short's value as an unsigned long.
   * <p>
   * This converts the specified byte into a long. The least significant short
   * (16 bits) of the long will be identical to the short (16 bits) provided,
   * and the most significant 6 bytes (48 bits) of the long will be zero.
   * <p>
   * For many of the values in this USB API, unsigned shorts are used. However,
   * since Java does not include unsigned short in the language, those unsigned
   * shorts must be converted to a larger storage type before being used in
   * unsigned calculations.
   * <p>
   * @param s the short to convert.
   * @return An unsigned long representing the specified short.
   */
  public static long unsignedLong(short s) {
    return 0x000000000000ffff & s;
  }

  /**
   * Get the specified int's value as an unsigned long.
   * <p>
   * This converts the specified int into a long. The least significant int (32
   * bits) of the long will be identical to the int (32 bits) provided, and the
   * most significant int (32 bits) of the long will be zero.
   * <p>
   * @param i the int to convert.
   * @return An unsigned long representing the specified int.
   */
  public static long unsignedLong(int i) {
    return 0x00000000ffffffff & i;
  }

  /**
   * Convert 2 bytes into a short.
   * <p>
   * This converts the 2 bytes into a short. The msb will be the high byte (8
   * bits) of the short, and the lsb will be the low byte (8 bits) of the short.
   * <p>
   * @param msb The Most Significant Byte.
   * @param lsb The Least Significant Byte.
   * @return A short representing the bytes.
   */
  public static short toShort(byte msb, byte lsb) {
    return (short) ((0xff00 & (short) (msb << 8)) | (0x00ff & (short) lsb));
  }

  /**
   * Convert 4 bytes into an int.
   * <p>
   * This converts the 4 bytes into an int.
   * <p>
   * @param byte3 The byte to be left-shifted 24 bits.
   * @param byte2 The byte to be left-shifted 16 bits.
   * @param byte1 The byte to be left-shifted 8 bits.
   * @param byte0 The byte that will not be left-shifted.
   * @return An int representing the bytes.
   */
  public static int toInt(byte byte3, byte byte2, byte byte1, byte byte0) {
    return toInt(toShort(byte3, byte2), toShort(byte1, byte0));
  }

  /**
   * Convert 8 bytes into a long.
   * <p>
   * This converts the 8 bytes into a long.
   * <p>
   * @param byte7 The byte to be left-shifted 56 bits.
   * @param byte6 The byte to be left-shifted 48 bits.
   * @param byte5 The byte to be left-shifted 40 bits.
   * @param byte4 The byte to be left-shifted 32 bits.
   * @param byte3 The byte to be left-shifted 24 bits.
   * @param byte2 The byte to be left-shifted 16 bits.
   * @param byte1 The byte to be left-shifted 8 bits.
   * @param byte0 The byte that will not be left-shifted.
   * @return A long representing the bytes.
   */
  public static long toLong(byte byte7, byte byte6, byte byte5, byte byte4, byte byte3, byte byte2, byte byte1, byte byte0) {
    return toLong(toInt(byte7, byte6, byte5, byte4), toInt(byte3, byte2, byte1, byte0));
  }

  /**
   * Convert 2 shorts into an int.
   * <p>
   * This converts the 2 shorts into an int.
   * <p>
   * @param mss The Most Significant Short.
   * @param lss The Least Significant Short.
   * @return An int representing the shorts.
   */
  public static int toInt(short mss, short lss) {
    return ((0xffff0000 & mss << 16) | (0x0000ffff & (int) lss));
  }

  /**
   * Convert 4 shorts into a long.
   * <p>
   * This converts the 4 shorts into a long.
   * <p>
   * @param short3 The short to be left-shifted 48 bits.
   * @param short2 The short to be left-shifted 32 bits.
   * @param short1 The short to be left-shifted 16 bits.
   * @param short0 The short that will not be left-shifted.
   * @return A long representing the shorts.
   */
  public static long toLong(short short3, short short2, short short1, short short0) {
    return toLong(toInt(short3, short2), toInt(short1, short0));
  }

  /**
   * Convert 2 ints into a long.
   * <p>
   * This converts the 2 ints into a long.
   * <p>
   * @param msi The Most Significant Int.
   * @param lsi The Least Significant Int.
   * @return A long representing the ints.
   */
  public static long toLong(int msi, int lsi) {
    /*
     * We can't represent a mask for the MSI, but that's ok, we don't really
     * need one; left-shifting sets the low bits to 0.
     */
    return (long) msi << 32 | (long) 0x00000000ffffffff & (long) lsi;
  }

  /**
   * Format a byte into a proper length hex String.
   * <p>
   * This is identical to Long.toHexString() except this pads (with 0's) to the
   * proper size.
   * <p>
   * @param b the byte to convert
   * @return the byte, converted to a hex string.
   */
  public static String toHexString(byte b) {
    return toHexString(unsignedLong(b), '0', 2, 2);
  }

  /**
   * Format a short into a proper length hex String.
   * <p>
   * This is identical to Long.toHexString() except this pads (with 0's) to the
   * proper size.
   * <p>
   * @param s the short to convert
   * @return the short, converted to a hex string.
   */
  public static String toHexString(short s) {
    return toHexString(unsignedLong(s), '0', 4, 4);
  }

  /**
   * Format a int into a proper length hex String.
   * <p>
   * This is identical to Long.toHexString() except this pads (with 0's) to the
   * proper size.
   * <p>
   * @param i the integer to convert
   * @return the integer, converted to a hex string.
   */
  public static String toHexString(int i) {
    return toHexString(unsignedLong(i), '0', 8, 8);
  }

  /**
   * Format a long into the specified length hex String.
   * <p>
   * This is identical to Long.toHexString() except this pads (with 0's) to the
   * proper size.
   * <p>
   * @param l the long to convert
   * @return the long, converted to a hex string.
   */
  public static String toHexString(long l) {
    return toHexString(l, '0', 16, 16);
  }

  /**
   * Format a long into the specified length hex String.
   * <p>
   * This is identical to Long.toHexString() except this pads (with 0's), or
   * truncates, to the specified size. If max &lt; min the functionality is
   * exactly as Long.toHexString().
   * <p>
   * @param l   the long to convert
   * @param c   the character to use for padding
   * @param min the min length of the resulting String
   * @param max the max length of the resulting String
   * @return the long, converted to a hex string.
   */
  public static String toHexString(long l, char c, int min, int max) {
    StringBuilder sb = new StringBuilder(Long.toHexString(l));

    if (max < min) {
      return sb.toString();
    }

    while (sb.length() < max) {
      sb.insert(0, c);
    }

    return sb.substring(sb.length() - min);
  }

  /**
   * Format a byte[] into a hex String.
   * <p>
   * This creates a String by concatenating the result of
   * <code>delimiter + {@link #toHexString(byte) toHexString(byte)}</code> for
   * each byte in the array. If the specified length is greater than the actual
   * array length, the array length is used. If the specified length (or array
   * length) is 0 or less, the resulting String will be an empty String.
   * <p>
   * @param delimiter The delimiter to prefix every byte with.
   * @param array     The byte[] to convert.
   * @param length    The number of bytes to use.
   * @return A String representing the byte[].
   * @exception NullPointerException If the byte[] is null.
   */
  @SuppressWarnings("AssignmentToMethodParameter")
  public static String toHexString(String delimiter, byte[] array, int length) {
    StringBuilder sb = new StringBuilder();

    if (length > array.length) {
      length = array.length;
    }

    if (length < 0) {
      length = 0;
    }

    for (int i = 0; i < length; i++) {
      sb.append(delimiter).append(toHexString(array[i]));
    }

    return sb.toString();
  }

  /**
   * Format a short[] into a hex String.
   * <p>
   * This creates a String by concatenating the result of
   * <code>delimiter + {@link #toHexString(short) toHexString(short)}</code> for
   * each short in the array. If the specified length is greater than the actual
   * array length, the array length is used. If the specified length (or array
   * length) is 0 or less, the resulting String will be an empty String.
   * <p>
   * @param delimiter The delimiter to prefix every short with.
   * @param array     The short[] to convert.
   * @param length    The number of shorts to use.
   * @return A String representing the short[].
   * @exception NullPointerException If the short[] is null.
   */
  @SuppressWarnings("AssignmentToMethodParameter")
  public static String toHexString(String delimiter, short[] array, int length) {
    StringBuilder sb = new StringBuilder();

    if (length > array.length) {
      length = array.length;
    }

    if (length < 0) {
      length = 0;
    }

    for (int i = 0; i < length; i++) {
      sb.append(delimiter).append(toHexString(array[i]));
    }

    return sb.toString();
  }

  /**
   * Format a int[] into a hex String.
   * <p>
   * This creates a String by concatenating the result of
   * <code>delimiter + {@link #toHexString(int) toHexString(int)}</code> for
   * each int in the array. If the specified length is greater than the actual
   * array length, the array length is used. If the specified length (or array
   * length) is 0 or less, the resulting String will be an empty String.
   * <p>
   * @param delimiter The delimiter to prefix every int with.
   * @param array     The int[] to convert.
   * @param length    The number of ints to use.
   * @return A String representing the int[].
   * @exception NullPointerException If the int[] is null.
   */
  @SuppressWarnings("AssignmentToMethodParameter")
  public static String toHexString(String delimiter, int[] array, int length) {
    StringBuilder sb = new StringBuilder();

    if (length > array.length) {
      length = array.length;
    }

    if (length < 0) {
      length = 0;
    }

    for (int i = 0; i < length; i++) {
      sb.append(delimiter).append(toHexString(array[i]));
    }

    return sb.toString();
  }

  /**
   * Format a long[] into a hex String.
   * <p>
   * This creates a String by concatenating the result of
   * <code>delimiter + {@link #toHexString(long) toHexString(long)}</code> for
   * each long in the array. If the specified length is greater than the actual
   * array length, the array length is used. If the specified length (or array
   * length) is 0 or less, the resulting String will be an empty String.
   * <p>
   * @param delimiter The delimiter to prefix every long with.
   * @param array     The long[] to convert.
   * @param length    The number of longs to use.
   * @return A String representing the long[].
   * @exception NullPointerException If the long[] is null.
   */
  @SuppressWarnings("AssignmentToMethodParameter")
  public static String toHexString(String delimiter, long[] array, int length) {
    StringBuilder sb = new StringBuilder();

    if (length > array.length) {
      length = array.length;
    }

    if (length < 0) {
      length = 0;
    }

    for (int i = 0; i < length; i++) {
      sb.append(delimiter).append(toHexString(array[i]));
    }

    return sb.toString();
  }

  /**
   * Format a byte[] into a hex String.
   * <p>
   * This calls
   * {@link #toHexString(String,byte[],int) toHexString(delimiter, array, array.length)}.
   * <p>
   * @param delimiter The delimiter to prefix every byte with.
   * @param array     The byte[] to convert.
   * @return A String representing the byte[].
   * @exception NullPointerException If the byte[] is null.
   */
  public static String toHexString(String delimiter, byte[] array) {
    return toHexString(delimiter, array, array.length);
  }

  /**
   * Format a short[] into a hex String.
   * <p>
   * This calls
   * {@link #toHexString(String,short[],int) toHexString(delimiter, array, array.length)}.
   * <p>
   * @param delimiter The delimiter to prefix every short with.
   * @param array     The short[] to convert.
   * @return A String representing the short[].
   * @exception NullPointerException If the short[] is null.
   */
  public static String toHexString(String delimiter, short[] array) {
    return toHexString(delimiter, array, array.length);
  }

  /**
   * Format a int[] into a hex String.
   * <p>
   * This calls
   * {@link #toHexString(String,int[],int) toHexString(delimiter, array, array.length)}.
   * <p>
   * @param delimiter The delimiter to prefix every int with.
   * @param array     The int[] to convert.
   * @return A String representing the int[].
   * @exception NullPointerException If the int[] is null.
   */
  public static String toHexString(String delimiter, int[] array) {
    return toHexString(delimiter, array, array.length);
  }

  /**
   * Format a long[] into a hex String.
   * <p>
   * This calls
   * {@link #toHexString(String,long[],int) toHexString(delimiter, array, array.length)}.
   * <p>
   * @param delimiter The delimiter to prefix every long with.
   * @param array     The long[] to convert.
   * @return A String representing the long[].
   * @exception NullPointerException If the long[] is null.
   */
  public static String toHexString(String delimiter, long[] array) {
    return toHexString(delimiter, array, array.length);
  }

  //----------------------------------------------------------------------------
  // From http://snippets.dzone.com/posts/show/93
  /**
   * Get an unsigned integer from a two bytes
   * <p>
   * @param bytes a two-byte array
   * @return an unsigned integer
   */
  public static int intFrom2Bytes(byte[] bytes) {
    return intFrom2Bytes(bytes, 0);
  }

  /**
   * Unsigned Long from 8 bytes
   * <p>
   * @param bytes an 8-byte array
   * @param index the index offset into the byte array to sample
   * @return Unsigned Long
   */
  public static long longFromBytes(byte[] bytes, int index) {
    if ((bytes == null) || (bytes.length < 7)) {
      return -1;
    }
    int idx = index;
    long val = 0;
    for (int i = 0; i < 8; i++) {
      if (i < 7) {
        val |= (((long) bytes[idx + i] & 0x000000ff) << (64 - ((i + 1) * 8)));
      } else {
        val |= ((long) bytes[idx + i] & 0x000000ff);
      }
    }
    return val;
  }

  /**
   * Tests whether one byte array contains another.
   * <p>
   * @param a          the byte array to examine
   * @param b          the byte array we're looking for
   * @param startIndex the index in a to begin looking
   * @return TRUE if the first byte array contains the second
   */
  public static boolean contains(byte[] a, byte[] b, int startIndex) {
    boolean isEqual = false;
    if ((a != null) && (b != null)) {
      for (int i = startIndex; (i < b.length) || (startIndex + i < a.length); i++) {
        isEqual = a[i] == b[i];
        if (!isEqual) {
          break;
        }
      }
    }
    return isEqual;
  }

  /**
   * Unsigned Long to 8 bytes
   * <p>
   * @param l        a Java long
   * @param arr      a byte array, 8-bytes or longer
   * @param startIdx the start index into the array where the LONG should be
   *                 written
   */
  public static void longToBytes(long l, byte[] arr, int startIdx) {
    if (arr == null) {
      return;
    }
    int idx = startIdx;
    arr[idx++] = (byte) ((l >>> 56) & 0x00000000000000ff);
    arr[idx++] = (byte) ((l >>> 48) & 0x00000000000000ff);
    arr[idx++] = (byte) ((l >>> 40) & 0x00000000000000ff);
    arr[idx++] = (byte) ((l >>> 32) & 0x00000000000000ff);
    arr[idx++] = (byte) ((l >>> 24) & 0x00000000000000ff);
    arr[idx++] = (byte) ((l >>> 16) & 0x00000000000000ff);
    arr[idx++] = (byte) ((l >>> 8) & 0x00000000000000ff);
    arr[idx] = (byte) (l & 0x00000000000000ff);
  }

  /**
   * Convert an Integer to four bytes
   * <p>
   * @param number           the number to convert to a 4 byte integer
   * @param destination      the destination byte array to insert the new
   *                         4-bytes into
   * @param destinationIndex the index location where the number should be
   *                         copies
   */
  public static void intToBytes(int number, byte[] destination, int destinationIndex) {
    if (destination == null) {
      return;
    }
    int idx = destinationIndex;
    final int MASK = 0x000000ff;
    destination[idx++] = (byte) ((number >>> 24) & MASK);
    destination[idx++] = (byte) ((number >>> 16) & MASK);
    destination[idx++] = (byte) ((number >>> 8) & MASK);
    destination[idx] = (byte) (number & MASK);
  }

  /**
   * Reverse the order of bytes in an array. This is an attempt at big/little
   * endian conversion.
   * <p>
   * @param in the byte array to reverse
   * @return the reversed byte array (copy of)
   */
  public static byte[] reverse(byte[] in) {
    int len = in.length;
    byte[] out = new byte[len];
    for (int i = 0; i < len; i++) {
      out[len - i - 1] = in[i];
    }
    return out;
  }

  /**
   * Convert a byte stream to a printable string of bytes, optionally with
   * spaces between each byte value.
   * <p>
   * @param bytes  an array of bytes
   * @param spaces indicator that each byte should be separated by a space
   *               character
   * @return a (potentially very long) non-null String
   */
  public static String toString(byte[] bytes, boolean spaces) {
    StringBuilder sb = new StringBuilder();
    if (bytes != null) {
      for (byte b : bytes) {
        sb.append(Integer.toHexString(b & 0x000000FF)).append(spaces ? " " : "");
      }
    }
    return sb.toString();
  }

  /**
   * Format a byte array with a numbered header. This produces a multi-line
   * output useful for identifying each byte hex value and its corresponding
   * index. Example output:
   * <pre>
   * -----------------------------------------------------------------
   * |00 | 01 | 02 | 03 | 04 | 05 | 06 | 07 | 08 | 09 | 10 | 11 | 12 | - index number
   * +---+----+----+----+----+----+----+----+----+----+----+----+----+-
   * | 0 |  1 |  2 | 55 |  3 |  6 | ff |  5 |  4 |  6 |  9 |  8 |  7 | - byte hex value
   * -----------------------------------------------------------------
   * </pre>
   * <p>
   * @param bytes a byte array
   * @return a multi-line string showing each byte and its corresponding index
   *         value
   */
  public static String toStringFormatted(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    DecimalFormat df = new DecimalFormat("00 | ");
    if (bytes != null) {
      int len = bytes.length;
      for (int i = 0; i < bytes.length; i++) {
        sb.append("-----");
      }
      sb.append("\n|");
      for (int i = 0; i < bytes.length; i++) {
        sb.append(df.format(i));
//        sb.append(String.format("[%2d]", i));
      }
      sb.append("\n+");
      for (int i = 0; i < bytes.length; i++) {
        sb.append("---+-");
      }
      sb.append("\n|");
      for (byte b : bytes) {
        sb.append(String.format("%2s | ", Integer.toHexString(b & 0x000000FF)));
//        sb.append(String.format("[%02]", Integer.toHexString(b & 0x000000FF)));
      }
      sb.append("\n");
      for (int i = 0; i < bytes.length; i++) {
        sb.append("-----");
      }

    }
    return sb.toString();
  }

  /**
   * Format a byte with a numbered header. This produces a multi-line output
   * useful for identifying each bit value and its corresponding index. Example
   * output:
   * <pre>
   * +---------------+
   * |0 1 2 3 4 5 6 7|  -- bit address
   * +---------------+
   * |0 1 0 0 0 0 0 0|  -- bit value
   * +---------------+
   * </pre>
   * <p>
   * @param byteCode a byte
   * @return a multi-line string showing each bit and its corresponding index
   *         value
   */
  public static String toStringFormatted(byte byteCode) {
    StringBuilder sb = new StringBuilder("+---------------+\n|");
    for (int i = 0; i < 8; i++) {
      sb.append(i).append(i == 7 ? "" : " ");
    }
    sb.append("|\n+---------------+\n|");
    for (int i = 0; i < 8; i++) {
      sb.append(getBit(byteCode, i)).append(i == 7 ? "" : " ");
    }
    sb.append("|\n+---------------+\n|");
    return sb.toString();
  }

  /**
   * Format a byte into a string of 0/1 bits. e..g. "0 1 0 0 0 0 0 0"
   * <p>
   * @param byteCode a byte
   * @param spaces   whether to add spaces between the bits
   * @return a string showing each bit value as one or zero
   */
  public static String toString(byte byteCode, boolean spaces) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      sb.append(getBit(byteCode, i)).append(i == 7 ? "" : spaces ? " " : "");
    }
    return sb.toString();

  }

  /**
   * Format a byte array to a printable String with spaces. This is a shortcut
   * to {@link #toString(byte[], true)}
   * <p>
   * @param bytes an array of bytes
   * @return a (potentially very long) non-null String
   */
  public static String toString(byte[] bytes) {
    return toString(bytes, true);
  }

  /**
   * Print an integer as a Hex String.
   * <p>
   * @param number an integer number
   * @return the number as a Hex String.
   */
  public static String toString(int number) {
    return Integer.toHexString(number);
  }

  /**
   * Generate an array containing a random sequence of bytes. This is useful for
   * testing when a non-null, non-zero data payload is needed.
   * <p>
   * @param size the desired array size
   * @return a byte array
   */
  public static byte[] randomBytes(int size) {
    byte[] bytes = new byte[size];
    Random random = new Random();
    for (int i = 0; i < size; i++) {
      bytes[i] = (byte) random.nextInt();
    }
    return bytes;
  }

  /**
   * Copy a repeated pattern of bytes into a larger byte array the specified
   * number of times. This is useful for testing when a non-null, non-zero data
   * payload is needed.
   * <p>
   * The returned byte array is pattern.size * iterations long.
   * <p>
   * @param pattern    the desired byte pattern to repeat
   * @param iterations the number of iterations to repeat
   * @return a byte[] array that contains a repeating pattern iteration.
   */
  public static byte[] patternBytes(byte[] pattern, int iterations) {
    byte[] bytes = new byte[pattern.length * iterations];
    for (int i = 0; i < iterations; i++) {
      System.arraycopy(pattern, 0, bytes, pattern.length * i, pattern.length);
    }
    return bytes;
  }

  //----------------------------------------------------------------------------
  // Byte Conversion Utilities 
  // from: http://www.daniweb.com/code/snippet216874.html#
  //"primitive type --> byte[] data" Methods
  public static byte[] toBytes(byte data) {
    return new byte[]{data};
  }

  public static byte[] toBytes(byte[] data) {
    return data;
  }

  /**
   * Convert a SHORT into a two-byte array.
   * <p>
   * @param data a short number
   * @return a two-byte array
   */
  public static byte[] toBytes(short data) {
    return new byte[]{(byte) ((data >> 8) & 0xff), (byte) ((data) & 0xff),};
  }

  public static byte[] toBytes(short[] data) {
    if (data == null) {
      return null;
    }
    byte[] bytes = new byte[data.length * 2];
    for (int i = 0; i < data.length; i++) {
      System.arraycopy(toBytes(data[i]), 0, bytes, i * 2, 2);
    }
    return bytes;
  }

  public static byte[] toBytes(char data) {
    return new byte[]{(byte) ((data >> 8) & 0xff), (byte) (data & 0xff),};
  }

  public static byte[] toBytes(char[] data) {
    if (data == null) {
      return null;
    }
    byte[] bytes = new byte[data.length * 2];
    for (int i = 0; i < data.length; i++) {
      System.arraycopy(toBytes(data[i]), 0, bytes, i * 2, 2);
    }
    return bytes;
  }

  public static byte[] toBytes(int data) {
    return new byte[]{(byte) ((data >> 24) & 0xff),
                      (byte) ((data >> 16) & 0xff),
                      (byte) ((data >> 8) & 0xff),
                      (byte) ((data) & 0xff)};
  }

  public static byte[] toBytes(int[] data) {
    if (data == null) {
      return null;
    }
    byte[] bytes = new byte[data.length * 4];
    for (int i = 0; i < data.length; i++) {
      System.arraycopy(toBytes(data[i]), 0, bytes, i * 4, 4);
    }
    return bytes;
  }

  public static byte[] toBytes(long data) {
    return new byte[]{(byte) ((data >> 56) & 0xff),
                      (byte) ((data >> 48) & 0xff),
                      (byte) ((data >> 40) & 0xff),
                      (byte) ((data >> 32) & 0xff),
                      (byte) ((data >> 24) & 0xff),
                      (byte) ((data >> 16) & 0xff),
                      (byte) ((data >> 8) & 0xff),
                      (byte) ((data) & 0xff),};
  }

  public static byte[] toBytes(long[] data) {
    if (data == null) {
      return null;
    }
    byte[] bytes = new byte[data.length * 8];
    for (int i = 0; i < data.length; i++) {
      System.arraycopy(toBytes(data[i]), 0, bytes, i * 8, 8);
    }
    return bytes;
  }

  public static byte[] toBytes(float data) {
    return toBytes(Float.floatToRawIntBits(data));
  }

  public static byte[] toBytes(float[] data) {
    if (data == null) {
      return null;
    }
    byte[] bytes = new byte[data.length * 4];
    for (int i = 0; i < data.length; i++) {
      System.arraycopy(toBytes(data[i]), 0, bytes, i * 4, 4);
    }
    return bytes;
  }

  public static byte[] toBytes(Double data) {
    return toBytes(Double.doubleToRawLongBits(data));
  }

  public static byte[] toBytes(Double[] data) {
    if (data == null) {
      return null;
    }
    byte[] bytes = new byte[data.length * 8];
    for (int i = 0; i < data.length; i++) {
      System.arraycopy(toBytes(data[i]), 0, bytes, i * 8, 8);
    }
    return bytes;
  }

  public static byte[] toBytes(double data) {
    return toBytes(Double.doubleToRawLongBits(data));
  }

  public static byte[] toBytes(double[] data) {
    if (data == null) {
      return null;
    }
    byte[] bytes = new byte[data.length * 8];
    for (int i = 0; i < data.length; i++) {
      System.arraycopy(toBytes(data[i]), 0, bytes, i * 8, 8);
    }
    return bytes;
  }

  public static byte[] toBytes(boolean data) {
    return new byte[]{(byte) (data ? 0x01 : 0x00)}; // bool -> {1 byte}
  }

  public static byte[] toBytes(boolean[] data) {
    /**
     * Advanced Technique: The byte array contains information about how many
     * boolean values are involved, so the exact array is returned when later
     * decoded.
     */
    if (data == null) {
      return null;
    }
    int len = data.length;
    byte[] lena = toBytes(len); // int conversion; length array = lena
    byte[] bytes = new byte[lena.length + (len / 8) + (len % 8 != 0 ? 1 : 0)];
    // (Above) length-array-length + sets-of-8-booleans +? byte-for-remainder
    System.arraycopy(lena, 0, bytes, 0, lena.length);
    // (Below) algorithm by Matthew Cudmore: boolean[] -> bits -> byte[]
    for (int i = 0, j = lena.length, k = 7; i < data.length; i++) {
      bytes[j] |= (data[i] ? 1 : 0) << k--;
      if (k < 0) {
        j++;
        k = 7;
      }
    }
    return bytes;
  }

  public static byte[] toBytes(String data) {
    return (data == null) ? null : data.getBytes();
  }

  public static byte[] toBytes(String[] data) {
    /**
     * Advanced Technique: Generates an indexed byte array which contains the
     * array of Strings. The byte array contains information about the number of
     * Strings and the length of each String.
     */
    if (data == null) {
      return null;
    }
    // ---------- flags:
    int totalLength = 0; // Measure length of final byte array
    int bytesPos = 0; // Used later
    // ----- arrays:
    byte[] dLen = toBytes(data.length); // byte array of data length
    totalLength += dLen.length;
    int[] sLens = new int[data.length]; // String lengths = sLens
    totalLength += (sLens.length * 4);
    byte[][] strs = new byte[data.length][]; // array of String bytes
    // ----- pack strs:
    for (int i = 0; i < data.length; i++) {
      if (data[i] != null) {
        strs[i] = toBytes(data[i]);
        sLens[i] = strs[i].length;
        totalLength += strs[i].length;
      } else {
        sLens[i] = 0;
        strs[i] = new byte[0]; // prevent null entries
      }
    }
    byte[] bytes = new byte[totalLength]; // final array
    System.arraycopy(dLen, 0, bytes, 0, 4);
    byte[] bsLens = toBytes(sLens); // byte version of String sLens
    System.arraycopy(bsLens, 0, bytes, 4, bsLens.length);
    // -----
    bytesPos += 4 + bsLens.length; // mark position
    // -----
    for (byte[] sba : strs) {
      System.arraycopy(sba, 0, bytes, bytesPos, sba.length);
      bytesPos += sba.length;
    }
    return bytes;
  }

  //"byte[] data --> primitive type" Methods
  public static byte toByte(byte[] data) {
    return ((data == null) || (data.length == 0)) ? 0x0 : data[0];
  }

  public static short toShort(byte[] data) {
    if ((data == null) || (data.length != 2)) {
      return 0x0;
    }
    return (short) ((0xff & data[0]) << 8 | (0xff & data[1]));
  }

  public static short[] toShortArray(byte[] data) {
    if ((data == null) || (data.length % 2 != 0)) {
      return null;
    }
    short[] shts = new short[data.length / 2];
    for (int i = 0; i < shts.length; i++) {
      shts[i] = toShort(new byte[]{data[(i * 2)], data[(i * 2) + 1]});
    }
    return shts;
  }

  public static char toChar(byte[] data) {
    if ((data == null) || (data.length != 2)) {
      return 0x0;
    }
    return (char) ((0xff & data[0]) << 8 | (0xff & data[1]));
  }

  public static char[] toCharArray(byte[] data) {
    if ((data == null) || (data.length % 2 != 0)) {
      return null;
    }
    char[] chrs = new char[data.length / 2];
    for (int i = 0; i < chrs.length; i++) {
      chrs[i] = toChar(new byte[]{data[(i * 2)], data[(i * 2) + 1],});
    }
    return chrs;
  }

  public static int toInt(byte[] data) {
    if ((data == null) || (data.length != 4)) {
      return 0x0;
    }
    return ( // NOTE: type cast not necessary for int
      (0xff & data[0]) << 24 | (0xff & data[1]) << 16 | (0xff & data[2]) << 8 | (0xff & data[3]));
  }

  public static int[] toIntArray(byte[] data) {
    if ((data == null) || (data.length % 4 != 0)) {
      return null;
    }
    int[] ints = new int[data.length / 4];
    for (int i = 0; i < ints.length; i++) {
      ints[i] = toInt(new byte[]{data[(i * 4)], data[(i * 4) + 1], data[(i * 4) + 2], data[(i * 4) + 3],});
    }
    return ints;
  }

  public static long toLong(byte[] data) {
    if ((data == null) || (data.length != 8)) {
      return 0x0;
    }
    return ( /**
       * (Below) convert to longs before shift because digits are lost with ints
       * beyond the 32-bit limit
       */
      (long) (0xff & data[0]) << 56 | (long) (0xff & data[1]) << 48 | (long) (0xff & data[2]) << 40 | (long) (0xff & data[3]) << 32
      | (long) (0xff & data[4]) << 24 | (long) (0xff & data[5]) << 16 | (long) (0xff & data[6]) << 8 | (long) (0xff & data[7]));
  }

  public static long[] toLongArray(byte[] data) {
    if ((data == null) || (data.length % 8 != 0)) {
      return null;
    }
    long[] lngs = new long[data.length / 8];
    for (int i = 0; i < lngs.length; i++) {
      lngs[i] = toLong(new byte[]{data[(i * 8)], data[(i * 8) + 1], data[(i * 8) + 2], data[(i * 8) + 3], data[(i * 8) + 4], data[(i * 8) + 5],
                                  data[(i * 8) + 6], data[(i * 8) + 7],});
    }
    return lngs;
  }

  public static float toFloat(byte[] data) {
    if ((data == null) || (data.length != 4)) {
      return 0x0;
    }
    return Float.intBitsToFloat(toInt(data));
  }

  public static float[] toFloatArray(byte[] data) {
    if ((data == null) || (data.length % 4 != 0)) {
      return null;
    }
    float[] flts = new float[data.length / 4];
    for (int i = 0; i < flts.length; i++) {
      flts[i] = toFloat(new byte[]{data[(i * 4)], data[(i * 4) + 1], data[(i * 4) + 2], data[(i * 4) + 3],});
    }
    return flts;
  }

  public static double toDouble(byte[] data) {
    if ((data == null) || (data.length != 8)) {
      return 0x0;
    }
    return Double.longBitsToDouble(toLong(data));
  }

  public static double[] toDoubleArray(byte[] data) {
    if (data == null) {
      return null;
    }
    if (data.length % 8 != 0) {
      return null;
    }
    double[] dbls = new double[data.length / 8];
    for (int i = 0; i < dbls.length; i++) {
      dbls[i] = toDouble(new byte[]{data[(i * 8)], data[(i * 8) + 1], data[(i * 8) + 2], data[(i * 8) + 3], data[(i * 8) + 4], data[(i * 8) + 5],
                                    data[(i * 8) + 6], data[(i * 8) + 7],});
    }
    return dbls;
  }

  public static boolean toBoolean(byte[] data) {
    return ((data == null) || (data.length == 0)) ? false : data[0] != 0x00;
  }

  public static boolean[] toBooleanArray(byte[] data) {
    /**
     * Advanced Technique: Extract the boolean array's length from the first
     * four bytes in the char array, and then read the boolean array.
     */
    if ((data == null) || (data.length < 4)) {
      return null;
    }
    int len = toInt(new byte[]{data[0], data[1], data[2], data[3]});
    boolean[] bools = new boolean[len];
    /**
     * pack booleans.
     */
    for (int i = 0, j = 4, k = 7; i < bools.length; i++) {
      bools[i] = ((data[j] >> k--) & 0x01) == 1;
      if (k < 0) {
        j++;
        k = 7;
      }
    }
    return bools;
  }

  /**
   * Extract the String array length from the first four bytes in the char
   * array, and then read the int array denoting the String lengths, and then
   * read the Strings.
   * <p>
   * @param data
   * @return
   */
  public static String[] toStringArray(byte[] data) {
    if ((data == null) || (data.length < 4)) {
      return null;
    }
    byte[] bBuff = new byte[4];
    System.arraycopy(data, 0, bBuff, 0, 4);
    int saLen = toInt(bBuff);
    if (data.length < (4 + (saLen * 4))) {
      return null;
    }

    bBuff = new byte[saLen * 4];
    System.arraycopy(data, 4, bBuff, 0, bBuff.length);
    int[] sLens = toIntArray(bBuff);
    if (sLens == null) {
      return null;
    }

    String[] strings = new String[saLen];
    for (int i = 0, dataPos = 4 + (saLen * 4); i < saLen; i++) {
      if (sLens[i] > 0) {
        if (data.length >= (dataPos + sLens[i])) {
          bBuff = new byte[sLens[i]];
          System.arraycopy(data, dataPos, bBuff, 0, sLens[i]);
          dataPos += sLens[i];
          strings[i] = toString(bBuff);
        } else {
          return null;
        }
      }
    }

    return strings;
  }

  /**
   * Decodes a binary-coded decimal into a string and returns it.
   * <p>
   * @param bcd The binary-coded decimal to decode.
   * @return The decoded binary-coded decimal.
   */
  public static String decodeBCD(final short bcd) {
    return String.format("%x.%02x", (bcd & 0xFF00) >> 8, bcd & 0x00FF);
  }
}
