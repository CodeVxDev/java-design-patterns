package dev.codevx.patterns.observer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.observer.handrolled.Order;
import dev.codevx.patterns.observer.handrolled.OrderListener;
import dev.codevx.patterns.observer.handrolled.Orders;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** The hand-rolled pitfalls. The Spring ones are in SpringPitfallsTest. */
@DisplayName("Observer pitfalls, by hand")
class PitfallsTest {

  @Test
  @DisplayName("Pitfall: a listener that throws fails pay() and skips every listener after it")
  void throwingListenerFailsPay() {
    List<String> heard = new ArrayList<>();
    Orders orders = new Orders();
    orders.subscribe(order -> {
      throw new IllegalStateException("email is down");
    });
    orders.subscribe(order -> heard.add("stock " + order.id()));

    assertThatThrownBy(() -> orders.pay(new Order("p-1"))).hasMessage("email is down");
    assertThat(heard).isEmpty(); // the order no longer knows its listeners, but it still waits for them
  }

  @Test
  @DisplayName("Pitfall: with an ArrayList, a listener that unsubscribes during the loop throws CME")
  void arrayListBreaksOnUnsubscribe() {
    List<OrderListener> listeners = new ArrayList<>();
    OrderListener once = new OrderListener() {
      public void onPaid(Order order) {
        listeners.remove(this); // a one-shot listener removing itself
      }
    };
    listeners.add(once);
    listeners.add(order -> {});

    assertThatThrownBy(() -> {
      for (OrderListener l : listeners) {
        l.onPaid(new Order("p-2"));
      }
    }).isInstanceOf(ConcurrentModificationException.class);
  }

  @Test
  @DisplayName("Fix: Orders keeps its listeners in a CopyOnWriteArrayList, so the loop sees a snapshot")
  void copyOnWriteSurvivesUnsubscribe() {
    Orders orders = new Orders();
    List<String> heard = new ArrayList<>();
    OrderListener once = new OrderListener() {
      public void onPaid(Order order) {
        heard.add("once " + order.id());
        orders.unsubscribe(this);
      }
    };
    orders.subscribe(once);

    orders.pay(new Order("p-3"));
    orders.pay(new Order("p-4"));

    assertThat(heard).containsExactly("once p-3");
  }
}
