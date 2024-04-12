package server;

import chess.*;
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
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverMessage;
import webSocketMessages.userCommands.JoinPlayerMessage;
import webSocketMessages.userCommands.MakeMoveMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebSocket
public class WSServer {

    Gson gson = new Gson();
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;
    ConnectionManager manager;

    //UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO
    public WSServer(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.manager = new ConnectionManager();
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
                AuthData token = null;
                try {
                    token = this.authDAO.getAuth(request.getAuthString());
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
                this.manager.add(token.authToken(), game.gameID(), session);
                String note = "a new player has joined " + game.gameName() + ", as the color "
                        + request.playerColor.toString() + ".";
                sendNotes(note, session, game);

            } else if (msg.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER) {
                //this does much the same work as joinPlayer, but with much less verification.
                //First, verifying the authToken.
                JoinObserverMessage request = gson.fromJson(message, JoinObserverMessage.class);
                GameData game = null;
                AuthData token = null;
                try {
                    token = this.authDAO.getAuth(request.getAuthString());
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
                this.manager.add(token.authToken(), game.gameID(), session);
                String note = "a new player has joined " + game.gameName() + " as an observer.";
                sendNotes(note, session, game);

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
                String note = "A player has moved " + assignChessNotation(move.getStartPosition())
                        + " to " + assignChessNotation(move.getEndPosition()) + ".";


            } else {
                throw new Exception("You idiot");
            }

        } catch (Exception e) {
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
            session.getRemote().sendString(gson.toJson(error));
        }
    }

    private void sendNotes(String note, Session session, GameData game) throws IOException {
        NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                note);
        for (Session individual : this.manager.getAllInGame(game.gameID())) {
            if (individual != session) {
                individual.getRemote().sendString(gson.toJson(notification));
            }
        }
    }

    private String assignChessNotation(ChessPosition position) {
        int row = position.getRow();
        String temp = "";
        if (row == 1) {
            temp += "a";
        } else if (row == 2){
            temp += "b";
        } else if (row == 3) {
            temp += "c";
        } else if (row == 4) {
            temp += "d";
        } else if (row == 5) {
            temp += "e";
        } else if (row == 6) {
            temp += "f";
        } else if (row == 7) {
            temp += "g";
        } else if (row == 8) {
            temp += "h";
        } else {
            return null;
        }
        temp += position.getColumn();
        return temp;
    }

}
