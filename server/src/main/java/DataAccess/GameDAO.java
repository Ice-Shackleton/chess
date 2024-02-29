package DataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {

    public void clearAccess();

    public ArrayList<GameData> getGameList();

    public ArrayList<String> getObservers(int gameID);

    public boolean colorOccupied(String color, int gameID);

    public int createGame(String gameName);

    public boolean joinGame(String color, int gameID, String username);

}
