package dev.codevx.patterns.decorator.handrolled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The real client. Like a real payment API, it keeps the result of each key and returns it when the same key comes
 * again, without charging twice. A charge with no key (null) is always a new charge.
 */
public class BankGateway implements Gateway {
  private final List<Long> ledger = new ArrayList<>();
  private final Map<String, Receipt> byKey = new HashMap<>();
  private boolean loseNextReply;

  /** A bank whose first reply gets lost: the card is charged, but the caller sees a timeout. */
  public static BankGateway losingFirstReply() {
    BankGateway bank = new BankGateway();
    bank.loseNextReply = true;
    return bank;
  }

  @Override
  public Receipt charge(String key, long amt) {
    if (key != null && byKey.containsKey(key)) {
      return byKey.get(key);
    }
    ledger.add(amt);
    Receipt receipt = new Receipt(amt);
    if (key != null) {
      byKey.put(key, receipt);
    }
    if (loseNextReply) {
      loseNextReply = false;
      throw new Timeout();
    }
    return receipt;
  }

  /** How many times the card was charged. */
  public int charges() {
    return ledger.size();
  }

  @Override
  public String toString() {
    return "BankGateway";
  }
}
