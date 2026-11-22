package dev.codevx.patterns.decorator.spring;

import org.springframework.stereotype.Service;

/**
 * Holds a Gateway and can't tell how many wrappers it is talking to. The order's ID is the idempotency key: made
 * once per payment, outside the retry, so every try sends the same one.
 *
 * <p>The constructor is the one Lombok's {@code @RequiredArgsConstructor} would generate, written out.
 */
@Service
public class Checkout {
  private final Gateway gateway;

  public Checkout(Gateway gateway) {
    this.gateway = gateway;
  }

  public Receipt pay(String orderId, long amt) {
    return gateway.charge(orderId, amt);
  }

  public Gateway gateway() {
    return gateway;
  }
}
