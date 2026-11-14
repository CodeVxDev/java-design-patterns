package dev.codevx.patterns.state.spring;

public record Order(String id, Status status) {
  public Order withStatus(Status next) {
    return new Order(id, next);
  }
}
