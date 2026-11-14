package dev.codevx.patterns.strategy.spring;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/** Run: ./mvnw -pl behavioral/strategy spring-boot:run */
@SpringBootApplication
public class StrategyApp {
  public static void main(String[] args) {
    SpringApplication.run(StrategyApp.class, args);
  }

  @Bean
  ApplicationRunner demo(Checkout checkout) {
    return args -> {
      System.out.println("bean names: " + checkout.types());
      System.out.println(checkout.pay("payLater", 4900));
    };
  }
}
