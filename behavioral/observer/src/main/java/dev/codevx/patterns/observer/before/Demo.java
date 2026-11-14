package dev.codevx.patterns.observer.before;

// Run without a build: java src/main/java/dev/codevx/patterns/observer/before/Demo.java
public class Demo {
  public static void main(String[] args) {
    System.out.println("pay() calls four teams:");
    new Orders().pay(new Order("order-42"));
  }
}
