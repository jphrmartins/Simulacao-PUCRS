package queue.simulation.builder;

public class IdAlreadyTakenException extends RuntimeException {
    public IdAlreadyTakenException(int id) {
        super(String.format("The %d arleady took", id));
    }
}
