package dev.codevx.patterns.observer.before;

/** The teams the order calls directly. Each one is a dependency of Orders. */
final class Teams {
  private Teams() {}

  static class Email {
    void send(Order order) {
      System.out.println("  email for " + order.id());
    }
  }

  static class Inventory {
    void reserve(Order order) {
      System.out.println("  stock reserved for " + order.id());
    }
  }

  static class Analytics {
    void track(Order order) {
      System.out.println("  tracked " + order.id());
    }
  }

  static class Loyalty {
    void addPoints(Order order) {
      System.out.println("  points for " + order.id());
    }
  }
}
