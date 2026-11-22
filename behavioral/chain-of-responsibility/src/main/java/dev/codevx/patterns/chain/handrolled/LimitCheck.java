package dev.codevx.patterns.chain.handrolled;

/** The risk team's spending limit: one more class, and no other check or Checkout changes. */
public class LimitCheck implements Check {
  static final long LIMIT = 500_00;

  @Override
  public void check(Order o, Chain next) {
    if (o.total() > LIMIT) {
      throw new Rejected("limit");
    }
    next.proceed(o);
  }
}
