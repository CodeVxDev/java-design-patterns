package dev.codevx.patterns.chain.spring;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Spring builds the chain: it hands over every Check bean as a List, sorted by {@code @Order}. Beans without one
 * come last, in registration order, which nobody should rely on (pitfall 3).
 *
 * <p>The constructor is the one Lombok's {@code @RequiredArgsConstructor} would generate, written out.
 */
@Service
public class Checkout {
  private final List<Check> checks;

  public Checkout(List<Check> checks) {
    this.checks = checks;
  }

  public void validate(Order o) {
    new Chain(checks.iterator()).proceed(o);
  }

  /** The checks' class names, in the order the chain runs them. */
  public List<String> checks() {
    return checks.stream().map(c -> c.getClass().getSimpleName()).toList();
  }
}
