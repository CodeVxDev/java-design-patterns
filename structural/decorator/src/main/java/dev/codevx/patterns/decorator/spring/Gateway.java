package dev.codevx.patterns.decorator.spring;

/**
 * The interface every wrapper shares with the client it holds. The video's Gateway is {@code charge(long amt)}, to
 * fit the screen; a charge that is retried needs an idempotency key (pitfall 2), so this one takes it, as real
 * payment APIs do. The key is made once per payment, outside the retry.
 */
public interface Gateway {
  Receipt charge(String key, long amt);
}
