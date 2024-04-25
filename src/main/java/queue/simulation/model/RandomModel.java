package queue.simulation.model;

import queue.simulation.random.RandomGenerator;
import queue.simulation.random.RandomLinearCongruentialGenerator;
import queue.simulation.random.SequenceRandomGenerator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RandomModel {
    private int interactions;
    private List<Long> seeds;
    private List<Double> randomNumbers;

    public int getInteractions() {
        return interactions;
    }

    public void setInteractions(int interactions) {
        this.interactions = interactions;
    }

    public List<Long> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<Long> seeds) {
        this.seeds = seeds;
    }

    public List<Double> getRandomNumbers() {
        return randomNumbers;
    }

    public void setRandomNumbers(List<Double> randomNumbers) {
        this.randomNumbers = randomNumbers;
    }

    public List<RandomGenerator> toGenerators() {
        if (randomNumbers != null && !randomNumbers.isEmpty()) {
            return Collections.singletonList(new SequenceRandomGenerator(randomNumbers));
        } else if (interactions > 0 && seeds != null && !seeds.isEmpty()){
            return seeds.stream().map(it -> new RandomLinearCongruentialGenerator(it, interactions))
                    .collect(Collectors.toList());
        }
        return Collections.singletonList(new RandomLinearCongruentialGenerator());
    }
}
