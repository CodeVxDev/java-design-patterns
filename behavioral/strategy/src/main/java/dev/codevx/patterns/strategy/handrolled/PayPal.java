package dev.codevx.patterns.strategy.handrolled;

public class PayPal implements PaymentStrategy {
  @Override
  public String pay(long amount) {
    return "PayPal pays " + amount;
  }
}
