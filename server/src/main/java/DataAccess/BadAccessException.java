package DataAccess;

public class BadAccessException extends Exception{

    /**
     * This error covers use cases where nonsense garbage has been entered as a parameter. Most useful for
     * methods that require user input.
     * @param message
     */
    public BadAccessException(String message) {
        super(message);
    }

}
