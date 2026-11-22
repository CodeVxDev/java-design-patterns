package dev.codevx.patterns.decorator.before;

/**
 * Both extras: a third class, which copies the other two because Java has one superclass. Add metrics and the
 * three extras need seven classes; a fourth extra makes fifteen. The order of the extras is fixed in each class.
 */
public class LoggingRetryingGateway extends BankGateway {
  @Override
  public Receipt charge(long amt) {
    System.out.println("  log: charge " + amt);
    try {
      return super.charge(amt);
    } catch (Timeout e) {
      return super.charge(amt);
    }
  }
}
