package dev.codevx.patterns.chain.spring;

import org.springframework.stereotype.Component;

/**
 * A bean, so Spring adds it to the {@code List<Check>} that Checkout asks for. The annotation is written out in
 * full because the domain class is called Order too, and two types with one simple name can't both be imported.
 */
@Component
@org.springframework.core.annotation.Order(1)
public class FraudCheck implements Check {
  @Override
  public void check(Order o, Chain next) {
    if (o.risky()) {
      throw new Rejected("fraud");
    }
    next.proceed(o);
  }
}
