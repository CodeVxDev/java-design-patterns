package dev.codevx.patterns.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import dev.codevx.patterns.strategy.handrolled.Card;
import dev.codevx.patterns.strategy.handrolled.Checkout;
import dev.codevx.patterns.strategy.handrolled.PayLater;
import dev.codevx.patterns.strategy.handrolled.PayPal;
import dev.codevx.patterns.strategy.jdk.SortDemo;
import dev.codevx.patterns.strategy.jdk.SortDemo.Item;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Strategy, by hand")
class HandRolledTest {

  @Test
  @DisplayName("Checkout runs whichever strategy the caller hands it")
  void callerPicksTheStrategy() {
    Checkout checkout = new Checkout(new Card());
    assertThat(checkout.pay(4900)).isEqualTo("Card pays 4900");

    checkout.use(new PayPal());
    assertThat(checkout.pay(4900)).isEqualTo("PayPal pays 4900");
  }

  @Test
  @DisplayName("A new payment type is one new class, and Checkout doesn't change")
  void newTypeIsOneNewClass() {
    assertThat(new Checkout(new PayLater()).pay(4900)).isEqualTo("PayLater pays 4900");
  }

  @Test
  @DisplayName("A strategy interface with one method takes a lambda")
  void lambdaIsAStrategy() {
    assertThat(new Checkout(amount -> "UPI pays " + amount).pay(4900)).isEqualTo("UPI pays 4900");
  }

  @Test
  @DisplayName("Comparator is the JDK's Strategy: the same sort with a different rule")
  void comparatorIsAStrategy() {
    List<Item> items = List.of(
        new Item("mouse", 2500, 4.1), new Item("keyboard", 4900, 4.7), new Item("cable", 900, 3.9));
    assertThat(SortDemo.sortedNames(items, SortDemo.BY_PRICE))
        .containsExactly("cable", "mouse", "keyboard");
    assertThat(SortDemo.sortedNames(items, SortDemo.BY_RATING))
        .containsExactly("keyboard", "mouse", "cable");
  }
}
