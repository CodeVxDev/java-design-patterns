package dev.codevx.patterns.proxy.spring;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/** Run: ./mvnw -pl structural/proxy spring-boot:run */
@SpringBootApplication
public class ProxyApp {
  public static void main(String[] args) {
    SpringApplication.run(ProxyApp.class, args);
  }

  @Bean
  ApplicationRunner demo(OrderService service, FixedOrderService fixed, OrderRepository orders) {
    return args -> {
      System.out.println("The OrderService you get: " + service.getClass().getSimpleName());
      System.out.println("The OrderRepository you get: " + orders.getClass().getName());

      attempt("save(lamp), called from outside", () -> service.save(new Order("lamp")), orders);
      attempt("placeOrder(lamp), save() called from inside", () -> service.placeOrder(new Order("lamp")), orders);
      attempt("fixed placeOrder(lamp), through OrderWriter", () -> fixed.placeOrder(new Order("lamp")), orders);
    };
  }

  private static void attempt(String what, Runnable call, OrderRepository orders) {
    orders.deleteAll();
    try {
      call.run();
    } catch (IllegalStateException e) {
      System.out.println(what + ": " + e.getMessage() + ", saved orders: " + orders.findByStatus("PLACED"));
    }
  }
}
