package dev.codevx.patterns.proxy.before;

import java.util.ArrayList;
import java.util.List;

/**
 * The orders table, in memory. Inside a transaction an insert waits for the commit; outside one it is saved at
 * once (auto-commit), and nothing can undo it.
 */
public class Orders {
  private final List<String> rows = new ArrayList<>();
  private final Tx tx;

  public Orders(Tx tx) {
    this.tx = tx;
  }

  public void insert(Order o) {
    if (tx.active()) {
      tx.defer(() -> rows.add(o.id()));
    } else {
      rows.add(o.id());
    }
  }

  public List<String> saved() {
    return List.copyOf(rows);
  }
}
