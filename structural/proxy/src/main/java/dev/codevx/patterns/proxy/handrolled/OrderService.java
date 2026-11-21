package dev.codevx.patterns.proxy.handrolled;

/**
 * The real object: only the work, no transaction lines. In Spring, save() would carry {@code @Transactional};
 * here the proxy decides which methods get a transaction.
 */
public class OrderService {
  private final Orders orders;
  private final Stock stock;

  public OrderService(Orders orders, Stock stock) {
    this.orders = orders;
    this.stock = stock;
  }

  public void placeOrder(Order o) {
    validate(o);
    save(o); // this.save(): the real object's own method, so the proxy never sees this call
  }

  public void save(Order o) {
    orders.insert(o);
    stock.reserve(o);
  }

  private void validate(Order o) {
    if (o.item().isBlank()) {
      throw new IllegalArgumentException("an order needs an item");
    }
  }
}
