package dev.codevx.patterns.state.handrolled;

public final class HasCard implements AtmState {
  public static final HasCard INSTANCE = new HasCard();

  private HasCard() {}

  @Override
  public AtmState insertCard(Atm atm) {
    throw new NotAllowed(this, "card already in");
  }

  @Override
  public AtmState enterPin(Atm atm) {
    return HasPin.INSTANCE;
  }

  @Override
  public AtmState withdraw(Atm atm) {
    throw new NotAllowed(this, "enter your PIN first");
  }
}
