package dev.codevx.patterns.proxy.handrolled;

import java.util.ArrayList;
import java.util.List;

/**
 * A transaction: writes made while it is active wait for commit(), and rollback() drops them. run() wraps a
 * piece of work in begin, commit and rollback, the lines every method carried in before/.
 */
public class Tx {
  private final List<Runnable> pending = new ArrayList<>();
  private boolean active;

  public boolean active() {
    return active;
  }

  public void defer(Runnable write) {
    pending.add(write);
  }

  public void begin() {
    active = true;
  }

  public void commit() {
    pending.forEach(Runnable::run);
    end();
  }

  public void rollback() {
    end();
  }

  public void run(Runnable work) {
    begin();
    try {
      work.run();
      commit();
    } catch (RuntimeException e) {
      rollback();
      throw e;
    }
  }

  private void end() {
    pending.clear();
    active = false;
  }
}
