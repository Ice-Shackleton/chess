package DataAccess;

import model.UserData;

import java.util.HashSet;

public interface UserDAO {

    public void clearAccess() throws dataAccess.DataAccessException;

    public UserData getUser(String username) throws dataAccess.DataAccessException;

    public void createUser(String username, String email, String password) throws IncorrectException, dataAccess.DataAccessException;

    public boolean checkPassword(String username, String password) throws dataAccess.DataAccessException;

}
