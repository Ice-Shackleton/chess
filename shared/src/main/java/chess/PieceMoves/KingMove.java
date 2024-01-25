package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;
public class KingMove {

    /**
     * This variable stores all the moves a single King can make from its current
     * position on the board.
     */
    private static HashSet<ChessMove> kMoves = new HashSet<ChessMove>();

    /**
     * This function finds all the valid moves the king can make from its current position on
     * the board. A valid move is one that ends on an empty space within the bounds of the
     * {@link ChessBoard}. Every time this method is called, it resets the hash set.
     *
     * @param board The current board state as a {@link ChessBoard} object.
     * @param space The piece's current position on the board as a {@link ChessPosition} object.
     * @return A hash set containing all valid moves a king can make at its current position.
     */
    public static Collection<ChessMove> kingMove (ChessBoard board, ChessPosition space) {
        kMoves = new HashSet<ChessMove>();
        ChessPosition start = new ChessPosition(space.getRow(), space.getColumn());
        int[] dRows = {-1,-1, -1, 0, 1, 1,  1, 0};
        int[] dCols = { 1, 0, -1,-1,-1, 0,  1, 1};

        for (int i = 0; i < 8; i++) {
            int nextRow = start.getRow() + dRows[i];
            int nextCol = start.getColumn() + dCols[i];
            ChessPosition end = new ChessPosition(nextRow, nextCol);
            if(board.getPiece(end) == null && moveCalculator.inBounds(nextRow, nextCol)){
                ChessMove x = new ChessMove(start, end, null);
                kMoves.add(x);
            }
            if (moveCalculator.isEnemyPiece(start, board, end)){
                ChessMove x = new ChessMove(start, end, null);
                kMoves.add(x);
            }
        }
        return kMoves;
    }

}
