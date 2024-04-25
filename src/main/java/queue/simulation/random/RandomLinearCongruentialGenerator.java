package queue.simulation.random;

import queue.simulation.scheduler.ExitSimulationException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class RandomLinearCongruentialGenerator implements RandomGenerator {

    private final static BigInteger C = BigInteger.valueOf(104003L);
    private final static BigInteger A = BigInteger.valueOf(1140671485);
    private final static BigInteger M = BigInteger.valueOf(2).pow(48);
    private final BigInteger seed;
    private BigInteger last;
    private int maxInvocations;
    private int invocations;


    public RandomLinearCongruentialGenerator(long seed, int maxInvocations) {
        this(seed);
        this.maxInvocations = maxInvocations;
    }

    public RandomLinearCongruentialGenerator(int maxInvocations) {
        this();
        this.maxInvocations = maxInvocations;
    }

    public RandomLinearCongruentialGenerator(long seed) {
        this.seed = BigInteger.valueOf(seed);
    }

    public RandomLinearCongruentialGenerator() {
        seed = BigInteger.valueOf(28229L);
        maxInvocations = 1000;
    }


    @Override
    public double nextDouble() {
        if (maxInvocations > 0 && invocations > maxInvocations) {
            throw new ExitSimulationException();
        }
        invocations++;
        BigInteger x = last != null ? last : seed;
        last = A.multiply(x).add(C).mod(M);
        BigDecimal result = BigDecimal.valueOf(last.longValue())
                .divide(BigDecimal.valueOf(M.longValue()), 16, RoundingMode.HALF_EVEN);
        return result.doubleValue();
    }
}
