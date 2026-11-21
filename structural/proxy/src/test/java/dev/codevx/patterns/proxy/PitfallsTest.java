package dev.codevx.patterns.proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.proxy.spring.FixedOrderService;
import dev.codevx.patterns.proxy.spring.Order;
import dev.codevx.patterns.proxy.spring.OrderRepository;
import dev.codevx.patterns.proxy.spring.OrderService;
import dev.codevx.patterns.proxy.spring.ProxyApp;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/** Numbered as in the README: each pitfall shows the failure first, then the fix. */
@SpringBootTest(classes = ProxyApp.class)
@Import({PitfallsTest.Reports.class, PitfallsTest.Ledger.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Proxy pitfalls, in Spring")
class PitfallsTest {

  @Autowired OrderService service;
  @Autowired FixedOrderService fixed;
  @Autowired OrderRepository orders;
  @Autowired Reports reports;
  @Autowired Ledger ledger;

  @BeforeEach
  void empty() {
    orders.deleteAll();
  }

  @Test
  @org.junit.jupiter.api.Order(1)
  @DisplayName("1. Pitfall: placeOrder() calls @Transactional save() from inside, so half the order stays saved")
  void selfCallSkipsTransaction() {
    assertThatThrownBy(() -> service.placeOrder(new Order("lamp"))).hasMessage("out of stock: lamp");

    assertThat(orders.findByStatus("PLACED")).extracting(Order::getItem).containsExactly("lamp");
  }

  @Test
  @org.junit.jupiter.api.Order(2)
  @DisplayName("1. Fix: save() moves to another bean, the call crosses its proxy, and the order is rolled back")
  void otherBeanRollsBack() {
    assertThatThrownBy(() -> fixed.placeOrder(new Order("lamp"))).hasMessage("out of stock: lamp");

    assertThat(orders.count()).isZero();
  }

  @Test
  @org.junit.jupiter.api.Order(3)
  @DisplayName("2. Pitfall: an @Async method called from inside runs on the caller's thread")
  void selfCallSkipsAsync() {
    String caller = Thread.currentThread().getName();

    assertThat(reports.buildFromInside().join()).isEqualTo(caller);
  }

  @Test
  @org.junit.jupiter.api.Order(4)
  @DisplayName("2. Fix: called through the proxy, the same @Async method runs on another thread")
  void outsideCallIsAsync() {
    String caller = Thread.currentThread().getName();

    assertThat(reports.build().join()).isNotEqualTo(caller);
  }

  @Test
  @org.junit.jupiter.api.Order(5)
  @DisplayName("3. Pitfall: a final @Transactional method can't be overridden, so it runs with no transaction")
  void finalMethodSkipsProxy() {
    assertThat(ledger.inTransaction()).isTrue();
    assertThat(ledger.finalInTransaction()).isFalse();
  }

  /** Test-only bean: the same trap with @Async. */
  @EnableAsync
  static class Reports {
    @Async
    public CompletableFuture<String> build() {
      return CompletableFuture.completedFuture(Thread.currentThread().getName());
    }

    public CompletableFuture<String> buildFromInside() {
      return build();
    }
  }

  /** Test-only bean: the CGLIB subclass overrides inTransaction() but can't override a final method. */
  static class Ledger {
    @Transactional
    public boolean inTransaction() {
      return TransactionSynchronizationManager.isActualTransactionActive();
    }

    @Transactional
    public final boolean finalInTransaction() {
      return TransactionSynchronizationManager.isActualTransactionActive();
    }
  }
}
