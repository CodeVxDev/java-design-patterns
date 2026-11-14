package dev.codevx.patterns.state.spring;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Looks up the bean for a status. It checks at startup that every status has exactly one bean, so a status
 * added without its state class stops the application from starting instead of failing the first order
 * that reaches it.
 */
@Component
public class OrderStates {
  private final Map<Status, OrderState> byStatus = new EnumMap<>(Status.class);

  public OrderStates(List<OrderState> states) {
    for (OrderState state : states) {
      OrderState clash = byStatus.put(state.status(), state);
      if (clash != null) {
        throw new IllegalStateException("two beans for " + state.status() + ": "
            + clash.getClass().getSimpleName() + ", " + state.getClass().getSimpleName());
      }
    }
    Set<Status> missing = EnumSet.allOf(Status.class);
    missing.removeAll(byStatus.keySet());
    if (!missing.isEmpty()) {
      throw new IllegalStateException("no OrderState bean for " + missing);
    }
  }

  public OrderState of(Status status) {
    return byStatus.get(status);
  }
}
