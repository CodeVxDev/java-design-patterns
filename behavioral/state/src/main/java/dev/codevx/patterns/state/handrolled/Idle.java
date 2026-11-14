package dev.codevx.patterns.state.handrolled;

public final class Idle implements AtmState {
  /** States hold no data, so one shared instance is enough. */
  public static final Idle INSTANCE = new Idle();

  private Idle() {}

  @Override
  public AtmState insertCard(Atm atm) {
    return HasCard.INSTANCE;
  }

  @Override
  public AtmState enterPin(Atm atm) {
    throw new NotAllowed(this, "insert a card first");
  }

  @Override
  public AtmState withdraw(Atm atm) {
    throw new NotAllowed(this, "insert a card first");
  }
}
