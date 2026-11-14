package dev.codevx.patterns.observer.handrolled;

public class Order {
  private final String id;
  private boolean paid;

  public Order(String id) {
    this.id = id;
  }

  public String id() {
    return id;
  }

  public boolean paid() {
    return paid;
  }

  public void markPaid() {
    paid = true;
  }
}
