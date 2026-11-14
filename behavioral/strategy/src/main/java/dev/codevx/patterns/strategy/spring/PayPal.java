package dev.codevx.patterns.strategy.spring;

import org.springframework.stereotype.Component;

@Component
public class PayPal implements PaymentStrategy {
  @Override
  public String pay(long amount) {
    return "PayPal pays " + amount;
  }
}
