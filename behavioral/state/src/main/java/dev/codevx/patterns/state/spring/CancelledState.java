package dev.codevx.patterns.state.spring;

import org.springframework.stereotype.Component;

@Component
class CancelledState implements OrderState {
  @Override
  public Status status() {
    return Status.CANCELLED;
  }
}
