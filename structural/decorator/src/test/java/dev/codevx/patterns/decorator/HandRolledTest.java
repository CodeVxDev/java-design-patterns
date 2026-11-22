package dev.codevx.patterns.decorator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.decorator.handrolled.BankGateway;
import dev.codevx.patterns.decorator.handrolled.Gateway;
import dev.codevx.patterns.decorator.handrolled.Logged;
import dev.codevx.patterns.decorator.handrolled.Receipt;
import dev.codevx.patterns.decorator.handrolled.Retried;
import dev.codevx.patterns.decorator.handrolled.Timeout;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Decorator, by hand")
class HandRolledTest {
  final List<String> lines = new ArrayList<>();

  @Test
  @DisplayName("Logged adds its log line and always calls through: the bank does the charge")
  void loggedCallsThrough() {
    BankGateway bank = new BankGateway();

    Receipt receipt = new Logged(bank, lines::add).charge("order-1", 4900);

    assertThat(receipt).isEqualTo(new Receipt(4900));
    assertThat(lines).containsExactly("charge 4900");
    assertThat(bank.charges()).isEqualTo(1);
  }

  @Test
  @DisplayName("Retried hides a timeout from the caller: the second try returns the receipt")
  void retriedHidesTheTimeout() {
    BankGateway bank = BankGateway.losingFirstReply();

    assertThatThrownBy(() -> BankGateway.losingFirstReply().charge("order-2", 4900)).isInstanceOf(Timeout.class);
    assertThat(new Retried(bank).charge("order-2", 4900)).isEqualTo(new Receipt(4900));
  }

  @Test
  @DisplayName("The extras stack at runtime in one line, and the caller only ever holds a Gateway")
  void stackedInOneLine() {
    BankGateway bank = BankGateway.losingFirstReply();
    Gateway gateway =
        new Logged(
            new Retried(
                bank), lines::add);

    assertThat(gateway.charge("order-3", 4900)).isEqualTo(new Receipt(4900));
    assertThat(gateway).hasToString("Logged(Retried(BankGateway))");
    assertThat(lines).containsExactly("charge 4900");
    assertThat(bank.charges()).isEqualTo(1);
  }
}
