/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See readme.md for licensing information.
 */
package javax.usb;

/**
 * USB Services ServicesInstanceConfigurationuration.
 * <p>
 * This is a container class for the Properties file.
 *
 * @author Klaus Reimer (k@ailis.de)
 * @author Key Bridge LLC (remove timeout, scanInterval properties - use
 * defaults)
 */
public final class UsbServiceInstanceConfiguration {

  /**
   * 5000 ms.
   * <p>
   * The default USB communication timeout in milliseconds.
   */
  public static final int TIMEOUT = 5000;

  /**
   * 500 ms.
   * <p>
   * The default scan interval in milliseconds.
   */
  public static final int SCAN_INTERVAL = 500;

}
