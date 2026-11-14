package dev.codevx.patterns.state.spring;

/**
 * The behaviour of one status, as a stateless Spring bean. Each action returns the status to move to; an
 * action the status doesn't allow keeps the default and refuses with a reason.
 */
public interface OrderState {
  Status status();

  default Status pay(Order order) {
    throw refuse("pay");
  }

  default Status ship(Order order) {
    throw refuse("ship");
  }

  default Status cancel(Order order) {
    throw refuse("cancel");
  }

  private IllegalStateException refuse(String action) {
    return new IllegalStateException("can't " + action + " an order that is " + status());
  }
}
