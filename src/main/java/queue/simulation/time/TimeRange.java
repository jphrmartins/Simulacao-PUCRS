package queue.simulation.time;

import queue.simulation.random.RandomGenerator;

public class TimeRange {
    private final int min;
    private final int max;
    private RandomGenerator random;

    public TimeRange(int min, int max, RandomGenerator random) {
        this.max = max;
        this.min = min;
        this.random = random;
    }

    public double next() {
        double rand = random.nextDouble();
        return (max - min) * rand + min;
    }

    public double generateMi() {
        return 60.0 / ((max + min * 1.0) / 2.0);
    }

    @Override
    public String toString() {
        return min + ".." + max;
    }
}
