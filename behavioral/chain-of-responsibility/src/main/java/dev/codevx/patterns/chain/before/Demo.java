package dev.codevx.patterns.chain.before;

// Run without a build: java src/main/java/dev/codevx/patterns/chain/before/Demo.java
public class Demo {
  public static void main(String[] args) {
    Checkout checkout = new Checkout();
    System.out.println("good order: " + outcome(checkout, new Order("order-1", false, true, true, 49_00)));
    System.out.println("risky order: " + outcome(checkout, new Order("order-2", true, true, true, 49_00)));
    System.out.println("huge order: " + outcome(checkout, new Order("order-3", false, true, true, 900_00)));
    System.out.println("It works, but the spending limit was one more edit to validate(), like every rule before it.");
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
