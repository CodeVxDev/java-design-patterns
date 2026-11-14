package dev.codevx.patterns.state.spring;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/** In memory, to keep the demo free of a database. With JPA: {@code @Enumerated(EnumType.STRING)}. */
@Repository
public class OrderRepository {
  private final Map<String, Status> rows = new ConcurrentHashMap<>();

  public Order find(String id) {
    Status status = rows.get(id);
    if (status == null) {
      throw new NoSuchElementException("no order " + id);
    }
    return new Order(id, status);
  }

  public void save(Order order) {
    rows.put(order.id(), order.status());
  }
}
