package dev.codevx.patterns.observer.spring;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Turns on @Async, and gives async failures somewhere to go: an exception in an @Async void method never
 * reaches the caller, so without a handler (or logging and retries in the listener) nobody sees it.
 */
@Configuration
@EnableAsync
class AsyncConfig implements AsyncConfigurer {
  private final Activity activity;

  AsyncConfig(Activity activity) {
    this.activity = activity;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return (error, method, params) -> activity.record("async failure: " + error.getMessage());
  }
}
