package queue.simulation.scheduler;

public class FirstInEvent {
    private String where;
    private double when;

    public FirstInEvent(String where, double when) {
        this.where = where;
        this.when = when;
    }

    public String getWhere() {
        return where;
    }

    public double getWhen() {
        return when;
    }
}
