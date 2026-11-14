package dev.codevx.patterns.state.enums;

/** One method checks every move against the table. */
public class Shipment {
  private ShipmentStatus status = ShipmentStatus.CREATED;

  public void moveTo(ShipmentStatus next) {
    if (!status.canMoveTo(next)) {
      throw new IllegalStateException(status + " can't go to " + next);
    }
    status = next;
  }

  public ShipmentStatus status() {
    return status;
  }
}
