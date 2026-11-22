package dev.codevx.patterns.decorator;

import static org.assertj.core.api.Assertions.assertThat;

import dev.codevx.patterns.decorator.spring.BankGateway;
import dev.codevx.patterns.decorator.spring.Checkout;
import dev.codevx.patterns.decorator.spring.DecoratorApp;
import dev.codevx.patterns.decorator.spring.Gateway;
import dev.codevx.patterns.decorator.spring.Receipt;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = DecoratorApp.class)
@DisplayName("Decorator, in Spring")
class SpringTest {

  @Autowired Checkout checkout;
  @Autowired BankGateway bank;
  @Autowired ApplicationContext context;

  @Test
  @DisplayName("Checkout gets the stack built in the config, Logged(Retried(BankGateway)), not the plain client")
  void checkoutGetsTheStack() {
    assertThat(checkout.gateway()).hasToString("Logged(Retried(BankGateway))");
  }

  @Test
  @DisplayName("There are two Gateway beans, the plain client and the stack, which is why one is @Primary")
  void twoGatewayBeans() {
    Map<String, Gateway> gateways = context.getBeansOfType(Gateway.class);

    assertThat(gateways).containsOnlyKeys("bankGateway", "gateway");
  }

  @Test
  @DisplayName("A payment whose first reply is lost is retried with the same key, and the card is charged once")
  void retriedOnceChargedOnce() {
    Receipt receipt = checkout.pay("order-7", 4900);

    assertThat(receipt).isEqualTo(new Receipt("order-7", 4900));
    assertThat(bank.charges("order-7")).isEqualTo(1);
  }
}
