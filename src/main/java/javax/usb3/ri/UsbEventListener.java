/*
 * Copyright (C) 2011 Klaus Reimer
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
package javax.usb3.ri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

/**
 * Base class for event listener lists.
 *
 * @param <T> The event listener type.
 * @author Klaus Reimer
 * @author Jesse Caulfield
 */
public abstract class UsbEventListener<T extends EventListener> {

  /**
   * The list with registered listeners.
   */
  private final List<T> listeners = Collections.synchronizedList(new ArrayList<T>());

  /**
   * Adds a listener.
   *
   * @param listener The listener to add.
   */
  public final void add(final T listener) {
    if (this.listeners.contains(listener)) {
      return;
    }
    this.listeners.add(listener);
  }

  /**
   * Removes a listener.
   *
   * @param listener The listener to remove.
   */
  public final void remove(final T listener) {
    this.listeners.remove(listener);
  }

  /**
   * Removes all registered listeners.
   */
  public final void clear() {
    this.listeners.clear();
  }

  /**
   * Returns an array with the currently registered listeners. The returned
   * array is detached from the internal list of registered listeners.
   *
   * @return Array with registered listeners.
   */
  public abstract T[] toArray();

  /**
   * Returns the listeners list.
   *
   * @return The listeners list.
   */
  protected final List<T> getListeners() {
    return this.listeners;
  }
}
