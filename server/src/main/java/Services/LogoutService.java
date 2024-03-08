package Services;

import DataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;

public class LogoutService {

    private AuthDAO authDAO;

    public LogoutService(AuthDAO authInput){
        this.authDAO = authInput;
    }

    public void logout(String authToken) throws dataAccess.DataAccessException {
        AuthData token;
        try {
            token = this.authDAO.getAuth(authToken);
            if (token == null) {
                throw new DataAccessException("This token doesn't exist.");
            }
            this.authDAO.deleteAuth(token.authToken());
        } catch (dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException("this token probably doesn't exist.");
        }
    }
}
