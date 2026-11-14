package dev.codevx.patterns.observer.spring;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * The fix from the episode. @TransactionalEventListener waits for the commit (its default phase), so no
 * email goes out for a payment that rolled back. @Async runs it on another thread, so a slow or broken
 * email server can't slow or fail pay(). It needs @EnableAsync once (see AsyncConfig).
 */
@Component
class EmailListener {
  private final Activity activity;

  EmailListener(Activity activity) {
    this.activity = activity;
  }

  @Async
  @TransactionalEventListener
  void on(OrderPaid event) {
    activity.record("email " + event.orderId());
  }
}
