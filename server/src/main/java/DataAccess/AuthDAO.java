package DataAccess;

public interface AuthDAO {

    public void clearAccess();

    public String createAuth(String username);
}
