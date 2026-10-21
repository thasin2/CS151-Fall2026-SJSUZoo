package zoo.exception;
 
// Thrown when someone tries to add an object that is equal to one the zoo already has
public class DuplicateEntryException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;
 
    public DuplicateEntryException(String message) {
        super(message);
    }
}
 
 