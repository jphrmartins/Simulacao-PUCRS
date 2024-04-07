public class TimeHandler {
    private final TimeRange in;
    private final TimeRange out;

    public TimeHandler(TimeRange in, TimeRange out) {
        this.in = in;
        this.out = out;
    }

    public Event nextIn(double globalTime, int from) {
        double next = in.next();
        return Event.newIn(globalTime + next, from);
    }

    public Event nextOut(double globalTime, int from){
        double next = out.next();
        return Event.newOut(globalTime + next, from);
    }
}
