package zoo.exception;

// Thrown when an action would cost more money than the budget has left
public class BudgetExceededException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    public BudgetExceededException(String message) {
        super(message);
    }
}
