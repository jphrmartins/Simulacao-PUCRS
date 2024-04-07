import java.util.Random;

public class RandomWIthLimit extends Random {
    private int i;
    private int limit;

    public RandomWIthLimit(long seed, int limit) {
        super(seed);
        this.limit = limit;
        this.i = 1;
    }

    @Override
    public double nextDouble() {
        if (i > limit){
            throw new ExitSimulationException();
        }
        i++;
        return super.nextDouble();
    }
}
