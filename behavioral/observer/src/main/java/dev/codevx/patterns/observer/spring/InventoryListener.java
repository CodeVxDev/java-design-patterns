package dev.codevx.patterns.observer.spring;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Same thread, same transaction, on purpose: the payment must not succeed unless stock was reserved, so
 * this listener's failure should fail the payment. Decide per listener.
 */
@Component
class InventoryListener {
  private final Activity activity;

  InventoryListener(Activity activity) {
    this.activity = activity;
  }

  @EventListener
  void on(OrderPaid event) {
    activity.record("stock " + event.orderId());
  }
}
