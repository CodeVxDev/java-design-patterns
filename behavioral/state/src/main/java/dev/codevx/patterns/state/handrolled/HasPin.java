package dev.codevx.patterns.state.handrolled;

public final class HasPin implements AtmState {
  public static final HasPin INSTANCE = new HasPin();

  private HasPin() {}

  @Override
  public AtmState insertCard(Atm atm) {
    throw new NotAllowed(this, "finish first");
  }

  @Override
  public AtmState enterPin(Atm atm) {
    throw new NotAllowed(this, "PIN already entered");
  }

  /** The one existing class that knows NoCash exists: somebody has to send the ATM there. */
  @Override
  public AtmState withdraw(Atm atm) {
    atm.dispense();
    return atm.notes() > 0 ? Idle.INSTANCE : NoCash.INSTANCE;
  }
}
