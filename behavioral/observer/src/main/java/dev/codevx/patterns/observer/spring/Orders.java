package dev.codevx.patterns.observer.spring;

import java.util.NoSuchElementException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The subject, Spring style: it publishes an event and keeps no list. Spring finds every listener.
 *
 * <p>The constructor is the one Lombok's {@code @RequiredArgsConstructor} would generate, written out.
 */
@Service
public class Orders {
  private final JdbcClient jdbc;
  private final ApplicationEventPublisher pub;

  public Orders(JdbcClient jdbc, ApplicationEventPublisher pub) {
    this.jdbc = jdbc;
    this.pub = pub;
  }

  @Transactional
  public void create(String id) {
    jdbc.sql("insert into orders (id, paid) values (?, false)").param(id).update();
  }

  /**
   * Plain {@code @EventListener}s run inside publishEvent: same thread, same transaction, before the
   * commit. {@code @TransactionalEventListener}s run after the commit, and not at all on a rollback.
   */
  @Transactional
  public void pay(String id) {
    int rows = jdbc.sql("update orders set paid = true where id = ?").param(id).update();
    if (rows == 0) {
      throw new NoSuchElementException("no order " + id);
    }
    pub.publishEvent(new OrderPaid(id));
  }

  public boolean isPaid(String id) {
    return jdbc.sql("select paid from orders where id = ?").param(id).query(Boolean.class).single();
  }
}
