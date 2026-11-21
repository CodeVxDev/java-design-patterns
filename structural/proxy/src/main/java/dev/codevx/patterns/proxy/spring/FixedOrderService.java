package dev.codevx.patterns.proxy.spring;

import org.springframework.stereotype.Service;

/**
 * The video's fix (it calls this class OrderService too; it is renamed here so both can live in one app): the
 * call to save() leaves the object, crosses OrderWriter's proxy, and runs inside a transaction.
 */
@Service
public class FixedOrderService {
  final OrderWriter writer;

  public FixedOrderService(OrderWriter writer) {
    this.writer = writer;
  }

  public void placeOrder(Order o) {
    if (o.getItem() == null || o.getItem().isBlank()) {
      throw new IllegalArgumentException("an order needs an item");
    }
    writer.save(o);
  }
}
