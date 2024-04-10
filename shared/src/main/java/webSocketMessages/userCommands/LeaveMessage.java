package webSocketMessages.userCommands;

public class LeaveMessage extends UserGameCommand {

    public Integer gameId;

    public LeaveMessage(String authToken, Integer gameId) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "LeaveMessage{" +
                "gameId=" + gameId +
                ", commandType=" + commandType +
                '}';
    }
}
