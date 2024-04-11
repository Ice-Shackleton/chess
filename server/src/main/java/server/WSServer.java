package server;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

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
                ChessGame game = new ChessGame();
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
