package dev.codevx.patterns.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.state.spring.OrderRepository;
import dev.codevx.patterns.state.spring.OrderService;
import dev.codevx.patterns.state.spring.StateApp;
import dev.codevx.patterns.state.spring.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = StateApp.class)
@DisplayName("State, with Spring: the status is stored, its behaviour is a bean")
class SpringTest {

  @Autowired OrderService service;
  @Autowired OrderRepository orders;

  @Test
  @DisplayName("Each action goes to the bean for the stored status, which returns the next status")
  void statusMovesThroughItsBeans() {
    service.create("o-1");
    assertThat(service.pay("o-1").status()).isEqualTo(Status.PAID);
    assertThat(service.ship("o-1").status()).isEqualTo(Status.SHIPPED);
    assertThat(orders.find("o-1").status()).isEqualTo(Status.SHIPPED);
  }

  @Test
  @DisplayName("A shipped order refuses cancel(), and the stored status stays SHIPPED")
  void shippedRefusesCancel() {
    service.create("o-2");
    service.pay("o-2");
    service.ship("o-2");
    assertThatThrownBy(() -> service.cancel("o-2"))
        .hasMessage("can't cancel an order that is SHIPPED");
    assertThat(orders.find("o-2").status()).isEqualTo(Status.SHIPPED);
  }

  @Test
  @DisplayName("The same call does different things in different states: cancel() works until it ships")
  void sameCallDifferentState() {
    service.create("o-3");
    service.pay("o-3");
    assertThat(service.cancel("o-3").status()).isEqualTo(Status.CANCELLED);
  }
}
