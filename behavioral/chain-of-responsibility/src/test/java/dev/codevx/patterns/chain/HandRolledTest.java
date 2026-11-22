package dev.codevx.patterns.chain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.chain.handrolled.Check;
import dev.codevx.patterns.chain.handrolled.Checkout;
import dev.codevx.patterns.chain.handrolled.CouponCheck;
import dev.codevx.patterns.chain.handrolled.FraudCheck;
import dev.codevx.patterns.chain.handrolled.LimitCheck;
import dev.codevx.patterns.chain.handrolled.Order;
import dev.codevx.patterns.chain.handrolled.StockCheck;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Chain of Responsibility, by hand")
class HandRolledTest {
  static final Order GOOD = new Order("good", false, true, true, 49_00);
  static final Order RISKY = new Order("risky", true, true, true, 49_00);
  static final Order HUGE = new Order("huge", false, true, true, 900_00);

  final List<String> reached = new ArrayList<>();

  /** Wraps a check to record that the order reached it. */
  Check seen(Check check) {
    return (o, next) -> {
      reached.add(check.getClass().getSimpleName());
      check.check(o, next);
    };
  }

  @Test
  @DisplayName("An order that passes every check reaches the end of the chain")
  void goodOrderPasses() {
    Checkout checkout = new Checkout(List.of(seen(new FraudCheck()), seen(new StockCheck()), seen(new CouponCheck())));

    assertThatCode(() -> checkout.validate(GOOD)).doesNotThrowAnyException();
    assertThat(reached).containsExactly("FraudCheck", "StockCheck", "CouponCheck");
  }

  @Test
  @DisplayName("A risky order is stopped by the fraud check, and the checks after it are never reached")
  void fraudStopsTheRest() {
    Checkout checkout = new Checkout(List.of(seen(new FraudCheck()), seen(new StockCheck()), seen(new CouponCheck())));

    assertThatThrownBy(() -> checkout.validate(RISKY)).hasMessage("fraud");
    assertThat(reached).containsExactly("FraudCheck");
  }

  @Test
  @DisplayName("The spending limit is one more class in the list; no check and not Checkout is edited")
  void addingACheckIsOneClass() {
    Checkout without = new Checkout(List.of(new FraudCheck(), new StockCheck(), new CouponCheck()));
    Checkout with = new Checkout(List.of(new FraudCheck(), new StockCheck(), new CouponCheck(), new LimitCheck()));

    assertThatCode(() -> without.validate(HUGE)).doesNotThrowAnyException();
    assertThatThrownBy(() -> with.validate(HUGE)).hasMessage("limit");
  }

  @Test
  @DisplayName("Reordering the list reorders the checks: with the limit first, a huge risky order stops on the limit")
  void reorderingIsTheList() {
    Order hugeAndRisky = new Order("both", true, true, true, 900_00);

    assertThatThrownBy(() -> new Checkout(List.of(new FraudCheck(), new LimitCheck())).validate(hugeAndRisky))
        .hasMessage("fraud");
    assertThatThrownBy(() -> new Checkout(List.of(new LimitCheck(), new FraudCheck())).validate(hugeAndRisky))
        .hasMessage("limit");
  }
}
