package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {

    public String notification;

    public NotificationMessage(ServerMessageType type, String notification) {
        super(type);
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                '\'' + notification + '\'' +
                '}';
    }
    
}
