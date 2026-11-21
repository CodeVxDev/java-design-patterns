package dev.codevx.patterns.proxy.spring;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An interface nobody implements. Spring Data hands out a JDK dynamic proxy for it: save() and findById() go to
 * its own SimpleJpaRepository, and findByStatus() is answered by a query the proxy builds from the method's name.
 */
public interface OrderRepository extends
    JpaRepository<Order, Long> {
  List<Order> findByStatus(
      String status);
}
