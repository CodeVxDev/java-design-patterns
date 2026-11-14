# Where the patterns live in Spring and the JDK

"Where have you used it?" is the follow-up to every pattern question. Name the real code below rather than a
textbook example. A dash means there is no common example worth naming.

| Pattern                 | JDK                                                                    | Spring and its ecosystem                                                              |
| ----------------------- | ---------------------------------------------------------------------- | ------------------------------------------------------------------------------------- |
| Strategy                | `Comparator`, `RejectedExecutionHandler`                               | `Map<String, Strategy>` injection, Spring Security `PasswordEncoder`s                 |
| State                   | —                                                                      | Spring Statemachine; in practice, an enum status column with behaviour per value      |
| Observer                | `PropertyChangeListener`, `java.util.concurrent.Flow`                  | `ApplicationEventPublisher`, `@EventListener`, `@TransactionalEventListener`          |
| Chain of Responsibility | Servlet `Filter` / `FilterChain`                                       | Spring Security's `SecurityFilterChain`, MVC `HandlerInterceptor`s                    |
| Decorator               | `BufferedInputStream`, `Collections.unmodifiableList`, `synchronizedList` | `HttpServletRequestWrapper` subclasses such as `ContentCachingRequestWrapper`      |
| Proxy                   | `java.lang.reflect.Proxy`                                              | Spring AOP behind `@Transactional`, `@Async`, `@Cacheable`; Spring Data repositories; Hibernate lazy loading |
| Adapter                 | `Arrays.asList`, `InputStreamReader`                                   | Spring MVC `HandlerAdapter`                                                           |
| Facade                  | `java.nio.file.Files`                                                  | `JdbcTemplate`, `RestClient`; SLF4J                                                   |
| Mediator                | —                                                                      | Saga orchestrators; MVC's `DispatcherServlet` coordinating handlers and resolvers     |
| Singleton               | `Runtime.getRuntime()`, enum singletons                                | Singleton bean scope: one per container, not one per JVM                              |
| Factory Method          | `Calendar.getInstance()`, `List.of`, `NumberFormat.getInstance()`      | `@Bean` methods, `FactoryBean`                                                        |
| Abstract Factory        | `DocumentBuilderFactory`, JDBC `Connection`                            | `@Profile`-specific `@Configuration` classes                                          |
| Builder                 | `StringBuilder`, `HttpRequest.newBuilder()`, `Stream.builder()`        | `UriComponentsBuilder`, `RestClient.builder()`; Lombok `@Builder`                     |
| Prototype               | `clone()`, copy constructors                                           | Lombok `toBuilder` and `@With`. (Spring's *prototype scope* creates a new bean per lookup; it doesn't copy one, despite the name.) |
| Memento                 | JDBC `Savepoint`                                                       | —                                                                                     |
| Command                 | `Runnable`, `Callable` with `ExecutorService`                          | `@Scheduled` tasks, job queues                                                        |
| Template Method         | `AbstractList`, `InputStream.read(byte[])` built on `read()`          | `OncePerRequestFilter.doFilterInternal`; `HttpServlet.service` → `doGet`              |
| Iterator                | `Iterator`, `Spliterator`, for-each, `ResultSet`                       | Spring Data `Page` / `Slice`, `Stream` query results                                  |
| Composite               | AWT `Container`                                                        | Spring Boot's composite health contributors, `CompositePropertySource`               |
| Visitor                 | `Files.walkFileTree` with `FileVisitor`, `javax.lang.model` visitors  | `BeanDefinitionVisitor`; ASM's `ClassVisitor` (repackaged inside Spring)              |
| Interpreter             | `java.util.regex.Pattern`                                              | SpEL; Spring Data's derived queries (`findByNameAndCity` parsed into a query tree)    |
| Flyweight               | `Integer.valueOf` cache (-128 to 127), `Boolean.valueOf`, the String pool | —                                                                                   |
| Bridge                  | JDBC: `DriverManager` and the API on one side, drivers on the other    | —                                                                                     |
