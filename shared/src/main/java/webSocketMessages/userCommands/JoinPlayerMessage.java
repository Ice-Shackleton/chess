package webSocketMessages.userCommands;

import chess.ChessGame;
import org.eclipse.jetty.server.Authentication;

public class JoinPlayerMessage extends UserGameCommand {

    public Integer gameID;
    public ChessGame.TeamColor playerColor;

    public JoinPlayerMessage(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    @Override
    public String toString() {
        return "JoinPlayerMessage{" +
                "gameID=" + gameID +
                ", playerColor=" + playerColor +
                ", commandType=" + commandType +
                '}';
    }
}
