# Cheat sheet: all 23 patterns

One line each: what it does, the cue that points to it, and where you've already seen it in Java. Patterns with
code link to their folder.

## Creational: how objects get made

| Pattern          | In one line                                                                              | The cue                                              | You've seen it in                                       |
| ---------------- | ---------------------------------------------------------------------------------------- | ---------------------------------------------------- | ------------------------------------------------------- |
| Singleton        | The class itself guarantees one instance and a global way to reach it.                   | "Exactly one, everywhere": a config, a registry.     | Enum singleton, `Runtime.getRuntime()`, Spring's singleton scope (one per container). |
| Factory Method   | A method decides which class to instantiate, so callers ask for an interface.            | The caller shouldn't name the concrete class.        | `Calendar.getInstance()`, `List.of(...)`, `@Bean` methods. |
| Abstract Factory | One factory makes a whole family of related objects that must not be mixed.              | Several products that vary together, per region or platform. | `DocumentBuilderFactory`, JDBC `Connection`, Spring `@Profile` configurations. |
| Builder          | Assemble a complex object step by step, then get it finished and immutable.              | Many fields, most optional; telescoping constructors. | `HttpRequest.newBuilder()`, `StringBuilder`, Lombok `@Builder`. |
| Prototype        | Make new objects by copying a finished one.                                              | Creating from scratch is expensive; copies differ a little. | Copy constructors, `toBuilder()`, `clone()` (and its traps). |

## Structural: how objects are put together

| Pattern   | In one line                                                                                   | The cue                                                   | You've seen it in                                            |
| --------- | --------------------------------------------------------------------------------------------- | --------------------------------------------------------- | ------------------------------------------------------------ |
| Adapter   | Wrap a class so it fits the interface your code expects.                                      | A third-party API doesn't match your interface.           | `Arrays.asList`, `InputStreamReader`, Spring MVC `HandlerAdapter`. |
| [Decorator](../structural/decorator) | Wrap an object in another with the same interface to add behaviour; it always calls through. | Add logging, buffering, retries without subclassing. | `BufferedInputStream`, `GZIPInputStream`, `Collections.synchronizedList`. |
| [Proxy](../structural/proxy) | A stand-in with the same interface that controls access: it decides if and when the call happens. | Lazy loading, access checks, remote calls, transactions. | Spring AOP (`@Transactional`, `@Cacheable`), Spring Data repositories, Hibernate lazy loading. |
| Facade    | One simple front door to a subsystem that stays usable on its own.                             | Five calls in a fixed order that every caller repeats.    | `JdbcTemplate`, SLF4J, `java.nio.file.Files`.                |
| Composite | Treat a single object and a group of them the same way, as a tree.                            | Part-whole hierarchies: folders, menus, org charts.       | Spring Boot's composite health contributors, UI component trees. |
| Bridge    | Split two dimensions that vary into two hierarchies joined by one field.                       | M × N subclasses (kinds × channels, shapes × renderers).  | JDBC: the API on one side, drivers on the other.             |
| Flyweight | Share immutable objects instead of creating identical ones.                                   | Huge numbers of objects with the same values.             | `Integer.valueOf` (-128 to 127), the String pool.            |

## Behavioral: how objects talk

| Pattern                                         | In one line                                                                          | The cue                                                     | You've seen it in                                             |
| ----------------------------------------------- | ------------------------------------------------------------------------------------ | ----------------------------------------------------------- | ------------------------------------------------------------- |
| [Strategy](../behavioral/strategy)              | Each way of doing a job in its own class; the caller picks one.                      | Many ways to do the same job, and new ones keep coming.     | `Comparator`, Spring `Map<String, Strategy>` injection.       |
| [State](../behavioral/state)                    | Each step of a lifecycle in its own class; the object moves itself between them.      | "First…, then…, only after…": the same call acts differently per step. | Order status enums, vending machines, Spring Statemachine. |
| [Observer](../behavioral/observer)              | Announce a change once; any number of listeners react without the announcer knowing them. | One change, many reactions.                             | Spring `@EventListener`, Kafka topics, `PropertyChangeListener`. |
| [Chain of Responsibility](../behavioral/chain-of-responsibility) | Pass a request along a chain of handlers; any of them can handle it or stop it.       | Several checks in order, any of which can say no.           | Servlet filters, Spring Security's filter chain.              |
| Command                                         | Turn a request into an object you can queue, log, retry or undo.                      | Undo/redo, job queues, "do this later".                     | `Runnable` with `ExecutorService`, job queues.                |
| Template Method                                 | A base class fixes the steps; subclasses fill in one or two.                          | Same algorithm, one step differs.                           | `HttpServlet.service` → `doGet`, `OncePerRequestFilter`, `AbstractList`. |
| Iterator                                        | Walk a collection without knowing how it's stored.                                    | Traverse lists, trees, pages of results.                    | `Iterator`, for-each, `ResultSet`, paged APIs.                |
| Mediator                                        | Parts talk to one coordinator instead of to each other.                               | Everything calls everything; N² dependencies.               | Saga orchestrators, a chat room, air-traffic control.         |
| Memento                                         | Save a sealed snapshot of an object's state so it can be restored.                    | Undo by "go back to how it was".                            | JDBC `Savepoint`, editor undo history.                        |
| Visitor                                         | Add an operation to a set of classes without editing them.                            | Many operations over a stable set of types.                 | `Files.walkFileTree` with `FileVisitor`, compiler ASTs, sealed types with `switch`. |
| Interpreter                                     | A class per grammar rule, combined into a tree that evaluates an expression.          | Users type rules or queries as text.                        | Regex, SpEL, Spring Data's derived query methods.             |
