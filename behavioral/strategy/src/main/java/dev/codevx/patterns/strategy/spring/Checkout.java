package dev.codevx.patterns.strategy.spring;

import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * Spring injects every PaymentStrategy bean into the map, keyed by bean name. A @Component's default name is
 * its class name with a lowercase first letter: "card", "payPal", "wallet", "payLater".
 *
 * <p>The constructor is the one Lombok's {@code @RequiredArgsConstructor} would generate, written out.
 */
@Service
public class Checkout {
  private final Map<String, PaymentStrategy> strategies;

  public Checkout(Map<String, PaymentStrategy> strategies) {
    this.strategies = strategies;
  }

  public String pay(String type, long amount) {
    PaymentStrategy strategy = strategies.get(type);
    if (strategy == null) { // without this, an unknown type is a NullPointerException on the next line
      throw new IllegalArgumentException(
          "Unsupported payment type: " + type + ", expected one of " + strategies.keySet());
    }
    return strategy.pay(amount);
  }

  public Set<String> types() {
    return strategies.keySet();
  }
}
