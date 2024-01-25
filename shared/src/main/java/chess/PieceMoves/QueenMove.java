package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMove {

    /**
     * This variable stores all the moves a single queen can make from its current
     * position on the board.
     */
    private static HashSet<ChessMove> qMoves = new HashSet<ChessMove>();

    /**
     * This method compiles all the moves that a queen can make, which are equal to a bishop's moves
     * plus a rook's moves from the same board position.
     * @param board The current board state as a {@link ChessBoard} object.
     * @param space The piece's current position on the board as a {@link ChessPosition} object.
     * @return A hash set containing all valid moves a queen can make at its current position.
     */
    public static Collection<ChessMove> queenMove(ChessBoard board, ChessPosition space) {
        qMoves = new HashSet<ChessMove>();
        qMoves.addAll(BishopMove.bishopMove(board, space));
        qMoves.addAll(RookMove.rookMove(board, space));
        return qMoves;
    }


}
