package dev.codevx.patterns.strategy.spring;

import org.springframework.stereotype.Component;

@Component
public class Wallet implements PaymentStrategy {
  @Override
  public String pay(long amount) {
    return "Wallet pays " + amount;
  }
}
