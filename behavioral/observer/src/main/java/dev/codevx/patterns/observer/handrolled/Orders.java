package dev.codevx.patterns.observer.handrolled;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The subject. It announces once and loops over whoever subscribed; a new team subscribes without a change
 * here.
 *
 * <p>The loop runs in the caller's thread: pay() returns only after the last listener, and a listener that
 * throws fails pay() and skips the listeners after it. CopyOnWriteArrayList lets a listener unsubscribe
 * while the loop runs; a plain ArrayList would throw ConcurrentModificationException.
 */
public class Orders {
  private final List<OrderListener> listeners = new CopyOnWriteArrayList<>();

  public void subscribe(OrderListener listener) {
    listeners.add(listener);
  }

  public void unsubscribe(OrderListener listener) {
    listeners.remove(listener);
  }

  public void pay(Order order) {
    order.markPaid();
    for (OrderListener listener : listeners) {
      listener.onPaid(order);
    }
  }
}
