package windy.exception;

/**
 * Represents the error that user's input format is wrong
 */
public class InvalidInputFormatException extends Exception {
    public InvalidInputFormatException(String message) {
        super(message);
    }
}
