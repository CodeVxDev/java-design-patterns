package dev.codevx.patterns.proxy.handrolled;

// Run without a build: java src/main/java/dev/codevx/patterns/proxy/handrolled/Demo.java
public class Demo {
  public static void main(String[] args) {
    Tx tx = new Tx();
    Orders orders = new Orders(tx);
    OrderService service = new OrderServiceProxy(new OrderService(orders, new Stock("book", "pen")), tx);

    service.save(new Order("order-1", "book"));
    System.out.println("save(book) through the proxy, committed: " + orders.saved());

    attempt("save(lamp) through the proxy", () -> service.save(new Order("order-2", "lamp")), orders);
    attempt(
        "placeOrder(lamp), which calls save() from inside",
        () -> service.placeOrder(new Order("order-3", "lamp")),
        orders);
    System.out.println("order-3 stayed: the call from inside went to the real save(), not the proxy's.");
  }

  private static void attempt(String what, Runnable call, Orders orders) {
    try {
      call.run();
    } catch (IllegalStateException e) {
      System.out.println(what + " failed (" + e.getMessage() + "), saved orders: " + orders.saved());
    }
  }
}
