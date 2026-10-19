package zoo.exception;

// Thrown when a staff member's pay would be zero or negative
public class InvalidPayException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public InvalidPayException(String message) {
        super(message);
    }
}
