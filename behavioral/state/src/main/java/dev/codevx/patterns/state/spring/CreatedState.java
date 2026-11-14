package dev.codevx.patterns.state.spring;

import org.springframework.stereotype.Component;

@Component
class CreatedState implements OrderState {
  @Override
  public Status status() {
    return Status.CREATED;
  }

  @Override
  public Status pay(Order order) {
    return Status.PAID;
  }

  @Override
  public Status cancel(Order order) {
    return Status.CANCELLED;
  }
}
