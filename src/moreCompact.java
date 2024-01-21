import java.util.Arrays;

public class moreCompact {
    private static int[] array;
    private static int[] buffer;
    private static int[] indices;
    private static int[] powers;    //buffer for the powers, only there to safe some multiplications, not essential


    public static void sort(int[] input) {
        array = input;
        buffer = new int[array.length];
        indices = new int[array.length];
        powers = new int[array.length];
        initialPartition();
        partialSort(0,Integer.MAX_VALUE);
        //System.out.println(partitions);
    }

    //scaledTo passes the biggest value that could have been placed into the current subarray
    private static int partialSort(int begin, int scaledTo) {

        //last element or subarray contains only one element
       if (begin == array.length - 1 || array[begin+1] > scaledTo) {return begin + 1;};

        int min = array[begin];
        int max = min;

        //determine the maximum/minimum
        int value;
        int end = begin + 1;
        while (end < array.length && array[end] <= scaledTo) {
            value = array[end];
            if (value < min) {
                min = value;
            } else if (value > max) {
                max = value;
            }
            end++;
        }

        if (max == min) { return end; }

        int key;
        for (int i = begin; i < end; i++) {
            key = linearScaler(array[i], min, max, array.length);
            indices[key]++;
        }

        //determine offsets:
        int newOffset = end;
        for (int i = indices.length - 1; i >= 0; i--) {
            indices[i] = newOffset - indices[i];
            newOffset = indices[i];
        }


        //categories the values
        for (int i = begin; i < end; i++) {
            key = linearScaler(array[i], min, max, array.length);
            buffer[indices[key]++] = array[i];
        }

        //store values back
        System.arraycopy(buffer, begin, array, begin, end - begin);

        //wipe indices array
        Arrays.fill(indices, 0);


        //partitions++;
        //Helpers.printArr(array);

        int beginIndex = begin;
        int i = 0;
        while (beginIndex < end) {
            beginIndex = partialSort(beginIndex, calcMax(min, max, i+1) - 1); //key calcMax returns min of i + 1 -> min - 1 = max of i
            i++;
        }
        return end;
    }

    //partitions the array according to the powers of n on initial call of the function if range of values >= n²
    private static void initialPartition () {
        int min = array[0];
        int max = min;
        //determine the maximum/minimum
        int value;
        for (int k : array) {
            value = k;
            if (value < min) {
                min = value;
            } else if (value > max) {
                max = value;
            }
        }

        int log = calcPowers(max - min + 1);

        if (log > 1) {
            int key;
            for (int j : array) {
                key = ScalingSort.exponentialScaler(j, powers);
                indices[key]++;
            }

            //determine offsets, only the indices for existing key-values are != 0, so we can efficiently delete values later
            int newOffset;
            int prevValues = indices[0];
            for (int i = 1; i < indices.length; i++) {
                if (indices[i] != 0) {
                    newOffset = indices[i] + prevValues;
                    indices[i] = prevValues;
                    prevValues = newOffset;
                }
            }
            indices[0] = 0;

            //partition the array
            for (int j : array) {
                key = exponentialScaler(j, powers);
                buffer[indices[key]++] = j;
            }

            //store values back
            System.arraycopy(buffer, 0, array, 0, array.length);

            //wipe indices array
            Arrays.fill(indices, 0);

            //partitions++;
            //Helpers.printArr(array);

            int beginIndex = 0;
            int i = 0;
            while (beginIndex < array.length) {
                beginIndex = partialSort(beginIndex,powers[i] - 1);
                i++;
            }

        } else {
            partialSort(0, Integer.MAX_VALUE);
        }
    }

    //calculates the log n (range of values) and caches the powers
    private static int calcPowers (int valueRange) {
        int factor = array.length;
        int base = array.length;
        int log = 0;
        while (factor <= valueRange && factor > 0) {
            powers[log] = factor;
            factor *= base;
            log++;
        }
        return log;
    }

    //calculates key -> the smallest value that was placed into the bucket
    //-> i + 1 calculates the max + 1 value of the i-subarray
    private static int calcMax(int min, int max, int key) {
        int buf = (max - min + 1) * key;
        buf = buf / array.length;
        buf = buf + min;

        return buf + 1;
    }

    //same principle as the linearScaler, key = 0 ->  val < 1, key = 1 -> val < 2,...
    public static int exponentialScaler(int value, int[] powers) {
        int i = 0;
        while (powers[i] > 0 && value > powers[i]) {
            i++;
        }
        return i;
    }

    public static int linearScaler(int value, int min, int max, int n) {

        long key;
        long scaler = max - min + 1;    //-> range of values

        key = (long) (n) * (long) (value - min);    //cast to long in case result of multiplication is > Integer.Max
        key = key / scaler;

        return (int) key;
    }
}
