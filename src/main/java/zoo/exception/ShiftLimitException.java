package zoo.exception;

// Thrown when a staff member would go over the weekly shift limit for full-time or part-time staff
public class ShiftLimitException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    public ShiftLimitException(String message) {
        super(message);
    }
}
