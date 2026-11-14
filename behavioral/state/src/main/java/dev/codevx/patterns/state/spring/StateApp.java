package dev.codevx.patterns.state.spring;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/** Run: ./mvnw -pl behavioral/state spring-boot:run */
@SpringBootApplication
public class StateApp {
  public static void main(String[] args) {
    SpringApplication.run(StateApp.class, args);
  }

  @Bean
  ApplicationRunner demo(OrderService service) {
    return args -> {
      service.create("order-42");
      System.out.println(service.pay("order-42"));
      System.out.println(service.ship("order-42"));
      try {
        service.cancel("order-42");
      } catch (IllegalStateException refused) {
        System.out.println("refused: " + refused.getMessage());
      }
    };
  }
}
