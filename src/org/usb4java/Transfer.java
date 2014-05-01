/*
 * Copyright 2013 Klaus Reimer <k@ailis.de>
 * Copyright 2013 Luca Longinotti <l@longi.li>
 * See LICENSE.md for licensing information.
 *
 * Based on libusb <http://libusb.info/>:
 *
 * Copyright 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright 2010-2012 Peter Stuge <peter@stuge.se>
 * Copyright 2008-2013 Nathan Hjelm <hjelmn@users.sourceforge.net>
 * Copyright 2009-2013 Pete Batard <pete@akeo.ie>
 * Copyright 2009-2013 Ludovic Rousseau <ludovic.rousseau@gmail.com>
 * Copyright 2010-2012 Michael Plante <michael.plante@gmail.com>
 * Copyright 2011-2013 Hans de Goede <hdegoede@redhat.com>
 * Copyright 2012-2013 Martin Pieuchot <mpi@openbsd.org>
 * Copyright 2012-2013 Toby Gray <toby.gray@realvnc.com>
 */
package org.usb4java;

import java.nio.ByteBuffer;
import org.usb4java.libusbutil.ITransferCallback;

/**
 * The generic USB transfer structure.
 * <p>
 * The user populates this structure and then submits it in order to request a
 * transfer. After the transfer has completed, the library populates the
 * transfer with the results and passes it back to the user.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
  * @author Jesse Caulfield <jesse@caulfield.org>
 */
public final class Transfer {
  // Maps to JNI native class

  /**
   * The native pointer to the transfer structure.
   */
  private long transferPointer;

  /**
   * Keeping a reference to the buffer has multiple benefits: faster get(), GC
   * prevention (while Transfer is alive) and you can check the buffer's
   * original capacity (needed to check setLength() properly).
   */
  private ByteBuffer transferBuffer;

  /**
   * Package-private constructor to prevent manual instantiation. Transfers are
   * always created by JNI with allocTransfer().
   */
  public Transfer() {
    // Empty
  }

  /**
   * Returns the native pointer.
   * <p>
   * @return The native pointer.
   */
  public long getPointer() {
    return this.transferPointer;
  }

  /**
   * Returns the handle of the device that this transfer will be submitted to.
   * <p>
   * @return The handle of the device.
   */
  public native DeviceHandle devHandle();

  /**
   * Sets the handle of the device that this transfer will be submitted to.
   * <p>
   * @param handle The handle of the device.
   */
  public native void setDevHandle(final DeviceHandle handle);

  /**
   * Returns the bitwise OR combination of libusb transfer flags.
   * <p>
   * @return The transfer flags.
   */
  public native byte flags();

  /**
   * Sets the bitwise OR combination of libusb transfer flags.
   * <p>
   * @param flags The transfer flags to set.
   */
  public native void setFlags(final byte flags);

  /**
   * Returns the address of the endpoint where this transfer will be sent.
   * <p>
   * @return The endpoint address.
   */
  public native byte endpoint();

  /**
   * Sets the address of the endpoint where this transfer will be sent.
   * <p>
   * @param endpoint The endpoint address to set
   */
  public native void setEndpoint(final byte endpoint);

  /**
   * Returns the type of the endpoint.
   * <p>
   * @return The endpoint type.
   */
  public native byte type();

  /**
   * Sets the type of the endpoint.
   * <p>
   * @param type The endpoint type to set.
   */
  public native void setType(final byte type);

  /**
   * Returns the timeout for this transfer in milliseconds. A value of 0
   * indicates no timeout.
   * <p>
   * @return The timeout.
   */
  public native long timeout();

  /**
   * Sets the timeout for this transfer in milliseconds. A value of 0 indicates
   * no timeout.
   * <p>
   * @param timeout The timeout to set.
   */
  public native void setTimeout(final long timeout);

  /**
   * Returns the status of the transfer. Read-only, and only for use within
   * transfer callback function.
   * <p>
   * If this is an isochronous transfer, this field may read
   * {@link LibUsb#TRANSFER_COMPLETED} even if there were errors in the frames.
   * Use the status field in each packet to determine if errors occurred.
   * <p>
   * @return The transfer status.
   */
  public native int status();

  /**
   * Returns the length of the data buffer.
   * <p>
   * @return The data buffer length.
   */
  public native int length();

  /**
   * Sets the length of the data buffer.
   * <p>
   * This is checked against the maximum capacity of the supplied ByteBuffer.
   * <p>
   * @param length The data buffer length to set.
   */
  public void setLength(final int length) {
    // Verify that the new length doesn't exceed the current buffer's
    // maximum capacity.
    if (length != 0) {
      if (this.transferBuffer == null) {
        throw new IllegalArgumentException(
          "buffer is null, only a length of 0 is allowed");
      }

      if (this.transferBuffer.capacity() < length) {
        throw new IllegalArgumentException(
          "buffer too small for requested length");
      }
    }

    // Native call.
    this.setLengthNative(length);
  }

  /**
   * Native method called internally to set the length of the data buffer.
   * <p>
   * @param length The length to set.
   */
  native void setLengthNative(final int length);

  /**
   * Returns the actual length of data that was transferred. Read-only, and only
   * for use within transfer callback function. Not valid for isochronous
   * endpoint transfers.
   * <p>
   * @return The actual length of the transferred data.
   */
  public native int actualLength();

  /**
   * Returns the current callback object.
   * <p>
   * @return The current callback object.
   */
  public native ITransferCallback callback();

  /**
   * Sets the callback object.
   * <p>
   * This will be invoked when the transfer completes, fails, or is cancelled.
   * <p>
   * @param callback The callback object to use.
   */
  public native void setCallback(final ITransferCallback callback);

  /**
   * Returns the current user data object.
   * <p>
   * @return The current user data object.
   */
  public native Object userData();

  /**
   * Sets the user data object, representing user context data to pass to the
   * callback function and that can be accessed from there.
   * <p>
   * @param userData The user data object to set.
   */
  public native void setUserData(final Object userData);

  /**
   * Returns the data buffer.
   * <p>
   * @return The data buffer.
   */
  public ByteBuffer buffer() {
    return this.transferBuffer;
  }

  /**
   * Sets the data buffer.
   * <p>
   * @param buffer The data buffer to set.
   */
  public void setBuffer(final ByteBuffer buffer) {
    // Native call.
    this.setBufferNative(buffer);

    if (buffer != null) {
      // Set new length based on buffer's capacity.
      this.setLengthNative(buffer.capacity());
    } else {
      this.setLengthNative(0);
    }

    // Once we know the native calls have gone through, update the
    // reference.
    this.transferBuffer = buffer;
  }

  /**
   * Native method called internally to set the data buffer.
   * <p>
   * @param buffer The data buffer to set.
   */
  native void setBufferNative(final ByteBuffer buffer);

  /**
   * Returns the number of isochronous packets. Only used for I/O with
   * isochronous endpoints.
   * <p>
   * @return The number of isochronous packets.
   */
  public native int numIsoPackets();

  /**
   * Sets the number of isochronous packets.
   * <p>
   * @param numIsoPackets The number of isochronous packets to set.
   */
  public native void setNumIsoPackets(final int numIsoPackets);

  /**
   * Array of isochronous packet descriptors, for isochronous transfers only.
   * <p>
   * @return The array of isochronous packet descriptors.
   */
  public native IsoPacketDescriptor[] isoPacketDesc();

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result)
      + (int) (this.transferPointer ^ (this.transferPointer >>> 32));
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final Transfer other = (Transfer) obj;
    return this.transferPointer == other.transferPointer;
  }

  @Override
  public String toString() {
    return String.format("libusb transfer 0x%x", this.transferPointer);
  }
}
