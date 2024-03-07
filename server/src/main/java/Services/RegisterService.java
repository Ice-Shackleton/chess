package Services;

import DataAccess.AuthDAO;
import DataAccess.BadAccessException;
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

    public String register(String username, String email, String password) throws dataAccess.DataAccessException, BadAccessException {

        UserData newUser = this.userDAO.getUser(username);
        if (newUser != null) {
            throw new dataAccess.DataAccessException("Tried to make a user with an already extant username.");
        }
        if(email == null || password == null){
            throw new BadAccessException("User did not provide specified fields.");
        }
        this.userDAO.createUser(username, email, password);
        try {
            return this.authDAO.createAuth(username);
        } catch (dataAccess.DataAccessException e){
            throw new dataAccess.DataAccessException("you've done something very wrong to see this.");
        }
    }
}
