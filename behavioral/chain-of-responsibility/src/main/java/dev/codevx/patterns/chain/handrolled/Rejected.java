package dev.codevx.patterns.chain.handrolled;

/** Thrown by a check that stops the order; the message names the check. */
public class Rejected extends RuntimeException {
  public Rejected(String reason) {
    super(reason);
  }
}
