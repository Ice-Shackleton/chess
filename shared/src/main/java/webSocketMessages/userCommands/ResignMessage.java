package webSocketMessages.userCommands;

public class ResignMessage extends UserGameCommand {

    public Integer gameId;

    public ResignMessage(String authToken, Integer gameId) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "ResignMessage{" +
                "gameId=" + gameId +
                ", commandType=" + commandType +
                '}';
    }

}
