package dataAccess;

/**
 * Indicates there was an error connecting to the database, often because of an incorrect authToken
 * being used to connect.
 */
public class DataAccessException extends Exception{
    public DataAccessException(String message) {
        super(message);
    }
}
