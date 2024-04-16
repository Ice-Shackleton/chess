package dataAccess;

import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLUserDAO implements UserDAO{

    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]+$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    public SQLUserDAO() throws dataAccess.DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(
                    """
                        CREATE TABLE  IF NOT EXISTS userDAO (
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        PRIMARY KEY (username)
                        )""")) {
                preparedStatement.executeUpdate();

            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearAccess() throws dataAccess.DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("DELETE FROM userDAO")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }
    }

    /**
     * Returns an {@link UserData} user from the database.
     * @param username The requested username of the user.
     * @return Returns the userData if {@param username} exists, null otherwise.
     */
    @Override
    public UserData getUser(String username) throws dataAccess.DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM userDAO WHERE username = ?")) {
                preparedStatement.setString(1, username);
                ResultSet token = preparedStatement.executeQuery();
                if (token.next()){
                    return new UserData(username, token.getString("password"),
                                        token.getString("email"));
                }
                return null;

            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }
    }

    /**
     * Creates a new {@link UserData} user using the provided parameters. Will throw an error if
     * an attempt to make a user with an already existing username is performed.
     * @param username
     * @param email
     * @param password
     */
    @Override
    public void createUser(String username, String email, String password) throws DataAccessException {
        UserData newUser = getUser(username);
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO userDAO (username, password, email) VALUES(?, ?, ?)")) {

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    /**
     * A simple method to compare a passed-in password with a matching user in the database.
     * @param username
     * @param password
     * @return true if passwords match, false otherwise.
     */
    @Override
    public boolean checkPassword(String username, String password) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM userDAO WHERE username = ?")) {
                preparedStatement.setString(1, username);
                ResultSet token = preparedStatement.executeQuery();
                if (token.next()){
                    return password.equals(token.getString("password"));
                }
                return false;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
