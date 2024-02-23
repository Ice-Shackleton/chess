package DataAccess;
import model.*;

import java.util.HashSet;

/**
 * A Database will track three separate types of data:
 * 1. User data, which will store a user's username, password, and email.
 * 2. Authority data, which stores authTokens for games and logins.
 * 3. Game data, which stores {@link chess.ChessGame} games and their players.
 */
public class Database {

    private HashSet<UserData> users;
    private HashSet<AuthData> auth;
    private HashSet<GameData> game;


    public Database() {
        users = new HashSet<UserData>();
        auth = new HashSet<AuthData>();
        game = new HashSet<GameData>();
    }

    public HashSet<UserData> getUserData() {
        return users;
    }

    public HashSet<AuthData> getAuthData() {
        return auth;
    }

    public HashSet<GameData> getGameData() {
        return game;
    }


}
