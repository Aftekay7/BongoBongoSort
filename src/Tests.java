import java.util.Arrays;

import static java.util.Arrays.sort;

public class Tests {

    /**
     * Tests sort-algorithm on specified array
     *
     * @param testarray specifies the testarray, if null random arrays are created
     */
    public static void compareOnArray(int[] testarray) {
        int[] arr_1;
        long avg_duration = 0;
        long min = 1000000;
        long max = 0;
        long duration;
        long startTime;
        long endTime;
        int count = 100;

        for (int i = 0; i < count; i++) {
            arr_1 = testarray.clone();

            startTime = System.nanoTime();
            //sort(arr_1);
            KraemerSort.sortArray(arr_1);
            //ScalingSort.sort(arr_1);
            endTime = System.nanoTime();
            duration = endTime - startTime;


            if (i == 0) {
                //System.out.println(duration);
                continue;
            }


            if (duration > max) {
                max = duration;
            } else if (duration < min) {
                min = duration;
            }


            //System.out.println(duration);
            avg_duration += duration;


        }
        avg_duration = avg_duration/(count - 1);


        System.out.println("Best time : "+ min);
        System.out.println("Average time: " + avg_duration);
        System.out.println("Worst time: " + max);



    }


    /**
     * Tests sort-algorithm until a case is found where array is not sorted correctly or an exception is thrown or
     *
     * @param max maximum of length and value range (see below)
     *            Random array has: - a random length n: 1 <= n < max,
     *            - filled with random values x: 1 <= x < w,
     *            - where w is a random value with 1 <= w < max,
     *            <p>
     *            If an Edge-Case is found, Exception, Input Array and output Array are printed.
     */
    public static void test(int max) {
        //maximum value of the array length/max of range of values
        int length = RandomGenerator.random(1, max);
        int upper_value_bound = RandomGenerator.random(1, max);
        int lower_value_bound = RandomGenerator.random(0, upper_value_bound);
        int[] arr;
        int[] arr_copy;

        boolean running = true;
        while (running) {

            arr = Helpers.RandArray(length, lower_value_bound, upper_value_bound);
            arr_copy = arr.clone();

            try {
                BongoBongo.sort(arr);
                if (!Helpers.checkArray(arr)) {
                    System.out.println("Array not sorted");
                    Helpers.printArr(arr_copy);
                    Helpers.printArr(arr);
                    running = false;
                }
            } catch (NullPointerException | ArithmeticException | IndexOutOfBoundsException e) {
                System.out.println(e + "Something crashed, here are the arrays");
                Helpers.printArr(arr_copy);
                Helpers.printArr(arr);
                running = false;
            }
        }
    }


    /**
     * Compares Speed of BongoBongo-Sort and Quicksort on the same Array for <testcases> cases.
     * @param testarray specifies the test array, if null random arrays are created.
     * Random array has: - a random length n = <length>, if <length> != 0 or
     * n: -1  <=  n  <  <max>, if <length> == 0
     * - is filled with random values x: 1 <= x < w,
     * - where w is a random value with: 1 <=  w  <  <max>,
     * <p>
     * Function returns an evaluation of the testcases (#wins, average time, average win time, slowest/fastest time)
     * for both sort-algorithms
     */
    public static void compareSpeed(int length, int max, int testcases, int[] testarray) {
        int bongo_win_count = 0;
        long bongo_avg_win_time = 0; //average win
        long bongo_duration;
        long bongo_avg_duration = 0; //average time
        long bongo_max_time = 0;
        long bongo_min_time = 200000;

        int quick_win_time = 0;
        long quick_avg_win_time = 0; //average win
        long quick_duration;
        long quick_avg_duration = 0; //avg time
        long quick_min_time = 200000;
        long quick_max_time = 0;

        int draws = 0;
        long difference; //difference of duration of the two algorithms
        int upper_value_bound;  //(max + 1) of the range of values of the array
        int lower_value_bound;  //min of the range of values of the array

        long startTime;
        long endTime;


        long bongo_biggest_win = 0;
        long bongo_biggest_loss = 0;

        float avg;

        int n = testcases;
        while (n > 0) {

            //generating random values
            if (length == 0 && testarray == null) {
                length = RandomGenerator.random(1, max);
            }

            int[] arr_1;
            int[] arr_2;
            int[] arr_temp;

            if (testarray == null) {
                upper_value_bound = RandomGenerator.random(1, max);
                lower_value_bound = RandomGenerator.random(0, upper_value_bound);

                //generate random array using the randomized parameters
                arr_temp = Helpers.RandArray(length, lower_value_bound, upper_value_bound);
                arr_1 = arr_temp.clone(); //clone arrays so nothing interferes
                arr_2 = arr_temp.clone();
            } else {
                arr_1 = testarray.clone();
                arr_2 = arr_1.clone();
                arr_temp = testarray;
            }

            //executing bongobongo and measuring time
            startTime = System.nanoTime();
            //BongoBongo.sort(arr_1);
            KraemerSort.sortArray(arr_1);
            endTime = System.nanoTime();
            bongo_duration = endTime - startTime;


            //sum up durations to get average
            bongo_avg_duration += bongo_duration;


            //updating max/min time of bongosort
            if (bongo_duration < bongo_min_time) {
                bongo_min_time = bongo_duration;
            } else if (bongo_duration > bongo_max_time) {
                bongo_max_time = bongo_duration;
            }

            //check if array is sorted
            if (!Helpers.checkArray(arr_1)) {
                System.out.println("Array not sorted");
                System.out.println("Input: ");
                Helpers.printArr(arr_temp);
                System.out.println("\n\n");
                System.out.println("Output: ");
                Helpers.printArr(arr_1);
                throw new IllegalArgumentException("array not sorted");
            }

            //executing quicksort and measuring time
            startTime = System.nanoTime();
            Arrays.sort(arr_2);
            endTime = System.nanoTime();


            quick_duration = endTime - startTime;
            quick_avg_duration += quick_duration;

            //updating max/min duration of quicksort
            if (quick_duration < quick_min_time) {
                quick_min_time = quick_duration;
            } else if (quick_duration > quick_max_time) {
                quick_max_time = quick_duration;
            }

            //calculate the difference of time of the two algorithms
            difference = bongo_duration - quick_duration;

            //evaluate test iteration
            if (difference < 0) {
                bongo_win_count++;
                bongo_avg_win_time -= difference;

                if (difference < bongo_biggest_win) {
                    bongo_biggest_win = difference * -1;
                }

            } else if (difference == 0) {
                draws++;

            } else {
                quick_win_time++;
                quick_avg_win_time += difference;

                if (difference > bongo_biggest_loss) {
                    bongo_biggest_loss = difference;
                }
            }

            n--;
        }

        //calculate the average time in case of winning
        if (bongo_win_count != 0) {
            bongo_avg_win_time = bongo_avg_win_time / bongo_win_count;
        } else {
            bongo_avg_win_time = 0;
        }
        quick_avg_win_time = quick_avg_win_time / quick_win_time;

        //calculate the avg durations over all testcases
        bongo_avg_duration = bongo_avg_duration / testcases;
        quick_avg_duration = quick_avg_duration / testcases;

        avg = (float) (bongo_max_time) / 1000000;
        String b_avg_rounded = String.format("%.2f", avg);
        avg = (float) (quick_max_time) / 1000000;
        String q_avg_rounded = String.format("%.2f", avg);

        avg = (float) (bongo_biggest_win) / 1000000;
        String b_win_rounded = String.format("%.2f", avg);
        avg = (float) (bongo_biggest_loss) / 1000000;
        String q_win_rounded = String.format("%.2f", avg);


        //determine algorithm with lower average duration
        String winner;
        long winner_time;
        if (bongo_avg_duration - quick_avg_duration > 0) {
            winner = "Quicksort";
            winner_time = bongo_avg_duration - quick_avg_duration;
        } else {
            winner = "Bongobongo";
            winner_time = quick_avg_duration - bongo_avg_duration;
        }

        System.out.println("Result: Bongobongo-Sort  " + bongo_win_count + " : " + quick_win_time + "  Quick-Sort, draws: " + draws);
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.println("Bongobongo took on average " + bongo_avg_duration + "ns, fastest sort took " + bongo_min_time + "ns, slowest was " + bongo_max_time + "ns. (~" + b_avg_rounded + "ms)");
        System.out.println("Quicksort took on average " + quick_avg_duration + "ns, fastest sort took " + quick_min_time + "ns, slowest was " + quick_max_time + "ns. (~" + q_avg_rounded + "ms)");
        System.out.println("Therefore, " + winner + " was " + winner_time + "ns faster on average");
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.println("Bongobongo was on average " + bongo_avg_win_time + "ns faster in case of winning. Biggest win was by " + bongo_biggest_win + "ns. (~" + b_win_rounded + "ms)");
        System.out.println("Quicksort was on average  " + quick_avg_win_time + "ns faster in case of winning. Biggest win was by " + bongo_biggest_loss + "ns.(~" + q_win_rounded + "ms)");
    }


    /**
     * Compares Speed of BongoBongo-Sort and Quicksort on the same Arrays.
     * Random array has: - length n: n = <n_base> * <n_factor>^(k) with k: 0 < k < <n_value_count>
     * - is filled with random values x: 0 <= x < w,
     * - with w in [1, n/8, n/4, n/2, n, 2n, 4n, 8n, 16n, n!, n², n³] (see value_bound-function)
     * <p>
     * Function returns the best, worst and average time for both sorting algorithms for every combination of w and n.
     * Per combination 100 arrays are created and sorted. value can be changed by adjusting the "cases_per_column variable"
     */
    public static void testSpeed(int n_value_count, int n_base, int n_factor) {
        int value_bound_count = 12;
        int[] n_values = new int[n_value_count];
        String[] value_bound = {"1", "n/8", "n/4", "n/2", "n", "2n", "4n", "8n", "16n", "n!", "n²", "n³"};
        long bongo_avg_duration;
        long quick_avg_duration;
        long bongo_duration;
        long quick_duration;
        long bongo_max_time;
        long quick_max_time;
        long bongo_min_time;
        long quick_min_time;
        long startTime;
        long endTime;
        int upper_value_bound;
        int lower_value_bound = 0;
        int length = 1;
        long case_count;
        long cases_per_column = 100;
        int failed_sort = 0;

        long[][][] bongo_result_table = new long[n_value_count][value_bound_count][3];
        long[][][] quick_result_table = new long[n_value_count][value_bound_count][3];


        for (int i = 0; i < n_value_count; i++) {
            n_values[i] = n_base;
            n_base *= n_factor;
            if (n_base < 0) {
                n_value_count = i;
                System.out.println("highest n: " + n_values[i]);
                break;
            }
        }

        for (int i = 0; i < n_value_count; i++) {
            length = n_values[i];

            for (int k = 0; k < value_bound_count; k++) {
                upper_value_bound = Helpers.generateValueBound(length, k);
                if (upper_value_bound < 0) {
                    continue;
                }

                bongo_avg_duration = 0;
                quick_avg_duration = 0;
                bongo_max_time = 0;
                quick_max_time = 0;
                bongo_min_time = 100000000;
                quick_min_time = 100000000;
                case_count = 0;
                while (case_count < cases_per_column) {

                    //generate random array using the randomized parameters
                    int[] arr_1 = Helpers.RandArray(length, lower_value_bound, upper_value_bound);
                    int[] arr_2 = arr_1.clone();

                    //executing bongobongo and measuring time
                    startTime = System.nanoTime();
                    BongoBongo.sort(arr_1);
                    endTime = System.nanoTime();
                    bongo_duration = endTime - startTime;

                    //executing quicksort and measuring time
                    startTime = System.nanoTime();
                    Arrays.sort(arr_2);
                    endTime = System.nanoTime();
                    quick_duration = endTime - startTime;


                    //edge case -> the very first sort takes for whatever reason an absurd amount of time -> delete from statistic
                    if (i == 0 && k == 0 && case_count == 0) {
                        case_count++;
                        continue;
                    }
                    //sum up durations to get average
                    bongo_avg_duration += bongo_duration;
                    quick_avg_duration += quick_duration;


                    //updating max/min time of bongosort
                    if (bongo_duration < bongo_min_time) {
                        bongo_min_time = bongo_duration;
                    } else if (bongo_duration > bongo_max_time) {
                        bongo_max_time = bongo_duration;
                    }
                    //updating max/min time of qs
                    if (quick_duration < quick_min_time) {
                        quick_min_time = quick_duration;
                    } else if (quick_duration > quick_max_time) {
                        quick_max_time = quick_duration;
                    }

                    //check if array is sorted
                    if (!Helpers.checkArray(arr_1)) {
                        /*
                        System.out.println("Array not sorted");
                        System.out.println("Input: ");
                        printArr(arr_temp);
                        System.out.println("\n\n");
                        System.out.println("Output: ");
                        printArr(arr_1);
                         */
                        failed_sort++;
                    }
                    case_count++;
                }

                //calculate avg duration and store statistics into result table
                bongo_avg_duration = bongo_avg_duration / case_count;
                quick_avg_duration = quick_avg_duration / case_count;
                bongo_result_table[i][k][0] = bongo_min_time;
                bongo_result_table[i][k][1] = bongo_avg_duration;
                bongo_result_table[i][k][2] = bongo_max_time;

                quick_result_table[i][k][0] = quick_min_time;
                quick_result_table[i][k][1] = quick_avg_duration;
                quick_result_table[i][k][2] = quick_max_time;
            }

        }


        //Building the output strings
        String bongo_table = TableToString(bongo_result_table, n_values, value_bound_count, value_bound);
        String quick_table = TableToString(quick_result_table, n_values, value_bound_count, value_bound);

        System.out.println("BongoBongosort:");
        System.out.println(bongo_table);
        System.out.println("Failed sorts: " + failed_sort);
        System.out.println("\n\n");
        System.out.println("Quicksort:");
        System.out.println(quick_table);

    }


    /*
        creates String of param result_table
        n_values -> used n-values
        value_bound_count -> number of columns (= number of used value_bounds)
        value_bound -> names of the columns
    */
    public static String TableToString(long[][][] result_table, int[] n_values, int value_bound_count, String[] value_bound) {
        String table = "";
        String header = "  n-values | stat ";
        String column_header;

        //build the header
        for (int i = 0; i < value_bound_count; i++) {
            column_header = "| " + value_bound[i];

            while (column_header.length() < 11) {
                column_header += " ";
            }
            header += column_header;
        }

        header += "\n";

        //build separation line
        String sep_line = "-----------+------";
        for (int i = 0; i < value_bound_count; i++) {
            sep_line += "+----------";
        }
        sep_line += "\n";


        table += header;
        table += sep_line;


        //build the rest
        String row;
        String cell;
        for (int i = 0; i < n_values.length; i++) {
            for (int stat_index = 0; stat_index < 3; stat_index++) {

                //first column
                if (stat_index == 0) {
                    row = "           | best ";
                } else if (stat_index == 1) {
                    row = " " + n_values[i];
                    while (row.length() < 11) {
                        row += " ";
                    }
                    row += "| avg  ";
                } else {
                    row = "           | worst";
                }

                //extract values
                for (int k = 0; k < value_bound_count; k++) {
                    cell = "| " + formatDigit(result_table[i][k][stat_index]);
                    while (cell.length() < 11) {
                        cell += " ";
                    }
                    row += cell;
                }
                table += row;
                table += "\n";
            }
            table += sep_line;
        }


        return table;
    }


    //formats digit depending on the value and adds the fitting unit.
    public static String formatDigit(long val) {
        double div = 1000;
        double buf;
        long x = val;

        if (val < 1000) {
            return val + "ns";
        }
        x /= 1000;
        if (x < 100) {
            buf = val / div;
            return String.format("%.2f", buf) + "μs";
        }
        x /= 1000;
        if (x < 1000) {
            buf = val / (div * div);
            return String.format("%.3f", buf) + "ms";
        }
        return "";
    }
}
