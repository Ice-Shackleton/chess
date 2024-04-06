package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    public ChessGame game;

    public LoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    @Override
    public String toString() {
        return "LoadGameMessage{" +
                "game=" + game.getGameName() +
                ", serverMessageType=" + serverMessageType +
                '}';
    }
}
