package dev.codevx.patterns.chain.handrolled;

import java.util.Iterator;

/** Hands the order to whichever check is next in the list. When the list runs out, the order has passed. */
public class Chain {
  final Iterator<Check> rest;

  public Chain(Iterator<Check> rest) {
    this.rest = rest;
  }

  public void proceed(Order o) {
    if (rest.hasNext()) {
      rest.next().check(o, this);
    }
  }
}
