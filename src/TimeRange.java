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
        return (max - min) * random.nextDouble() + min;

    }

    @Override
    public String toString() {
        return "IntervaloTempo{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
