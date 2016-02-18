/*
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
package javax.usb3.enumerated;

import javax.usb3.request.BMRequestType;

/**
 * An enumerated list of standard Feature Selectors available when using the
 * CLEAR_FEATURE, SET_FEATURE standard device requests.
 * <p>
 * Feature selectors are used when enabling or setting features, such as remote
 * wakeup, specific to a device, interface, or endpoint.
 * <p>
 * If an unsupported or invalid request is made to a USB device, the device
 * responds by returning STALL in the Data or Status stage of the request. If
 * the device detects the error in the Setup stage, it is preferred that the
 * device returns STALL at the earlier of the Data or Status stage. Receipt of
 * an unsupported or invalid request does NOT cause the optional Halt feature on
 * the control pipe to be set. If for any reason, the device becomes unable to
 * communicate via its Default Control Pipe due to an error condition, the
 * device must be reset to clear the condition and restart the Default Control
 * Pipe.
 *
 * @author Jesse Caulfield
 */
public enum EFeatureSelector {

  DEVICE_REMOTE_WAKEUP((byte) 0x01, BMRequestType.ERecipient.DEVICE),
  ENDPOINT_HALT((byte) 0x00, BMRequestType.ERecipient.ENDPOINT),
  TEST_MODE((byte) 0x02, BMRequestType.ERecipient.DEVICE);
  private final byte byteCode;
  private final BMRequestType.ERecipient recipient;

  private EFeatureSelector(byte byteCode, BMRequestType.ERecipient recipient) {
    this.byteCode = byteCode;
    this.recipient = recipient;
  }

  public byte asByte() {
    return byteCode;
  }

  /**
   * The type of device for which this feature is valid.
   *
   * @return the valid recipient type
   */
  public BMRequestType.ERecipient getRecipient() {
    return recipient;
  }

}
