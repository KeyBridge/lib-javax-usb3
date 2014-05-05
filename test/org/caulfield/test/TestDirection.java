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
package org.caulfield.test;

import javax.usb.ri.enumerated.EEndpointDirection;
import javax.usb.ri.request.BMRequestType;
import javax.usb.util.ByteUtil;

/**
 *
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
public class TestDirection {

  public static void main(String[] args) {
    byte bEndpointAddress = (byte) 0x81;

    System.out.println("direction " + EEndpointDirection.fromByte(bEndpointAddress));
    BMRequestType bmRequest = new BMRequestType(EEndpointDirection.HOST_TO_DEVICE, BMRequestType.EType.CLASS, BMRequestType.ERecipient.DEVICE);

    System.out.println("bmReq " + ByteUtil.toHexString(bmRequest.getByteCode()));

  }
}
