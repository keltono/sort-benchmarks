package sort;

import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(JQF.class)
public class TimSortTest {

    @Fuzz(repro="target/fuzz-results/corpus/")
    public void testTimSort(@Size(max=SortTest.MAX_SIZE) List<@InRange(minInt=SortTest.MIN_ELEMENT, maxInt=SortTest.MAX_ELEMENT) Integer> input) {
        SortTest.testSort(new TimSort(), input);
    }
}
