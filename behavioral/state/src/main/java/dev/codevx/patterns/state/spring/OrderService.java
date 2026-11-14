package dev.codevx.patterns.state.spring;

import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/** Load the status, let its state decide, save what it returns. No if-else on the status. */
@Service
public class OrderService {
  private final OrderRepository orders;
  private final OrderStates states;

  public OrderService(OrderRepository orders, OrderStates states) {
    this.orders = orders;
    this.states = states;
  }

  public Order create(String id) {
    Order order = new Order(id, Status.CREATED);
    orders.save(order);
    return order;
  }

  public Order pay(String id) {
    return move(id, OrderState::pay);
  }

  public Order ship(String id) {
    return move(id, OrderState::ship);
  }

  public Order cancel(String id) {
    return move(id, OrderState::cancel);
  }

  private Order move(String id, BiFunction<OrderState, Order, Status> action) {
    Order order = orders.find(id);
    Order next = order.withStatus(action.apply(states.of(order.status()), order));
    orders.save(next);
    return next;
  }
}
