package webSocketMessages.userCommands;

public class JoinObserverMessage extends UserGameCommand{

    public Integer gameID;

    public JoinObserverMessage(String authToken, Integer gameID) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return "JoinObserverMessage{" +
                "authToken=" + this.getAuthString() +
                ", gameId=" + gameID +
                ", commandType=" + commandType +
                '}';
    }
}
