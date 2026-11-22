package dev.codevx.patterns.chain.spring;

import org.springframework.stereotype.Component;

/** The spending limit: one more bean. Spring finds it and adds it to the list; Checkout doesn't change. */
@Component
@org.springframework.core.annotation.Order(4)
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
