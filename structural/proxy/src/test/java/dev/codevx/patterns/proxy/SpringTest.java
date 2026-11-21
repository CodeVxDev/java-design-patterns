package dev.codevx.patterns.proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.proxy.spring.Order;
import dev.codevx.patterns.proxy.spring.OrderRepository;
import dev.codevx.patterns.proxy.spring.OrderService;
import dev.codevx.patterns.proxy.spring.ProxyApp;
import java.lang.reflect.Proxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ProxyApp.class)
@DisplayName("Proxy, in Spring")
class SpringTest {

  @Autowired OrderService service;
  @Autowired OrderRepository orders;

  @BeforeEach
  void empty() {
    orders.deleteAll();
  }

  @Test
  @DisplayName("The OrderService Spring injects is a generated subclass of it, not OrderService itself")
  void serviceIsCglibSubclass() {
    assertThat(AopUtils.isCglibProxy(service)).isTrue();
    assertThat(service.getClass()).isNotEqualTo(OrderService.class);
    assertThat(service.getClass().getSuperclass()).isEqualTo(OrderService.class);
    assertThat(service.getClass().getSimpleName()).startsWith("OrderService$$SpringCGLIB$$");
  }

  @Test
  @DisplayName("Called from outside, @Transactional save() rolls the order back when the stock call fails")
  void outsideCallRollsBack() {
    assertThatThrownBy(() -> service.save(new Order("lamp"))).hasMessage("out of stock: lamp");

    assertThat(orders.count()).isZero();
  }

  @Test
  @DisplayName("The repository is a JDK dynamic proxy for an interface nobody implemented")
  void repositoryIsJdkProxy() {
    service.save(new Order("book"));

    assertThat(Proxy.isProxyClass(orders.getClass())).isTrue();
    assertThat(orders.findByStatus("PLACED")).extracting(Order::getItem).containsExactly("book");
  }
}
