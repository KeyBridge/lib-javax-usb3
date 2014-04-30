/*
 * Copyright (C) 2011 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */
package org.usb4java.javax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

/**
 * Base class for event listener lists.
 * <p>
 * @author Klaus Reimer (k@ailis.de)
 * @param <T> The event listener type.
 */
abstract class EventListenerList<T extends EventListener> {

  /**
   * The list with registered listeners.
   */
  private final List<T> listeners = Collections
    .synchronizedList(new ArrayList<T>());

  /**
   * Adds a listener.
   * <p>
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
   * <p>
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
   * <p>
   * @return Array with registered listeners.
   */
  public abstract T[] toArray();

  /**
   * Returns the listeners list.
   * <p>
   * @return The listeners list.
   */
  protected final List<T> getListeners() {
    return this.listeners;
  }
}
