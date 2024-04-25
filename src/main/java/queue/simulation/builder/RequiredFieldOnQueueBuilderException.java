package queue.simulation.builder;

public class RequiredFieldOnQueueBuilderException extends RuntimeException {
    public RequiredFieldOnQueueBuilderException(String what) {
        super(String.format("The field '%s' is Required", what));
    }
}
