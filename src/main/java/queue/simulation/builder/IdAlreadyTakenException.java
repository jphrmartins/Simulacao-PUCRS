package queue.simulation.builder;

public class IdAlreadyTakenException extends RuntimeException {
    public IdAlreadyTakenException(String id) {
        super(String.format("The %s arleady took", id));
    }
}
