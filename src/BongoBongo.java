public class BongoBongo {


    //initial call of the sort-algorithm
    public static void sort(int[] arr) {

        //initializes array of Meta-nodes with the length of the array
        NodeMeta[] container = new NodeMeta[arr.length];

        //fills array with empty meta-nodes
        for (int i = 0; i < container.length; i++) {
            container[i] = new NodeMeta();
        }

        //finds the minimum- and maximum-value contained by the array
        int min = arr[0], max = arr[0];
        for (int j : arr) {
            if (j < min) {
                min = j;
            } else if (j > max) {
                max = j;
            }
        }

        /*
        - initial fill of the containers:
        - every value is sorted into the value-stack according to the "hash"-function (see docs there for further details)
         */
        for (int value : arr) {
            int index = fancyHash(value, min, max, arr.length);

            if (container[index] == null) {
                container[index] = new NodeMeta();
                container[index].addValue(value);

            } else {
                container[index].addValue(value);
            }
        }

        //stores the lowest free index of the sorted array
        int insert_index = 0;

        //starts the recursion for every list (if list is not empty)
        for (NodeMeta meta : container) {
            if (meta.getCount() > 0) {
                insert_index = rec(meta, insert_index, arr);
            }
        }

    }




    /**
        - min, max, value_count (or n/array_length) = meta information of the (sub-)list that has to be sorted
        - let A be a set:    For all x in A: 0 <= x - min <= max - min
        - function shifts value by the minimum so that key(max) = n-1 and key(min) = 0
        - function calculates the (relativ) position of the value in the set A and maps this to the array-indices
        -> key(value) = 0 <=> value - min < 1/value_count
           key(value) = 1 <=> value - min < 2/value_count & >= above
           key(value) = 2 <=> value - min < 3/value_count & >= above
           ...
           key(value) = n-1 <=> value = max (as (value - min) = value_range -> cancelled)

           the key resembles in which (0 - n-1) percent the value (-min) in set A is or
           the (minimum) number of smaller values than value (-min)

           -> if value_count < value_range, this function is surjective, but as soon as value_count >= value_range
              it becomes bijective -> values will be sorted after interpreting key-values as indices
     */
    public static int fancyHash(int value, int min, int max, int value_count) {

        int key;
        int value_range = max - min;    //range of values -> maximum of set a

        //edge-case where in initial fill all values are equal (min = max -> value_range = 0)
        if (value_range == 0) {
            value_range = 1;
        }

        key = ((value_count - 1) * (value - min)) / value_range;

        //edge case where values are too big -> overflow -> negative indices. Therefore, we switch to long
        if (key < 0) {
            long buff = (long) (value_count - 1) * (long) (value - min);
            buff = buff / value_range;
            key = (int) buff;
        }
        return key;
    }





    /**
        - recursive form of the sorting-algorithm
        - meta: provides meta-information of the stack that has to be sorted
        - insert_index: lowest free index of the (final) sorted array
        - arr: output array
    */
    public static int rec(NodeMeta meta, int insert_index, int[] arr) {

        int count = meta.getCount();            //number of values (or "array length")
        int max = meta.getTail().getValue();    //maximum of the stack
        int min = meta.getHead().getValue();    //minimum of the sack

        /*
            - count <= 3 -> list is sorted (as list has form of min - value - max)
            - max = min -> list is sorted (as only equal values are in this stack)
            - else: list is not sorted yet
        */
        if (count <= 3 || max == min) {
            Node head = meta.getHead();
            insert_index = insert(arr, insert_index, head); //elements of stack are copied into the output array

        } else {
            NodeMeta[] container = new NodeMeta[count]; //initialize new containers

            Node head = meta.getHead();

            //iterate over list and "hash" every value
            while (head != null) {

                int value = head.getValue();
                int index = fancyHash(value, min, max, count);

                //insert into stacks according to the "hash"-value
                if (container[index] == null) {
                    container[index] = new NodeMeta();
                    container[index].addValue(value);

                } else {
                    container[index].addValue(value);
                }
                head = head.getNext();
            }

            //repeat process until sorted
            for (NodeMeta meta_l : container) {
                if (meta_l != null && meta_l.getCount() > 0) {
                    insert_index = rec(meta_l, insert_index, arr);
                }
            }

        }

        return insert_index;
    }




    //inserts sorted values into the output array, index is the lowest free index in the output array
    public static int insert(int[] arr, int index, Node head) {

        //iterates over all (sorted) stack-elements and copies them to arr[index + index-of-element-in-stack]
        while (head != null) {
            arr[index] = head.getValue();
            index++;
            head = head.getNext();
        }
        return index;
    }
}
