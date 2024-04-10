import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class Scheduler {
    private double currentTime;
    private final List<Event> eventList;
    private final List<Event> processedEvents;
    private final List<Queue> queues;

    public Scheduler() {
        this.currentTime = 0;
        this.eventList = new ArrayList<>();
        this.queues = new ArrayList<>();
        processedEvents = new ArrayList<>();
    }

    public Scheduler withQueue(Queue queue) {
        this.queues.add(queue);
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
            eventList.add(Event.newIn(firstIn, 0));
            while (true) {
                Event event = proxEvento();
                queues.forEach(it -> it.updateTime(event, currentTime));
                currentTime = event.getTime();
                event.setWasExecuted();
                if (queues.size() > 1 && event.isOut() && queues.get(queues.size() - 1).getId() != event.getFrom()) {
                    Queue toQueue = queues.get(event.getFrom() + 1);
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
        StringBuilder builder = new StringBuilder("Scheduler: \n");
        builder.append("Tempo final: ").append(currentTime).append("\n");
        builder.append("\n=============================\n");
        queues.forEach(queue -> builder.append(queue.print(this)));
        long totalLoss = queues.stream().mapToLong(Queue::getLoss).sum();
        builder.append("\n=============================\n");
        builder.append("Perda Total: ").append(totalLoss);
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
