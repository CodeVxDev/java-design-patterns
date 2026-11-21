package dev.codevx.patterns.proxy.spring;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The trap, as in the video. Spring hands every other bean a generated subclass of this class
 * (OrderService$$SpringCGLIB$$0) that runs save() inside a transaction. But placeOrder() calls save() on this,
 * the real object, so that call never crosses the proxy: no transaction, and when the stock call fails the
 * order stays saved (the repository's own save() commits at once).
 *
 * <p>The constructor is the one Lombok's {@code @RequiredArgsConstructor} would generate, written out.
 */
@Service
public class OrderService {
  private final OrderRepository orders;
  private final Stock stock;

  public OrderService(OrderRepository orders, Stock stock) {
    this.orders = orders;
    this.stock = stock;
  }

  public void placeOrder(Order o) {
    validate(o);
    save(o);
  }

  @Transactional
  public void save(Order o) {
    orders.save(o); // the video's orders.insert(o): a JPA repository calls it save()
    stock.reserve(o);
  }

  private void validate(Order o) {
    if (o.getItem() == null || o.getItem().isBlank()) {
      throw new IllegalArgumentException("an order needs an item");
    }
  }
}
