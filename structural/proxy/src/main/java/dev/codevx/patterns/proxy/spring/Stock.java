package dev.codevx.patterns.proxy.spring;

import java.util.Set;
import org.springframework.stereotype.Component;

/** Stands in for the stock service: a lamp is never in stock, so reserving one fails. */
@Component
public class Stock {
  private final Set<String> inStock = Set.of("book", "pen", "mug");

  public void reserve(Order o) {
    if (!inStock.contains(o.getItem())) {
      throw new IllegalStateException("out of stock: " + o.getItem());
    }
  }
}
