package dev.codevx.patterns.chain.spring;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/** Run: ./mvnw -pl behavioral/chain-of-responsibility spring-boot:run */
@SpringBootApplication
public class ChainApp {
  public static void main(String[] args) {
    SpringApplication.run(ChainApp.class, args);
  }

  @Bean
  ApplicationRunner demo(Checkout checkout) {
    return args -> {
      System.out.println("The checks Spring hands over, in @Order order: " + checkout.checks());
      System.out.println("good order: " + outcome(checkout, new Order("order-1", false, true, true, 49_00)));
      System.out.println("out-of-stock order: " + outcome(checkout, new Order("order-2", false, false, true, 49_00)));
      System.out.println("huge order: " + outcome(checkout, new Order("order-3", false, true, true, 900_00)));
    };
  }

  private static String outcome(Checkout checkout, Order o) {
    try {
      checkout.validate(o);
      return "passed every check";
    } catch (Rejected e) {
      return "stopped by " + e.getMessage();
    }
  }
}
