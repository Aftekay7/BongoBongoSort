//Viel Spaß und Danke für das Interesse :)

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[] test = Helpers.RandArray(4000,0,1000);
        Tests.compareSpeed(0,0,1000,test);
    }
}


