package dev.codevx.patterns.decorator.spring;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/** Run: ./mvnw -pl structural/decorator spring-boot:run */
@SpringBootApplication
public class DecoratorApp {
  public static void main(String[] args) {
    SpringApplication.run(DecoratorApp.class, args);
  }

  @Bean
  ApplicationRunner demo(Checkout checkout, BankGateway bank) {
    return args -> {
      System.out.println("The Gateway Checkout gets: " + checkout.gateway());
      System.out.println("paid: " + checkout.pay("order-41", 4900));
      System.out.println("the card was charged " + bank.charges("order-41") + " time for order-41");
    };
  }
}
