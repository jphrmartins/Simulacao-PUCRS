package queue.simulation.scheduler;

public class Event {
    private Type type;
    private int from;
    private Integer to;
    private double time;
    private boolean executed;

    private Event(Type type, double time, int from, Integer to) {
        this.type = type;
        this.time = time;
        this.from = from;
        this.to = to;
    }

    private Event(Type type, double time, int from) {
        this.type = type;
        this.time = time;
        this.from = from;
    }

    private Event(Type type, double time, int from, boolean executed) {
        this(type, time, from);
        this.executed = executed;
    }

    public static Event newOut(double tempo, int from, Integer to) {
        return new Event(Type.OUT, tempo, from, to);
    }

    public static Event newIn(double tempo, int from) {
        return new Event(Type.IN, tempo, from);
    }

    public Event toPass() {
        return new Event(Type.PASS, this.time, this.from, this.executed);
    }

    public boolean isIn() {
        return Type.IN.equals(type);
    }

    public boolean isOut() {
        return Type.OUT.equals(type);
    }

    public boolean isPass() {
        return Type.PASS.equals(type);
    }

    public int getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public Type getType() {
        return type;
    }

    public double getTime() {
        return time;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setWasExecuted() {
        executed = true;
    }

    @Override
    public String toString() {
        return "queue.simulation.scheduler.Event{" +
                "Type=" + type +
                ", time=" + time +
                ", executed=" + executed +
                ", from=" + from +
                '}';
    }

    public enum Type {
        IN, OUT, PASS
    }

}
