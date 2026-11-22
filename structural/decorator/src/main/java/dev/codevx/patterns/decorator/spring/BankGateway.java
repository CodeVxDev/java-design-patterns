package dev.codevx.patterns.decorator.spring;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The real client, and a bean. It implements Gateway, so Spring sees two Gateway beans: this one and the stack
 * built in GatewayConfig (pitfall 3).
 *
 * <p>It stands in for a bank on a bad network: the reply to the first try of every payment is lost, so the card is
 * charged but the caller sees a timeout. Like a real payment API it keeps each key's result and returns it when
 * the key comes again, without a second charge.
 */
@Component
public class BankGateway implements Gateway {
  private static final Logger log = LoggerFactory.getLogger(BankGateway.class);
  private final Map<String, Receipt> byKey = new HashMap<>();
  private final Map<String, Integer> charges = new HashMap<>();

  @Override
  public Receipt charge(String key, long amt) {
    Receipt done = byKey.get(key);
    if (done != null) {
      log.info("bank: {} again, no new charge", key);
      return done;
    }
    Receipt receipt = new Receipt(key, amt);
    byKey.put(key, receipt);
    charges.merge(key, 1, Integer::sum);
    log.info("bank: charged {} for {}, reply lost", amt, key);
    throw new Timeout();
  }

  /** How many times the card was charged for this payment. */
  public int charges(String key) {
    return charges.getOrDefault(key, 0);
  }

  @Override
  public String toString() {
    return "BankGateway";
  }
}
