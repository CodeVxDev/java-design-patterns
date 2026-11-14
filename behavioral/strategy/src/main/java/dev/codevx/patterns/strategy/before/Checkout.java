package dev.codevx.patterns.strategy.before;

/**
 * Where the episode starts: one method, one branch per payment type. Adding PayLater means editing this
 * class again, and every edit can break the branches that already worked. A switch has the same problem.
 */
public class Checkout {
  public enum Type { CARD, PAYPAL, WALLET, PAY_LATER }

  public String pay(Type type, long amount) {
    if (type == Type.CARD) {
      return "Card pays " + amount;
    } else if (type == Type.PAYPAL) {
      return "PayPal pays " + amount;
    } else if (type == Type.WALLET) {
      return "Wallet pays " + amount;
    } else if (type == Type.PAY_LATER) { // the branch that PayLater added
      return "PayLater pays " + amount;
    }
    throw new IllegalArgumentException("Unsupported payment type: " + type);
  }
}
