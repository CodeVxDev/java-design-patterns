package dev.codevx.patterns.proxy.before;

/**
 * Without a proxy, every method that must be all-or-nothing carries the transaction lines itself: two lines of
 * work, seven of plumbing, repeated in every such method of every service.
 */
public class OrderService {
  private final Tx tx;
  private final Orders orders;
  private final Stock stock;

  public OrderService(Tx tx, Orders orders, Stock stock) {
    this.tx = tx;
    this.orders = orders;
    this.stock = stock;
  }

  public void save(Order o) {
    tx.begin();
    try {
      orders.insert(o);
      stock.reserve(o);
      tx.commit();
    } catch (RuntimeException e) {
      tx.rollback();
      throw e;
    }
  }

  public void saveAll(Order first, Order second) {
    tx.begin(); // the same seven lines again, around different work
    try {
      orders.insert(first);
      orders.insert(second);
      stock.reserve(first);
      stock.reserve(second);
      tx.commit();
    } catch (RuntimeException e) {
      tx.rollback();
      throw e;
    }
  }
}
