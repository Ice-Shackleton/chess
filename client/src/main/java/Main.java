import chess.*;
import ui.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: ");
        gameClientInterface newGame = new gameClientInterface("localhost:" + 8080);
        chessClientInterface newClient = new chessClientInterface("localhost:" + 8080);
    }
}