package DataAccess;

import model.UserData;

import java.util.HashSet;

public interface UserDAO {

    public void clearAccess();

    public void getUser(String username) throws dataAccess.DataAccessException;

}
