package dev.codevx.patterns.chain.before;

/**
 * One method, an if for every rule. Every new rule (here the risk team's spending limit) is another edit to this
 * method, and the rules can't be reordered, switched off or tested on their own.
 */
public class Checkout {
  static final long LIMIT = 500_00;

  public void validate(Order o) {
    if (o.risky()) {
      throw new Rejected("fraud");
    }
    if (!o.inStock()) {
      throw new Rejected("stock");
    }
    if (!o.couponValid()) {
      throw new Rejected("coupon");
    }
    if (o.total() > LIMIT) { // edit #4: the spending limit
      throw new Rejected("limit");
    }
  }
}
