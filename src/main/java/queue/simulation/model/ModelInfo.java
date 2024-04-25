package queue.simulation.model;

import queue.simulation.builder.QueueBuilder;
import queue.simulation.random.RandomGenerator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModelInfo {
    private FirstArrivalModel firstArrival;
    private List<QueueModel> queues;
    private List<NetworkMappingModel> network;
    private RandomModel randomModel;

    public FirstArrivalModel getFirstArrival() {
        return firstArrival;
    }

    public void setFirstArrival(FirstArrivalModel firstArrival) {
        this.firstArrival = firstArrival;
    }

    public Map<Integer, QueueBuilder> getQueueMap(RandomGenerator randomGenerator) {
        return queues.stream().map(it -> it.toQueue(randomGenerator))
                .collect(Collectors.toMap(QueueBuilder::getId, Function.identity()));
    }

    public void setQueues(List<QueueModel> queues) {
        this.queues = queues;
    }

    public Map<Integer, List<NetworkMappingModel>> getNetwork() {
        return network.stream()
                .collect(Collectors.groupingBy(
                        NetworkMappingModel::getFromQueueId,
                        Collectors.mapping(
                                Function.identity(),
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        mapping -> mapping.stream()
                                                .sorted(Comparator.comparing(NetworkMappingModel::getProbability))
                                                .collect(Collectors.toList())
                                )
                        )
                ));
    }


    public void setNetwork(List<NetworkMappingModel> network) {
        this.network = network;
    }

    public RandomModel getRandomModel() {
        return randomModel;
    }

    public void setRandomModel(RandomModel randomModel) {
        this.randomModel = randomModel;
    }
}
