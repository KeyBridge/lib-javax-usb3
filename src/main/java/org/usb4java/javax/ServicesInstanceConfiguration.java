/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

/**
 * USB Services ServicesInstanceConfigurationuration.
 * <p>
 * This is a container class for the Properties file.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @author Key Bridge LLC (remove timeout, scanInterval properties - use
 * defaults)
 */
public final class ServicesInstanceConfiguration {

  /**
   * Base key name for properties.
   */
  private static final String KEY_BASE = "org.usb4java.javax.";

  /**
   * 5000 ms. The default USB communication timeout in milliseconds. To set this
   * value add a "org.usb4java.javax.timeout" entry to the
   * "javax.usb.properties" file.
   */
  private static final int DEFAULT_TIMEOUT = 5000;

  /**
   * 500 ms. The default scan interval in milliseconds. To set this value add a
   * "org.usb4java.javax.scanInterval" entry to the "javax.usb.properties" file.
   */
  private static final int DEFAULT_SCAN_INTERVAL = 500;

  /**
   * Key name for USB communication timeout.
   */
  private static final String TIMEOUT_KEY = KEY_BASE + "timeout";

  /**
   * Key name for USB communication timeout.
   */
  private static final String SCAN_INTERVAL_KEY = KEY_BASE + "scanInterval";

  /**
   * The timeout for USB communication in milliseconds.
   */
  private int timeout = DEFAULT_TIMEOUT;

  /**
   * The scan interval in milliseconds.
   */
  private int scanInterval = DEFAULT_SCAN_INTERVAL;

  /**
   * Constructs new configuration from the specified properties.
   */
  public ServicesInstanceConfiguration() {
  }

  /**
   * Returns the USB communication timeout in milliseconds.
   * <p>
   * @return The USB communication timeout in milliseconds.
   */
  public int getTimeout() {
    return this.timeout;
  }

  /**
   * Returns the scan interval in milliseconds.
   * <p>
   * @return The scan interval in milliseconds.
   */
  public int getScanInterval() {
    return this.scanInterval;
  }
}
