# Assignment 6 - Visitor Pattern

In-memory file system. Java 17 and Maven.

Student: Kuatbay Merey  
Group: SE-2537

[GitHub repository](https://github.com/bossmerej-ship-it/SDP-6)

## Build and run

Install JDK 17 and Maven. Run these commands from the repository root:

```text
mvn test
mvn package
java -jar target/visitor-pattern-1.0.jar
```

The application needs no input. It prints the same result on every run:

```text
Root: workspace
Total size: 1800 bytes
Search: suffix=.txt, minimum=200 bytes
assets/drafts/outline.txt
docs/guide.txt
Files: 7
Directories: 5
Maximum depth: 3
```

To change the search, pass a case-sensitive suffix and an inclusive minimum byte size:

```text
java -jar target/visitor-pattern-1.0.jar .md 100
```

This finds `docs/readme.md`. The size and statistics still cover the whole tree. The first Maven build downloads its plugins and JUnit; the application itself has no external libraries.

## Example data

```text
workspace/
  notes.txt (120)
  assets/
    logo.png (800)
    drafts/
      outline.txt (240)
      todo.txt (80)
  docs/
    guide.txt (400)
    readme.md (160)
  empty/
  zero.bin (0)
```

Numbers are byte sizes. This is an in-memory model; it does not read or change real files.

## Design

Ten small application classes/interfaces use the standard Maven layout:

- `Element`, `Node`, `FileNode`, `DirectoryNode` - data, names and ownership.
- `Visitor` - the two overloaded `visit` methods.
- `TreeVisitor` - shared traversal, path/depth context and reset before each scan.
- `TotalSizeVisitor`, `SearchVisitor`, `StatisticsVisitor` - independent operations.
- `Main` - builds the example tree and prints results.

The visitor owns traversal. `accept` only calls `visitor.visit(this)` and never walks children. `TreeVisitor.visit(DirectoryNode)` visits each child once. Statistics calls that inherited traversal after counting the directory.

Start each operation with `visitor.scan(root)`, then read its getters. Instances are reusable for sequential scans: results, path and depth reset every time. Use separate instances for concurrent work, and read results only after a successful scan.

Search accepts any `Predicate<String>` over a filename and a non-negative minimum size. Both conditions must match. Paths omit the chosen directory root, use `/`, and are sorted lexicographically. A file used as the root is returned by its own name. Root depth is zero; each child adds one, including empty directories.

Names are non-blank, case-sensitive path segments; `.`, `..`, `/` and `\` are rejected. Files reject negative sizes. `addChild` rejects duplicate sibling names, an existing parent and ancestor/self cycles before changing the tree. Children and search results are immutable snapshots. Null children/criteria are rejected with `NullPointerException`; other invalid values/mutations use `IllegalArgumentException`.

Sizes use `long`; a total above `Long.MAX_VALUE` throws `ArithmeticException`. Traversal is recursive and limited by the JVM stack for extremely deep trees.

## Extend an operation

Add a subclass of `TreeVisitor`, implement `reset()` and `visit(FileNode)`, and add result getters. To collect directory data, override `visit(DirectoryNode)` and call `super.visit(directory)` once. `StatisticsVisitor` was added this way in its own commit; neither element nor its `accept` method changed.

A new element type has a wider cost: add its overloaded method to `Visitor` and implement it in the concrete visitors. Update traversal if the new type contains children.

## Tests and report

`mvn test` runs **30 JUnit 5 tests** in `ElementTest`, `TotalSizeVisitorTest`, `SearchVisitorTest`, and `StatisticsVisitorTest`. They cover dispatch, nested totals, combined search conditions, relative/sorted paths, statistics, empty trees, repeat scans and invalid mutations.

[PDF report](Assignment_6_Visitor_Pattern_Report.pdf) contains three report pages and a compact UML diagram on page 2, all in portrait A4. [Markdown report](Assignment_6_Visitor_Pattern_Report.md) contains the same text. The diagram is supplied as [PNG](docs/visitor-uml.png), [SVG](docs/visitor-uml.svg), and [PlantUML source](docs/visitor-uml.puml).
