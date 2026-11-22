package dev.codevx.patterns.decorator.handrolled;

// Run without a build: java src/main/java/dev/codevx/patterns/decorator/handrolled/Demo.java
public class Demo {
  public static void main(String[] args) {
    BankGateway bank = BankGateway.losingFirstReply();
    Gateway gateway =
        new Logged(
            new Retried(
                bank));
    System.out.println(gateway + ", the bank's first reply is lost:");
    System.out.println("  " + gateway.charge("order-41", 4900) + ", charged " + times(bank));

    BankGateway bank2 = BankGateway.losingFirstReply();
    Gateway swapped = new Retried(new Logged(bank2));
    System.out.println(swapped + ", the same three objects, the logger inside the retry:");
    System.out.println("  " + swapped.charge("order-42", 4900) + ", charged " + times(bank2));

    BankGateway bank3 = BankGateway.losingFirstReply();
    System.out.println("Retried(BankGateway) with no idempotency key:");
    System.out.println("  " + new Retried(bank3).charge(null, 4900) + ", charged " + times(bank3));
  }

  static String times(BankGateway bank) {
    return bank.charges() == 1 ? "once" : bank.charges() + " times";
  }
}
