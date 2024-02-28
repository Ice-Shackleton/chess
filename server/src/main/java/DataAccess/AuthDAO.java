package DataAccess;

import model.AuthData;

public interface AuthDAO {

    public void clearAccess();

    public String createAuth(String username);

    public AuthData getAuth(String authToken);

    public void deleteAuth(String authToken);
}
