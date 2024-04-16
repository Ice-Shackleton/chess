import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: ");

        var newBoard = new ChessBoard();
        newBoard.resetBoard();
        System.out.println(newBoard.toString());
        System.out.println("\n" + newBoard.toString());
    }
}
