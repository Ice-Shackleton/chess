package webSocketMessages.userCommands;

public class JoinObserverMessage extends UserGameCommand{

    public Integer gameId;

    public JoinObserverMessage(String authToken, Integer gameId) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "JoinObserverMessage{" +
                "gameId=" + gameId +
                '}';
    }
}
