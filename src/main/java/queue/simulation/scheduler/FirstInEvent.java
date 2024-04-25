package queue.simulation.scheduler;

public class FirstInEvent {
    private int where;
    private double when;

    public FirstInEvent(int where, double when) {
        this.where = where;
        this.when = when;
    }

    public int getWhere() {
        return where;
    }

    public double getWhen() {
        return when;
    }
}
