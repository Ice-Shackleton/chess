package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveMessage extends UserGameCommand {

    public Integer gameId;
    public ChessMove move;

    public MakeMoveMessage(String authToken, Integer gameId, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameId = gameId;
        this.move = move;
    }

    @Override
    public String toString() {
        return "MakeMoveMessage{" +
                "gameId=" + gameId +
                ", commandType=" + commandType +
                '}';
    }
}
