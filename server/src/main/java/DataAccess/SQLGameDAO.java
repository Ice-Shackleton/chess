package DataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {

    private static int gameIDGenerator = 0;

    public SQLGameDAO() throws dataAccess.DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    """
                            CREATE TABLE  IF NOT EXISTS gameDAO (
                            gameID INT NOT NULL,
                            whiteUsername VARCHAR(255) NOT NULL,
                            blackUsername VARCHAR(255) NOT NULL,
                            gameName VARCHAR(255) NOT NULL,
                            chessGame longtext NOT NULL,
                            observers longtext NOT NULL,
                            PRIMARY KEY (authToken) 
                            )""")) {
                preparedStatement.executeUpdate();

            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clearAccess() throws dataAccess.DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM gameDAO")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<GameData> getGameList() throws dataAccess.DataAccessException {
        ArrayList<GameData> thisList = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM gameDAO")) {
                ResultSet games = preparedStatement.executeQuery();
                while (games.next()) {
                    ChessGame thisGame = new Gson().fromJson(games.getString("chessGame"), ChessGame.class);
                    int gameID = Integer.parseInt(games.getString("gameID"));
                    thisList.add(new GameData(gameID, games.getString("whiteUsername"),
                            games.getString("blackUsername"), thisGame));
                }
                return thisList;
            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }
    }

    @Override
    public ArrayList<String> getObservers(int gameID) throws dataAccess.DataAccessException {
        ArrayList<String> thisList = new ArrayList<String>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM gameDAO WHERE gameID = ?")) {
                preparedStatement.setString(1, Integer.toString(gameID));
                ResultSet observers = preparedStatement.executeQuery();
                if (observers.next()) {
                    thisList = new Gson().fromJson(observers.getString("observers"), ArrayList.class);
                }
                return thisList;
            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean colorOccupied(String color, int gameID) throws dataAccess.DataAccessException {
        if(color == null){
            return false;
        }
        ResultSet games;
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM gameDAO WHERE gameID = ?")) {
                games = preparedStatement.executeQuery();
                games.next();
                if(color.equals("WHITE")){
                    return !(games.getString("whiteUsername").isEmpty());
                }
                if(color.equals("BLACK")){
                    return !(games.getString("blackUsername").isEmpty());
                }
                return true;
            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        int gameID = gameIDGenerator+1;
        gameIDGenerator +=1;
        ChessGame thisGame = new ChessGame();
        ArrayList<String> observers = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO gameDAO (gameID, whiteUsername, blackUsername, gameName, chessGame, observers)" +
                            " VALUES(?, ?, ?, ?, ?, ?)")) {

                preparedStatement.setString(1, Integer.toString(gameID));
                preparedStatement.setString(2, null);
                preparedStatement.setString(3, null);
                preparedStatement.setString(4, gameName);
                preparedStatement.setString(5, new Gson().toJson(thisGame, ChessGame.class));
                preparedStatement.setString(6, new Gson().toJson(observers, ArrayList.class));

                preparedStatement.executeUpdate();

                return gameID;
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean joinGame(String color, int gameID, String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM gameDAO WHERE gameID = ?")) {
                preparedStatement.setString(1, Integer.toString(gameID));
                ResultSet games = preparedStatement.executeQuery();

                if (games.next()){
                    //First, we need to handle the color.
                    java.sql.PreparedStatement newStatement;
                    if (color == null){
                        ArrayList<String> observers = new Gson().fromJson(games.getString("observers"), ArrayList.class);
                        observers.add(username);
                        newStatement = conn.prepareStatement("UPDATE gameDAO SET observers = ? WHERE gameID = ?");
                        newStatement.setString(1, new Gson().toJson(observers, ArrayList.class));
                        newStatement.setString(2, Integer.toString(gameID));
                        newStatement.executeUpdate();
                        return true;

                    } else {
                        //Now, we attempt to insert the username into the game and update the data.
                        ChessGame game = new Gson().fromJson(games.getString("chessGame"), ChessGame.class);
                        if (color.equals("WHITE")){
                            newStatement = conn.prepareStatement("UPDATE gameDAO SET whiteUsername = ? WHERE gameID = ?");
                            newStatement.setString(1, username);
                            newStatement.setString(2, Integer.toString(gameID));
                            newStatement.executeUpdate();
                            return true;
                        } else if (color.equals("BLACK")) {
                            newStatement = conn.prepareStatement("UPDATE gameDAO SET blackUsername = ? WHERE gameID = ?");
                            newStatement.setString(1, username);
                            newStatement.setString(2, Integer.toString(gameID));
                            newStatement.executeUpdate();
                            return true;
                        } else {
                            throw new DataAccessException("user entered something that was neither color");
                        }

                    }
                }

                return false;
            }
        } catch (SQLException | dataAccess.DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }
    }
}