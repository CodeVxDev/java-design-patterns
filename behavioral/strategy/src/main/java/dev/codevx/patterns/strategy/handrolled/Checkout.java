package dev.codevx.patterns.strategy.handrolled;

import java.util.Objects;

/**
 * The context. It knows only the interface, so it never changes when a payment type is added. The caller
 * picks the strategy; that is what separates Strategy from State, where the object changes its own.
 */
public class Checkout {
  private PaymentStrategy strategy;

  public Checkout(PaymentStrategy strategy) {
    use(strategy);
  }

  /** The caller swaps the behaviour. */
  public void use(PaymentStrategy strategy) {
    this.strategy = Objects.requireNonNull(strategy, "strategy");
  }

  public String pay(long amount) {
    return strategy.pay(amount);
  }
}
