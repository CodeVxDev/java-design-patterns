package dev.codevx.patterns.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.strategy.spring.Checkout;
import dev.codevx.patterns.strategy.spring.StrategyApp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = StrategyApp.class)
@DisplayName("Strategy, with Spring")
class SpringTest {

  @Autowired Checkout checkout;

  @Test
  @DisplayName("Spring fills Map<String, PaymentStrategy> with every strategy bean, keyed by bean name")
  void mapHoldsEveryBean() {
    assertThat(checkout.types()).containsExactlyInAnyOrder("card", "payPal", "wallet", "payLater");
  }

  @Test
  @DisplayName("PayLater is reachable by its bean name, with no change to Checkout")
  void newBeanJoinsTheMap() {
    assertThat(checkout.pay("payLater", 4900)).isEqualTo("PayLater pays 4900");
  }

  @Test
  @DisplayName("An unknown type fails with a message that names it")
  void unknownTypeFailsClearly() {
    assertThatThrownBy(() -> checkout.pay("upi", 4900))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("upi");
  }
}
