import java.util.Random;

public class TimeRange {
    private final int min;
    private final int max;
    private Random random;

    public TimeRange(int min, int max, Random random) {
        this.max = max;
        this.min = min;
        this.random = random;
    }

    public double next() {
        double rand = random.nextDouble();
        return (max - min) * rand + min;

    }

    @Override
    public String toString() {
        return min + ".." + max;
    }
}
