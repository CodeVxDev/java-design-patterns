package dev.codevx.patterns.chain.handrolled;

/** A check in the other shape of the pattern: it throws to stop the order, and never calls the next one. */
public interface Rule {
  void check(Order o);
}
