package dev.codevx.patterns.proxy.before;

// Run without a build: java src/main/java/dev/codevx/patterns/proxy/before/Demo.java
public class Demo {
  public static void main(String[] args) {
    Tx tx = new Tx();
    Orders orders = new Orders(tx);
    OrderService service = new OrderService(tx, orders, new Stock("book", "pen"));

    service.save(new Order("order-1", "book"));
    System.out.println("save(book) committed: " + orders.saved());

    try {
      service.save(new Order("order-2", "lamp"));
    } catch (IllegalStateException e) {
      System.out.println("save(lamp) failed (" + e.getMessage() + "), rolled back: " + orders.saved());
    }
    System.out.println("It works, but every method repeats begin, commit and rollback around its own work.");
  }
}
