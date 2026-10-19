package zoo.ui;

// Thrown when the user types EXIT at any prompt, so the program can end cleanly from anywhere
public class ExitRequestedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExitRequestedException() {
        super("The user asked to exit.");
    }
}
