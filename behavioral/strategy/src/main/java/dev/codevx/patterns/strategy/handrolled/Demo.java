package dev.codevx.patterns.strategy.handrolled;

import java.util.List;

// Run without a build: java src/main/java/dev/codevx/patterns/strategy/handrolled/Demo.java
public class Demo {
  public static void main(String[] args) {
    Checkout checkout = new Checkout(new Card());
    for (PaymentStrategy strategy : List.of(new Card(), new PayPal(), new Wallet(), new PayLater())) {
      checkout.use(strategy); // swap the behaviour
      System.out.println(checkout.pay(4900)); // Checkout never changes
    }

    // A one-off strategy needs no class at all: the interface has one method.
    checkout.use(amount -> "UPI pays " + amount);
    System.out.println(checkout.pay(4900));
  }
}
