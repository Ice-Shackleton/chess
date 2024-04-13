package webSocketMessages.userCommands;

public class ResignMessage extends UserGameCommand {

    public Integer gameID;

    public ResignMessage(String authToken, Integer gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }

    @Override
    public String toString() {
        return "ResignMessage{" +
                "gameId=" + gameID +
                ", commandType=" + commandType +
                '}';
    }

}
