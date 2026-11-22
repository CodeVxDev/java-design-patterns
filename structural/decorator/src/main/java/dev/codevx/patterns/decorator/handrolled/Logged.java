package dev.codevx.patterns.decorator.handrolled;

import java.util.function.Consumer;

/** A wrapper with the client's own interface. It adds its one job, a log line, and always calls through. */
public class Logged implements Gateway {
  final Gateway inner;
  private final Consumer<String> log;

  public Logged(Gateway inner) {
    this(inner, line -> System.out.println("  log: " + line));
  }

  /** Takes where the lines go, so a test can count them. */
  public Logged(Gateway inner, Consumer<String> log) {
    this.inner = inner;
    this.log = log;
  }

  @Override
  public Receipt charge(String key, long amt) {
    log.accept("charge " + amt);
    return inner.charge(key, amt);
  }

  @Override
  public String toString() {
    return "Logged(" + inner + ")";
  }
}
