package dev.codevx.patterns.decorator.before;

/** The real client. Its first call times out, so a retry has something to do. */
public class BankGateway implements Gateway {
  private int calls;

  @Override
  public Receipt charge(long amt) {
    if (++calls == 1) {
      throw new Timeout();
    }
    return new Receipt(amt);
  }
}
