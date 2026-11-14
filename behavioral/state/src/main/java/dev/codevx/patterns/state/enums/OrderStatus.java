package dev.codevx.patterns.state.enums;

/**
 * State packed into an enum: each constant is a state, and a constant with its own rule overrides the
 * method. SHIPPED refuses cancel(), and there is no if-else on the status anywhere.
 */
public enum OrderStatus {
  CREATED,
  PAID,
  CANCELLED,
  SHIPPED {
    @Override
    public OrderStatus cancel() {
      throw new TooLate();
    }
  };

  public OrderStatus cancel() {
    return CANCELLED;
  }

  public static class TooLate extends IllegalStateException {
    public TooLate() {
      super("already shipped, too late to cancel");
    }
  }
}
