package serverFacade;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;
import static ui.EscapeSequences.*;

public class SocketFacade extends Endpoint {

    Gson gson = new Gson();
    public Session session;

    public SocketFacade(String url) throws Exception {
        URI uri = new URI("ws://" + url + "/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                evalPrint(message);
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    /**
     * This method evaluates a websocket message and prints out its contents, based on the {@link webSocketMessages.serverMessages.ServerMessage}
     * command type of the message.
     * @param message a Json string.
     */
    public void evalPrint(String message) {
        try {
            ServerMessage response = gson.fromJson(message, ServerMessage.class);
            if (response.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                LoadGameMessage effect = gson.fromJson(message, LoadGameMessage.class);
                System.out.println("\n" + effect.game.getBoard().toString());
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "[GAME] >>> ");
            } else {
                ErrorMessage error = gson.fromJson(message, ErrorMessage.class);
                throw new Exception(error.errorMessage);
            }
        } catch (Exception e) {
            System.out.println("\n" + SET_TEXT_COLOR_BLUE + e.getMessage());
        }
    }

}