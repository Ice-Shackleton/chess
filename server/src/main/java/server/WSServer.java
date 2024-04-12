package server;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverMessage;
import webSocketMessages.userCommands.JoinPlayerMessage;
import webSocketMessages.userCommands.MakeMoveMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.ArrayList;

@WebSocket
public class WSServer {

    Gson gson = new Gson();
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;

    //UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO
    public WSServer(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {

        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            UserGameCommand msg = gson.fromJson(message, UserGameCommand.class);

            if (msg.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER) {
                JoinPlayerMessage request = gson.fromJson(message, JoinPlayerMessage.class);
                GameData game = null;
                //Implementing the logic of join_player here.
                //First, verifying the authToken.
                try {
                    AuthData token = this.authDAO.getAuth(request.getAuthString());
                    if (token == null) {
                        throw new dataAccess.DataAccessException("no such authToken exists");
                    }
                } catch (Exception e) {
                    throw new Exception("invalid authToken provided.");
                }
                //now, verifying details about the game.
                ArrayList<GameData> temp = gameDAO.getGameList();
                int i = 0;
                while (i < temp.size()) {
                    if (temp.get(i).gameID() == request.gameID) {
                        game = temp.get(i);
                        break;
                    }
                    i++;
                }
                //verifying data is correct.
                if (game == null) {
                    throw new Exception("no game found");
                }
                if (game.whiteUsername() == null) {
                    if (game.blackUsername() == null) {
                        throw new Exception("no users are in this game.");
                    }
                }

                if (request.playerColor == ChessGame.TeamColor.WHITE) {
                    if (game.whiteUsername() == null) {
                        throw new Exception("user is not in specified color.");
                    }
                }
                if (request.playerColor == ChessGame.TeamColor.BLACK) {
                    if (game.blackUsername() == null) {
                        throw new Exception("user is not in specified color.");
                    }
                }

                LoadGameMessage response = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
                session.getRemote().sendString(gson.toJson(response));

            } else if (msg.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER) {
                //this does much the same work as joinPlayer, but with much less verification.
                //First, verifying the authToken.
                JoinObserverMessage request = gson.fromJson(message, JoinObserverMessage.class);
                GameData game = null;
                try {
                    AuthData token = this.authDAO.getAuth(request.getAuthString());
                    if (token == null) {
                        throw new dataAccess.DataAccessException("no such authToken exists");
                    }
                } catch (Exception e) {
                    throw new Exception("invalid authToken provided.");
                }
                //now, verifying details about the game.
                ArrayList<GameData> temp = gameDAO.getGameList();
                int i = 0;
                while (i < temp.size()) {
                    if (temp.get(i).gameID() == request.gameId) {
                        game = temp.get(i);
                        break;
                    }
                    i++;
                }
                //verifying data is correct.
                if (game == null) {
                    throw new Exception("no game found");
                }
                LoadGameMessage response = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
                session.getRemote().sendString(gson.toJson(response));

            } else if (msg.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {

                MakeMoveMessage request = gson.fromJson(message, MakeMoveMessage.class);
                ChessMove move = request.move;
                ChessGame game = this.gameDAO.getSingleGame(request.gameId);

                try {
                    game.makeMove(move);
                } catch (InvalidMoveException e) {
                    throw new Exception("attempted to move a piece who's color was not in turn order.");
                }
                this.gameDAO.updateSingleGame(request.gameId, game);
                LoadGameMessage response = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
                session.getRemote().sendString(gson.toJson(response));

            } else {
                throw new Exception("You idiot");
            }

        } catch (Exception e) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
            session.getRemote().sendString(gson.toJson(error));
        }
    }
}
