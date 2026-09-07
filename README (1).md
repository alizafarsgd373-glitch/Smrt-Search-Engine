# Smart Article Search System (Java)

A Java port of the original C++ "Smart Article Search System" project.

## Project structure

```
SmartSearchJava/
├── src/
│   ├── Main.java                        (menu-driven driver, was main.cpp)
│   ├── system/
│   │   └── SmartSearchSystem.java       (was System/SmartSearchSystem.h/.cpp)
│   └── structures/
│       ├── Article.java                 (was Structures/Article.h)
│       ├── HashMap.java                 (was Structures/HashMap.h)
│       ├── Stack.java                   (was Structures/Stack.h)
│       ├── Queue.java                   (was Structures/Queue.h)
│       └── PriorityQueue.java           (was Structures/PriorityQueue.h)
└── Data/
    └── Articles.txt                     (sample article data)
```

## Note on the custom data structures

The original C++ project's `HashMap`, `Stack`, `Queue`, and `PriorityQueue`
implementations (their `.h`/`.cpp` bodies) were not available when this port
was written — only `SmartSearchSystem.h`/`.cpp` and `main.cpp` were provided.
The Java versions here were written from scratch to match the exact
**interface and observable behavior** that `SmartSearchSystem.cpp` relies on:

- `Stack`: LIFO of search queries (`push`, `empty`, `display`)
- `Queue`: FIFO of article IDs, used for recent articles and BFS
  (`enqueue`, `dequeue`, `front`, `empty`, `size`, `getData`)
- `HashMap`: separate-chaining hash table mapping `word -> per-article
  frequency` (`insert(word, articleId)`, `getFrequency(word)`)
- `PriorityQueue`: binary max-heap of `{score, articleId}` pairs, used to
  rank search results (`push`, `top`, `pop`, `empty`)

If your original structures had different internal behavior (e.g. a
different hash function, tie-breaking rule, or capacity limit), the exact
output could differ slightly even though the overall program behaves the
same way.

## Build & run

From the `SmartSearchJava` directory:

```bash
javac -d out src/Main.java src/system/SmartSearchSystem.java src/structures/*.java
java -cp out Main
```

Make sure you run the `java` command from a directory where `Data/Articles.txt`
is reachable (same as the original C++ program, which reads it as a relative
path).

## Menu

```
1. Search Articles
2. Read Article
3. Search History
4. Recently Viewed
5. Add Bookmark
6. Show Bookmarks
7. Discover Related Articles
8. Display Articles
0. Exit
```
