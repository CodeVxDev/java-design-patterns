package dev.codevx.patterns.strategy.spring;

public interface PaymentStrategy {
  String pay(long amount);
}
