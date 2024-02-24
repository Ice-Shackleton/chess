package DataAccess;

import model.UserData;

import java.util.HashSet;

public class RAMUserDAO implements UserDAO {

    private HashSet<UserData> userData;

    public RAMUserDAO() {
        this.userData = new HashSet<UserData>();
    }

    @Override
    public void clearAccess() {
        this.userData.clear();
    }

    @Override
    public void getUser(String username) throws dataAccess.DataAccessException {

    }


}
