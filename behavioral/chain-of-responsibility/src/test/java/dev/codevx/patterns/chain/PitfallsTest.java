package dev.codevx.patterns.chain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.chain.handrolled.Chain;
import dev.codevx.patterns.chain.handrolled.Check;
import dev.codevx.patterns.chain.handrolled.Checkout;
import dev.codevx.patterns.chain.handrolled.Order;
import dev.codevx.patterns.chain.handrolled.Rejected;
import dev.codevx.patterns.chain.handrolled.Rule;
import dev.codevx.patterns.chain.handrolled.Rules;
import dev.codevx.patterns.chain.handrolled.StockCheck;
import dev.codevx.patterns.chain.spring.TokenFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/** Numbered as in the README: each pitfall shows the failure first, then the fix. */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Chain of Responsibility pitfalls")
class PitfallsTest {
  static final Order OUT_OF_STOCK = new Order("order-42", false, false, true, 49_00);

  @Test
  @org.junit.jupiter.api.Order(1)
  @DisplayName("1. Pitfall: a check that forgets next.proceed(o) skips the rest, and an out-of-stock order goes through")
  void forgottenNextSkipsTheRest() {
    Checkout checkout = new Checkout(List.of(new AuditCheck(), new StockCheck()));

    assertThatCode(() -> checkout.validate(OUT_OF_STOCK)).doesNotThrowAnyException();
  }

  @Test
  @org.junit.jupiter.api.Order(2)
  @DisplayName("1. Fix: in the runner shape a loop moves the order on, so no rule can skip the ones after it")
  void runnerCantSkip() {
    Rule audit = o -> {}; // the same audit: it has nothing to pass on
    Rule stock = o -> {
      if (!o.inStock()) {
        throw new Rejected("stock");
      }
    };
    Rules rules = new Rules(List.of(audit, stock));

    assertThatThrownBy(() -> rules.validate(OUT_OF_STOCK)).hasMessage("stock");
  }

  @Test
  @org.junit.jupiter.api.Order(3)
  @DisplayName("2. Pitfall: a servlet filter that forgets chain.doFilter: the controller never runs, the client gets an empty 200")
  void forgottenDoFilterIsAnEmpty200() throws Exception {
    MockHttpServletResponse res = run(new ForgetfulFilter(), withToken());

    assertThat(res.getStatus()).isEqualTo(200);
    assertThat(res.getContentAsString()).isEmpty();
  }

  @Test
  @org.junit.jupiter.api.Order(4)
  @DisplayName("2. Fix: the filter ends with chain.doFilter(req, res), and the controller answers")
  void doFilterReachesTheController() throws Exception {
    MockHttpServletResponse res = run(new TokenFilter(), withToken());

    assertThat(res.getContentAsString()).isEqualTo("orders");
  }

  @Test
  @org.junit.jupiter.api.Order(5)
  @DisplayName("3. Pitfall: without @Order, the List follows registration order, so the slow check runs before the cheap one")
  void withoutOrderTheListFollowsRegistration() {
    try (var context = new AnnotationConfigApplicationContext(SlowCheck.class, CheapCheck.class, Checks.class)) {
      assertThat(context.getBean(Checks.class).names()).containsExactly("SlowCheck", "CheapCheck");
    }
    try (var context = new AnnotationConfigApplicationContext(CheapCheck.class, SlowCheck.class, Checks.class)) {
      assertThat(context.getBean(Checks.class).names()).containsExactly("CheapCheck", "SlowCheck");
    }
  }

  @Test
  @org.junit.jupiter.api.Order(6)
  @DisplayName("3. Fix: with @Order, the cheap check comes first whatever the registration order")
  void orderFixesTheList() {
    try (var context =
        new AnnotationConfigApplicationContext(OrderedSlowCheck.class, OrderedCheapCheck.class, Checks.class)) {
      assertThat(context.getBean(Checks.class).names()).containsExactly("OrderedCheapCheck", "OrderedSlowCheck");
    }
  }

  // ---- test-only checks and filters ----

  /** Logs the order for an audit, and forgets to pass it on. */
  static class AuditCheck implements Check {
    @Override
    public void check(Order o, Chain next) {
      // next.proceed(o);  <- forgotten
    }
  }

  /** Lets the request through its own check, then forgets to call the rest of the chain. */
  static class ForgetfulFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
      // chain.doFilter(req, res);  <- forgotten
    }
  }

  /** Stands in for the controller at the end of the filter chain. */
  static class Controller extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
      res.getWriter().write("orders");
    }
  }

  static MockHttpServletRequest withToken() {
    MockHttpServletRequest req = new MockHttpServletRequest("GET", "/orders");
    req.addHeader("Authorization", "Bearer abc");
    return req;
  }

  static MockHttpServletResponse run(Filter filter, MockHttpServletRequest req) throws Exception {
    MockHttpServletResponse res = new MockHttpServletResponse();
    new MockFilterChain(new Controller(), filter).doFilter(req, res);
    return res;
  }

  /** Asks Spring for every Check bean as a List, the way Checkout does. */
  static class Checks {
    final List<Check> checks;

    Checks(List<Check> checks) {
      this.checks = checks;
    }

    List<String> names() {
      return checks.stream().map(c -> c.getClass().getSimpleName()).toList();
    }
  }

  /** Calls another service: slow, so it should run last. */
  static class SlowCheck implements Check {
    @Override
    public void check(Order o, Chain next) {
      next.proceed(o);
    }
  }

  /** Looks at the order only: cheap, so it should run first. */
  static class CheapCheck implements Check {
    @Override
    public void check(Order o, Chain next) {
      next.proceed(o);
    }
  }

  @org.springframework.core.annotation.Order(2)
  static class OrderedSlowCheck extends SlowCheck {}

  @org.springframework.core.annotation.Order(1)
  static class OrderedCheapCheck extends CheapCheck {}
}
