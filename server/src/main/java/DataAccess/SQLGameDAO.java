package DataAccess;

import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() {
        
    }

    @Override
    public void clearAccess() {

    }

    @Override
    public ArrayList<GameData> getGameList() {
        return null;
    }

    @Override
    public ArrayList<String> getObservers(int gameID) {
        return null;
    }

    @Override
    public boolean colorOccupied(String color, int gameID) {
        return false;
    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public boolean joinGame(String color, int gameID, String username) {
        return false;
    }
}
