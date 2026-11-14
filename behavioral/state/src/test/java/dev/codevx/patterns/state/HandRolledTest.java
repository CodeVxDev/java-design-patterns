package dev.codevx.patterns.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.state.enums.OrderStatus;
import dev.codevx.patterns.state.enums.Shipment;
import dev.codevx.patterns.state.enums.ShipmentStatus;
import dev.codevx.patterns.state.handrolled.Atm;
import dev.codevx.patterns.state.handrolled.HasCard;
import dev.codevx.patterns.state.handrolled.HasPin;
import dev.codevx.patterns.state.handrolled.Idle;
import dev.codevx.patterns.state.handrolled.NoCash;
import dev.codevx.patterns.state.handrolled.NotAllowed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("State, by hand and as an enum")
class HandRolledTest {

  @Test
  @DisplayName("The ATM moves itself: each call returns the next state, and nobody sets it from outside")
  void atmMovesItself() {
    Atm atm = new Atm(2);
    assertThat(atm.state()).isSameAs(Idle.INSTANCE);
    atm.insertCard();
    assertThat(atm.state()).isSameAs(HasCard.INSTANCE);
    atm.enterPin();
    assertThat(atm.state()).isSameAs(HasPin.INSTANCE);
    atm.withdraw();
    assertThat(atm.state()).isSameAs(Idle.INSTANCE); // cash left: back to Idle
  }

  @Test
  @DisplayName("A refused call says why, and the state doesn't change")
  void refusalSaysWhy() {
    Atm atm = new Atm(1);
    assertThatThrownBy(atm::withdraw)
        .isInstanceOf(NotAllowed.class)
        .hasMessage("Idle: insert a card first");
    assertThat(atm.state()).isSameAs(Idle.INSTANCE);
  }

  @Test
  @DisplayName("The new state is one new class: the last withdrawal moves the ATM to NoCash")
  void lastWithdrawalMovesToNoCash() {
    Atm atm = new Atm(1);
    atm.insertCard();
    atm.enterPin();
    atm.withdraw();
    assertThat(atm.state()).isSameAs(NoCash.INSTANCE);
    assertThatThrownBy(atm::insertCard).hasMessage("NoCash: out of cash");
  }

  @Test
  @DisplayName("The if-else version and the State version agree on every step")
  void sameBehaviourAsTheIfElse() {
    var before = new dev.codevx.patterns.state.before.Atm(1);
    Atm after = new Atm(1);
    for (String step : new String[] {"withdraw", "insertCard", "insertCard", "enterPin", "withdraw",
        "insertCard"}) {
      boolean beforeRefused = refused(() -> run(step, before));
      boolean afterRefused = refused(() -> run(step, after));
      assertThat(afterRefused).as(step).isEqualTo(beforeRefused);
      assertThat(after.state().name().toUpperCase())
          .isEqualTo(before.state().name().replace("_", ""));
    }
  }

  @Test
  @DisplayName("As an enum: SHIPPED overrides cancel(), so there's no if-else on the status")
  void enumConstantsCarryBehaviour() {
    assertThat(OrderStatus.PAID.cancel()).isEqualTo(OrderStatus.CANCELLED);
    assertThatThrownBy(OrderStatus.SHIPPED::cancel).isInstanceOf(OrderStatus.TooLate.class);
  }

  @Test
  @DisplayName("As a transition table: the enum says which status may follow which")
  void transitionTable() {
    Shipment shipment = new Shipment();
    shipment.moveTo(ShipmentStatus.PACKED);
    shipment.moveTo(ShipmentStatus.SHIPPED);
    assertThatThrownBy(() -> shipment.moveTo(ShipmentStatus.CANCELLED))
        .hasMessage("SHIPPED can't go to CANCELLED");
  }

  private static void run(String step, Object atm) {
    switch (atm) {
      case Atm a -> {
        switch (step) {
          case "insertCard" -> a.insertCard();
          case "enterPin" -> a.enterPin();
          default -> a.withdraw();
        }
      }
      case dev.codevx.patterns.state.before.Atm a -> {
        switch (step) {
          case "insertCard" -> a.insertCard();
          case "enterPin" -> a.enterPin();
          default -> a.withdraw();
        }
      }
      default -> throw new IllegalArgumentException();
    }
  }

  private static boolean refused(Runnable step) {
    try {
      step.run();
      return false;
    } catch (IllegalStateException e) {
      return true;
    }
  }
}
