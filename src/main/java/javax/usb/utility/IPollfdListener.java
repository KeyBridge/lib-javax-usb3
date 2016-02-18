/*
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
 * Copyright 2013 Klaus Reimer 
 * Copyright (C) 2014 Key Bridge LLC. All Rights Reserved.
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
package javax.usb.utility;

import java.io.FileDescriptor;

/**
 * Listener interface for polling file descriptor ({@code pollfd})
 * notifications.
 *
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public interface IPollfdListener {

  /**
   * Callback function, invoked when a new file descriptor should be added to
   * the set of file descriptors monitored for events.
   *
   * @param fd       the new file descriptor.
   * @param events   events to monitor for.
   * @param userData User data pointer.
   */
  public void pollfdAdded(FileDescriptor fd, int events, Object userData);

  /**
   * Callback function, invoked when a file descriptor should be removed from
   * the set of file descriptors being monitored for events.
   * <p>
   * After returning from this callback, do not use that file descriptor again.
   *
   * @param fd       The file descriptor to stop monitoring.
   * @param userData User data pointer.
   */
  public void pollfdRemoved(FileDescriptor fd, Object userData);
}
