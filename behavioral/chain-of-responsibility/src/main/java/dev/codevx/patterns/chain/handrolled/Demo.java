package dev.codevx.patterns.chain.handrolled;

import java.util.List;

// Run without a build: java src/main/java/dev/codevx/patterns/chain/handrolled/Demo.java
public class Demo {
  public static void main(String[] args) {
    Order good = new Order("order-1", false, true, true, 49_00);
    Order risky = new Order("order-2", true, true, true, 49_00);
    Order huge = new Order("order-3", false, true, true, 900_00);

    Checkout checkout = new Checkout(List.of(new FraudCheck(), new StockCheck(), new CouponCheck()));
    System.out.println("good order: " + outcome(checkout, good));
    System.out.println("risky order: " + outcome(checkout, risky));
    System.out.println("huge order: " + outcome(checkout, huge));

    Checkout withLimit =
        new Checkout(List.of(new FraudCheck(), new StockCheck(), new CouponCheck(), new LimitCheck()));
    System.out.println("add LimitCheck, huge order: " + outcome(withLimit, huge));

    Order hugeAndRisky = new Order("order-4", true, true, true, 900_00);
    Checkout limitFirst = new Checkout(List.of(new LimitCheck(), new FraudCheck(), new StockCheck()));
    System.out.println("reorder, limit first, huge risky order: " + outcome(limitFirst, hugeAndRisky));
    System.out.println("No check was edited: each one only knows to stop the order or call next.proceed(o).");
  }

  static String outcome(Checkout checkout, Order o) {
    try {
      checkout.validate(o);
      return "passed every check";
    } catch (Rejected e) {
      return "stopped by " + e.getMessage();
    }
  }
}
