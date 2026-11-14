package dev.codevx.patterns.state.before;

/**
 * Where the episode starts: one status field, and every method checks it with the same if-else. Adding
 * NO_CASH meant a new branch in every method, and a missed branch is a bug nobody sees until it runs.
 */
public class Atm {
  public enum State { IDLE, HAS_CARD, HAS_PIN, NO_CASH }

  private State state = State.IDLE;
  private int notes;

  public Atm(int notes) {
    this.notes = notes;
    if (notes == 0) {
      state = State.NO_CASH;
    }
  }

  public State state() {
    return state;
  }

  public void insertCard() {
    if (state == State.IDLE) {
      state = State.HAS_CARD;
    } else if (state == State.HAS_CARD) {
      throw new IllegalStateException("card already in");
    } else if (state == State.HAS_PIN) {
      throw new IllegalStateException("finish first");
    } else if (state == State.NO_CASH) {
      throw new IllegalStateException("out of cash");
    }
  }

  public void enterPin() {
    if (state == State.IDLE) {
      throw new IllegalStateException("insert a card first");
    } else if (state == State.HAS_CARD) {
      state = State.HAS_PIN;
    } else if (state == State.HAS_PIN) {
      throw new IllegalStateException("PIN already entered");
    } else if (state == State.NO_CASH) {
      throw new IllegalStateException("out of cash");
    }
  }

  public void withdraw() {
    if (state == State.IDLE) {
      throw new IllegalStateException("insert a card first");
    } else if (state == State.HAS_CARD) {
      throw new IllegalStateException("enter your PIN first");
    } else if (state == State.HAS_PIN) {
      notes--;
      state = notes > 0 ? State.IDLE : State.NO_CASH;
    } else if (state == State.NO_CASH) {
      throw new IllegalStateException("out of cash");
    }
  }
}
