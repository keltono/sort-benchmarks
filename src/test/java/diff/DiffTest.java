package diff;

import cmu.pasta.mu2.diff.Comparison;
import cmu.pasta.mu2.diff.Diff;
import cmu.pasta.mu2.diff.Mu2;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import org.junit.runner.RunWith;
import sort.BubbleSort;

import java.util.ArrayList;
import java.util.List;

import static sort.SortTest.testSort;

@RunWith(Mu2.class)
public class DiffTest {
    protected static final int MAX_SIZE = 160;
    protected static final int MIN_ELEMENT = 0;
    protected static final int MAX_ELEMENT = 10;

    @Diff(cmp = "compare")
    public List<Integer> testBubbleSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        return new BubbleSort().sort(input);
    }

    @Comparison
    public Boolean compare(List l1, List l2) {
        if(l1 == null || l2 == null) return false;
        if(l1.size() != l2.size()) return false;
        for(int c = 0; c < l1.size(); c++) {
            if(!l1.equals(l2)) return false;
        }
        return true;
    }
    //TODO add integration test

    //@Test
    public void dummyTest() {
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(2);
        list.add(3);
        testSort(new BubbleSort(), list);
    }
}
