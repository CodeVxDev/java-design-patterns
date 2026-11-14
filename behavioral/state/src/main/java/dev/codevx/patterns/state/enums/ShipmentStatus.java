package dev.codevx.patterns.state.enums;

/**
 * The lighter variant: a transition table. The enum only says which status may follow which. It suits a
 * status that gates moves; when each state also behaves differently, give the behaviour to the states.
 */
public enum ShipmentStatus {
  CREATED, PACKED, SHIPPED, DELIVERED, CANCELLED;

  public boolean canMoveTo(ShipmentStatus next) {
    return switch (this) {
      case CREATED -> next == PACKED || next == CANCELLED;
      case PACKED -> next == SHIPPED || next == CANCELLED;
      case SHIPPED -> next == DELIVERED;
      case DELIVERED, CANCELLED -> false;
    };
  }
}
