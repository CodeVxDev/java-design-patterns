package dev.codevx.patterns.observer.spring;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/** Run: ./mvnw -pl behavioral/observer spring-boot:run */
@SpringBootApplication
public class ObserverApp {
  public static void main(String[] args) {
    SpringApplication.run(ObserverApp.class, args);
  }

  @Bean
  ApplicationRunner demo(Orders orders, Activity activity) {
    return args -> {
      orders.create("order-42");
      System.out.println("pay(order-42) on thread " + Thread.currentThread().getName() + ":");
      orders.pay("order-42");
      System.out.println("pay returned, paid = " + orders.isPaid("order-42"));
      activity.await("email order-42", java.time.Duration.ofSeconds(2)); // the async email
    };
  }
}
