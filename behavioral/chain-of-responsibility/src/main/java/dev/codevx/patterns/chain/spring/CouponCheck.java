package dev.codevx.patterns.chain.spring;

import org.springframework.stereotype.Component;

@Component
@org.springframework.core.annotation.Order(3)
public class CouponCheck implements Check {
  @Override
  public void check(Order o, Chain next) {
    if (!o.couponValid()) {
      throw new Rejected("coupon");
    }
    next.proceed(o);
  }
}
