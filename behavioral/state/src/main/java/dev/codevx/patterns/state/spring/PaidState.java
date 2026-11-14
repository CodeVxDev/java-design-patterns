package dev.codevx.patterns.state.spring;

import org.springframework.stereotype.Component;

@Component
class PaidState implements OrderState {
  @Override
  public Status status() {
    return Status.PAID;
  }

  @Override
  public Status ship(Order order) {
    return Status.SHIPPED;
  }

  @Override
  public Status cancel(Order order) {
    // A real PaidState would start the refund here: behaviour that only this status has.
    return Status.CANCELLED;
  }
}
