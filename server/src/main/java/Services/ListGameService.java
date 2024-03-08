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
        AuthData token;
        try {
            token = this.authDAO.getAuth(authToken);
        } catch (dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException("you're an idiot.");
        }
        if(token == null){
            throw new dataAccess.DataAccessException("no such authToken exists");
        }
        try {
            return this.gameDAO.getGameList();
        } catch (dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }


    }


}
