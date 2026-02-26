package tommy;

/**
 * Custom exception class for errors specific to the Tommy chatbot application.
 */
public class TommyException extends Exception {
    /**
     * Constructs a new TommyException with the specified detail message.
     *
     * @param message the detail message (saved for later retrieval)
     */
    public TommyException(String message) {
        super(message);
    }
}
