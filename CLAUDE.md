# java-design-patterns

The runnable code behind the CodeVx design-pattern videos (the blog and video engine live in the separate
`CodeVxBlog` repo), and a revision guide for interview candidates. Public: write for a reader who found it from a
video or a search, not for us.

## Layout and rules

- One Maven module per pattern under `creational/`, `structural/` or `behavioral/`, listed in the root `pom.xml`.
  Not numbered: the series order can change.
- Packages per module: `before/` (the code the pattern fixes), `handrolled/` (plain Java), then the production
  version: `spring/` when Spring has a real one, `jdk/` when the JDK has it, or another honest name (`enums/` for
  State). Don't force a Spring version where none exists.
- Every package except `spring/` is self-contained (no imports from sibling packages) and has a `Demo` main, so
  `java src/main/java/.../Demo.java` runs it with no build. `scripts/run-demos.sh` checks all of them.
- Tests: `HandRolledTest`, `SpringTest`, `PitfallsTest` (and `SpringPitfallsTest` when the Spring pitfalls need
  their own listeners or config). Every test has a full-sentence `@DisplayName`. Pitfall tests carry the
  README's number (`"2. Pitfall: …"`, then `"2. Fix: …"` when the fix is code) and run in that order
  (`@TestMethodOrder(OrderAnnotation)` + `@Order`). A pitfall in a README is either tested or listed under "Not
  in the tests".
- Versions come from the Spring Boot parent in the root `pom.xml` (exact version); add no versions in modules.
  Java 25. No Lombok: write the constructor and say in a comment that `@RequiredArgsConstructor` generates it.
- Code in a module should match the episode's on-screen code where it can (`CodeVxBlog/episodes/<slug>/code/`),
  and fix the shortcuts the video took for time (say so in a comment).

## A pattern's README (the revision card)

Sections, in order: header line (category · episode · video · post), the intent as a quote, The problem, The
pattern (Mermaid class diagram), The code (table of packages with run commands), Pitfalls (numbered, each linked to
its test), Often confused with, Interview questions (ending with the episode's trap and its answer), Say it in 30
seconds, Spot it in LLD interviews. Keep it to a 2-minute read.

## Adding a pattern

1. Copy the closest module, rename the packages, add the module to the root `pom.xml`.
2. Write the code and tests, then the README.
3. Update the root `README.md` table (Code ✓, video and post links), and check `docs/cheat-sheet.md`,
   `docs/confusions.md` and `docs/in-spring-and-jdk.md` still match.
4. `./mvnw verify && ./scripts/run-demos.sh` must pass (CI runs both). `mvnw` is pinned to Maven 3.9.16;
   the test tree output comes from `maven-surefire-junit5-tree-reporter` (root `pom.xml`).
