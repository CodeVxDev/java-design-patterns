# Observer

**Behavioral** · Episode 3 · [Video](https://youtube.com/shorts/FDtWvAIfF-s) ·
[Blog post](https://codevx.dev/post/observer-pattern-in-java)

> When something happens, the object it happened to announces it once. Any number of listeners react, and the
> announcer doesn't know who they are.

## The problem

Paying an order calls every team itself ([`before/Orders.java`](src/main/java/dev/codevx/patterns/observer/before/Orders.java)).
A fourth team means editing `pay()` and adding a dependency, and any team's failure fails the payment.

```java
order.markPaid();
email.send(order);
inventory.reserve(order);
analytics.track(order);
loyalty.addPoints(order); // the edit every new team needs
```

## The pattern

```mermaid
classDiagram
  class Orders {
    -List~OrderListener~ listeners
    +subscribe(OrderListener)
    +unsubscribe(OrderListener)
    +pay(Order)
  }
  class OrderListener {
    <<interface>>
    +onPaid(Order)
  }
  Orders o--> "*" OrderListener : for each: onPaid(order)
  OrderListener <|.. Email
  OrderListener <|.. Inventory
  OrderListener <|.. Loyalty
```

In Spring there is no list at all: `Orders` calls `publishEvent(new OrderPaid(id))`, and every `@EventListener`
method that takes an `OrderPaid` receives it.

## The code

| Folder                                                       | What it shows                                                                                 | Run                                                                 |
| ------------------------------------------------------------ | --------------------------------------------------------------------------------------------- | ------------------------------------------------------------------- |
| [`before/`](src/main/java/dev/codevx/patterns/observer/before)         | `pay()` calling four teams                                                                    | `java src/main/java/dev/codevx/patterns/observer/before/Demo.java`     |
| [`handrolled/`](src/main/java/dev/codevx/patterns/observer/handrolled) | A listener interface, a subject with subscribe/unsubscribe, and a listener that fails `pay()` | `java src/main/java/dev/codevx/patterns/observer/handrolled/Demo.java` |
| [`spring/`](src/main/java/dev/codevx/patterns/observer/spring)         | `ApplicationEventPublisher` with a real transaction (in-memory H2) and three listeners, each a different choice | `./mvnw -pl behavioral/observer spring-boot:run` (from the repo root) |

The three Spring listeners, and why each is written the way it is:

| Listener                                                                                     | Annotations                                                    | Why                                                                                 |
| -------------------------------------------------------------------------------------------- | -------------------------------------------------------------- | ----------------------------------------------------------------------------------- |
| [`InventoryListener`](src/main/java/dev/codevx/patterns/observer/spring/InventoryListener.java) | `@EventListener`                                               | No stock, no payment: same thread and transaction, so its failure fails the payment. |
| [`EmailListener`](src/main/java/dev/codevx/patterns/observer/spring/EmailListener.java)         | `@Async @TransactionalEventListener`                           | Only after the commit, on another thread: email can't fail or slow `pay()`.         |
| [`LoyaltyListener`](src/main/java/dev/codevx/patterns/observer/spring/LoyaltyListener.java)     | `@TransactionalEventListener @Transactional(REQUIRES_NEW)`     | Writes after the commit, so it needs its own transaction.                           |

Tests: `./mvnw -pl behavioral/observer test` runs [`HandRolledTest`](src/test/java/dev/codevx/patterns/observer/HandRolledTest.java),
[`SpringTest`](src/test/java/dev/codevx/patterns/observer/SpringTest.java),
[`PitfallsTest`](src/test/java/dev/codevx/patterns/observer/PitfallsTest.java) and
[`SpringPitfallsTest`](src/test/java/dev/codevx/patterns/observer/SpringPitfallsTest.java).

## Pitfalls

Each one is a test: the failure first, then the fix.

By hand ([`PitfallsTest`](src/test/java/dev/codevx/patterns/observer/PitfallsTest.java)):

1. **The order no longer knows its listeners, but it still waits for them.** The loop runs in the caller's thread:
   a listener that throws fails `pay()` and skips every listener after it.
2. **A listener that unsubscribes during the loop** breaks an `ArrayList` one of two ways: a
   `ConcurrentModificationException`, or, when it was second to last, no error at all and the last listener is
   silently skipped. Keep listeners in a `CopyOnWriteArrayList`: the loop walks a snapshot.

With Spring ([`SpringPitfallsTest`](src/test/java/dev/codevx/patterns/observer/SpringPitfallsTest.java)):

3. **A plain `@EventListener` runs in the payment's thread and transaction.** Its exception rolls the payment back.
   For a listener that mustn't be able to do that, `@Async` moves it to another thread.
4. **A plain `@EventListener` runs before the commit**, so it can send an email for a payment that then rolls back.
   `@TransactionalEventListener` waits for the commit (its default phase) and does nothing on a rollback.
5. **An `@Async` listener's exception never reaches `pay()`.** Without an `AsyncUncaughtExceptionHandler`
   ([`AsyncConfig`](src/main/java/dev/codevx/patterns/observer/spring/AsyncConfig.java)), logging or retries, nobody
   sees it.
6. **No transaction, no event.** A `@TransactionalEventListener` is skipped when nothing is in a transaction, the
   usual "my listener never fires" in a test. `fallbackExecution = true` runs it anyway.

Not in the tests, but worth saying in an interview:

- **Not every listener should be async.** Decide per listener: must the payment fail if this fails?
- **Order isn't promised** unless you add `@Order`. If the order matters, you may not want listeners at all.
- **Events in memory are lost** if the application stops between the commit and the listener. Save the event in
  the payment's transaction and publish it from there: the outbox pattern.
- **A listener nobody unsubscribes stays reachable** from the subject's list, along with everything it holds (the
  "lapsed listener" leak). Spring beans live as long as the context, so this bites hand-rolled code.

## Often confused with

| Pattern                    | The question that separates them                                                                                         |
| -------------------------- | ------------------------------------------------------------------------------------------------------------------------ |
| Chain of Responsibility    | Can a receiver stop the request? A listener is only told; a link in a chain can say no, and the order of links matters.  |
| Mediator                   | Who knows whom? A subject doesn't know its listeners; a mediator knows every colleague and tells each one what to do.    |
| Publish/subscribe (Kafka)  | Observer is in-process and usually synchronous. Pub/sub puts a broker in between: async, durable, at-least-once, so consumers must handle duplicates. |

## Interview questions

- **An order got paid. Should it call email, inventory and analytics itself?** **No:** it announces, they listen.
- **Is `@EventListener` synchronous?** Yes, by default: same thread, same transaction, and `publishEvent` returns
  after the last listener.
- **How do you send the email only if the payment commits?** `@TransactionalEventListener`, whose default phase is
  `AFTER_COMMIT`. Add `@Async` so it can't slow or fail the payment.
- **What if the app crashes right after the commit?** The in-memory event is lost. Use the outbox pattern: write
  the event in the same transaction, publish it from the table.
- **Observer vs Kafka?** Same idea between services: a topic, consumer groups, and a publisher that doesn't know who
  listens. What changes is delivery: at least once, so listeners must be idempotent.
- **The trap:** fraud, stock and coupon checks run before an order is paid, and any of them can stop it. Observer
  again? **No:** a listener can't say no. That's Chain of Responsibility (episode 4).

## Say it in 30 seconds

"Observer decouples the thing that changed from the things that react. The subject keeps a list of listeners
behind one interface and notifies them all, so a new reaction is a new listener and the subject never changes. In
Spring, the subject publishes an event and `@EventListener` methods receive it, but by default they run in the same
thread and transaction, so a failing listener still fails the caller. For side effects like email I use
`@Async @TransactionalEventListener`, and the outbox pattern when an event must not be lost."

## Spot it in LLD interviews

One thing changes and many others react, and the thing that changed shouldn't know who they are: order
management, notification service (email, SMS, push), stock ticker, auction bids, a news feed or channel
subscriptions, chat presence, config or feature-flag changes.
