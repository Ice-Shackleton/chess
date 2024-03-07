package Services;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import DataAccess.IncorrectException;
import DataAccess.UserDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class LoginService {

    private UserDAO userDAO;
    private AuthDAO authDAO;

    public LoginService(UserDAO userInput, AuthDAO authInput) {
        this.userDAO = userInput;
        this.authDAO = authInput;
    }

    public String login(String username, String password) throws DataAccessException, IncorrectException {
        UserData user = this.userDAO.getUser(username);
        if (user == null){
            throw new DataAccessException("user does not exist.");
        }
        if(!this.userDAO.checkPassword(username, password)){
            throw new IncorrectException("Incorrect password.");
        }
        String token;
        try {
            token = this.authDAO.createAuth(username);
            return token;
        } catch (dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException("this absolutely should not throw, you're an idiot");
        }

    }
}
