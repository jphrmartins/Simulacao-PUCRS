import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
            eventList.add(Event.newIn(firstIn, -1));
            while (true) {
                Event event = proxEvento();
                double currentTimeBeforeEvent = currentTime;
                currentTime = event.getTime();
                event.setWasExecuted();
                processedEvents.add(event);
                if (queues.size() > 1 && event.isOut() && queues.get(queues.size() - 1).getId() != event.getFrom()) {
                    Queue queue = queues.get(event.getFrom() + 1);
                    queue.execute(this, event.toPass(), currentTimeBeforeEvent);
                } else {
                    Queue queue = queues.get(event.getFrom());
                    queue.execute(this, event, currentTimeBeforeEvent);
                }

            }
        } catch (ExitSimulationException ex) {
            System.out.println("Ended");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Scheduler: \n");
        queues.forEach(queue -> builder.append(queue.print(this)));
        long totalLoss = queues.stream().mapToLong(Queue::getLoss).sum();
        builder.append("\n=============================\n");
        builder.append("Perda Total: ").append(totalLoss);
        builder.append("\n=============================\n");
        builder.append("Eventos Processados: ");
        processedEvents.forEach(event -> builder.append(event).append("\n"));
        builder.append("\n=============================\n");
        builder.append("Eventos: ");
        eventList.forEach(event -> builder.append(event).append("\n"));

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
