import java.util.Random;

public class RandomGenerator {
    private final static Random random = new Random();



    //lowerBound inclusive, upperBound exclusive
    public static int random(int lowerBound, int upperBound) {
            return random.nextInt(upperBound - lowerBound) + lowerBound;
    }
}