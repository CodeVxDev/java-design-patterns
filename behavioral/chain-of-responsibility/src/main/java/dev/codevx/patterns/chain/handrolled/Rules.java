package dev.codevx.patterns.chain.handrolled;

import java.util.List;

/**
 * The runner shape: this loop moves the order on, not the checks. No rule can forget to pass it along and
 * silently skip the rest (pitfall 1), at the price that a rule can't do work after the others have run, which a
 * servlet filter that times a request needs.
 */
public class Rules {
  private final List<Rule> rules;

  public Rules(List<Rule> rules) {
    this.rules = rules;
  }

  public void validate(Order o) {
    for (Rule rule : rules) {
      rule.check(o); // throws to stop
    }
  }
}
