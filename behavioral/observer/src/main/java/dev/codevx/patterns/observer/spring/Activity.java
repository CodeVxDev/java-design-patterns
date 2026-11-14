package dev.codevx.patterns.observer.spring;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

/**
 * Stands in for the real email, inventory and loyalty systems: every listener records what it did and on
 * which thread, so the demo can print it and the tests can check it.
 */
@Component
public class Activity {
  public record Entry(String what, String thread) {}

  private final List<Entry> entries = new CopyOnWriteArrayList<>();

  public void record(String what) {
    Entry entry = new Entry(what, Thread.currentThread().getName());
    entries.add(entry);
    System.out.println("  " + entry.what() + "  [thread " + entry.thread() + "]");
  }

  public Optional<Entry> find(String what) {
    return entries.stream().filter(e -> e.what().equals(what)).findFirst();
  }

  /** For listeners on another thread: waits up to the timeout for the entry to appear. */
  public Optional<Entry> await(String what, Duration timeout) throws InterruptedException {
    long deadline = System.nanoTime() + timeout.toNanos();
    while (System.nanoTime() < deadline) {
      Optional<Entry> entry = find(what);
      if (entry.isPresent()) {
        return entry;
      }
      Thread.sleep(10);
    }
    return find(what);
  }
}
