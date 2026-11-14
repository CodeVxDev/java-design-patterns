package dev.codevx.patterns.state.before;

// Run without a build: java src/main/java/dev/codevx/patterns/state/before/Demo.java
public class Demo {
  public static void main(String[] args) {
    Atm atm = new Atm(1); // cash for one withdrawal
    call(atm, "withdraw", atm::withdraw);
    call(atm, "insertCard", atm::insertCard);
    call(atm, "enterPin", atm::enterPin);
    call(atm, "withdraw", atm::withdraw);
    call(atm, "insertCard", atm::insertCard);
  }

  static void call(Atm atm, String name, Runnable action) {
    Atm.State before = atm.state();
    try {
      action.run();
      System.out.println(name + "(): " + before + " -> " + atm.state());
    } catch (IllegalStateException refused) {
      System.out.println(name + "(): " + before + " refuses: " + refused.getMessage());
    }
  }
}
