package zoo.exception;

// Thrown when a suspended staff member tries to do any work
public class StaffSuspendedException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    public StaffSuspendedException(String message) {
        super(message);
    }
}
