package dev.codevx.patterns.observer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.observer.spring.Activity;
import dev.codevx.patterns.observer.spring.ObserverApp;
import dev.codevx.patterns.observer.spring.OrderPaid;
import dev.codevx.patterns.observer.spring.Orders;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Each pitfall shows the failure first, then the fix. Extra listeners (below) react only to their own
 * order ids, so they don't disturb the other tests.
 */
@SpringBootTest(classes = ObserverApp.class)
@Import(SpringPitfallsTest.TestListeners.class)
@DisplayName("Observer pitfalls, with Spring")
class SpringPitfallsTest {

  @Autowired Orders orders;
  @Autowired Activity activity;
  @Autowired ApplicationEventPublisher publisher;

  @Test
  @DisplayName("Pitfall: a plain @EventListener that throws rolls the payment back")
  void throwingListenerRollsBack() {
    orders.create("down-1");

    assertThatThrownBy(() -> orders.pay("down-1")).hasMessage("email is down");
    assertThat(orders.isPaid("down-1")).isFalse();
  }

  @Test
  @DisplayName("Pitfall: a plain @EventListener runs before the commit, so it acts on a payment that rolls back")
  void plainListenerRunsBeforeCommit() {
    orders.create("down-2");

    assertThatThrownBy(() -> orders.pay("down-2")).hasMessage("email is down");
    assertThat(activity.find("plain down-2")).isPresent(); // it ran...
    assertThat(orders.isPaid("down-2")).isFalse(); // ...for a payment that never happened
  }

  @Test
  @DisplayName("Fix: a @TransactionalEventListener runs only after the commit, so a rollback sends nothing")
  void afterCommitListenerSkipsRollback() throws InterruptedException {
    orders.create("down-3");

    assertThatThrownBy(() -> orders.pay("down-3")).hasMessage("email is down");
    assertThat(activity.await("email down-3", Duration.ofMillis(500))).isEmpty();
    assertThat(activity.find("points down-3")).isEmpty();
  }

  @Test
  @DisplayName("Fix: an @Async listener that fails can't fail pay()")
  void asyncFailureDoesNotFailPay() {
    orders.create("async-1");

    orders.pay("async-1");

    assertThat(orders.isPaid("async-1")).isTrue();
  }

  @Test
  @DisplayName("Pitfall: an @Async listener's exception never reaches the caller; only a handler sees it")
  void asyncFailureIsSilent() throws InterruptedException {
    orders.create("async-2");
    orders.pay("async-2"); // returns normally

    assertThat(activity.await("async failure: analytics is down for async-2", Duration.ofSeconds(5)))
        .isPresent(); // seen only because AsyncConfig registers an AsyncUncaughtExceptionHandler
  }

  @Test
  @DisplayName("Pitfall: with no transaction, a @TransactionalEventListener is skipped without a word")
  void noTransactionNoEvent() throws InterruptedException {
    publisher.publishEvent(new OrderPaid("no-tx-1")); // outside any transaction

    assertThat(activity.find("stock no-tx-1")).isPresent(); // the plain listener ran
    assertThat(activity.await("email no-tx-1", Duration.ofMillis(500))).isEmpty(); // this one didn't
  }

  @Test
  @DisplayName("Fix: fallbackExecution = true runs it anyway when there's no transaction")
  void fallbackExecutionRuns() {
    publisher.publishEvent(new OrderPaid("no-tx-2"));

    assertThat(activity.find("fallback no-tx-2")).isPresent();
  }

  /** Listeners only these tests use. Ordered, because listeners otherwise run in no promised order. */
  static class TestListeners {
    private final Activity activity;

    TestListeners(Activity activity) {
      this.activity = activity;
    }

    @EventListener
    @Order(1)
    void plain(OrderPaid event) {
      if (event.orderId().startsWith("down-")) {
        activity.record("plain " + event.orderId());
      }
    }

    @EventListener
    @Order(2)
    void emailDown(OrderPaid event) {
      if (event.orderId().startsWith("down-")) {
        throw new IllegalStateException("email is down");
      }
    }

    @Async
    @TransactionalEventListener
    void analyticsDown(OrderPaid event) {
      if (event.orderId().startsWith("async-")) {
        throw new IllegalStateException("analytics is down for " + event.orderId());
      }
    }

    @TransactionalEventListener(fallbackExecution = true)
    void fallback(OrderPaid event) {
      if (event.orderId().startsWith("no-tx-")) {
        activity.record("fallback " + event.orderId());
      }
    }
  }
}
