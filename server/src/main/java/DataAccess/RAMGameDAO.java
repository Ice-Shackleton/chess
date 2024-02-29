package DataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class RAMGameDAO implements GameDAO{

    private final HashMap<Integer, GameData> gameData;
    private static int gameIDGenerator = 0;
    private final HashMap<Integer, ArrayList<String>> observers;

    public RAMGameDAO() {
        this.gameData = new HashMap<Integer, GameData>();
        this.observers = new HashMap<Integer, ArrayList<String>>();
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

    @Override
    public ArrayList<String> getObservers(int gameID) {
        return this.observers.get(gameID);
    }

    /**
     * a method for determining if a player slot is open. Will always return true if the color does not
     * exactly match 'WHITE' or 'BLACK'.
     * @param color
     * @param gameID
     * @return whether whiteUsername/blackUsername is empty; true if null, false otherwise.
     */
    @Override
    public boolean colorOccupied(String color, int gameID) {
        if(color == null){
            return false;
        }
        if(this.gameData.get(gameID) == null){
            return false;
        }
        if(color.equals("WHITE")){
            return !(this.gameData.get(gameID).whiteUsername() == null);
        }
        if(color.equals("BLACK")){
            return !(this.gameData.get(gameID).blackUsername() == null);
        }
        return true;
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

    /**
     * Adds a user to a game. If no color is specified the user is joined as an observer, unless that user
     * is already observing the game.
     *
     * @param color    Must be either 'WHITE' or 'BLACK'
     * @param gameID
     * @param username
     * @return
     */
    @Override
    public boolean joinGame(String color, int gameID, String username){
        if (!(this.gameData.get(gameID) == null)) {
            if (color == null) {

                ArrayList<String> temp;
                if (this.gameData.get(gameID) == null) {
                    temp = this.observers.get(gameID);
                } else {
                    temp = new ArrayList<String>();
                }

                if (temp != null && !(temp.contains(username))) {
                    temp.add(username);
                }
                this.observers.put(gameID, temp);

            } else {

                ChessGame game = this.gameData.get(gameID).game();
                if (color.equals("WHITE")) {
                    String blackUser = this.gameData.get(gameID).blackUsername();
                    GameData newColor = new GameData(gameID, username, blackUser, game);
                    this.gameData.put(gameID, newColor);
                }
                if (color.equals("BLACK")) {
                    String whiteUser = this.gameData.get(gameID).whiteUsername();
                    GameData newColor = new GameData(gameID, whiteUser, username, game);
                    this.gameData.put(gameID, newColor);
                }

            }
            return true;
        }
        return false;
    }
}
