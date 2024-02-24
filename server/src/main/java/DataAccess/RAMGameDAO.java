package DataAccess;

import model.GameData;

import java.util.HashMap;
import java.util.HashSet;

public class RAMGameDAO implements GameDAO{

    private HashMap<Integer, GameData> gameData;

    public RAMGameDAO() {
        this.gameData = new HashMap<Integer, GameData>();
    }

    @Override
    public void clearAccess() {
        this.gameData.clear();
    }
}
