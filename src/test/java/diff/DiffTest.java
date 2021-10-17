package diff;

import cmu.pasta.mu2.diff.Comparison;
import cmu.pasta.mu2.diff.Diff;
import cmu.pasta.mu2.diff.Mu2;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import org.junit.runner.RunWith;
import sort.BubbleSort;
import sort.TimSort;

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
        List<Integer> toReturn = new BubbleSort().sort(input);
        return toReturn;
    }

    @Diff(cmp = "noncompare")
    public List<Integer> otherBubbleSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new BubbleSort(), input);
        return null;
    }

    @Diff(cmp = "compare")
    public List<Integer> testTimSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        List<Integer> toReturn = new TimSort().sort(input);
        return toReturn;
    }

    @Diff(cmp = "noncompare")
    public List<Integer> otherTimSort(@Size(max=MAX_SIZE) List<@InRange(minInt=MIN_ELEMENT, maxInt=MAX_ELEMENT) Integer> input) {
        testSort(new TimSort(), input);
        return null;
    }

    @Comparison
    public static Boolean compare(List l1, List l2) {
        //System.out.println("comparing " + l1 + " to " + l2);
        if(l1 == null || l2 == null) return false;
        if(l1.size() != l2.size()) return false;
        for(int c = 0; c < l1.size(); c++) {
            if(!l1.equals(l2)) return false;
        }
        return true;
    }

    @Comparison
    public static Boolean noncompare(List l1, List l2) {
        //System.out.println("noncomparing " + l1 + " to " + l2);
        return true;
    }
}
