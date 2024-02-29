package DataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RAMGameDAO implements GameDAO{

    private final HashMap<Integer, GameData> gameData;
    private static int gameIDGenerator = 0;

    public RAMGameDAO() {
        this.gameData = new HashMap<Integer, GameData>();
    }

    @Override
    public void clearAccess() {
        this.gameData.clear();
    }

    @Override
    public ArrayList<GameData> getGameList() {
        if (this.gameData.isEmpty()){
            return null;
        }
        return new ArrayList<GameData>(this.gameData.values());
    }

    /**
     * Creates a new {@link chess.ChessGame} game and the associated {@link GameData} along with it.
     * @param gameName
     * @return the gameID of the newly created game.
     */
    @Override
    public int createGame(String gameName) {
        int gameID = gameIDGenerator+1;
        gameIDGenerator +=1;
        GameData newGame = new GameData(gameID, null, null, new ChessGame());
        this.gameData.put(gameID, newGame);
        return gameID;
    }


}
