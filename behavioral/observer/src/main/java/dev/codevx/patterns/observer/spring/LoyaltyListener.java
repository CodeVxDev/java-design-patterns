package dev.codevx.patterns.observer.spring;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * The new team's one class. It writes after the payment's commit, when that transaction is over, so it
 * needs one of its own: REQUIRES_NEW. (Spring rejects a plain @Transactional here.)
 */
@Component
class LoyaltyListener {
  private final JdbcClient jdbc;
  private final Activity activity;

  LoyaltyListener(JdbcClient jdbc, Activity activity) {
    this.jdbc = jdbc;
    this.activity = activity;
  }

  @TransactionalEventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  void on(OrderPaid event) {
    jdbc.sql("insert into points (order_id) values (?)").param(event.orderId()).update();
    activity.record("points " + event.orderId());
  }
}
