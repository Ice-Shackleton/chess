import chess.*;
import com.google.gson.Gson;
import serverFacade.SocketFacade;
import ui.*;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.userCommands.JoinObserverMessage;
import webSocketMessages.userCommands.JoinPlayerMessage;
import webSocketMessages.userCommands.MakeMoveMessage;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: ");

        try {
            ChessClientInterface newClient = new ChessClientInterface("localhost:" + 8080);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}