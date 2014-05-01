/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.libusbutil;

import java.io.*;
import java.util.Locale;

/**
 * Utility class to load native libraries from classpath.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 */
public final class Loader {

  /**
   * Buffer size used for copying data.
   */
  private static final int BUFFER_SIZE = 8192;

  /**
   * Constant for OS X operating system.
   */
  private static final String OS_OSX = "osx";

  /**
   * Constant for OS X operating system.
   */
  private static final String OS_MACOSX = "macosx";

  /**
   * Constant for Linux operating system.
   */
  private static final String OS_LINUX = "linux";

  /**
   * Constant for Windows operating system.
   */
  private static final String OS_WINDOWS = "windows";

  /**
   * Constant for FreeBSD operating system.
   */
  private static final String OS_FREEBSD = "freebsd";

  /**
   * Constant for SunOS operating system.
   */
  private static final String OS_SUNOS = "sunos";

  /**
   * Constant for i386 architecture.
   */
  private static final String ARCH_I386 = "i386";

  /**
   * Constant for x86 architecture.
   */
  private static final String ARCH_X86 = "x86";

  /**
   * Constant for x86_64 architecture.
   */
  private static final String ARCH_X86_64 = "x86_64";

  /**
   * Constant for amd64 architecture.
   */
  private static final String ARCH_AMD64 = "amd64";

  /**
   * Constant for so file extension.
   */
  private static final String EXT_SO = "so";

  /**
   * Constant for dll file extension.
   */
  private static final String EXT_DLL = "dll";

  /**
   * Constant for dylib file extension.
   */
  private static final String EXT_DYLIB = "dylib";

  /**
   * The temporary directory for native libraries.
   */
  private static File tmp;

  /**
   * If library is already loaded.
   */
  private static boolean loaded = false;

  /**
   * Private constructor to prevent instantiation.
   */
  private Loader() {
    // Nothing to do here
  }

  /**
   * Returns the operating system name. This could be "linux", "windows" or
   * "osx" or (for any other non-supported platform) the value of the "os.name"
   * property converted to lower case and with removed space characters.
   * <p>
   * @return The operating system name.
   */
  private static String getOSName() {
    final String os = System.getProperty("os.name").toLowerCase(Locale.US).replace(" ", "");
    if (os.contains(OS_WINDOWS)) {
      return OS_WINDOWS;
    }
    if (os.equals(OS_MACOSX)) {
      return OS_OSX;
    }
    return os;
  }

  /**
   * Returns the CPU architecture. This will be "x86" or "x86_64" (Platform
   * names i386 und amd64 are converted accordingly) or (when platform is
   * unsupported) the value of os.arch converted to lower-case and with removed
   * space characters.
   * <p>
   * @return The CPU architecture
   */
  private static String getOSArch() {
    final String arch = System.getProperty("os.arch").toLowerCase(Locale.US).replace(" ", "");
    if (arch.equals(ARCH_I386)) {
      return ARCH_X86;
    }
    if (arch.equals(ARCH_AMD64)) {
      return ARCH_X86_64;
    }
    return arch;
  }

  /**
   * Returns the shared library extension name.
   * <p>
   * @return The shared library extension name.
   */
  private static String getLibrarySuffix() {
    final String os = getOSName();
    final String key = "usb4java.libext." + getOSName();
    final String ext = System.getProperty(key);
    if (ext != null) {
      return ext;
    }
    if (os.equals(OS_LINUX) || os.equals(OS_FREEBSD) || os.equals(OS_SUNOS)) {
      return EXT_SO;
    }
    if (os.equals(OS_WINDOWS)) {
      return EXT_DLL;
    }
    if (os.equals(OS_OSX)) {
      return EXT_DYLIB;
    }
    throw new LoaderException("Unable to determine the shared library "
      + "file extension for operating system '" + os
      + "'. Please specify Java parameter -D" + key
      + "=<FILE-EXTENSION>");
  }

  /**
   * Creates the temporary directory used for unpacking the native libraries.
   * This directory is marked for deletion on exit.
   * <p>
   * @return The temporary directory for native libraries.
   */
  private static File createTempDirectory() {
    // Return cached tmp directory when already created
    if (tmp != null) {
      return tmp;
    }

    try {
      tmp = File.createTempFile("usb4java", null);
      if (!tmp.delete()) {
        throw new IOException("Unable to delete temporary file " + tmp);
      }
      if (!tmp.mkdirs()) {
        throw new IOException("Unable to create temporary directory " + tmp);
      }
      tmp.deleteOnExit();
      return tmp;
    } catch (final IOException e) {
      throw new LoaderException("Unable to create temporary directory for usb4java natives: " + e, e);
    }
  }

  /**
   * Returns the platform name. This could be for example "linux-x86" or
   * "windows-x86_64".
   * <p>
   * @return The architecture name. Never null.
   */
//  private static String getPlatform() {    return getOSName() + "-" + getOSArch();  }
  /**
   * Returns the name of the JNA usb4java native library. This could be
   * "libusb4java.dll" for example.
   * <p>
   * @return The usb4java native library name. Never null.
   */
  private static String getLibraryFilename() {
    return "libusb4java." + getLibrarySuffix();
  }

  /**
   * Returns the name of the libusb native library. This could be "libusb0.dll"
   * for example or null if this library is not needed on the current platform
   * (Because it is provided by the operating system).
   * <p>
   * @return The libusb native library name or null if not needed.
   */
  private static String getLibraryNativeFileName() {
    final String os = getOSName();
    if (os.equals(OS_WINDOWS)) {
      return "libusb-1.0." + EXT_DLL;
    }
    return null;
  }

  /**
   * Copies the specified input stream to the specified output file.
   * <p>
   * @param input  The input stream.
   * @param output The output file.
   * @throws IOException If copying failed.
   */
//  private static void copy(final InputStream input, final File output)
//    throws IOException {
//    final byte[] buffer = new byte[BUFFER_SIZE];
//    final FileOutputStream stream = new FileOutputStream(output);
//    try {
//      int read;
//      while ((read = input.read(buffer)) != -1) {
//        stream.write(buffer, 0, read);
//      }
//    } finally {
//      stream.close();
//    }
//  }
  /**
   * Extracts a single library.
   * <p>
   * @param platform The platform name (For example "linux-x86")
   * @param lib      The library name to extract (For example "libusb0.dll")
   * @return The absolute path to the extracted library.
   */
//  private static String extractLibrary(final String platform, final String lib) {
//    // Extract the usb4java library
//    final String source = '/' + Loader.class.getPackage().getName().replace('.', '/') + '/' + platform + "/" + lib;
//
//    // Check if native library is present
//    final URL url = Loader.class.getResource(source);
//    if (url == null) {
//      throw new LoaderException("Native library not found in classpath: " + source);
//    }
//
//    // If native library was found in an already extracted form then
//    // return this one without extracting it
//    if ("file".equals(url.getProtocol())) {
//      try {
//        return new File(url.toURI()).getAbsolutePath();
//      } catch (final URISyntaxException e) {
//        // Can't happen because we are not constructing the URI
//        // manually. But even when it happens then we fall back to
//        // extracting the library.
//        throw new LoaderException(e.toString(), e);
//      }
//    }
//
//    // Extract the library and return the path to the extracted file.
//    final File dest = new File(createTempDirectory(), lib);
//    try {
//      final InputStream stream = Loader.class.getResourceAsStream(source);
//      if (stream == null) {
//        throw new LoaderException("Unable to find " + source
//          + " in the classpath");
//      }
//      try {
//        copy(stream, dest);
//      } finally {
//        stream.close();
//      }
//    } catch (final IOException e) {
//      throw new LoaderException("Unable to extract native library " + source + " to " + dest + ": " + e, e);
//    }
//
//    // Mark usb4java library for deletion
//    dest.deleteOnExit();
//
//    return dest.getAbsolutePath();
//  }
  /**
   * Loads the libusb native wrapper library. Can be safely called multiple
   * times. Duplicate calls are ignored. This method is automatically called
   * when the {@link LibUsb} class is loaded. When you need to do it earlier (To
   * catch exceptions for example) then simply call this method manually.
   * <p>
   * @throws LoaderException When loading the native wrapper libraries failed.
   */
//  public static synchronized void load() {
//    // Do nothing if already loaded (or still loading)
//    if (loaded) {
//      return;
//    }
//
//    final String platform = getPlatform();
//    final String lib = getLibraryFilename();
//    final String extraLib = getLibraryNativeFileName();
//    if (extraLib != null) {
//      System.load(extractLibrary(platform, extraLib));
//    }
//    System.load(extractLibrary(platform, lib));
//    loaded = true;
//  }
  /**
   * Load a native library with version support.
   * <p>
   * For example: <code> loadLibrary("librxtxSerial.so, "0.4.0") </code> will
   * attempt to load the native library named "librxtxSerial.so" and copy it to
   * the system with the name "librxtxSerial.so.0.4.0".
   * <p>
   * The library must be located in the appropriate operating system
   * subdirectory within the source tree under 'src/resources/'. e.g.
   * 'src/resources/i386'
   */
  public static void load() {
    if (loaded) {
      return;
    }
    File destinationFile = new File(System.getProperty("java.io.tmpdir") + File.separator + getLibraryFilename());
    /**
     * If the destination file does not exist then extract the library from the
     * jar file.
     */
    if (!destinationFile.exists()) {
      String source = "/resources/" + getOSName() + "/" + getOSArch() + "/" + getLibraryFilename();
      System.out.println("DEBUG EmbeddedLibraryLoader.loadLibrary source      " + source);
      System.out.println("DEBUG EmbeddedLibraryLoader.loadLibrary destination " + destinationFile);
      System.out.println("DEBUG EmbeddedLibraryLoader.loadLibrary dest file does not exist - copying");
      copyResourceToFile(source, destinationFile);
      /**
       * Mark the file to be deleted upon exit to leave no trace.
       */
//      destinationFile.deleteOnExit();
    }
    /**
     * Load the native library.
     */
    System.load(destinationFile.getAbsolutePath());
    loaded = true;
  }

  /**
   * Copies a resource embedded in this JAR file to the file system
   * <p>
   * @param source @param destination @return the copied file
   */
  @SuppressWarnings("NestedAssignment")
  private static void copyResourceToFile(String source, File destinationFile) {
    try (OutputStream out = new FileOutputStream(destinationFile); InputStream in = new BufferedInputStream(Loader.class.getResourceAsStream(source))) {
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
    } catch (FileNotFoundException ex) {
      System.err.println(ex.getMessage() + " in the specified directory.");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

}
