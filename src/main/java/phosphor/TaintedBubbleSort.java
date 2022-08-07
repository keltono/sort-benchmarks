package phosphor;

import edu.columbia.cs.psl.phosphor.runtime.MultiTainter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;

import static sort.SortUtils.greater;
import static sort.SortUtils.swap;


public class TaintedBubbleSort {

  /**
   * This method implements the Generic Bubble Sort
   *
   * @param array The array to be sorted Sorts the array in ascending order
   */

  //control flow, doesn't work
  public static  Integer[] taintedSort1(Integer[] array) {
    for (int i = 0, size = array.length; i < size - 1; ++i) {
      boolean swapped = false;
      for (int j = 0; j < size - 1 - i; ++j) {
        if (greater(array[j], MultiTainter.taintedInt(array[j + 1],"TAINT1"))) {
          swap(array, j, j+1);
          swapped = true;
        }
      }
      if (!swapped) {
        break;
      }
    }
    return array;
  }

  //direct data change, works
  public  Integer[] taintedSort2(Integer[] array) {
    for (int i = 0, size = array.length; i < size - 1; ++i) {
      boolean swapped = false;
      for (int j = 0; j < size - 1 - i; ++j) {
        if (greater(array[j], array[j+1])) {
          swap(array,MultiTainter.taintedInt( j,"TAINT2"), MultiTainter.taintedInt(j+1,"TAINT8"));
          swapped = true;
        }
      }
      if (!swapped) {
        break;
      }
    }
    return array;
  }

  //control flow, doesn't work.
  public static Integer[] taintedSort3(Integer[] array) {
    for (int i = 0, size = array.length; i < size - 1; ++i) {
      boolean swapped = false;
      for (int j = 0; j < MultiTainter.taintedInt( size - 1 - i,"TAINT3"); ++j) {
        if (greater(array[j], array[j+1])) {
          swap(array, j, j+1);
          swapped = true;
        }
      }
      if (!swapped) {
        break;
      }
    }
    return array;
  }

  //j goes to affect the data directly, so this would taint the output
  //of course the actual mutation would cause an infinite loop
  //I'm going to consider this a partial success
  public static Integer[] taintedSort4(Integer[] array) {
    for (int i = 0, size = array.length; i < size - 1; ++i) {
      boolean swapped = false;
      for (int j = 0; j < size  -1 - i; MultiTainter.taintedInt(++j,"TAINT4")) {
        if (greater(array[j], array[j+1])) {
          swap(array, j, j+1);
          swapped = true;
        }
      }
      if (!swapped) {
        break;
      }
    }
    return array;
  }

  //control flow, doesn't work
  public static Integer[] taintedSort5(Integer[] array) {
    for (int i = 0, size = array.length; MultiTainter.taintedBoolean(i < size - 1, "TAINT5"); ++i) {
      boolean swapped = false;
      for (int j = 0; j < size  -1 - i; ++j) {
        if (greater(array[j], array[j+1])) {
          swap(array, j, j+1);
          swapped = true;
        }
      }
      if (!swapped) {
        break;
      }
    }
    return array;
  }
  //control flow, doesn't work
  public static Integer[] taintedSort6(Integer[] array) {
    for (int i = 0, size = array.length; i< size - 1; MultiTainter.taintedInt(++i, "TAINT6")) {
      boolean swapped = false;
      for (int j = 0; j < size - 1 - i; ++j) {
        if (greater(array[j], array[j+1])) {
          swap(array, j, j+1);
          swapped = true;
        }
      }
      if (!swapped) {
        break;
      }
    }
    return array;
  }
  //control flow, doesn't work
  public static Integer[] taintedSort7(Integer[] array) {
    for (int i = 0, size = array.length; i< size - 1;++i) {
      boolean swapped = false;
      for (int j = 0; j < size - 1 - i; ++j) {
        if (greater(array[j], array[j+1])) {
          swap(array, j, j+1);
          swapped = true;
        }
      }
      if (MultiTainter.taintedBoolean(!swapped,"TAINT7")) {
        break;
      }
    }
    return array;
  }
}
