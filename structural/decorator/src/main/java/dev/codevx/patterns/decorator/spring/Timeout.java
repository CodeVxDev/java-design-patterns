package dev.codevx.patterns.decorator.spring;

/** The bank didn't answer in time. */
public class Timeout extends RuntimeException {
  public Timeout() {
    super("timeout");
  }
}
