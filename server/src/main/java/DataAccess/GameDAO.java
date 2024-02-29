package DataAccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public interface GameDAO {

    public void clearAccess();

    public ArrayList<GameData> getGameList();

    public int createGame(String gameName);

}
