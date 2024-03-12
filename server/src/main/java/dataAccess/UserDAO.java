package dataAccess;

import model.UserData;

public interface UserDAO {

    public void clearAccess() throws dataAccess.DataAccessException;

    public UserData getUser(String username) throws dataAccess.DataAccessException;

    public void createUser(String username, String email, String password) throws dataAccess.DataAccessException;

    public boolean checkPassword(String username, String password) throws dataAccess.DataAccessException;

}
