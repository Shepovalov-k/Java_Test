# Java Test Project

Minimal Maven Java project with a sample `HelloApp` and unit test.

Build:

```bash
mvn -B -DskipTests=false package
```

Run (after `mvn package`):

```bash
java -cp target/classes com.example.HelloApp
```

Run tests:

```bash
mvn test
```
