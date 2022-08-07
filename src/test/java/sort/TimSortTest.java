package sort;

import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(JQF.class)
public class TimSortTest {

    @Fuzz
    public void fuzzTimSort(@Size(max=SortTest.MAX_SIZE) List<Integer> input) {
        SortTest.testSort(new TimSort(), input);
    }
}
