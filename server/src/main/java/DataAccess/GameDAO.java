package DataAccess;

import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GameDAO {

    public void clearAccess() throws dataAccess.DataAccessException;

    public ArrayList<GameData> getGameList() throws dataAccess.DataAccessException;

    public ArrayList<String> getObservers(int gameID) throws dataAccess.DataAccessException;

    public boolean colorOccupied(String color, int gameID) throws dataAccess.DataAccessException, SQLException, BadAccessException;

    public int createGame(String gameName) throws dataAccess.DataAccessException;

    public boolean joinGame(String color, int gameID, String username) throws dataAccess.DataAccessException;

}
