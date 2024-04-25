package queue.simulation.queue;

public class QueueDestinations {
    private Integer to;
    private double probability;

    public QueueDestinations(Integer to, double probability) {
        this.to = to;
        this.probability = probability;
    }

    public Integer getTo() {
        return to;
    }

    public double getProbability() {
        return probability;
    }
}
