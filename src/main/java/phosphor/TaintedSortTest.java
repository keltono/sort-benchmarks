package phosphor;

import edu.columbia.cs.psl.phosphor.runtime.MultiTainter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;
import org.junit.Assert;
import org.junit.Test;

public class TaintedSortTest {

    @Test
    public void testBubbleSort1() {
        //sorted
        Integer[] input = new Integer[]{1, 2,3, 4, 5, 6, 7, 8, 9};
        Integer[] out = TaintedBubbleSort.taintedSort1(input);
        Assert.assertFalse(isOutputTainted(out));
        //random
        out = TaintedBubbleSort.taintedSort1(new Integer[]{-16491, -99351, 209611, 534797, 518453, 192217, -421941, 263207, 460552, 250216});
        // would be true if control tracking was set, but that is not implemented for the new version.
        Assert.assertFalse(isOutputTainted(out));
    }
    @Test
    public void testBubbleSort2(){
        TaintedBubbleSort bubbleSort = new TaintedBubbleSort();
        //sorted
        Integer[] out = bubbleSort.taintedSort2(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Assert.assertFalse(isOutputTainted(out));
        //random
        out = bubbleSort.taintedSort2(new Integer[]{-16491, -99351, 209611, 534797, 518453, 192217, -421941, 263207, 460552, 250216});
        Assert.assertTrue(isOutputTainted(out));
    }
    @Test
    public void testBubbleSort3(){
        //sorted
        Integer[] out = TaintedBubbleSort.taintedSort3(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Assert.assertFalse(isOutputTainted(out));
        //random
        out = TaintedBubbleSort.taintedSort3(new Integer[]{-16491, -99351, 209611, 534797, 518453, 192217, -421941, 263207, 460552, 250216});
        // would be true if control tracking was set, but that is not implemented for the new version.
        Assert.assertFalse(isOutputTainted(out));
    }
    @Test
    public void testTimSort1(){
        //sorted
        Integer[] out = TaintedBubbleSort.taintedSort3(new Integer[]{-16491, -99351, 209611, 534797, 518453, 192217, -421941, 263207, 460552, 250216});
        // would be true if control tracking was set, but that is not implemented for the new version.
        Assert.assertFalse(isOutputTainted(out));
    }

    public boolean isOutputTainted(Integer[] input){
        for(int i = 0; i < input.length; i++){
            System.out.println("checking taint for index " + i + ", which is " + input[i]);
            Taint taint = MultiTainter.getTaint(input[i]);
            if (taint == null){
                continue;
            }
            System.out.println(taint);
            if(!taint.isEmpty()){
                return true;
            }
        }
        return false;
    }
}

