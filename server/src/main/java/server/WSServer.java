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
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

@WebSocket
public class WSServer {

    Gson gson = new Gson();
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;
    ConnectionManager manager;
    HashSet<Integer> destroyedGames;
    //UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO
    public WSServer(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.manager = new ConnectionManager();
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.destroyedGames = new HashSet<>();
    }

    public void clearAll() {
        this.manager.connections.clear();
        this.manager.openGames.clear();
        this.destroyedGames.clear();
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
                try {
                    game = this.gameDAO.getSingleGame(request.gameID);
                } catch (Exception e) {
                    throw new Exception("provided game does not exist.");
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
                    if (!game.whiteUsername().equals(token.username())) {
                            throw new Exception("user attempting to join white, which is already occupied.");
                    }

                }
                if (request.playerColor == ChessGame.TeamColor.BLACK) {
                    if (game.blackUsername() == null) {
                        throw new Exception("user is not in specified color.");
                    }
                    if (!game.blackUsername().equals(token.username())) {
                        throw new Exception("user attempting to join black, which is already occupied.");
                    }
                }

                //System.out.println("user attempting to join is this: " + request.getAuthString());
                //System.out.println("attempting to join game '" + game.gameName() + "'");

                LoadGameMessage response = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
                session.getRemote().sendString(gson.toJson(response));
                this.manager.add(token.authToken(), game.gameID(), session);
                String note = "a new player has joined " + game.gameName() + ", as the color "
                        + request.playerColor.toString() + ".";
                sendNotes(note, session, game.gameID());

            } else if (msg.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER) {
                //this does much the same work as joinPlayer, but with much less verification.
                //First, verifying the authToken.
                JoinObserverMessage request = gson.fromJson(message, JoinObserverMessage.class);
                GameData game = this.gameDAO.getSingleGame(request.gameID);
                AuthData token = null;
                try {
                    token = this.authDAO.getAuth(request.getAuthString());
                    if (token == null) {
                        throw new dataAccess.DataAccessException("no such authToken exists");
                    }
                } catch (Exception e) {
                    throw new Exception("invalid authToken provided.");
                }
                //verifying data is correct.
                if (game == null) {
                    throw new Exception("no game found");
                }
                LoadGameMessage response = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
                session.getRemote().sendString(gson.toJson(response));
                this.manager.add(token.authToken(), game.gameID(), session);
                String note = "a new player has joined " + game.gameName() + " as an observer.";
                sendNotes(note, session, game.gameID());

            } else if (msg.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {

                MakeMoveMessage request = gson.fromJson(message, MakeMoveMessage.class);
                if (this.destroyedGames.contains(request.gameID)) {
                    throw new Exception("attempted to make a move on a game where no more moves can be made, " +
                            "due to resignation or stalemate.");
                }

                //make sure user matches team of move
                ChessMove move = request.move;
                GameData data = this.gameDAO.getSingleGame(request.gameID);
                ChessGame game = data.game();
                AuthData activeUser = this.authDAO.getAuth(request.getAuthString());
                ChessPiece activePiece = game.getBoard().getPiece(move.getStartPosition());
                if (game.getTeamTurn() != activePiece.getTeamColor()) {
                    throw new Exception("attempted to move a piece out of turn order.");
                }
                if(game.getTeamTurn() == ChessGame.TeamColor.WHITE) {
                    if (!data.whiteUsername().equals(activeUser.username())) {
                        throw new Exception("a user who is not the active team is attempting to make a move."); 
                    }
                } else {
                    if (!data.blackUsername().equals(activeUser.username())) {
                        throw new Exception("a user who is not the active team is attempting to make a move.");
                    }
                }

                try {
                    game.makeMove(move);
                } catch (InvalidMoveException e) {
                    throw new Exception("invalid move.");
                }
                this.gameDAO.updateSingleGame(request.gameID, game);
                LoadGameMessage response = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
                session.getRemote().sendString(gson.toJson(response));
                sendLoads(gson.toJson(response), session, request.gameID);
                String note = "A player has moved " + assignChessNotation(move.getStartPosition())
                        + " to " + assignChessNotation(move.getEndPosition()) + ".";
                if (game.isInCheck(game.getTeamTurn()) && !game.isInCheckmate(game.getTeamTurn())) {
                    note += "\n team " + game.getTeamTurn() + " is in check.";
                } else if (game.isInCheckmate(game.getTeamTurn())) {
                    note += "\nteam " + game.getTeamTurn()
                            + " is in checkmate! no more moves can be made in game " + game.getGameName() + ".";
                    System.out.println(note);
                    this.manager.destroyGame(request.getAuthString());
                    this.destroyedGames.add(request.gameID);
                }  else if (game.isInStalemate(game.getTeamTurn())) {
                    note += "\nteam " + game.getTeamTurn()
                            + " is in stalemate! no more moves can be made in game " + game.getGameName() + ".";
                    System.out.println(note);
                    this.manager.destroyGame(request.getAuthString());
                    this.destroyedGames.add(request.gameID);
                }

                sendNotes(note, session, request.gameID);


            } else if (msg.getCommandType() == UserGameCommand.CommandType.LEAVE) {
                LeaveMessage request = gson.fromJson(message, LeaveMessage.class);
                GameData game = null;
                ArrayList<GameData> temp = gameDAO.getGameList();
                int i = 0;
                while (i < temp.size()) {
                    if (temp.get(i).gameID() == request.gameID) {
                        game = temp.get(i);
                        break;
                    }
                    i++;
                }
                String note = "A user has left game '" + game.gameName() + "'.";
                NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        note);
                session.getRemote().sendString(gson.toJson(notification));

                sendNotes(note, session, game.gameID());
                this.manager.remove(request.getAuthString());

            } else if (msg.getCommandType() == UserGameCommand.CommandType.RESIGN) {
                ResignMessage request = gson.fromJson(message, ResignMessage.class);
                GameData game = this.gameDAO.getSingleGame(request.gameID);
                AuthData activeUser = this.authDAO.getAuth(request.getAuthString());
                if (game == null) {
                    throw new Exception("attempted to resign from a game that does not exist");
                }

                if(this.destroyedGames.contains(request.gameID)) {
                    throw new Exception("cannot resign from a game where one or more players have already resigned.");
                }

                if ( (!activeUser.username().equals(game.whiteUsername()))) {
                    if (!activeUser.username().equals(game.blackUsername())) {
                        throw new Exception("cannot attempt to resign when not one of the players.");
                    }
                }
                this.manager.destroyGame(request.getAuthString());
                this.destroyedGames.add(game.gameID());
                String note = "a user has resigned from game '" + game.gameName() + "'.";
                NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        note);
                session.getRemote().sendString(gson.toJson(notification));
                sendNotes(note, session, game.gameID());

            } else {
                throw new Exception("provided an invalid game.");
            }

        } catch (Exception e) {
            System.out.println("an error occurred, with the following message: '" + e.getMessage() + "'.");
            ErrorMessage error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
            session.getRemote().sendString(gson.toJson(error));
        }
    }

    private void sendNotes(String note, Session session, Integer gameID) throws IOException {
        //System.out.println("Attempting to send message to players of game '" + gameID + "'");
        //System.out.println("notification: '" + note + "'");
        NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                note);
        for (Session individual : this.manager.getAllInGame(gameID)) {
            if (individual != session) {
                //System.out.println("\tSending a Notification to Session '" + individual.toString() + "'.");
                individual.getRemote().sendString(gson.toJson(notification));
            }
        }
    }

    private void sendLoads(String note, Session session, Integer gameID) throws IOException {
        //System.out.println("Attempting to send a loadGame message to game '" + gameID + "'");
        HashSet<Session> temp = this.manager.getAllInGame(gameID);
        for (Session individual : temp) {
            if (individual != session) {
                //System.out.println("\tSending a loadGame to Session '" + individual.toString() + "'.");
                individual.getRemote().sendString(note);
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
