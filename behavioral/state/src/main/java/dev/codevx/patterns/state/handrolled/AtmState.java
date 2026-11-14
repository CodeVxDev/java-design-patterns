package dev.codevx.patterns.state.handrolled;

/**
 * One class per state. Each call returns the state the ATM moves to. A state that doesn't allow a call
 * says why by throwing {@link NotAllowed}; quietly returning {@code this} hides the refusal from the caller.
 *
 * <p>The ATM is passed in, so a state can read and change the ATM's data (its cash) while holding none of
 * its own. That is what lets one instance of each state serve every ATM.
 */
public interface AtmState {
  AtmState insertCard(Atm atm);

  AtmState enterPin(Atm atm);

  AtmState withdraw(Atm atm);

  default String name() {
    return getClass().getSimpleName();
  }
}
