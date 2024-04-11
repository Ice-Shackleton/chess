import chess.*;
import com.google.gson.Gson;
import serverFacade.SocketFacade;
import ui.*;
import webSocketMessages.userCommands.JoinPlayerMessage;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: ");
        try {
            SocketFacade temp = new SocketFacade("localhost:" + 8080);
            JoinPlayerMessage join = new JoinPlayerMessage("null", 1, ChessGame.TeamColor.WHITE);
            temp.send(new Gson().toJson(join));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        chessClientInterface newClient = new chessClientInterface("localhost:" + 8080);
    }
}