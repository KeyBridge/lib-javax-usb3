/*
 * Copyright 2013 Luca Longinotti <l@longi.li>
 * See readme.md for licensing information.
 */
package javax.usb.utility;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;

/**
 * Buffer utility and convenience methods.
 * <p>
 * Provides some utility methods for working with {@link IntBuffer} and
 * {@link ByteBuffer} and for extracting subsets thereof.
 *
 * @author Luca Longinotti (l@longi.li)
 * @author Jesse Caulfield
 */
public final class BufferUtil {

  /**
   * The native size of the type {@code int}.
   */
  private static final int INT_SIZE = Integer.SIZE / Byte.SIZE;

  /**
   * The native size of the type {@code long}.
   */
  private static final int LONG_SIZE = Long.SIZE / Byte.SIZE;

  /**
   * Private constructor to prevent instantiation.
   */
  private BufferUtil() {
    // Empty
  }

  /**
   * Allocates a new direct {@link ByteBuffer} with the specified size and
   * returns it.
   *
   * @param bytes The size of the new byte buffer.
   * @return The allocated direct byte buffer.
   */
  public static ByteBuffer allocateByteBuffer(final int bytes) {
    return ByteBuffer.allocateDirect(bytes);
  }

  /**
   * Allocates a new {@link IntBuffer} with space for exactly one integer value.
   *
   * @return The allocated int buffer.
   */
  public static IntBuffer allocateIntBuffer() {
    return ByteBuffer.allocateDirect(INT_SIZE).asIntBuffer();
  }

  /**
   * Allocates a new {@link LongBuffer} with space for exactly one long value.
   *
   * @return The allocated long buffer.
   */
  public static LongBuffer allocateLongBuffer() {
    return ByteBuffer.allocateDirect(LONG_SIZE).asLongBuffer();
  }

  /**
   * Slices a part of the specified {@link ByteBuffer} into a new byte buffer
   * and returns it.
   *
   * @param buffer The byte buffer to slice data from.
   * @param offset The offset of the part to slice.
   * @param length The length of the part to slice.
   * @return The new byte buffer with the sliced part.
   */
  public static ByteBuffer slice(final ByteBuffer buffer, final int offset, final int length) {
    final int oldPosition = buffer.position();
    final int oldLimit = buffer.limit();

    buffer.position(offset);
    buffer.limit(offset + length);

    final ByteBuffer slice = buffer.slice();

    buffer.position(oldPosition);
    buffer.limit(oldLimit);

    return slice;
  }
}
