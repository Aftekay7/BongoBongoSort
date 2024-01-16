/**
 * Optimized version of the one and only BongoBongoSort
 * Sorts Array by dividing Array into subarrays and ordering the keys by scaling them in relation to
 * the minimum and maximum of the subarray
 * Sorts Arrays in best-case: O(n), average-case O(n), worst-case O(n²)
 */
public class ScalingSort {

    /**
     * counts how many times the arrays is scaled or how deep the recursion-tree is. (the last iteration is ignored)
     */
    private static int scalingCount = 0;

    /**
     * Input array that has to be sorted
     */
    private static int[] array;


    /**
     * copy of the input array
     */
    private static int[] buffer;


    /**
     *
     */
    private static  int limit = 0;

    /**
     * initializes the sort algorithm
     * @param input
     */

    public static void sort(int[] input) {
        scalingCount = 0;
        array = input;
        buffer = new int[array.length];

        limit = array.length/2;

        partialSort(0, array.length);

       //System.out.println(scalingCount);
    }

    /**
     * sorts the (sub-)array between params
     * @param begin (inclusive) index of (sub-)array
     * @param end (exclusive) index of (sub-)array
     */
    private static void partialSort(int begin, int end) {
        //illegal array sizes
        if (begin >= end || begin < 0) {
            return;
        }

        //array is sorted as it contains only 1 element or is empty
        if (begin + 1 >= end) {
            return;
        }

        int min = array[begin];
        int max = min;

        //determine the maximum/minimum
        int value;
        for (int i = begin + 1; i < end; i++) {
            value = array[i];
            if (value < min) {
                min = value;
            } else if (value > max) {
                max = value;
            }
        }

        //array is already sorted as it contains only equal elements
        if (min == max) {
            return;
        }

        //calculate the count of possible values
        int valueRange = max - min + 1;


        //n = array.length -> more storage intensiv, a bit slower but fewer steps as with n = end - begin
        //int pow_2 = array.length * array.length;
        //int n = end - begin;
        int n = array.length;



        /*
            check if range of values is still too large for an efficient linear scaling.
            -> "exponential scale" is only useful on the first iteration, if (original) range of values is greater than n²
            -> once, as we usually split in small subarrays, on which linearScale works faster (Didn't do the math yet, but behaved like this in edge cases)
            -> multiple uses of exp. scale is unlikely as range of values had to exceed n^n, which is impossible
            with current data types after a very small (original) array length.
            -> generic approach using the lengths of the sub-arrays for the exp. scale works too, but might take more steps than
            using just the linear scale.
         */
        if ( max / n > min && n > 5){
            //value range is still very big -> another exponential partition might be beneficial
            int[] powers = new int[array.length];

            int factor = n;
            int base = n;
            int log = 0;
            while (factor < valueRange && factor > 0) {
                powers[log] = factor;
                factor *= base;
                log++;
            }
            exponentialPartition(log,begin,end,powers);

        } else {

            //value range is small enough to sort with linearScaling
            linearPartition(min, max, n, begin, end);
        }


/*

        int buf = array[begin];
        int current = begin;
        int insert;
        /*
            sorts array without the need of a buffer array as it just switches the elements. NOT stable

        while (current < end) {
            value = buf;
            key = linearScaler(value, min, max, n);
            insert = copyIndices[key]++;

            if (insert == current) {
                current++;
            } else {
                buf = array[insert];
                array[insert] = value;
                current = insert;
            }
        }

        */

    //Helpers.printArr(array);
    //scalingCount++;


}


    /**
     * @param value value that has to be scaled
     * @param min   minimum of (sub-)array
     * @param max   maximum of (sub-)array
     * @param n     length or number values of the (sub-)array
     *              - let A be a set:    For all x in A: 0 <= x - min <= max - min
     *              - function shifts value by the minimum so that key(max) = n-1 and key(min) = 0
     *              - function calculates the (relativ) position of the value in the set A and maps this to the array-indices
     *              -> key(value) = 0 <=> value - min < 1/value_count
     *              key(value) = 1 <=> value - min < 2/value_count & >= above
     *              key(value) = 2 <=> value - min < 3/value_count & >= above
     *              ...
     *              key(value) = n-1 <=> value = max (as (value - min) = value_range -> cancelled)
     *              <p>
     *              the key resembles in which (0 - n-1) percent the value (-min) in set A is or
     *              the (minimum) number of smaller values than value (-min)
     *              <p>
     *              -> if value_count < value_range, this function is (only) surjective, but as soon as value_count >= value_range
     *              it becomes bijective -> values will be sorted after interpreting key-values as indices
     */
    private static int linearScaler(int value, int min, int max, int n) {

        long key;
        long scaler = max - min + 1;    //-> range of values

        key = (long) (n) * (long) (value - min);    //cast to long in case result of multiplication is > Integer.Max
        key = key / scaler;

        return (int) key;
    }


    /**
     * @param value   that has to be scaled
     * @return scaled value
     */
    private static int exponentialScaler(int value, int[] powers) {
        int i = 0;
        while (powers[i] > 0 && value > powers[i]) {
            i++;
        }
        return i;
    }


    /**
     * @param log     logarithm of the value-range to the base of n
     * @param begin   index
     * @param end     index
     *                partitions the array into log + 1 partitions and scales the values according to @func exponentialScaler
     */
    private static void exponentialPartition(int log, int begin, int end, int[] powers) {

        int[] copyIndices = new int[log + 1];

        //count the keys per partition
        int key;
        for (int i = begin; i < end; i++) {
            key = exponentialScaler(array[i], powers);
            copyIndices[key]++;
        }

        //determine offsets:
        int newOffset = end;
        for (int i = copyIndices.length - 1; i >= 0; i--) {
            copyIndices[i] = newOffset - copyIndices[i];
            newOffset = copyIndices[i];
        }

        //partition the array
        for (int i = begin; i < end; i++) {
            key = exponentialScaler(array[i], powers);
            buffer[copyIndices[key]++] = array[i];
        }

        //store values back
        for (int index = begin; index < end; index++) {
            array[index] = buffer[index];
        }

        scalingCount++;
        //Helpers.printArr(array);


        int beginIndex = begin;
        int endIndex;
        for (int i = 0; i < copyIndices.length; i++) {
            endIndex = copyIndices[i];
            partialSort(beginIndex, endIndex);
            beginIndex = endIndex;
        }
    }


    /**
     * @param min   of (sub-)array
     * @param max   of (sub-)array
     * @param n     length of (sub-)array
     * @param begin index of (sub-)array
     * @param end   index of (sub-)array
     *              <p>
     *              partitions the array into n partitions and scales the values according to @function linearScaler
     */
    private static void linearPartition(int min, int max, int n, int begin, int end) {

        //stores the count of each key (or how many values are assigned to each index) and later the Copy-Indices
        int[] copyIndices = new int[n];

        //determines the count of each key
        int key;
        for (int i = begin; i < end; i++) {
            key = linearScaler(array[i], min, max, n);
            copyIndices[key]++;
        }


        //determine offsets:
        int newOffset = end;
        for (int i = copyIndices.length - 1; i >= 0; i--) {
            copyIndices[i] = newOffset - copyIndices[i];
            newOffset = copyIndices[i];
        }


        //categories the values
        for (int i = begin; i < end; i++) {
            key = linearScaler(array[i], min, max, n);
            buffer[copyIndices[key]++] = array[i];
        }

        //store values back
        for (int index = begin; index < end; index++) {
            array[index] = buffer[index];
        }

        scalingCount++;
        //Helpers.printArr(array);

        int beginIndex = begin;
        int endIndex;
        for (int i = 0; i < copyIndices.length; i++) {
            endIndex = copyIndices[i];
            partialSort(beginIndex, endIndex);
            beginIndex = endIndex;
        }
    }

}
