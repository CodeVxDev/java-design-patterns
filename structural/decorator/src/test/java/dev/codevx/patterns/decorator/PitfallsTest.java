package dev.codevx.patterns.decorator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.decorator.handrolled.BankGateway;
import dev.codevx.patterns.decorator.handrolled.Logged;
import dev.codevx.patterns.decorator.handrolled.Retried;
import dev.codevx.patterns.decorator.spring.Gateway;
import dev.codevx.patterns.decorator.spring.GatewayConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Numbered as in the README: each pitfall shows the failure first, then the fix. */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Decorator pitfalls")
class PitfallsTest {
  final List<String> lines = new ArrayList<>();

  @Test
  @Order(1)
  @DisplayName("1. Pitfall: with the logger inside the retry, Retried(Logged(bank)), one charge is logged twice")
  void loggerInsideLogsEveryTry() {
    new Retried(new Logged(BankGateway.losingFirstReply(), lines::add)).charge("order-41", 4900);

    assertThat(lines).containsExactly("charge 4900", "charge 4900");
  }

  @Test
  @Order(2)
  @DisplayName("1. Fix: choose the order on purpose; Logged(Retried(bank)) logs each charge once")
  void loggerOutsideLogsEachCharge() {
    new Logged(new Retried(BankGateway.losingFirstReply()), lines::add).charge("order-41", 4900);

    assertThat(lines).containsExactly("charge 4900");
  }

  @Test
  @Order(3)
  @DisplayName("2. Pitfall: a retry without an idempotency key charges the card twice when a reply is lost")
  void retryWithoutKeyChargesTwice() {
    BankGateway bank = BankGateway.losingFirstReply();

    new Retried(bank).charge(null, 4900);

    assertThat(bank.charges()).isEqualTo(2);
  }

  @Test
  @Order(4)
  @DisplayName("2. Fix: one key per payment, sent by every try, and the bank charges once")
  void retryWithKeyChargesOnce() {
    BankGateway bank = BankGateway.losingFirstReply();

    new Retried(bank).charge("order-42", 4900);

    assertThat(bank.charges()).isEqualTo(1);
  }

  @Test
  @Order(5)
  @DisplayName("3. Pitfall: without @Primary, a 'Gateway paymentGateway' parameter fails at startup: two beans match")
  void withoutPrimaryAnUnknownNameFails() {
    assertThatThrownBy(() -> new AnnotationConfigApplicationContext(
            dev.codevx.patterns.decorator.spring.BankGateway.class, NoPrimary.class, ByPaymentGateway.class))
        .isInstanceOf(UnsatisfiedDependencyException.class)
        .hasRootCauseInstanceOf(NoUniqueBeanDefinitionException.class);
  }

  @Test
  @Order(6)
  @DisplayName("3. Pitfall: without @Primary, a 'Gateway bankGateway' parameter silently gets the plain client")
  void withoutPrimaryTheNamePicksThePlainClient() {
    try (var context = new AnnotationConfigApplicationContext(
        dev.codevx.patterns.decorator.spring.BankGateway.class, NoPrimary.class, ByBankGateway.class)) {
      assertThat(context.getBean(ByBankGateway.class).gateway).hasToString("BankGateway");
    }
  }

  @Test
  @Order(7)
  @DisplayName("3. Fix: with @Primary on the stack, every parameter name gets Logged(Retried(BankGateway))")
  void primaryWinsForEveryName() {
    try (var context = new AnnotationConfigApplicationContext(
        dev.codevx.patterns.decorator.spring.BankGateway.class,
        GatewayConfig.class,
        ByPaymentGateway.class,
        ByBankGateway.class)) {
      assertThat(context.getBean(ByPaymentGateway.class).gateway).hasToString("Logged(Retried(BankGateway))");
      assertThat(context.getBean(ByBankGateway.class).gateway).hasToString("Logged(Retried(BankGateway))");
    }
  }

  /** GatewayConfig without @Primary. */
  @Configuration
  static class NoPrimary {
    @Bean
    Gateway gateway(dev.codevx.patterns.decorator.spring.BankGateway bank) {
      return new dev.codevx.patterns.decorator.spring.Logged(new dev.codevx.patterns.decorator.spring.Retried(bank));
    }
  }

  /** Asks for a Gateway under a name that matches neither bean. */
  static class ByPaymentGateway {
    final Gateway gateway;

    ByPaymentGateway(Gateway paymentGateway) {
      this.gateway = paymentGateway;
    }
  }

  /** Asks for a Gateway under the plain client's bean name. */
  static class ByBankGateway {
    final Gateway gateway;

    ByBankGateway(Gateway bankGateway) {
      this.gateway = bankGateway;
    }
  }
}
