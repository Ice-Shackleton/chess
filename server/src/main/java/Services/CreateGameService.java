package Services;

import DataAccess.AuthDAO;
import DataAccess.BadAccessException;
import DataAccess.GameDAO;
import model.AuthData;
import model.GameData;

public class CreateGameService {

    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public CreateGameService(AuthDAO authInput, GameDAO gameInput){
        this.gameDAO = gameInput;
        this.authDAO = authInput;
    }

    public int CreateGame(String authToken, String gameName) throws dataAccess.DataAccessException, BadAccessException {
        AuthData token = this.authDAO.getAuth(authToken);
        if(token == null){
            throw new dataAccess.DataAccessException("no such authToken exists");
        }
        if(gameName.isEmpty()){
            throw new BadAccessException("user did not supply game name");
        }
        return this.gameDAO.createGame(gameName);
    }
}
