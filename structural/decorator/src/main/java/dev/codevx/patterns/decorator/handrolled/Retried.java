package dev.codevx.patterns.decorator.handrolled;

/**
 * Retries once after a timeout, with the same key, so the bank can tell it is the same payment. Kept to a single
 * retry, as in the video; a real one counts attempts and waits between them.
 */
public class Retried implements Gateway {
  final Gateway inner;

  public Retried(Gateway inner) {
    this.inner = inner;
  }

  @Override
  public Receipt charge(String key, long amt) {
    try {
      return inner.charge(key, amt);
    } catch (Timeout e) {
      return inner.charge(key, amt);
    }
  }

  @Override
  public String toString() {
    return "Retried(" + inner + ")";
  }
}
