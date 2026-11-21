package dev.codevx.patterns.proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.proxy.handrolled.Order;
import dev.codevx.patterns.proxy.handrolled.OrderService;
import dev.codevx.patterns.proxy.handrolled.OrderServiceProxy;
import dev.codevx.patterns.proxy.handrolled.Orders;
import dev.codevx.patterns.proxy.handrolled.Stock;
import dev.codevx.patterns.proxy.handrolled.Tx;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Proxy, by hand")
class HandRolledTest {
  final Tx tx = new Tx();
  final Orders orders = new Orders(tx);
  final OrderService service = new OrderServiceProxy(new OrderService(orders, new Stock("book")), tx);

  @Test
  @DisplayName("The proxy is an OrderService, so callers can't tell it from the real one")
  void sameType() {
    assertThat(service).isInstanceOf(OrderService.class);
  }

  @Test
  @DisplayName("Through the proxy, save() runs in a transaction and the order is committed")
  void commits() {
    service.save(new Order("o-1", "book"));

    assertThat(orders.saved()).containsExactly("o-1");
  }

  @Test
  @DisplayName("When the stock call fails, the proxy rolls the order back")
  void rollsBack() {
    assertThatThrownBy(() -> service.save(new Order("o-2", "lamp"))).hasMessage("out of stock: lamp");

    assertThat(orders.saved()).isEmpty();
  }
}
