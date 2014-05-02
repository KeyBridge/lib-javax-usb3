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

/**
 *
 * @author Jesse Caulfield <jesse@caulfield.org>
 */
public class TestByteArray {

  public static void main(String[] arg) {
    byte[] arr = new byte[17];

    for (int i = 0; i <= 17; i++) {
      try {
        arr[i] = (byte) i;
        System.out.println("add " + i);
      } catch (Exception e) {
        System.err.println("error adding  " + i);
      }
    }

  }
}
