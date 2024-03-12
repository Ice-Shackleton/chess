package Services;

import dataAccess.*;
import model.AuthData;

import java.sql.SQLException;

public class JoinGameService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public JoinGameService(AuthDAO authInput, GameDAO gameInput) {
        this.authDAO = authInput;
        this.gameDAO = gameInput;
    }

    public void joinGame(String authToken, String color, int gameID)
            throws BadAccessException, dataAccess.DataAccessException, IncorrectException {
        AuthData token;
        try {
            token = this.authDAO.getAuth(authToken);
        } catch (dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException("this absolutely should not throw, you're an idiot");
        }

        if (color != null && (!(color.equals("WHITE")) && !(color.equals("BLACK")))) {
            throw new BadAccessException("user put in an invalid color");
        }
        if (token == null) {
            throw new dataAccess.DataAccessException("user entered invalid authToken");
        }
        String username = token.username();
        try {
            if (this.gameDAO.colorOccupied(color, gameID)) {
                throw new IncorrectException("user attempted to join an already occupied spot.");
            }
        } catch (SQLException | IncorrectException e) {
            throw new IncorrectException(e.getMessage());
        } catch (BadAccessException e) {
            throw new BadAccessException(e.getMessage());
        }


        boolean testCase = this.gameDAO.joinGame(color, gameID, username);
        if (!testCase) {
            throw new BadAccessException("user provided an invalid gameID.");
        }
    }
}
