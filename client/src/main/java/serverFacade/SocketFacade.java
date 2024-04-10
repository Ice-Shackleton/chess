package serverFacade;

import com.google.gson.Gson;
import webSocketMessages.SocketMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class SocketFacade extends Endpoint {

    public Session session;
    private static final Gson gson = new Gson();

    public SocketFacade(String url) throws Exception {
        URI uri = new URI("ws://" + url + "/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                //when message is received, deserialize the object to get the type; use switch statement
                //to deserialize for a second time and run command
                System.out.println("Package received from server was as follows:");
                ServerMessage response = gson.fromJson(message, ServerMessage.class);
                if (response.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                    LoadGameMessage effect = gson.fromJson(message, LoadGameMessage.class);
                    System.out.println(effect);
                } else {
                    System.out.println("Something has gone SERIOUSLY wrong, dude.");
                }
            }
        });
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}