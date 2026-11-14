package dev.codevx.patterns.state.handrolled;

/** The state the episode adds: one new class. Atm, Idle and HasCard didn't change. */
public final class NoCash implements AtmState {
  public static final NoCash INSTANCE = new NoCash();

  private NoCash() {}

  @Override
  public AtmState insertCard(Atm atm) {
    throw new NotAllowed(this, "out of cash");
  }

  @Override
  public AtmState enterPin(Atm atm) {
    throw new NotAllowed(this, "out of cash");
  }

  @Override
  public AtmState withdraw(Atm atm) {
    throw new NotAllowed(this, "out of cash");
  }
}
