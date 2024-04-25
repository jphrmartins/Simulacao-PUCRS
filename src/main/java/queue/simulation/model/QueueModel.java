package queue.simulation.model;

import queue.simulation.builder.QueueBuilder;
import queue.simulation.random.RandomGenerator;

public class QueueModel {
    private int id;
    private int server;
    private Integer maxCapacity;
    private RangeModel in;
    private RangeModel out;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public RangeModel getIn() {
        return in;
    }

    public void setIn(RangeModel in) {
        this.in = in;
    }

    public RangeModel getOut() {
        return out;
    }

    public void setOut(RangeModel out) {
        this.out = out;
    }

    public QueueBuilder toQueue(RandomGenerator randomGenerator) {
        QueueBuilder builder = new QueueBuilder(randomGenerator)
                .withId(id)
                .withServidors(server)
                .withExitRange(out.getMin(), out.getMax());
        if (in != null) builder.withInRange(in.getMin(), in.getMax());
        if (maxCapacity != null) builder.withMaxCapacity(maxCapacity);

        return builder;
    }
    
}
