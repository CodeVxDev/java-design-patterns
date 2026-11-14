package dev.codevx.patterns.observer;

import static org.assertj.core.api.Assertions.assertThat;

import dev.codevx.patterns.observer.spring.Activity;
import dev.codevx.patterns.observer.spring.ObserverApp;
import dev.codevx.patterns.observer.spring.Orders;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

@SpringBootTest(classes = ObserverApp.class)
@DisplayName("Observer, with Spring events")
class SpringTest {

  @Autowired Orders orders;
  @Autowired Activity activity;
  @Autowired JdbcClient jdbc;

  @Test
  @DisplayName("publishEvent reaches every listener of OrderPaid; Orders keeps no list")
  void everyListenerHears() throws InterruptedException {
    orders.create("s-1");
    orders.pay("s-1");

    assertThat(orders.isPaid("s-1")).isTrue();
    assertThat(activity.find("stock s-1")).isPresent();
    assertThat(activity.find("points s-1")).isPresent();
    assertThat(activity.await("email s-1", Duration.ofSeconds(5))).isPresent();
  }

  @Test
  @DisplayName("A plain @EventListener runs on the caller's thread")
  void plainListenerSameThread() {
    orders.create("s-2");
    orders.pay("s-2");

    assertThat(activity.find("stock s-2").orElseThrow().thread())
        .isEqualTo(Thread.currentThread().getName());
  }

  @Test
  @DisplayName("@Async @TransactionalEventListener runs on another thread, after the commit")
  void asyncListenerOtherThread() throws InterruptedException {
    orders.create("s-3");
    orders.pay("s-3");

    assertThat(activity.await("email s-3", Duration.ofSeconds(5)).orElseThrow().thread())
        .isNotEqualTo(Thread.currentThread().getName());
  }

  @Test
  @DisplayName("A listener that writes after the commit does it in its own transaction (REQUIRES_NEW)")
  void afterCommitWriteIsSaved() {
    orders.create("s-4");
    orders.pay("s-4");

    assertThat(jdbc.sql("select count(*) from points where order_id = ?").param("s-4")
        .query(Integer.class).single()).isEqualTo(1);
  }
}
