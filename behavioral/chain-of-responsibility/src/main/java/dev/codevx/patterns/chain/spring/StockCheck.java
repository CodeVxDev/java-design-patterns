package dev.codevx.patterns.chain.spring;

import org.springframework.stereotype.Component;

@Component
@org.springframework.core.annotation.Order(2)
public class StockCheck implements Check {
  @Override
  public void check(Order o, Chain next) {
    if (!o.inStock()) {
      throw new Rejected("stock");
    }
    next.proceed(o);
  }
}
