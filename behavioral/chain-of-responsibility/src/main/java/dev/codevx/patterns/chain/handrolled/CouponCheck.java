package dev.codevx.patterns.chain.handrolled;

public class CouponCheck implements Check {
  @Override
  public void check(Order o, Chain next) {
    if (!o.couponValid()) {
      throw new Rejected("coupon");
    }
    next.proceed(o);
  }
}
