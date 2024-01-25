package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMove {

    /**
     * This variable stores all the moves a single knight can make from its current
     * position on the board.
     */
    private static HashSet<ChessMove> nMoves = new HashSet<ChessMove>();

    /**
     * This function finds all the valid moves a knight can make from its current position on
     * the board. A valid move is one that ends on an empty space within the bounds of the
     * {@link ChessBoard}. Every time this method is called, it resets the hash set.
     *
     * @param board The current board state as a {@link ChessBoard} object.
     * @param space The piece's current position on the board as a {@link ChessPosition} object.
     * @return A hash set containing all valid moves a bishop can make at its current position.
     */
    public static Collection<ChessMove> knightMove (ChessBoard board, ChessPosition space) {
        nMoves = new HashSet<ChessMove>();
        ChessPosition start = new ChessPosition(space.getRow(), space.getColumn());
        int[] dRows = { 2, 2, 1,-1,-2,-2, 1,-1};
        int[] dCols = { 1,-1, 2, 2, 1,-1,-2,-2};

        for (int i = 0; i < 8; i++) {
            int nextRow = start.getRow() + dRows[i];
            int nextCol = start.getColumn() + dCols[i];
            ChessPosition end = new ChessPosition(nextRow, nextCol);
            if(board.getPiece(end) == null && moveCalculator.inBounds(nextRow, nextCol)){
                ChessMove x = new ChessMove(start, end, null);
                nMoves.add(x);
            }
            if (moveCalculator.isEnemyPiece(start, board, end)){
                ChessMove x = new ChessMove(start, end, null);
                nMoves.add(x);
            }
        }
        return nMoves;
    }

}
