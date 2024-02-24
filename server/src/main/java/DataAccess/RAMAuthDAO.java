package DataAccess;

import model.AuthData;

import java.util.HashSet;

public class RAMAuthDAO implements AuthDAO{

    private HashSet<AuthData> authData;

    public RAMAuthDAO() {
        this.authData = new HashSet<AuthData>();
    }

    @Override
    public void clearAccess() {
        this.authData.clear();
    }
}
