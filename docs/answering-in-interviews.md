# Answering a pattern question

## The shape of a good answer

Whether the question is "explain Observer" or "design an order system", the strongest answers move through the
same six steps. Each pattern's README ends with a 30-second version of this.

1. **The problem, in the question's own words.** "Every new payment type means editing `pay()` again."
2. **The pattern and its intent, in one sentence.** "That's Strategy: each way of paying in its own class behind
   one interface."
3. **The shape.** Name the interface first and give it one method, then the context that holds it. Sketch it if
   there's a whiteboard.
4. **How a change lands.** "Adding PayLater is one new class; `Checkout` doesn't change." This is the
   open/closed principle, and it's what the interviewer is listening for.
5. **Where you've seen it.** Real code beats textbook examples: `Comparator`, Spring injecting
   `Map<String, PaymentStrategy>`. See [Where they live](in-spring-and-jdk.md).
6. **When you wouldn't, and what bites.** "With two cases that never change, an if-else is simpler." Then one
   pitfall from the README. This is the step that separates someone who has used the pattern from someone who has
   read about it.

When the follow-up is "isn't that just X?", answer with the question that separates the two
([Commonly confused patterns](confusions.md)) rather than restating both definitions.

## Spotting the pattern in an LLD round

LLD questions don't name the pattern. These are the phrases in a requirement that point to one:

| When the requirements say…                                            | Think               |
| --------------------------------------------------------------------- | ------------------- |
| "Support several ways to…", "pricing can be hourly, flat or…"         | Strategy            |
| "First…, then…, only after…, can't… once…"                            | State               |
| "When X happens, notify / update A, B and C"                          | Observer            |
| "Validate in order; any check can reject"                             | Chain of Responsibility |
| "Add logging / caching / retries to an existing client"               | Decorator or Proxy  |
| "Integrate a third-party SDK whose API doesn't match ours"            | Adapter             |
| "Undo / redo", "queue the request", "retry later"                     | Command (or Memento) |
| "Folders inside folders", "a menu with submenus"                      | Composite           |
| "Many kinds of X, each sent over many channels"                       | Bridge              |
| "Objects with many optional fields"                                   | Builder             |
| "Only one X in the whole system"                                      | Singleton (and say how you'd test it) |

Name the pattern after you've named the problem, not before: "every new rule edits this method, so I'll put each
rule behind an interface; that's Strategy."
