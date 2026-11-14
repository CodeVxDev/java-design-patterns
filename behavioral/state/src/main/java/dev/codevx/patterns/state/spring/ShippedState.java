package dev.codevx.patterns.state.spring;

import org.springframework.stereotype.Component;

/** Allows nothing: pay, ship and cancel all refuse. Too late to cancel once it ships. */
@Component
class ShippedState implements OrderState {
  @Override
  public Status status() {
    return Status.SHIPPED;
  }
}
