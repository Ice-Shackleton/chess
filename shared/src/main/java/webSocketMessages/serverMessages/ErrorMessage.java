package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {

    public String errorMessage;

    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                '\'' + errorMessage + '\'' +
                '}';
    }
}
