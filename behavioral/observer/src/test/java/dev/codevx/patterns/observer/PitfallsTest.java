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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/** Numbered as in the README. The hand-rolled pitfalls; the Spring ones (3 to 6) are in SpringPitfallsTest. */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Observer pitfalls, by hand")
class PitfallsTest {

  @Test
  @org.junit.jupiter.api.Order(1)
  @DisplayName("1. Pitfall: a listener that throws fails pay() and skips every listener after it")
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
  @org.junit.jupiter.api.Order(2)
  @DisplayName("2. Pitfall: with an ArrayList, a listener that unsubscribes mid-loop throws CME, or skips the next one")
  void arrayListBreaksOnUnsubscribe() {
    // Three listeners: the iterator notices the list changed and throws.
    List<String> heard = new ArrayList<>();
    List<OrderListener> three = new ArrayList<>();
    three.add(oneShot(three));
    three.add(order -> heard.add("stock " + order.id()));
    three.add(order -> heard.add("points " + order.id()));
    assertThatThrownBy(() -> notify(three, new Order("p-2")))
        .isInstanceOf(ConcurrentModificationException.class);

    // Two listeners: worse. hasNext() sees the shorter list and the loop just ends, so "stock" is never told.
    List<OrderListener> two = new ArrayList<>();
    two.add(oneShot(two));
    two.add(order -> heard.add("stock " + order.id()));
    notify(two, new Order("p-3"));
    assertThat(heard).isEmpty();
  }

  /** A listener that removes itself the first time it's told, the way one-shot listeners do. */
  private static OrderListener oneShot(List<OrderListener> listeners) {
    return new OrderListener() {
      public void onPaid(Order order) {
        listeners.remove(this);
      }
    };
  }

  /** The video's loop, over a plain ArrayList. */
  private static void notify(List<OrderListener> listeners, Order order) {
    for (OrderListener l : listeners) {
      l.onPaid(order);
    }
  }

  @Test
  @org.junit.jupiter.api.Order(3)
  @DisplayName("2. Fix: Orders keeps its listeners in a CopyOnWriteArrayList, so the loop sees a snapshot")
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
