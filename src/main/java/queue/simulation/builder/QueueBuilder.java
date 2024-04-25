package queue.simulation.builder;

import queue.simulation.model.NetworkMappingModel;
import queue.simulation.queue.Queue;
import queue.simulation.queue.QueueDestinations;
import queue.simulation.time.TimeHandler;
import queue.simulation.time.TimeRange;
import queue.simulation.random.RandomGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QueueBuilder {
    private static Set<Integer> takenIds;
    private static int ID = 0;
    private final RandomGenerator random;
    private Integer id;
    private TimeRange tempoEntrada;
    private TimeRange tempoSaida;
    private int servidores;
    private int capacidadeMaxima;
    private List<QueueDestinations> queueDestinationsList;

    public QueueBuilder(RandomGenerator random) {
        this.random = random;
        takenIds = new HashSet<>();
        capacidadeMaxima = -1;
        queueDestinationsList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public QueueBuilder withInRange(int min, int max) {
        tempoEntrada = new TimeRange(min, max, random);
        return this;
    }

    public QueueBuilder withId(int id) {
        if (takenIds.contains(id)) {
            throw new IdAlreadyTakenException(id);
        }
        this.id = id;
        return this;
    }

    public QueueBuilder withExitRange(int min, int max) {
        tempoSaida = new TimeRange(min, max, random);
        return this;
    }

    public QueueBuilder withServidors(int servidores) {
        this.servidores = servidores;
        return this;
    }

    public QueueBuilder withMaxCapacity(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
        return this;
    }
    public QueueBuilder withNetwork(List<NetworkMappingModel> network) {
        queueDestinationsList = network.stream().map(NetworkMappingModel::toDestination).collect(Collectors.toList());
        return this;
    }

    public Queue build() {
        if (id == null) return this.withId(ID++).build();
        if (tempoSaida == null) throw new RequiredFieldOnQueueBuilderException("tempoSaida");
        if (servidores == 0) throw new RequiredFieldOnQueueBuilderException("servidores");

        return new Queue(
                id,
                new TimeHandler(this.tempoEntrada, this.tempoSaida),
                this.servidores,
                this.capacidadeMaxima,
                queueDestinationsList,
                random
        );
    }
}
