package queue.simulation.scheduler;


import queue.simulation.queue.Queue;

import java.util.*;
import java.util.stream.IntStream;

public class Scheduler {
    private FirstInEvent firstInEvent;
    private double currentTime;
    private final List<Event> eventList;
    private final List<Event> processedEvents;
    private final Map<String, Queue> queues;
    private boolean printEvent;

    public Scheduler() {
        this.currentTime = 0;
        this.eventList = new ArrayList<>();
        this.queues = new HashMap<>();
        processedEvents = new ArrayList<>();
    }

    public Scheduler withQueue(Queue queue) {
        this.queues.put(queue.getId(), queue);
        if (firstInEvent == null && queue.hasInRange()) {
            this.firstInEvent = new FirstInEvent(queue.getId(), 0);
        }
        return this;
    }

    public Scheduler printEvent(boolean printEvent) {
        this.printEvent = printEvent;
        return this;
    }

    public Scheduler withFirstIn(FirstInEvent firstInEvent) {
        this.firstInEvent = firstInEvent;
        return this;
    }

    public void schedule(Event event) {
        this.eventList.add(event);
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void execute(double firstIn) {
        try {
            double inTime = firstInEvent.getWhen() > 0 ? firstInEvent.getWhen() : firstIn;
            eventList.add(Event.newIn(inTime, firstInEvent.getWhere()));
            while (true) {
                Event event = proxEvento();
                queues.values().forEach(it -> it.updateTime(event, currentTime));
                currentTime = event.getTime();
                event.setWasExecuted();
                if (event.isOut() && event.getTo() != null) {
                    Queue toQueue = queues.get(event.getTo());
                    Queue fromQueue = queues.get(event.getFrom());
                    Event pass = event.toPass();
                    fromQueue.executePass(this);
                    processedEvents.add(pass);
                    toQueue.execute(this, pass);
                } else {
                    processedEvents.add(event);
                    Queue queue = queues.get(event.getFrom());
                    queue.execute(this, event);
              }
            }
        } catch (ExitSimulationException ex) {
            System.out.println("Ended");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("queue.simulation.scheduler.Scheduler: \n");
        builder.append("Tempo final: ").append(currentTime).append("\n");
        builder.append("\n=============================\n");
        queues.values().forEach(queue -> builder.append(queue.print(this)));
        long totalLoss = queues.values().stream().mapToLong(Queue::getLoss).sum();
        builder.append("\n=============================\n");
        builder.append("Perda Total: ").append(totalLoss);
        if (printEvent) {
            builder.append("\n=============================\n");
            builder.append("Eventos Processados: \n");
            IntStream.range(0, processedEvents.size())
                    .forEach(idx -> builder.append("\t")
                            .append(idx).append(": ")
                            .append(processedEvents.get(idx))
                            .append("\n"));
            builder.append("\n=============================\n");
            builder.append("Eventos: \n");
            eventList.forEach(event -> builder.append("\t").append(event).append("\n"));
        }
        return builder.toString();
    }

    private Event proxEvento() {
        Event next = eventList.stream()
                .sorted(Comparator.comparingDouble(Event::getTime))
                .filter(it -> !it.isExecuted())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nenhum proximo evento a ser processado."));
        eventList.remove(next);
        return next;
    }
}
