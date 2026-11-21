package dev.codevx.patterns.proxy.before;

import java.util.ArrayList;
import java.util.List;

/** A transaction: writes made while it is active wait for commit(), and rollback() drops them. */
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

  private void end() {
    pending.clear();
    active = false;
  }
}
