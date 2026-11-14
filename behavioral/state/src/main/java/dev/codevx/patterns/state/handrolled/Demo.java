package dev.codevx.patterns.state.handrolled;

// Run without a build: java src/main/java/dev/codevx/patterns/state/handrolled/Demo.java
public class Demo {
  public static void main(String[] args) {
    Atm atm = new Atm(1); // cash for one withdrawal
    call(atm, "withdraw", atm::withdraw);
    call(atm, "insertCard", atm::insertCard);
    call(atm, "enterPin", atm::enterPin);
    call(atm, "withdraw", atm::withdraw); // the last notes leave
    call(atm, "insertCard", atm::insertCard);
  }

  static void call(Atm atm, String name, Runnable action) {
    String before = atm.state().name();
    try {
      action.run();
      System.out.println(name + "(): " + before + " -> " + atm.state().name());
    } catch (NotAllowed refused) {
      System.out.println(name + "(): refused, " + refused.getMessage());
    }
  }
}
