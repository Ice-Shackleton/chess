package Services;

import DataAccess.AuthDAO;
import DataAccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class ListGameService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public ListGameService(AuthDAO authInput, GameDAO gameInput) {
        this.authDAO = authInput;
        this.gameDAO = gameInput;
    }

    public ArrayList<GameData> listGames(String authToken) throws dataAccess.DataAccessException {
        AuthData token = this.authDAO.getAuth(authToken);
        if(token == null){
            throw new dataAccess.DataAccessException("no such authToken exists");
        }
        return this.gameDAO.getGameList();


    }


}
