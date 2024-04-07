public class Event {
    private Type type;
    private int from;
    private double time;
    private boolean executed;

    private Event(Type type, double time, int from) {
        this.type = type;
        this.time = time;
        this.from = from;
    }

    public static Event newOut(double tempo, int from) {
        return new Event(Type.OUT, tempo, from);
    }

    public static Event newIn(double tempo, int from) {
        return new Event(Type.IN, tempo, from);
    }

    public Event toPass() {
        return new Event(Type.PASS, this.time, this.from);
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
        return "Evento{" +
                "tipo=" + type +
                ", tempo=" + time +
                ", executed=" + executed +
                '}';
    }

    public enum Type {
        IN, OUT, PASS
    }

}
