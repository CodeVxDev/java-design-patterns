package dev.codevx.patterns.strategy.spring;

import org.springframework.stereotype.Component;

/** Added later as one new bean: Spring adds a "payLater" entry to Checkout's map. Nothing else changed. */
@Component
public class PayLater implements PaymentStrategy {
  @Override
  public String pay(long amount) {
    return "PayLater pays " + amount;
  }
}
