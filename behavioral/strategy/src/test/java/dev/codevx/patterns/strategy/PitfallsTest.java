package dev.codevx.patterns.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.strategy.spring.Checkout;
import dev.codevx.patterns.strategy.spring.PayLater;
import dev.codevx.patterns.strategy.spring.PaymentStrategy;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/** Numbered as in the README: each pitfall shows the failure first, then the fix. */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Strategy pitfalls")
class PitfallsTest {

  @Test
  @Order(1)
  @DisplayName("1. Pitfall: map.get(type) returns null for an unknown type, and the NPE says nothing about it")
  void rawLookupThrowsNpe() {
    Map<String, PaymentStrategy> strategies = Map.of("payLater", new PayLater());
    assertThatThrownBy(() -> strategies.get("upi").pay(4900))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @Order(2)
  @DisplayName("1. Fix: check the lookup and fail with the unknown type and the known ones")
  void guardedLookupNamesTheType() {
    Checkout checkout = new Checkout(Map.of("payLater", new PayLater()));
    assertThatThrownBy(() -> checkout.pay("upi", 4900))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unsupported payment type: upi, expected one of [payLater]");
  }

  @Test
  @Order(3)
  @DisplayName("2. Pitfall: the key is the bean name, so renaming the class breaks callers sending \"payLater\"")
  void renamingTheClassChangesTheKey() {
    try (var context = new AnnotationConfigApplicationContext(BuyNowPayLater.class)) {
      Map<String, PaymentStrategy> beans = context.getBeansOfType(PaymentStrategy.class);
      assertThat(beans).containsOnlyKeys("buyNowPayLater");
      assertThatThrownBy(() -> new Checkout(beans).pay("payLater", 4900))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Test
  @Order(4)
  @DisplayName("2. Fix: name the bean explicitly when the key comes from a request or a database row")
  void explicitNameSurvivesARename() {
    try (var context = new AnnotationConfigApplicationContext(RenamedWithStableKey.class)) {
      Map<String, PaymentStrategy> beans = context.getBeansOfType(PaymentStrategy.class);
      assertThat(new Checkout(beans).pay("pay-later", 4900)).isEqualTo("BNPL pays 4900");
    }
  }
}

/** PayLater after someone renamed the class: its default bean name is now "buyNowPayLater". */
@Component
class BuyNowPayLater implements PaymentStrategy {
  @Override
  public String pay(long amount) {
    return "BNPL pays " + amount;
  }
}

/** The same rename with an explicit bean name: the key stays "pay-later". */
@Component("pay-later")
class RenamedWithStableKey implements PaymentStrategy {
  @Override
  public String pay(long amount) {
    return "BNPL pays " + amount;
  }
}
