# Sort benchmarks

This repository contains various sorting algorithms extracted from the web. TimSort is the default sorting implementation in OpenJDK when calling `Collections.sort()`.

The various test methods in `sort.SortTest` take in exactly the same formal parameters. This is so that we can run a fuzzer on one of those methods, and then replay on another sorting method. This is useful to study cross-target coverage and mutation scores.

## Fuzzing

Let's run fuzzing on Quicksort
```
mvn jqf:fuzz -Dclass=sort.SortTest -Dmethod=testQuickSort -Dout=fuzz-results
```

Kill it after a while. The fuzzer-generated input corpus should be stored in `target/fuzz-results/corpus`.

## Repro

There are multiple ways to replay the executions for different purposes.

1. Repro single method with JQF and print integers

```
mvn jqf:repro  -Dclass=sort.SortTest -Dmethod=testQuickSort -DprintArgs -Dinput=target/fuzz-results/corpus/
```

You can also give a specific input file name in `-Dinput` if you want to run only one input

2. Maven test (the repro destination is hard-coded in a static field in `sort.SortTest`; change this if your directory is different)
```
mvn test -Dtest=sort.SortTest#testQuickSort
```

### Repro with coverage

3. Maven test with coverage on command-line

There are three phases to run: (1) `prepare-agent` instruments the classes to collect coverage, (2) `test` runs the test and logs coverage data in `jacoco.exec`, (3) `report` converts the logged exec data into HTML

```
rm -f target/jacoco.exec # Remove old exec
mvn jacoco:prepare-agent test -Dtest=sort.SortTest#testQuickSort jacoco:report
```

Open results as HTML: `target/site/jacoco/index.html`

4. Run in your IDE with coverage

In IntelliJ IDEA, click on the green arrow in the left margin and choose "Run with coverage". You can do this either for the whole class `sort.SortTest` or individual test methods.

### Repro with mutaion analysis

Run PIT via command line:

```
mvn org.pitest:pitest-maven:mutationCoverage -Dtest=sort.SortTest -Dtarget=sort.\*Sort
```

The parameter `-Dtest` gives the name of a test class to run. Annoyingly, PIT does not recognize test methods; it only runs whole classes at a time. If you want to run a single method, isolate it into its own class (e.g. see `sort.TimSortTest`).

The parameter `-Dtarget` gives a wildcard pattern for the classes to perform mutations on; that is, the application classes. Here, `sort.*Sort` will match all the implementations of various sorting algorithms such as `sort.QuickSort` and `sort.TimSort`. The more classes that match this pattern, the more number of mutations PIT will perform, and the longer mutation analysis will take to run.

Results should be in `target/pitest/index.html`. 