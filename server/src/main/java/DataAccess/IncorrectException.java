package DataAccess;

public class IncorrectException extends Exception{

    /**
     * This error handles cases where something DOES exist in the database, but the request is incorrect.
     * For example, entering the wrong password for a user would throw this exception.
     * @param message
     */
    public IncorrectException(String message) {
        super(message);
    }

}
