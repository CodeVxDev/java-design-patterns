# Java design patterns: runnable, with Spring and the pitfalls

The code behind the [CodeVxDev](https://codevx.dev) design-pattern videos
([YouTube](https://youtube.com/@CodeVxDev) · [Instagram](https://instagram.com/codevxdev)), and a revision guide
for interviews.

Every pattern here:

- **runs**: the plain versions with `java Demo.java`, no build tool needed; the Spring versions as real Spring
  Boot apps;
- shows **the code before the pattern**, the pattern **by hand**, and the version you'll meet **in production**
  (Spring, or the JDK where Spring has nothing to add);
- **proves its pitfalls with tests**: each one shows the failure first, then the fix, so `./mvnw test` reads like
  a checklist;
- has a **one-page README** to revise from: intent, diagram, pitfalls, the patterns it gets confused with,
  interview questions, and a 30-second answer.

## Revise in this order

1. [Cheat sheet](docs/cheat-sheet.md): all 23 patterns, one line each.
2. [Commonly confused patterns](docs/confusions.md): the pairs interviewers love, and the one question that
   separates each pair.
3. A pattern's README, then its `PitfallsTest`.
4. [Answering a pattern question](docs/answering-in-interviews.md): how to structure the answer, and the cues that
   point to each pattern in an LLD round.
5. [Where they live in Spring and the JDK](docs/in-spring-and-jdk.md): the real-world examples to name.

## The patterns

| #   | Pattern                                 | Category   | Often confused with     | Code | Video                                          | Post                                                        |
| --- | --------------------------------------- | ---------- | ----------------------- | ---- | ---------------------------------------------- | ----------------------------------------------------------- |
| 1   | [Strategy](behavioral/strategy)         | Behavioral | State, Template Method  | ✓    | [Watch](https://youtube.com/shorts/5QN0onacLTc) | [Read](https://codevx.dev/post/strategy-pattern-in-java)    |
| 2   | [State](behavioral/state)               | Behavioral | Strategy                | ✓    | [Watch](https://youtube.com/shorts/IPporXbhNcc) | [Read](https://codevx.dev/post/state-pattern-in-java)       |
| 3   | [Observer](behavioral/observer)         | Behavioral | Chain, Mediator, pub/sub | ✓    | Soon                                           | [Read](https://codevx.dev/post/observer-pattern-in-java)    |
| 4   | Chain of Responsibility                 | Behavioral | Observer, Decorator     |      |                                                |                                                             |
| 5   | Decorator                               | Structural | Chain, Proxy            |      |                                                |                                                             |
| 6   | [Proxy](structural/proxy)               | Structural | Decorator, Adapter      | ✓    | Soon                                           | [Read](https://codevx.dev/post/proxy-pattern-in-java)       |
| 7   | Adapter                                 | Structural | Proxy, Facade, Bridge   |      |                                                |                                                             |
| 8   | Facade                                  | Structural | Adapter, Mediator       |      |                                                |                                                             |
| 9   | Mediator                                | Behavioral | Facade, Observer        |      |                                                |                                                             |
| 10  | Singleton                               | Creational | Factory Method, Flyweight |    |                                                |                                                             |
| 11  | Factory Method                          | Creational | Singleton, Abstract Factory |  |                                                |                                                             |
| 12  | Abstract Factory                        | Creational | Factory Method          |      |                                                |                                                             |
| 13  | Builder                                 | Creational | Setters, Prototype      |      |                                                |                                                             |
| 14  | Prototype                               | Creational | Builder, Memento        |      |                                                |                                                             |
| 15  | Memento                                 | Behavioral | Prototype, Command      |      |                                                |                                                             |
| 16  | Command                                 | Behavioral | Memento, Strategy       |      |                                                |                                                             |
| 17  | Template Method                         | Behavioral | Strategy                |      |                                                |                                                             |
| 18  | Iterator                                | Behavioral | —                       |      |                                                |                                                             |
| 19  | Composite                               | Structural | Decorator, Visitor      |      |                                                |                                                             |
| 20  | Visitor                                 | Behavioral | Composite               |      |                                                |                                                             |
| 21  | Interpreter                             | Behavioral | Composite               |      |                                                |                                                             |
| 22  | Flyweight                               | Structural | Singleton               |      |                                                |                                                             |
| 23  | Bridge                                  | Structural | Adapter, Strategy       |      |                                                |                                                             |

The order follows the video series, where each episode ends on an interview trap the next one answers. The
[cheat sheet](docs/cheat-sheet.md) already covers all 23; the code arrives with each episode.

## Run it

You need **JDK 25**. Nothing else: the Maven wrapper downloads Maven on first use.

```console
./mvnw test                                   # every pattern's tests
./mvnw -pl behavioral/observer test           # one pattern
./mvnw -pl behavioral/observer spring-boot:run   # one pattern's Spring demo
```

The plain versions need no build at all. From a pattern's folder:

```console
cd behavioral/strategy
java src/main/java/dev/codevx/patterns/strategy/handrolled/Demo.java
```

## How a pattern is laid out

```
behavioral/observer/
├── README.md                    the revision card
└── src/
    ├── main/java/dev/codevx/patterns/observer/
    │   ├── before/              the code the pattern fixes
    │   ├── handrolled/          the pattern in plain Java, with a Demo
    │   └── spring/              the production version (or jdk/ when the JDK has it, enums/ for State)
    └── test/java/dev/codevx/patterns/observer/
        ├── HandRolledTest.java  what the pattern buys you
        ├── SpringTest.java      the Spring version working
        └── PitfallsTest.java    each pitfall: the failure, then the fix
```

Each package is self-contained (no imports across `before/`, `handrolled/` and `spring/`), which is what lets
`java Demo.java` run a plain version straight from source.

## License

[MIT](LICENSE). Use the code in your projects, interviews and notes.
