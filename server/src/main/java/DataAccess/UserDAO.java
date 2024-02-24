package DataAccess;

import model.UserData;

import java.util.HashSet;

public interface UserDAO {

    public void clearAccess();

    public UserData getUser(String username);

    public void createUser(String username, String email, String password);

}
