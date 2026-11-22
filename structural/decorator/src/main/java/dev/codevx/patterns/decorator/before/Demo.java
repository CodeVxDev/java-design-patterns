package dev.codevx.patterns.decorator.before;

// Run without a build: java src/main/java/dev/codevx/patterns/decorator/before/Demo.java
public class Demo {
  public static void main(String[] args) {
    System.out.println("LoggingGateway:");
    attempt(new LoggingGateway());
    System.out.println("RetryingGateway:");
    attempt(new RetryingGateway());
    System.out.println("LoggingRetryingGateway:");
    attempt(new LoggingRetryingGateway());
    System.out.println("Three classes for two extras. Add metrics and it's seven; switching retries off for one");
    System.out.println("caller means yet another class, because each combination is fixed when you compile.");
  }

  static void attempt(Gateway gateway) {
    try {
      System.out.println("  " + gateway.charge(4900));
    } catch (Timeout e) {
      System.out.println("  failed: timeout");
    }
  }
}
