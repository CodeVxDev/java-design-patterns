package dev.codevx.patterns.strategy.spring;

import org.springframework.stereotype.Component;

@Component
public class Card implements PaymentStrategy {
  @Override
  public String pay(long amount) {
    return "Card pays " + amount;
  }
}
