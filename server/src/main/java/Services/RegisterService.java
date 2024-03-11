package Services;

import DataAccess.*;
import model.AuthData;
import model.UserData;

public class RegisterService {

    private UserDAO userDAO;
    private AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public String register(String username, String email, String password) throws dataAccess.DataAccessException, BadAccessException, IncorrectException {
        UserData newUser;
        try {
            newUser = this.userDAO.getUser(username);
        } catch (dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException("This user probably doesn't exist.");
        }
        if (newUser != null) {
            throw new dataAccess.DataAccessException("Tried to make a user with an already extant username.");
        }
        if(email == null || password == null){
            throw new BadAccessException("User did not provide specified fields.");
        }

        try {
            this.userDAO.createUser(username, email, password);
            return this.authDAO.createAuth(username);
        } catch (dataAccess.DataAccessException e){
            throw new dataAccess.DataAccessException("you've done something very wrong to see this.");
        }
    }
}
