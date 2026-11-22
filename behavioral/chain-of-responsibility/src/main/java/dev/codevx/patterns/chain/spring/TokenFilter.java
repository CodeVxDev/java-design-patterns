package dev.codevx.patterns.chain.spring;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The chain every Spring web request already runs through: a servlet filter has the same shape as a check. With
 * no token it answers the request itself and returns, so nothing after it runs, not even the controller.
 * Otherwise it calls chain.doFilter to pass the request along.
 *
 * <p>OncePerRequestFilter runs once per request even when the request is forwarded internally. In a web app
 * Spring Boot adds this bean to the filter chain by itself; this module runs without a web server, and the tests
 * call it directly.
 */
@Component
public class TokenFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {
    if (!hasToken(req)) {
      reject(res);
      return;
    }
    chain.doFilter(req, res);
  }

  private boolean hasToken(HttpServletRequest req) {
    String header = req.getHeader("Authorization");
    return header != null && header.startsWith("Bearer ");
  }

  private void reject(HttpServletResponse res) throws IOException {
    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
