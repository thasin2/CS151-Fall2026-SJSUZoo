package zoo.exception;
 
// Thrown when the zoo cannot take any more visitors, either because it is at its daily capacity
// or because the system already holds the maximum number of objects
public class ZooFullException extends IllegalStateException {
    private static final long serialVersionUID = 1L;
 
    public ZooFullException(String message) {
        super(message);
    }
}
 