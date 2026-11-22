package dev.codevx.patterns.chain.spring;

/** An order at checkout, with the answers the checks need. The total is in cents. */
public record Order(String id, boolean risky, boolean inStock, boolean couponValid, long total) {}
