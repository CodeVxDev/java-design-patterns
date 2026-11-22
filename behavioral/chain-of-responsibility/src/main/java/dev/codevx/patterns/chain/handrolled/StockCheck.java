package dev.codevx.patterns.chain.handrolled;

public class StockCheck implements Check {
  @Override
  public void check(Order o, Chain next) {
    if (!o.inStock()) {
      throw new Rejected("stock");
    }
    next.proceed(o);
  }
}
