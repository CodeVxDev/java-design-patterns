package dev.codevx.patterns.observer.spring;

/** The event: a plain record. Listeners receive it by type; no interface to implement. */
public record OrderPaid(String orderId) {}
