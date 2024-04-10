package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.SocketMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerMessage;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WSServer {

    private static final Gson gson = new Gson();
    public WSServer() {
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand msg = gson.fromJson(message, UserGameCommand.class);
        try {
            if (msg.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER) {
                JoinPlayerMessage request = gson.fromJson(message, JoinPlayerMessage.class);
                //this is where we do stuff
                LoadGameMessage result = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, null);
                session.getRemote().sendString(gson.toJson(result));
            } else {
                throw new Exception("You fucking idiot");
            }
        } catch (Exception e) {
            SocketMessage error = new SocketMessage(e.getClass(), gson.toJson(e));
            session.getRemote().sendString(gson.toJson(error));
        }
        session.getRemote().sendString(message);

    }
}
