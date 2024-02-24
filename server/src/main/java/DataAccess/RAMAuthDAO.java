package DataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class RAMAuthDAO implements AuthDAO{

    private HashMap<String, AuthData> authData;

    public RAMAuthDAO() {
        this.authData = new HashMap<String, AuthData>();
    }

    @Override
    public void clearAccess() {
        this.authData.clear();
    }

    /**
     * Creates a new AuthToken and matches it with the provided username. Note that more than one
     * copy of a username can be in the database, so long as each authToken is unique.
     * @param username
     * @return the String authToken.
     */
    @Override
    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuthToken = new AuthData(authToken, username);
        this.authData.put(authToken, newAuthToken);
        return authToken;
    }
}
