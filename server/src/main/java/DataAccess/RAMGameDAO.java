package DataAccess;

import model.GameData;

import java.util.HashSet;

public class RAMGameDAO implements GameDAO{

    private HashSet<GameData> gameData;

    public RAMGameDAO() {
        this.gameData = new HashSet<GameData>();
    }

    @Override
    public void clearAccess() {
        this.gameData.clear();
    }
}
