package dev.codevx.patterns.observer.handrolled;

/** What every listener promises. The order knows only this, not who is listening. */
@FunctionalInterface
public interface OrderListener {
  void onPaid(Order order);
}
