package dev.codevx.patterns.observer;

import static org.assertj.core.api.Assertions.assertThat;

import dev.codevx.patterns.observer.handrolled.Order;
import dev.codevx.patterns.observer.handrolled.OrderListener;
import dev.codevx.patterns.observer.handrolled.Orders;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Observer, by hand")
class HandRolledTest {

  @Test
  @DisplayName("pay() tells every subscribed listener, without knowing who they are")
  void everyListenerHears() {
    List<String> heard = new ArrayList<>();
    Orders orders = new Orders();
    orders.subscribe(order -> heard.add("email " + order.id()));
    orders.subscribe(order -> heard.add("stock " + order.id()));

    orders.pay(new Order("o-1"));

    assertThat(heard).containsExactly("email o-1", "stock o-1");
  }

  @Test
  @DisplayName("A new team subscribes one more listener, and Orders doesn't change")
  void newListenerNoChange() {
    List<String> heard = new ArrayList<>();
    Orders orders = new Orders();
    orders.subscribe(order -> heard.add("email " + order.id()));
    orders.subscribe(order -> heard.add("points " + order.id()));

    orders.pay(new Order("o-2"));

    assertThat(heard).contains("points o-2");
  }

  @Test
  @DisplayName("An unsubscribed listener hears nothing more")
  void unsubscribe() {
    List<String> heard = new ArrayList<>();
    Orders orders = new Orders();
    OrderListener email = order -> heard.add("email " + order.id());
    orders.subscribe(email);
    orders.unsubscribe(email);

    orders.pay(new Order("o-3"));

    assertThat(heard).isEmpty();
  }
}
