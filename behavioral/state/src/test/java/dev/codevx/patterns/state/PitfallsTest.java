package dev.codevx.patterns.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.state.handrolled.Atm;
import dev.codevx.patterns.state.handrolled.HasPin;
import dev.codevx.patterns.state.handrolled.Idle;
import dev.codevx.patterns.state.handrolled.NoCash;
import dev.codevx.patterns.state.handrolled.NotAllowed;
import dev.codevx.patterns.state.spring.OrderState;
import dev.codevx.patterns.state.spring.OrderStates;
import dev.codevx.patterns.state.spring.Status;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/** Numbered as in the README: each pitfall shows the failure first, then the fix. */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("State pitfalls")
class PitfallsTest {

  @Test
  @Order(1)
  @DisplayName("1. Pitfall: a state that returns `this` for a forbidden call hides the refusal from the caller")
  void silentRefusal() {
    interface Light {
      Light press();
    }
    Light off = new Light() {
      public Light press() {
        return this; // "not allowed", said by doing nothing
      }
    };
    // The caller gets the same state back with no reason: was it refused, or did it work with no change?
    assertThat(off.press()).isSameAs(off);
  }

  @Test
  @Order(2)
  @DisplayName("1. Fix: refuse out loud, with the reason, and leave the state as it was")
  void refuseOutLoud() {
    Atm atm = new Atm(1);
    assertThatThrownBy(atm::enterPin)
        .isInstanceOf(NotAllowed.class)
        .hasMessage("Idle: insert a card first");
    assertThat(atm.state()).isSameAs(Idle.INSTANCE);
  }

  @Test
  @Order(3)
  @DisplayName("2. Pitfall: a shared state instance that keeps data shares it with every object using it")
  void sharedStateWithData() {
    class HasCardCountingPins { // a state that keeps per-ATM data in a field
      int wrongPins;

      void wrongPin() {
        wrongPins++;
      }

      boolean blocked() {
        return wrongPins >= 3;
      }
    }
    HasCardCountingPins shared = new HasCardCountingPins(); // one instance, like HasCard.INSTANCE
    Map<String, HasCardCountingPins> stateOfAtm = Map.of("A", shared, "B", shared);

    for (int i = 0; i < 3; i++) {
      stateOfAtm.get("A").wrongPin(); // three wrong PINs at ATM A...
    }
    assertThat(stateOfAtm.get("B").blocked()).isTrue(); // ...and ATM B blocks a customer who typed nothing
  }

  @Test
  @Order(4)
  @DisplayName("2. Fix: keep the data in the context, so the states hold nothing and one instance serves all")
  void dataInTheContext() {
    Atm a = new Atm(1);
    Atm b = new Atm(2);
    for (Atm atm : List.of(a, b)) {
      atm.insertCard();
      atm.enterPin();
      assertThat(atm.state()).isSameAs(HasPin.INSTANCE); // the same instance for both ATMs
      atm.withdraw();
    }
    assertThat(a.state()).isSameAs(NoCash.INSTANCE);
    assertThat(b.state()).isSameAs(Idle.INSTANCE);
  }

  enum StatusV1 { CREATED, PAID, SHIPPED }

  enum StatusV2 { CREATED, PAYMENT_PENDING, PAID, SHIPPED } // a status added in the middle

  @Test
  @Order(5)
  @DisplayName("3. Pitfall: storing the ordinal, then adding a status in the middle, changes what old rows mean")
  void ordinalShifts() {
    int storedLastYear = StatusV1.PAID.ordinal(); // JPA's default @Enumerated(ORDINAL) stores 1
    assertThat(StatusV2.values()[storedLastYear]).isEqualTo(StatusV2.PAYMENT_PENDING);
  }

  @Test
  @Order(6)
  @DisplayName("3. Fix: store the name (@Enumerated(EnumType.STRING)); a new status doesn't move the old ones")
  void nameIsStable() {
    String storedLastYear = StatusV1.PAID.name();
    assertThat(StatusV2.valueOf(storedLastYear)).isEqualTo(StatusV2.PAID);
  }

  @Test
  @Order(7)
  @DisplayName("4. Pitfall: a status with no state bean fails on the first order that reaches it, in production")
  void missingBeanFailsLate() {
    Map<Status, OrderState> byStatus = new EnumMap<>(Status.class);
    byStatus.put(Status.CREATED, () -> Status.CREATED); // SHIPPED was added, its bean wasn't
    assertThatThrownBy(() -> byStatus.get(Status.SHIPPED).cancel(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @Order(8)
  @DisplayName("4. Fix: check at startup that every status has exactly one bean")
  void missingBeanFailsAtStartup() {
    List<OrderState> beans = List.of(() -> Status.CREATED, () -> Status.PAID, () -> Status.CANCELLED);
    assertThatThrownBy(() -> new OrderStates(beans))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("no OrderState bean for [SHIPPED]");
  }
}
