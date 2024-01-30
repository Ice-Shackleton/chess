package chess.potentialMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Queen {

    /**
     * This method compiles all the moves that a queen can make, which are equal to a bishop's moves
     * plus a rook's moves from the same board position.
     * @param board The current board state as a {@link ChessBoard} object.
     * @param start The piece's current position on the board as a {@link ChessPosition} object.
     * @return A hash set containing all valid moves a queen can make at its current position.
     */
    public static Collection<ChessMove> queenMoves (ChessBoard board, ChessPosition start){
        HashSet<ChessMove> qMoves = new HashSet<ChessMove>();
        qMoves.addAll(Bishop.bishopMoves(board, start));
        qMoves.addAll(Rook.rookMoves(board, start));

        return qMoves;
    }
}
