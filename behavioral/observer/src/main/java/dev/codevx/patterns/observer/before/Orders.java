package dev.codevx.patterns.observer.before;

/**
 * Where the episode starts: paying an order calls every team itself. A fourth team (loyalty) meant editing
 * pay() and adding a dependency, and a failure in any team fails the payment.
 */
public class Orders {
  private final Teams.Email email = new Teams.Email();
  private final Teams.Inventory inventory = new Teams.Inventory();
  private final Teams.Analytics analytics = new Teams.Analytics();
  private final Teams.Loyalty loyalty = new Teams.Loyalty();

  public void pay(Order order) {
    order.markPaid();
    email.send(order);
    inventory.reserve(order);
    analytics.track(order);
    loyalty.addPoints(order); // the line the new team needed
  }
}
