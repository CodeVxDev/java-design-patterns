package dev.codevx.patterns.decorator.handrolled;

/** The bank didn't answer in time. */
public class Timeout extends RuntimeException {
  public Timeout() {
    super("timeout");
  }
}
