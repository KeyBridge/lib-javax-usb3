package javax.usb.event;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation. All
 * Rights Reserved.
 * <p>
 * This software is provided and licensed under the terms and conditions of the
 * Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */
import java.util.EventListener;

/**
 * Interface for receiving UsbServicesEvents.
 * <p>
 * @author E. Michael Maximilien
 */
public interface UsbServicesListener extends EventListener {

  /**
   * A UsbDevice was attached.
   * <p>
   * @param event The UsbServicesEvent.
   */
  public void usbDeviceAttached(UsbServicesEvent event);

  /**
   * A UsbDevice was detached.
   * <p>
   * @param event The UsbServicesEvent.
   */
  public void usbDeviceDetached(UsbServicesEvent event);
}
