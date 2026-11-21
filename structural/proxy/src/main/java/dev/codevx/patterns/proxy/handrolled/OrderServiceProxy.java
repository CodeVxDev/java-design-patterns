package dev.codevx.patterns.proxy.handrolled;

/**
 * The stand-in, written the way Spring generates it: a subclass, so it can be handed out wherever an
 * OrderService is expected, and callers can't tell. It overrides every public method and passes each call to
 * the real object; save() only runs inside a transaction, and the proxy decides whether its work is kept.
 *
 * <p>Like Spring's generated subclass, the fields it inherits stay empty: it never does the work itself.
 */
public class OrderServiceProxy extends OrderService {
  private final OrderService real;
  private final Tx tx;

  public OrderServiceProxy(OrderService real, Tx tx) {
    super(null, null);
    this.real = real;
    this.tx = tx;
  }

  @Override
  public void placeOrder(Order o) {
    real.placeOrder(o); // not transactional: passed on as it is
  }

  @Override
  public void save(Order o) {
    tx.run(() -> real.save(o));
  }
}
