package dev.codevx.patterns.state.handrolled;

/**
 * The context. It never asks which state it is in: it hands each call to the current state and keeps
 * whatever comes back. Nobody outside sets the state; the ATM moves itself, which is what makes this State
 * and not Strategy.
 */
public class Atm {
  private AtmState state;
  private int notes;

  public Atm(int notes) {
    this.notes = notes;
    this.state = notes > 0 ? Idle.INSTANCE : NoCash.INSTANCE;
  }

  public void insertCard() {
    state = state.insertCard(this);
  }

  public void enterPin() {
    state = state.enterPin(this);
  }

  public void withdraw() {
    state = state.withdraw(this);
  }

  public AtmState state() {
    return state;
  }

  public int notes() {
    return notes;
  }

  /** Called by HasPin. The data lives here, in the context, not in a state. */
  void dispense() {
    notes--;
  }
}
