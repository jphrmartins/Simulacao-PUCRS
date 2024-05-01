package queue.simulation.builder;

import queue.simulation.model.ModelInfo;
import queue.simulation.model.NetworkMappingModel;
import queue.simulation.queue.Queue;
import queue.simulation.random.RandomGenerator;
import queue.simulation.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Model {
    private final List<Scheduler> schedulers;

    public Model(ModelInfo model) {
        List<RandomGenerator> randoms = model.getRandomModel().toGenerators();
        schedulers = randoms.stream().map(random -> {
            Map<String, QueueBuilder> queueMap = model.getQueueMap(random);
            Map<String, List<NetworkMappingModel>> networkMappingModelMap = model.getNetwork();
            Scheduler scheduler = new Scheduler();
            queueMap.entrySet().stream().map(e -> {
                        String queueId = e.getKey();
                        QueueBuilder builder = e.getValue();
                        List<NetworkMappingModel> network = networkMappingModelMap.getOrDefault(queueId, new ArrayList<>());
                        builder.withNetwork(network);
                        return builder.build();
                    }).forEach(scheduler::withQueue);
            if (model.getFirstArrival() != null) {
                return scheduler.withFirstIn(model.getFirstArrival().toModel());
            }
            return scheduler;
        }).collect(Collectors.toList());
    }

    public List<Scheduler> getSchedulers() {
        return schedulers;
    }
}
