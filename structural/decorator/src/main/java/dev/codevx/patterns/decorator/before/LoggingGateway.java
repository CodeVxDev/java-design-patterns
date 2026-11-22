package dev.codevx.patterns.decorator.before;

/** Extra #1 as a subclass: logs every charge. */
public class LoggingGateway extends BankGateway {
  @Override
  public Receipt charge(long amt) {
    System.out.println("  log: charge " + amt);
    return super.charge(amt);
  }
}
