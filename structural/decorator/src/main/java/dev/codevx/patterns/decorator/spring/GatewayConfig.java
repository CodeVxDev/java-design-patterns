package dev.codevx.patterns.decorator.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * The stack, built once. BankGateway is a Gateway bean too, so this one is marked primary: without it, what a
 * caller gets depends on the name of its constructor parameter (pitfall 3).
 */
@Configuration
public class GatewayConfig {
  @Bean
  @Primary
  Gateway gateway(BankGateway bank) {
    return new Logged(
        new Retried(bank));
  }
}
