package Services;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class RegisterService {

    private UserDAO userDAO;
    private AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public String register(String username, String email, String password) throws dataAccess.DataAccessException {
        UserData newUser = this.userDAO.getUser(username);
        if (newUser != null) {
            throw new dataAccess.DataAccessException("Tried to make a user with an already extant username.");
        }
        this.userDAO.createUser(username, email, password);
        return this.authDAO.createAuth(username);
    }
}
