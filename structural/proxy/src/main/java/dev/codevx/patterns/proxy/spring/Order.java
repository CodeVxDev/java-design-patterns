package dev.codevx.patterns.proxy.spring;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** "order" is an SQL keyword, hence the table name. */
@Entity
@Table(name = "orders")
public class Order {
  @Id @GeneratedValue private Long id;
  private String item;
  private String status = "PLACED";

  protected Order() {} // for JPA

  public Order(String item) {
    this.item = item;
  }

  public Long getId() {
    return id;
  }

  public String getItem() {
    return item;
  }

  public String getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return "Order(" + item + ")";
  }
}
