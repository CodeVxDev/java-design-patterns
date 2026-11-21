package dev.codevx.patterns.proxy.spring;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The fix's other half: save() in its own bean, so every call to it arrives through its proxy. */
@Service
public class OrderWriter {
  private final OrderRepository orders;
  private final Stock stock;

  public OrderWriter(OrderRepository orders, Stock stock) {
    this.orders = orders;
    this.stock = stock;
  }

  @Transactional
  public void save(Order o) {
    orders.save(o);
    stock.reserve(o);
  }
}
