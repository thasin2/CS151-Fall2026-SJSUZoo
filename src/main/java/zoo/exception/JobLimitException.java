package zoo.exception;

// Thrown when a staff member tries to do more jobs than their shifts this week allow
public class JobLimitException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    public JobLimitException(String message) {
        super(message);
    }
}
