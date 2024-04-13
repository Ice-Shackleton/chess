package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveMessage extends UserGameCommand {

    public Integer gameID;
    public ChessMove move;

    public MakeMoveMessage(String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }

    @Override
    public String toString() {
        return "MakeMoveMessage{" +
                "gameId=" + gameID +
                ", commandType=" + commandType +
                '}';
    }
}
