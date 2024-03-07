package DataAccess;

import model.AuthData;

public interface AuthDAO {

    public void clearAccess() throws dataAccess.DataAccessException;

    public String createAuth(String username) throws dataAccess.DataAccessException;

    public AuthData getAuth(String authToken) throws dataAccess.DataAccessException;

    public void deleteAuth(String authToken) throws dataAccess.DataAccessException;
}
