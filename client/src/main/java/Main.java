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

        /*try {
            SocketFacade temp = new SocketFacade("localhost:" + 8080);
            JoinObserverMessage screen = new JoinObserverMessage("1396a76b-20ee-4bc5-ad16-058adad1c35d", 22);
            temp.send(new Gson().toJson(screen));
            ChessMove stuff = new ChessMove(new ChessPosition(3, 2), new ChessPosition(2,2), null);
            //ChessMove stuff = new ChessMove(new ChessPosition(7, 7), new ChessPosition(5,7), null);
            MakeMoveMessage move = new MakeMoveMessage("1396a76b-20ee-4bc5-ad16-058adad1c35d", 22, stuff);
            temp.send(new Gson().toJson(move));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } */



        try {
            chessClientInterface newClient = new chessClientInterface("localhost:" + 8080);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}