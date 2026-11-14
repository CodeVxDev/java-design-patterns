package dev.codevx.patterns.strategy.handrolled;

public class Wallet implements PaymentStrategy {
  @Override
  public String pay(long amount) {
    return "Wallet pays " + amount;
  }
}
