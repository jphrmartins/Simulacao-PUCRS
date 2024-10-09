package queue.simulation.model;

import queue.simulation.builder.QueueBuilder;
import queue.simulation.random.RandomGenerator;

public class QueueModel {
    private String id;
    private String name;
    private int server;
    private Integer maxCapacity;
    private RangeModel in;
    private RangeModel out;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getName() {
        return name != null ? name : id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QueueBuilder toQueue(RandomGenerator randomGenerator) {
        QueueBuilder builder = new QueueBuilder(randomGenerator)
                .withId(id)
                .withName(name)
                .withServidors(server)
                .withExitRange(out.getMin(), out.getMax());
        if (in != null) builder.withInRange(in.getMin(), in.getMax());
        if (maxCapacity != null) builder.withMaxCapacity(maxCapacity);

        return builder;
    }
    
}
