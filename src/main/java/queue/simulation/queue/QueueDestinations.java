package queue.simulation.queue;

public class QueueDestinations {
    private String to;
    private double probability;

    public QueueDestinations(String to, double probability) {
        this.to = to;
        this.probability = probability;
    }

    public String getTo() {
        return to;
    }

    public double getProbability() {
        return probability;
    }
}
