package DataAccess;

public class IncorrectException extends Exception{

    /**
     * This error handles cases where something DOES exist in the database, but the request is incorrect.
     * @param message
     */
    public IncorrectException(String message) {
        super(message);
    }

}
