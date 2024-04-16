package Services;

import dataAccess.AuthDAO;
import dataAccess.BadAccessException;
import dataAccess.GameDAO;
import dataAccess.DataAccessException;
import model.AuthData;

public class CreateGameService {

    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public CreateGameService(AuthDAO authInput, GameDAO gameInput){
        this.gameDAO = gameInput;
        this.authDAO = authInput;
    }

    public int createGame(String authToken, String gameName) throws dataAccess.DataAccessException, BadAccessException {
        AuthData token;
        try {
            token = this.authDAO.getAuth(authToken);
        } catch (dataAccess.DataAccessException e) {
            throw new DataAccessException("what are you even doing here, man");
        }
        if (token == null) {
            throw new dataAccess.DataAccessException("no such authToken exists");
        }
        if (gameName == null || gameName.isEmpty()) {
            throw new BadAccessException("user did not supply game name");
        }
        try {
            return this.gameDAO.createGame(gameName);
        } catch (DataAccessException e) {
            throw new DataAccessException("Never gonna GIVE YOU UP");
        }
    }
}
