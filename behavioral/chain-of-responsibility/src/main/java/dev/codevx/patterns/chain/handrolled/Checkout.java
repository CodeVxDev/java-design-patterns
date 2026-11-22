package dev.codevx.patterns.chain.handrolled;

import java.util.List;

/** Runs the order down the checks it is given. Adding, dropping or reordering checks never edits this class. */
public class Checkout {
  private final List<Check> checks;

  public Checkout(List<Check> checks) {
    this.checks = checks;
  }

  public void validate(Order o) {
    new Chain(checks.iterator()).proceed(o);
  }
}
