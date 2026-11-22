package dev.codevx.patterns.chain.before;

/** Thrown by a check that stops the order; the message names the check. */
public class Rejected extends RuntimeException {
  public Rejected(String reason) {
    super(reason);
  }
}
