package dev.codevx.patterns.strategy.jdk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The Strategy you already use: List.sort is the context, Comparator the strategy. The sort algorithm never
 * changes; the rule for "which comes first" is passed in.
 *
 * <p>Run without a build: java src/main/java/dev/codevx/patterns/strategy/jdk/SortDemo.java
 */
public class SortDemo {
  public record Item(String name, long price, double rating) {}

  public static final Comparator<Item> BY_PRICE = Comparator.comparingLong(Item::price);
  public static final Comparator<Item> BY_RATING =
      Comparator.comparingDouble(Item::rating).reversed();

  public static List<String> sortedNames(List<Item> items, Comparator<Item> order) {
    List<Item> copy = new ArrayList<>(items);
    copy.sort(order);
    return copy.stream().map(Item::name).toList();
  }

  public static void main(String[] args) {
    List<Item> items = List.of(
        new Item("mouse", 2500, 4.1), new Item("keyboard", 4900, 4.7), new Item("cable", 900, 3.9));
    System.out.println("by price:  " + sortedNames(items, BY_PRICE));
    System.out.println("by rating: " + sortedNames(items, BY_RATING));
  }
}
