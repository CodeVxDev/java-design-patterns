package dev.codevx.patterns.decorator.before;

/** Extra #2 as a subclass: retries once after a timeout. */
public class RetryingGateway extends BankGateway {
  @Override
  public Receipt charge(long amt) {
    try {
      return super.charge(amt);
    } catch (Timeout e) {
      return super.charge(amt);
    }
  }
}
