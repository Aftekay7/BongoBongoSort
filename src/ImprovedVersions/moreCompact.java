package ImprovedVersions;

import Util.Helpers;

import java.util.Arrays;

public class moreCompact {
    private static int[] array;     //original and output array
    private static int[] buffer;    //buffer for the sorting process
    private static int[] indices;   //lowest index of each subarray (or "bucket")
    private static int[] scalingValues;    //buffer for the keys, saves multiplications, not essential
    private static int n_2; //stores the value n^2, safes multiplications, not essential
    private static int n;   //length of array


    public static void sort(int[] input) {
        init(input);
        partialSort(0, n - 1);
        //System.out.println(partitions);
    }

    /**
     * sorts the partial subarray or splits it into further subarrays
     *
     * @param begin index of the first element in this subarray
     * @param end   index of the last element of this subarray
     * @return end + 1 (first index of the next subarray)
     */
    private static int partialSort(int begin, int end) {

        //check if subarray contains less than 4 elements -> can be sorted easily
        int c = end - begin;
        if (c < 3) {
            simpleCases(c, begin);
            return end + 1;
        }

        //get the metainformation of the subarray
        int min = getMin(begin, end);
        int max = getMax(begin, end);
        int valueRange = max - min + 1;

        //subarray only contains equal elements
        if (max == min) {
            return end + 1;
        }

        /*
            array has to be partitioned further
            -> decide which is the most effective way
         */

        int[] keys;

        //last partition -> (values - min) are the keys
        if (valueRange <= n) {
            keys = new int[valueRange];
            simpleKeys(min, max, keys);
            scalingValues = keys;

            //second last partition -> array can be split into (n-1) subarrays
        } else if (valueRange < n_2) {
            keys = new int[n];
            calcScalars(min, max, valueRange, keys);
            scalingValues = keys;

            //value-range is too big for an efficient linear partition -> categorize according to the powers of n
        } else {
            keys = new int[n];
            calcPowers(max, keys);
            scalingValues = keys;
        }

        //count the used keys to determine the size of the several subarrays
        int key;
        for (int i = begin; i <= end; i++) {
            key = getKey(array[i]);
            indices[key]++;
        }

        //determine offsets of the subarrays
        int newOffset = end + 1;
        for (int i = indices.length - 1; i >= 0; i--) {
            indices[i] = newOffset - indices[i];
            newOffset = indices[i];
        }

        //categories the values
        for (int i = begin; i <= end; i++) {
            key = getKey(array[i]);
            buffer[indices[key]++] = array[i];
        }

        //store values back
        System.arraycopy(buffer, begin, array, begin, end - begin + 1);
        Helpers.printArr(array);

        //wipe indices array
        Arrays.fill(indices, 0);

        //sort the new subarrays
        int beginIndex = begin;
        while (beginIndex <= end) {
            //getScaledTo returns min of the next bucket ->
            beginIndex = partialSort(beginIndex, getEnd(array[beginIndex], beginIndex, end));
            scalingValues = keys;
        }
        return end + 1;
    }


    /**
     * calculates the log of max and the powers of n according to which the values are later sorted.
     * powers are stored in the keys array
     *
     * @param max of the subarray
     */
    private static void calcPowers(int max, int[] keys) {
        int factor = array.length;
        int base = array.length;
        int log = 0;
        while (factor <= max && factor > 0) {
            keys[log] = factor;
            factor *= base;
            log++;
        }
        if (log < n) {
            keys[log] = max + 1;
        }
    }

    /**
     * calculates the min values of the buckets and stores them in the "keys"-array
     *
     * @param min        of the subarray
     * @param max        of the subarray
     * @param valueRange of the subarray
     */
    private static void calcScalars(int min, int max, int valueRange, int[] keys) {
        int bucketSize = valueRange / n;

        if (valueRange % n != 0) {
            bucketSize++;
        }
        ;

        int keyVal = min;
        for (int i = 0; i < n - 1; i++) {
            keyVal += bucketSize;
            keys[i] = keyVal;
        }

        keys[n - 1] = max + 1;
    }


    /**
     * initializes all the additional needed memory
     *
     * @param input passes the input array
     */
    private static void init(int[] input) {
        array = input;
        n = array.length;
        buffer = new int[n];

        //at least int[50] needed in case array is very small but range of values is very large.
        /*
        if (n > 50) {
            keys = new int[n];
        } else {
            keys = new int[50];
        }
         */
        indices = new int[n];
        n_2 = n * n;
    }


    /**
     * @param begin startindex of the (sub)array
     * @param end   endIndex of the next (sub)array
     */
    private static int getMin(int begin, int end) {
        int min = array[begin];

        while (begin < end) {

            if (array[begin] < min) {
                min = array[begin];
            }
            begin++;
        }
        return min;
    }


    /**
     * @param begin startindex of the (sub)array
     * @param end   last index of the subarray
     */
    private static int getMax(int begin, int end) {
        int value;
        int max = array[begin];

        while (begin <= end) {
            value = array[begin];
            if (value > max) {
                max = value;
            }
            begin++;
        }
        return max;
    }


    /**
     * determines the key of value by comparing value to the keys-array
     *
     * @param value according to which the key is returned
     * @return key of value
     */
    public static int getKey(int value) {
        int mid = scalingValues.length / 2;
        int h = scalingValues.length;
        int l = 0;

        if (scalingValues[scalingValues.length - 1] != 0 && value >= scalingValues[scalingValues.length - 2]) {
            return scalingValues.length - 1;
        }

        if (value < scalingValues[0]) {
            return 0;
        }

        while (l <= h) {
            if (scalingValues[mid] == 0) {
                h = mid - 1;
                mid = l + ((h - l) / 2);

            } else if (value >= scalingValues[mid]) {

                if (value < scalingValues[mid + 1]) {
                    return mid + 1;
                } else {
                    l = mid + 1;
                    mid = l + ((h - l) / 2);
                }

            } else {
                if (value >= scalingValues[mid - 1]) {
                    return mid;
                }
                h = mid - 1;
                mid = h - ((h - l) / 2);
            }
        }
        return -1;  //interval not found
    }

    /**
     * calculates the keys for the case that the valueRange < n, stores the keys in the keys array
     *
     * @param min of the subarray
     * @param max of the subarray
     */
    private static void simpleKeys(int min, int max, int[] keys) {
        int i = 0;
        while (min <= max) {
            min++;
            keys[i] = min;
            i++;
        }
    }

    /**
     * returns the index
     *
     * @return the min of the next bucket
     */
    private static int getEnd(int value, int begin, int end) {
        int bucket = getKey(value);
        int scaledTo = scalingValues[bucket];

        while (begin < end && array[begin + 1] < scaledTo ) {
            begin++;
        }
        return begin;
    }

    /**
     * handels the simple cases if the subarray contains less than 4 elements
     *
     * @param buf   length of the subarray
     * @param begin index of the subarray
     */
    private static void simpleCases(int buf, int begin) {
        if (buf == 0) {
            return;
        } else if (buf == 1) {
            buf = array[begin];
            array[begin] = array[begin + 1];
            array[begin + 1] = buf;

        } else {

            if (array[begin] > array[begin + 1]) {
                buf = array[begin];
                array[begin] = array[begin + 1];
                array[begin + 1] = buf;
            }
            if (array[begin + 1] > array[begin + 2]) {
                buf = array[begin + 1];
                array[begin + 1] = array[begin + 2];
                array[begin + 2] = buf;
            }
            if (array[begin] > array[begin + 1]) {
                buf = array[begin];
                array[begin] = array[begin + 1];
                array[begin + 1] = buf;
            }
        }
    }
}
