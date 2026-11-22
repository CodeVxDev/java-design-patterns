package dev.codevx.patterns.chain.handrolled;

public class FraudCheck implements Check {
  @Override
  public void check(Order o, Chain next) {
    if (o.risky()) {
      throw new Rejected("fraud");
    }
    next.proceed(o);
  }
}
