package serverFacade;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class SocketFacade extends Endpoint {

    public Session session;

    public SocketFacade(String url) throws Exception {
        URI uri = new URI("ws://" + url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
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