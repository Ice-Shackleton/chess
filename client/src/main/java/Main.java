import chess.*;
import com.google.gson.Gson;
import serverFacade.SocketFacade;
import ui.*;
import webSocketMessages.userCommands.JoinObserverMessage;
import webSocketMessages.userCommands.JoinPlayerMessage;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: ");

        try {
            SocketFacade temp = new SocketFacade("localhost:" + 8080);
            JoinObserverMessage join = new JoinObserverMessage("21149671-39ed-43d3-9a01-4b44723387d5", 1);
            temp.send(new Gson().toJson(join));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        try {
            chessClientInterface newClient = new chessClientInterface("localhost:" + 8080);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}