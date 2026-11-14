package dev.codevx.patterns.strategy.handrolled;

/** One way of doing the job. One method, so a lambda can be a strategy too. */
@FunctionalInterface
public interface PaymentStrategy {
  String pay(long amount);
}
