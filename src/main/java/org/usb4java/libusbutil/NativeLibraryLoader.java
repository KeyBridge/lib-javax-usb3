/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.libusbutil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class to load native libraries from classpath.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @author Jesse Caulfield
 */
public final class NativeLibraryLoader {

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
   * Private constructor to prevent instantiation.
   */
  private NativeLibraryLoader() {
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
    throw new RuntimeException("Unable to determine the shared library "
                               + "file extension for operating system '" + os
                               + "'. Please specify Java parameter -D" + key
                               + "=<FILE-EXTENSION>");
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
    Path destination = Paths.get(System.getProperty("java.io.tmpdir"), "javax-usb", getOSName(), getOSArch(), getLibraryFilename());
    if (destination.toFile().exists() && destination.toFile().length() > 0) {
      return;
    }
    /**
     * If the destination file does not exist then extract the library from the
     * jar file.
     */
    try {
      Files.createDirectories(destination.getParent());
      URL url = NativeLibraryLoader.class.getClassLoader().getResource("META-INF/nativelib/" + getOSName() + "/" + getOSArch() + "/" + getLibraryFilename());
      if (url == null) {
        System.out.println("ERROR: native lib not found ");
        return;
      }
      Path source = Paths.get(url.toURI());
      Logger.getLogger(NativeLibraryLoader.class.getName()).log(Level.FINE, "Copy USB native library to {0}", destination);
      /**
       * Mark the file to be deleted upon exit to leave no trace.
       */
      Files.copy(source, destination);
      destination.toFile().deleteOnExit();
      /**
       * Load the native library.
       */
      System.load(destination.toString());
    } catch (URISyntaxException | IOException e) {
      Logger.getLogger(NativeLibraryLoader.class.getName()).log(Level.SEVERE, null, e);
    }
  }

}
