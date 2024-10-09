package queue.simulation.time;

import queue.simulation.scheduler.Event;

public class TimeHandler {
    private final TimeRange in;
    private final TimeRange out;

    public TimeHandler(TimeRange in, TimeRange out) {
        this.in = in;
        this.out = out;
    }

    public Event nextIn(double globalTime, String from) {
        double next = in.next();
        return Event.newIn(globalTime + next, from);
    }

    public Event nextOut(double globalTime, String from, String to){
        double next = out.next();
        return Event.newOut(globalTime + next, from, to);
    }

    public double generateMi() {
        return out.generateMi();
    }

    public boolean hasIn() {
        return in != null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (in != null) {
            builder.append("IN: ")
                    .append(in).append(" ");
        }
        builder.append("OUT: ").append(out);
        return builder.toString();
    }
}
