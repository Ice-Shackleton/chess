package Services;

import DataAccess.AuthDAO;
import DataAccess.BadAccessException;
import DataAccess.GameDAO;
import DataAccess.IncorrectException;
import model.AuthData;

public class JoinGameService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public JoinGameService(AuthDAO authInput, GameDAO gameDAO) {
        this.authDAO = authInput;
        this.gameDAO = gameDAO;
    }

    public void joinGame(String authToken, String color, int gameID)
            throws BadAccessException, dataAccess.DataAccessException, IncorrectException
    {
        AuthData token = this.authDAO.getAuth(authToken);
        String username = token.username();

        if (color != null && (!(color.equals("WHITE")) && !(color.equals("BLACK")))) {
            throw new BadAccessException("user put in an invalid color");
        }
        if(token == null){
            throw new dataAccess.DataAccessException("user entered invalid authToken");
        }
        if(this.gameDAO.colorOccupied(color, gameID)){
            throw new IncorrectException("user attempted to join an already occupied spot.");
        }

        this.gameDAO.joinGame(color, gameID, username);
    }
}
