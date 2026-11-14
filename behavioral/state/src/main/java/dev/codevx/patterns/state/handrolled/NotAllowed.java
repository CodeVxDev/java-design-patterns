package dev.codevx.patterns.state.handrolled;

/** Thrown by a state for a call it doesn't allow, with the reason a user should see. */
public class NotAllowed extends IllegalStateException {
  public NotAllowed(AtmState state, String reason) {
    super(state.name() + ": " + reason);
  }
}
