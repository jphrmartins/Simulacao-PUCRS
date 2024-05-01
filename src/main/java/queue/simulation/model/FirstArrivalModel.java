package queue.simulation.model;

import queue.simulation.scheduler.FirstInEvent;

public class FirstArrivalModel {
    private String where;
    private double when;

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public double getWhen() {
        return when;
    }

    public void setWhen(double when) {
        this.when = when;
    }

    public FirstInEvent toModel() {
        return new FirstInEvent(where, when);
    }
}
