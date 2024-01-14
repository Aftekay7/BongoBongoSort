public class Helpers {



    //checks if whether array is sorted ascendingly
    public static boolean checkArray(int[] arr) {
        int i = 1;
        while (i < arr.length && arr[i] >= arr[i - 1]) {
            i++;
        }
        return i == arr.length;
    }


    //generates value bound ONLY in form of [1 - 1/8n - 1/4n - 1/2n - n - 2n - 4n - 8n - 16n - n! - n² - n³ ]
    public static int generateValueBound(int n, int i) {
        int w = 1;
        int w_1 = 2;
        int fac;

        if (i == 0) {
            return 1;
        } else if (i < 4) {
            i = 5 - i;

            while (w_1 > 0 && i > 1) {
                w_1 *= 2;
                i--;
            }

            w = n / w_1;
            if (w != 0) {
                return w;
            } else {
                return 1;
            }

        } else if (i == 4) {
            return n;

        } else if (i < 9) {
            w = 2 * (i - 4) * n;
            return w;

        } else if (i == 9) {

            int k = 1;
            fac = 1;

            while (k <= n) {
                fac *= n;
                k++;
            }
            if (fac < Integer.MAX_VALUE && fac > 0) {
                return fac;
            } else {
                return -1;

            }

        } else {
            w = n;
            i = i - 8;
            while (i > 0) {
                w *= w;
                i--;
            }
            if (w < Integer.MAX_VALUE && w > 0) {
                return w;
            } else {
                return -1;
            }
        }
    }

    //prints array
    public static void printArr(int[] arr) {
        System.out.print("{");
        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length - 1) {
                System.out.println(arr[i] + "}");
            } else {
                System.out.print(arr[i] + ", ");
            }
        }
    }


    //generates an array with specified length filled with random integers between 0 (inc) and length (excl), no duplicates
    public static int[] RandArray(int length) {
        int[] arr = new int[length];

        for (int i = 0; i < length; i++) {
            arr[i] = i;
        }

        for (int i = 0; i < length; i++) {
            int index = RandomGenerator.random(0, length);
            int buf = arr[i];
            arr[i] = arr[index];
            arr[index] = buf;
        }
        return arr;
    }

    //generates an array with specified length filled with random integers between upper (excl) and lower bound (incl)
    public static int[] RandArray(int length, int lower, int upper) {
        int[] arr = new int[length];

        for (int i = 0; i < length; i++) {
            arr[i] = RandomGenerator.random(lower, upper);

        }
        return arr;
    }
}
