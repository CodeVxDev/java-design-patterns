package dev.codevx.patterns.observer.handrolled;

// Run without a build: java src/main/java/dev/codevx/patterns/observer/handrolled/Demo.java
public class Demo {
  public static void main(String[] args) {
    Orders orders = new Orders();
    orders.subscribe(order -> System.out.println("  email for " + order.id()));
    orders.subscribe(order -> System.out.println("  stock reserved for " + order.id()));
    System.out.println("pay() knows only OrderListener:");
    orders.pay(new Order("order-42"));

    orders.subscribe(order -> System.out.println("  points for " + order.id()));
    System.out.println("one more listener, Orders unchanged:");
    orders.pay(new Order("order-43"));

    orders.subscribe(order -> {
      throw new IllegalStateException("email is down");
    });
    orders.subscribe(order -> System.out.println("  never printed for " + order.id()));
    try {
      orders.pay(new Order("order-44"));
    } catch (IllegalStateException e) {
      System.out.println("pay() failed: " + e.getMessage() + " (the listener after it never ran)");
    }
  }
}
