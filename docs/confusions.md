# Commonly confused patterns

Interviewers rarely ask "what is Decorator?". They ask "isn't that just a Proxy?". Each pair below comes with the
one question that separates them. Answer that question out loud and you've shown you know both.

| These two                              | Ask                                                         | Answer                                                                                                             |
| -------------------------------------- | ----------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------ |
| Strategy vs State                      | Who swaps the behaviour?                                    | Strategy: the caller, once; strategies don't know each other. State: the object itself, as it moves; a state names the next one. |
| Strategy vs Template Method            | Composition or inheritance?                                 | Strategy swaps a whole object at runtime. Template Method: the parent keeps the steps and a subclass fills in one, fixed at compile time. |
| Strategy vs Command                    | How, or what to do later?                                   | A strategy is how to do a job now. A command is a request packaged to queue, retry or undo.                        |
| Observer vs Chain of Responsibility    | Can a receiver stop the request?                            | A listener is only told; it can't say no. A link in a chain can stop the request, and the order matters.           |
| Observer vs Mediator                   | Who knows whom?                                             | A subject doesn't know its listeners. A mediator knows every part and directs them.                                |
| Observer vs pub/sub (Kafka)            | Is there a broker in between?                               | Observer is in-process and usually synchronous. Pub/sub adds a broker: async, durable, at-least-once delivery.     |
| Chain of Responsibility vs Decorator   | Does every wrapper call through?                            | A decorator always calls the next one and adds behaviour around it. A chain link may stop and never call the next. |
| Decorator vs Proxy                     | Does it decide whether the call happens?                    | A decorator adds behaviour and always calls through. A proxy controls access: it may delay, block or redirect the call. |
| Proxy vs Adapter                       | Does the interface change?                                  | A proxy has the same interface as the real thing. An adapter converts one interface into another.                  |
| Adapter vs Facade                      | Converting, or simplifying?                                 | An adapter makes one class fit an interface. A facade puts one simple front door on a whole subsystem, and converts nothing. |
| Adapter vs Bridge                      | Fixing after, or designing before?                          | An adapter is retrofitted to make existing classes fit. A bridge is designed up front so two dimensions can vary independently. |
| Facade vs Mediator                     | Do the parts know the coordinator?                          | A facade's parts don't know it exists. A mediator's parts send everything through it.                              |
| Singleton vs "just create it once"     | What stops a second instance?                               | Careful callers aren't a guarantee. A singleton's class rules out a second instance (private constructor, or an enum). |
| Singleton vs Factory Method            | `Calendar.getInstance()` twice: same object?                | No. "getInstance" is a factory method returning a new object each time, not a singleton.                           |
| Singleton vs Flyweight                 | One instance, or one per value?                             | A singleton has exactly one. A flyweight pool shares one instance per distinct value (`Integer.valueOf(127)`).     |
| Factory Method vs Abstract Factory     | One product, or a family?                                   | Factory Method creates one product. Abstract Factory creates a set of related products that must match (per region, per platform). |
| Builder vs setters                     | Can the object exist half-built?                            | With setters, yes, and it stays changeable. A builder hands over a finished, often immutable object.               |
| Builder vs Prototype                   | Build from scratch, or copy?                                | A builder assembles step by step. A prototype copies a finished object and changes the few fields that differ.     |
| Prototype vs Memento                   | Why is the copy kept?                                       | A prototype is copied to make new objects. A memento is a sealed snapshot kept only to restore the original.       |
| Memento vs Command (for undo)          | Store the state, or the action?                             | Memento stores what the state was. Command stores what you did and how to undo it, which is lighter for big objects. |
| Composite vs Visitor                   | Where does a new operation go?                              | Composite puts each operation in every node class. Visitor moves each operation into its own class.                |

The pairs follow the series: each episode's interview trap is one of these questions, and the next episode
answers it.
