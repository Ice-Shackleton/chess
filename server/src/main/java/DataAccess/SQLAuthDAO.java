package DataAccess;

import dataAccess.DataAccessException;
import model.AuthData;
import java.sql.*;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLAuthDAO implements AuthDAO {



    public SQLAuthDAO() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(
                    """
                        CREATE TABLE  IF NOT EXISTS authDAO (
                        authToken VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        PRIMARY KEY (authToken)
                        )""")) {
                preparedStatement.executeUpdate();

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearAccess() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authDAO")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuthToken = new AuthData(authToken, username);
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO authDAO (authToken, username) VALUES(?, ?)")) {

                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);

                preparedStatement.executeUpdate();

                return authToken;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM authDAO WHERE authToken = ?")) {
                preparedStatement.setString(1, authToken);
                ResultSet token = preparedStatement.executeQuery();
                if (token.next()){
                    return new AuthData(authToken, token.getString("username"));
                }
                return null;

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("DELETE FROM authDAO WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
