package dev.codevx.patterns.proxy.before;

import java.util.Set;

/** Stands in for the stock service: reserving an item that isn't in stock fails. */
public class Stock {
  private final Set<String> inStock;

  public Stock(String... inStock) {
    this.inStock = Set.of(inStock);
  }

  public void reserve(Order o) {
    if (!inStock.contains(o.item())) {
      throw new IllegalStateException("out of stock: " + o.item());
    }
  }
}
