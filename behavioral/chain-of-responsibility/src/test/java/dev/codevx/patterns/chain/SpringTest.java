package dev.codevx.patterns.chain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.codevx.patterns.chain.spring.ChainApp;
import dev.codevx.patterns.chain.spring.Checkout;
import dev.codevx.patterns.chain.spring.Order;
import dev.codevx.patterns.chain.spring.TokenFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@SpringBootTest(classes = ChainApp.class)
@DisplayName("Chain of Responsibility, in Spring")
class SpringTest {

  @Autowired Checkout checkout;
  @Autowired TokenFilter filter;

  @Test
  @DisplayName("Spring hands the checks over in @Order order, and the test pins it: the order is a contract")
  void checksArriveInOrder() {
    assertThat(checkout.checks()).containsExactly("FraudCheck", "StockCheck", "CouponCheck", "LimitCheck");
  }

  @Test
  @DisplayName("A good order passes every check bean")
  void goodOrderPasses() {
    assertThatCode(() -> checkout.validate(new Order("good", false, true, true, 49_00)))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("A huge order is stopped by LimitCheck, a bean that Checkout never names")
  void limitIsOneMoreBean() {
    assertThatThrownBy(() -> checkout.validate(new Order("huge", false, true, true, 900_00))).hasMessage("limit");
  }

  @Test
  @DisplayName("With no token, the filter answers 401 itself, and the rest of the chain never runs")
  void noTokenStopsTheRequest() throws Exception {
    MockHttpServletResponse res = new MockHttpServletResponse();
    MockFilterChain rest = new MockFilterChain();

    filter.doFilter(new MockHttpServletRequest("GET", "/orders"), res, rest);

    assertThat(res.getStatus()).isEqualTo(401);
    assertThat(rest.getRequest()).isNull();
  }

  @Test
  @DisplayName("With a token, the filter calls chain.doFilter and the request goes on")
  void tokenPassesItAlong() throws Exception {
    MockHttpServletRequest req = new MockHttpServletRequest("GET", "/orders");
    req.addHeader("Authorization", "Bearer abc");
    MockFilterChain rest = new MockFilterChain();

    filter.doFilter(req, new MockHttpServletResponse(), rest);

    assertThat(rest.getRequest()).isSameAs(req);
  }
}
