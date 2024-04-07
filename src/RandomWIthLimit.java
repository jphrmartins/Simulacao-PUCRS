import java.util.Random;

public class RandomWIthLimit extends Random {
    private Random random;
    private int i;
    private int limit;

    public RandomWIthLimit(long seed, int limit) {
        this.random = new Random(seed);
        this.limit = limit;
        this.i = 1;
    }

    @Override
    public double nextDouble() {
        if (i + 1 == limit) throw new ExitSimulationException();
        i++;
        return super.nextDouble();
    }
}
