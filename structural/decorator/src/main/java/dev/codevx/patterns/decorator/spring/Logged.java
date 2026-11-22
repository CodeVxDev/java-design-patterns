package dev.codevx.patterns.decorator.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Logs the charge and always calls through. Not a bean: GatewayConfig stacks it. */
public class Logged implements Gateway {
  private static final Logger log = LoggerFactory.getLogger(Logged.class);
  final Gateway inner;

  public Logged(Gateway inner) {
    this.inner = inner;
  }

  @Override
  public Receipt charge(String key, long amt) {
    log.info("log: charge {} for {}", amt, key);
    return inner.charge(key, amt);
  }

  @Override
  public String toString() {
    return "Logged(" + inner + ")";
  }
}
