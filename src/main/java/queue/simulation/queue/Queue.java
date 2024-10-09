package queue.simulation.queue;

import queue.simulation.Main;
import queue.simulation.random.RandomGenerator;
import queue.simulation.scheduler.Event;
import queue.simulation.scheduler.Scheduler;
import queue.simulation.time.TimeHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Queue {
    private final String id;
    private final String name;
    private final TimeHandler timeHandler;
    private final int servers;
    private final int maxCapacity;
    private final List<Double> timeBusy;
    private final List<QueueDestinations> queueDestinations;
    private final RandomGenerator random;
    private int busyServers;
    private int currentCapacity;
    private long loss;

    public Queue(String id, String name, TimeHandler timeHandler, int servers, int maxCapacity,
                 List<QueueDestinations> destinations, RandomGenerator randomGenerator) {
        this.id = id;
        this.name = name;
        this.timeHandler = timeHandler;
        this.maxCapacity = maxCapacity;
        this.servers = servers;
        this.queueDestinations = destinations;
        this.random = randomGenerator;
        timeBusy = new ArrayList<>();
        if (maxCapacity != -1) {
            IntStream.range(0, maxCapacity + 1).forEach(it -> timeBusy.add(0.0));
        }

    }

    public boolean hasInRange() {
        return timeHandler.hasIn();
    }

    public String getId() {
        return id;
    }

    public long getLoss() {
        return loss;
    }

    public List<Double> getTimeBusy() {
        return timeBusy;
    }

    public void executePass(Scheduler scheduler) {
        this.handleOut(scheduler);
    }

    public void updateTime(Event event, double currentTime) {
        double t = event.getTime() - currentTime;
        if (!timeBusy.isEmpty() && timeBusy.size() > currentCapacity) {
            timeBusy.set(currentCapacity, timeBusy.get(currentCapacity) + t);
        } else if (maxCapacity == -1) {
            timeBusy.add(t);
        } else {
            throw new RuntimeException("Error updating time of event: " + event + " For Queue: " + this);
        }
    }

    public void execute(Scheduler scheduler, Event event) {
        if (event.isIn()) {
            handleIn(scheduler, false);
        } else if (event.isOut()) {
            handleOut(scheduler);
        } else if (event.isPass()) {
            handleIn(scheduler, true);
        } else {
            throw new RuntimeException("Evento: " + event.getType() + " Não conhecido");
        }
    }

    private void handleIn(Scheduler scheduler, boolean isPass) {
        if (currentCapacity < maxCapacity || maxCapacity == -1) {
            currentCapacity++;
            if (busyServers < servers) {
                busyServers++;
                scheduler.schedule(timeHandler.nextOut(scheduler.getCurrentTime(), id, getDestination()));
            }
        } else {
            loss++;
        }
        if (!isPass) {
            scheduler.schedule(timeHandler.nextIn(scheduler.getCurrentTime(), this.id));
        }
    }

    private void handleOut(Scheduler scheduler) {
        currentCapacity--;
        busyServers--;
        if (currentCapacity > 0) {
            int availableServers = servers - busyServers;
            int waiting = currentCapacity - busyServers;
            for (int j = 0; j < waiting && j < availableServers; j++) {
                busyServers++;
                scheduler.schedule(timeHandler.nextOut(scheduler.getCurrentTime(), id, getDestination()));
            }
        }
    }

    private String getDestination() {
        if (queueDestinations.isEmpty()) return null;
        if (queueDestinations.get(0).getProbability() >= 1) return queueDestinations.get(0).getTo();
        double p = random.nextDouble();
        QueueDestinations destinations;
        double destProb = 0;
        for (int i = 0; i < queueDestinations.size(); i++) {
            destProb += queueDestinations.get(i).getProbability();
            destinations = queueDestinations.get(i);
            if (p <= destProb) {
                return destinations.getTo();
            }

        }
        return null;
    }

    public List<Statistics> calculateStatistc(Scheduler scheduler) {
        List<Statistics> statistics = new ArrayList<>(timeBusy.size());
        for (int i = 0; i < timeBusy.size(); i++) {
            double mi = timeHandler.generateMi();
            double probability = (timeBusy.get(i) / scheduler.getCurrentTime());
            statistics.add(new Statistics(i, BigDecimal.valueOf(mi), probability, servers));
        }
        return statistics;
    }

    public String print(Scheduler scheduler) {
        StringBuilder builder = new StringBuilder("Fila: " + id + " - Name: " + name + "\n");
        builder.append("Notacao: ")
                .append("g/g/")
                .append(servers)
                .append("/")
                .append(maxCapacity > 0 ? maxCapacity : "INFINTY")
                .append(" ")
                .append(timeHandler)
                .append("\n");
        builder.append("Tempos por posicao: \n");
        for (int i = 0; i < timeBusy.size(); i++)
            builder.append("\t Ocupacao: ").append(i).append(" -> Tempo: ").append(timeBusy.get(i)).append("\n");

        builder.append("\n===================\n");
        builder.append("Distribuição probabilidades por posição: \n");

        double sumFinal = 0;
        for (int i = 0; i < timeBusy.size(); i++) {
            double probability = (timeBusy.get(i) / scheduler.getCurrentTime());
            builder.append("\t Ocupacao: ").append(i).append(" -> ")
                    .append(probability * 100).append(" %")
                    .append("\n");
            sumFinal += probability;
        }
        builder.append("Total: ").append(sumFinal * 100).append("%\n");

        builder.append("I,PIi,N,D,U,W\n");
        List<Statistics> statistics = calculateStatistc(scheduler);
        statistics.forEach( it ->
            builder.append(it.getI())
                    .append(',')
                    .append(it.getProb().toPlainString())
                    .append(',')
                    .append(it.getPopulation().toPlainString())
                    .append(',')
                    .append(it.getFlow().toPlainString())
                    .append(',')
                    .append(it.getUsage().toPlainString())
                    .append(',')
                    .append(it.getResponseTime().toPlainString())
                    .append('\n')
        );
        Statistics total = statistics
                .stream()
                .reduce(new Statistics(), Statistics::merge);

        builder.append(total.getI())
                .append(',')
                .append(total.getProb().toPlainString())
                .append(',')
                .append(total.getPopulation().toPlainString())
                .append(',')
                .append(total.getFlow().toPlainString())
                .append(',')
                .append(total.getUsage().toPlainString())
                .append(',')
                .append(total.getResponseTime().toPlainString())
                .append('\n');

        builder.append("\n===================\n");
        builder.append("Quantidade de Perda: ");
        builder.append(loss);
        builder.append("\n===================\n\n");

        return builder.toString();
    }
}
