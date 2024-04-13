package webSocketMessages.userCommands;

public class LeaveMessage extends UserGameCommand {

    public Integer gameID;

    public LeaveMessage(String authToken, Integer gameID) {
        super(authToken);
        this.commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    @Override
    public String toString() {
        return "LeaveMessage{" +
                "gameId=" + gameID +
                ", commandType=" + commandType +
                '}';
    }
}
