package dev.codevx.patterns.strategy.before;

// Run without a build: java src/main/java/dev/codevx/patterns/strategy/before/Demo.java
public class Demo {
  public static void main(String[] args) {
    Checkout checkout = new Checkout();
    for (Checkout.Type type : Checkout.Type.values()) {
      System.out.println(checkout.pay(type, 4900)); // amounts are in cents
    }
  }
}
