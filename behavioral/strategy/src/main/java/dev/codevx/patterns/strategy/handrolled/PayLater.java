package dev.codevx.patterns.strategy.handrolled;

/** Added later as one new class: Checkout and the other strategies did not change. */
public class PayLater implements PaymentStrategy {
  @Override
  public String pay(long amount) {
    return "PayLater pays " + amount;
  }
}
