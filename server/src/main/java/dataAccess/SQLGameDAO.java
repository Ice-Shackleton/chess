package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class SQLGameDAO implements GameDAO {

    private static SQLGameDAO instance;
    private static int gameIDGenerator = 0;

    public static synchronized GameDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SQLGameDAO();
        }
        return instance;
    }

    public SQLGameDAO() throws dataAccess.DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    """
                            CREATE TABLE  IF NOT EXISTS gameDAO (
                            gameID INT NOT NULL AUTO_INCREMENT,
                            whiteUsername VARCHAR(255) DEFAULT NULL,
                            blackUsername VARCHAR(255) DEFAULT NULL,
                            gameName VARCHAR(255) NOT NULL,
                            chessGame longtext NOT NULL,
                            observers longtext DEFAULT NULL,
                            PRIMARY KEY (gameID)
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
                            games.getString("blackUsername"), thisGame, games.getString("gameName")));
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
    public boolean colorOccupied(String color, int gameID) throws dataAccess.DataAccessException, BadAccessException {
        if(color == null){
            return false;
        }
        ResultSet games;
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM gameDAO WHERE gameID = ?")) {
                preparedStatement.setString(1, Integer.toString(gameID));
                games = preparedStatement.executeQuery();
                if(games.next()){
                    if (color.equals("WHITE")) {
                        return games.getString("whiteUsername") != null;
                    }
                    if (color.equals("BLACK")) {
                        return (games.getString("blackUsername") != null);
                    }
                    return true;
                }
                throw new BadAccessException("User attempted to use a gameID that does not exist.");
            }
        } catch (SQLException | DataAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        } catch (BadAccessException e) {
            throw new BadAccessException(e.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        ChessGame thisGame = new ChessGame();
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO gameDAO (gameName, chessGame)" +
                            " VALUES(?, ?)")) {

                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, new Gson().toJson(thisGame));
                preparedStatement.executeUpdate();

                ArrayList<GameData> temp = getGameList();
                for (GameData game : temp) {
                    if (Objects.equals(game.gameName(), gameName)) {
                        return game.gameID();
                    }
                }
                throw new DataAccessException("for some reason, created game doesn't exist.");
            }
        } catch (SQLException | DataAccessException e) {
            gameIDGenerator-=1;
            System.out.println(e.getMessage());
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
                        /*
                        ArrayList<String> observers = new Gson().fromJson(games.getString("observers"), ArrayList.class);
                        observers.add(username);
                        newStatement = conn.prepareStatement("UPDATE gameDAO SET observers = ? WHERE gameID = ?");
                        newStatement.setString(1, new Gson().toJson(observers, ArrayList.class));
                        newStatement.setString(2, Integer.toString(gameID));
                        newStatement.executeUpdate();
                        */
                        return true;

                    } else {
                        //Now, we attempt to insert the username into the game and update the data.
                        ChessGame game = new Gson().fromJson(games.getString("chessGame"), ChessGame.class);
                        if (color.equals("WHITE")){
                            if (!colorOccupied(color, gameID)) {
                                newStatement = conn.prepareStatement("UPDATE gameDAO SET whiteUsername = ? WHERE gameID = ?");
                                newStatement.setString(1, username);
                                newStatement.setString(2, Integer.toString(gameID));
                                newStatement.executeUpdate();
                                return true;
                            } else {
                                return false;
                            }
                        } else if (color.equals("BLACK")) {
                            if (!colorOccupied(color, gameID)) {
                                newStatement = conn.prepareStatement("UPDATE gameDAO SET blackUsername = ? WHERE gameID = ?");
                                newStatement.setString(1, username);
                                newStatement.setString(2, Integer.toString(gameID));
                                newStatement.executeUpdate();
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            throw new DataAccessException("user entered something that was neither color");
                        }

                    }
                }

                return false;

            }
        } catch (SQLException | dataAccess.DataAccessException | BadAccessException e) {
            throw new dataAccess.DataAccessException(e.getMessage());
        }
    }

    @Override
    public ChessGame getSingleGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM gameDAO WHERE gameID = ?")) {
                preparedStatement.setString(1, Integer.toString(gameID));
                ResultSet games = preparedStatement.executeQuery();

                if (games.next()) {
                    return new Gson().fromJson(games.getString("chessGame"), ChessGame.class);
                }
                return null;
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateSingleGame(int gameID, ChessGame game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "UPDATE gameDAO SET chessGame = ? WHERE gameID = ?")) {

                preparedStatement.setString(1, new Gson().toJson(game));
                preparedStatement.setString(2, Integer.toString(gameID));
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

    }


}