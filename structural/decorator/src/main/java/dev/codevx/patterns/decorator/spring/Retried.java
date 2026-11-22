package dev.codevx.patterns.decorator.spring;

/**
 * Retries once after a timeout, with the same key. Kept to a single retry, as in the video; in production, use a
 * library's retry (Spring Retry, Resilience4j) with a limit and a backoff, and still send the key.
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
