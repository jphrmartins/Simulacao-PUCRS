package queue.simulation.model;

import queue.simulation.queue.QueueDestinations;

public class NetworkMappingModel {
    private int fromQueueId;
    private Integer toQueueId;
    private double probability;

    public int getFromQueueId() {
        return fromQueueId;
    }

    public void setFromQueueId(int fromQueueId) {
        this.fromQueueId = fromQueueId;
    }

    public void setToQueueId(int toQueueId) {
        this.toQueueId = toQueueId;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public QueueDestinations toDestination() {
        return new QueueDestinations(toQueueId, probability);
    }
}
