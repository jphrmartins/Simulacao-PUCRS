package queue.simulation.model;

import queue.simulation.queue.QueueDestinations;

public class NetworkMappingModel {
    private String fromQueueId;
    private String toQueueId;
    private double probability;

    public String getFromQueueId() {
        return fromQueueId;
    }

    public void setFromQueueId(String fromQueueId) {
        this.fromQueueId = fromQueueId;
    }

    public void setToQueueId(String toQueueId) {
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
