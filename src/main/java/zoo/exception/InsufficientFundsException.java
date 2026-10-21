package zoo.exception;
 
// Thrown when a visitor tries to pay for something they cannot afford
public class InsufficientFundsException extends IllegalStateException {
    private static final long serialVersionUID = 1L;
 
    public InsufficientFundsException(String message) {
        super(message);
    }
}
 
 