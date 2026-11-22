package dev.codevx.patterns.chain.spring;

/**
 * One check, one class. It either stops the order (throws {@link Rejected}) or passes it on with
 * {@code next.proceed(o)}. It holds a {@link Chain}, never the check after it, so it can't know what comes next.
 */
public interface Check {
  void check(Order o, Chain next);
}
