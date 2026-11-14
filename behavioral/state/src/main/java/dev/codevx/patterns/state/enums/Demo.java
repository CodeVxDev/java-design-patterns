package dev.codevx.patterns.state.enums;

// Run without a build: java src/main/java/dev/codevx/patterns/state/enums/Demo.java
public class Demo {
  public static void main(String[] args) {
    for (OrderStatus status : OrderStatus.values()) {
      try {
        System.out.println(status + ".cancel() -> " + status.cancel());
      } catch (OrderStatus.TooLate e) {
        System.out.println(status + ".cancel() throws TooLate");
      }
    }

    Shipment shipment = new Shipment();
    shipment.moveTo(ShipmentStatus.PACKED);
    shipment.moveTo(ShipmentStatus.SHIPPED);
    System.out.println("shipment is " + shipment.status());
    try {
      shipment.moveTo(ShipmentStatus.CANCELLED);
    } catch (IllegalStateException e) {
      System.out.println("refused: " + e.getMessage());
    }
  }
}
