package dev.codevx.patterns.strategy.handrolled;

public class Card implements PaymentStrategy {
  @Override
  public String pay(long amount) {
    return "Card pays " + amount;
  }
}
