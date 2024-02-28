package Services;

import DataAccess.AuthDAO;
import model.AuthData;

public class LogoutService {

    private AuthDAO authDAO;

    public LogoutService(AuthDAO authInput){
        this.authDAO = authInput;
    }

    public void logout(String authToken) throws dataAccess.DataAccessException {
        AuthData token = this.authDAO.getAuth(authToken);
        if(token == null){
            throw new dataAccess.DataAccessException("no such authToken exists");
        }
        this.authDAO.deleteAuth(token.authToken());
    }
}
