package queue.simulation.random;

import queue.simulation.scheduler.ExitSimulationException;

import java.util.List;

public class SequenceRandomGenerator implements RandomGenerator {
    private final List<Double> numbers;
    private int currentIndex;

    public SequenceRandomGenerator(List<Double> numbers) {
        this.numbers = numbers;
        currentIndex = 0;
    }

    @Override
    public double nextDouble() {
        if (currentIndex + 1 > numbers.size())
            throw new ExitSimulationException();
        return numbers.get(currentIndex++);
    }
}
