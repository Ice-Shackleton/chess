package DataAccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public interface GameDAO {

    public void clearAccess();

    public ArrayList<GameData> getGameList();

    public ArrayList<String> getObservers(int gameID);

    public boolean colorOccupied(String color, int gameID);

    public int createGame(String gameName);

    public void joinGame(String color, int gameID, String username);

}
