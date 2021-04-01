package sort;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.*;

@RunWith(JQF.class)
public class SortTest {
    
    protected static final int MAX_SIZE = 160;
    protected static final int MIN_ELEMENT = 0;
    protected static final int MAX_ELEMENT = 10;

    // The directory containing the fuzzer-saved inputs to replay when running with `mvn test` or the PIT plugin
    // the `repro` setting in @Fuzz is ignored when running JQF, so it won't affect fuzzing
    protected static final String DEFAULT_CORPUS_DIRECTORY = "target/fuzz-results/corpus";

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testBubbleSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new BubbleSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testCocktailShakerSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new CocktailShakerSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testCombSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new CombSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testCycleSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new CycleSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testGnomeSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new GnomeSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testHeapSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new HeapSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testInsertionSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new InsertionSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testMergeSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new MergeSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testPancakeSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new PancakeSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testQuickSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new QuickSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testSelectionSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new SelectionSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testShellSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new ShellSort(), input);
    }

    @Fuzz(repro=DEFAULT_CORPUS_DIRECTORY)
    public void testTimSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new TimSort(), input);
    }

    @Test
    public void testSomeSorts() {
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(2);
        list.add(3);
        testSort(new BubbleSort(), list);
        /*System.out.println("\nStarting a Test!\n");
        List<Integer> output = (new BubbleSort()).sort(list);
        int n = list.size();
        // Check length
        assertEquals("Length should match",
                n, output.size());
        // Check integrity
        for(Integer x : list) {
            assertTrue("Output should contain same elements as input",
                    output.contains(x));
        }
        // Check sortedness
        for (int i = 0; i < n-1; i++) {
            assertTrue(output.get(i) < output.get(i+1));
        }*/
    }

    public static <T extends Comparable<T>> void testSort(SortAlgorithm algorithm, List<T> input) {
        System.out.println("\nStarting a Test!\n");
        List<T> output = algorithm.sort(input);
        int n = input.size();
        // Check length
        assertEquals("Length should match",
                n, output.size());
        // Check integrity
        for(T x : input) {
            assertTrue("Output should contain same elements as input",
                    output.contains(x));
        }
        // Check sortedness
        for (int i = 0; i < n-1; i++) {
            assertThat(output.get(i), lessThanOrEqualTo(output.get(i+1)));
        }
    }
}
