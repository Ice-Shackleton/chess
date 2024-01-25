package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;
public class RookMove {


    /**
     * This variable stores all the moves a single rook can make from its current
     * position on the board.
     */
    private static HashSet<ChessMove> rMoves = new HashSet<ChessMove>();

    /**
     * This function finds all the valid moves a rook can make from its current position on
     * the board. A valid move is one that ends on an empty space within the bounds of the
     * {@link ChessBoard}. Every time this method is called, it resets the hash set.
     *
     * @param board The current board state as a {@link ChessBoard} object.
     * @param space The piece's current position on the board as a {@link ChessPosition} object.
     * @return A hash set containing all valid moves a bishop can make at its current position.
     */
    public static Collection<ChessMove> rookMove(ChessBoard board, ChessPosition space) {
        rMoves = new HashSet<ChessMove>();
        int[] dRows = {-1, 1, 0,  0};
        int[] dCols = { 0, 0, 1, -1};
        ChessPosition start = new ChessPosition(space.getRow(), space.getColumn());
        for (int i = 0; i < 4; i++) {
            int nextRow = start.getRow() + dRows[i];
            int nextCol = start.getColumn() + dCols[i];
            ChessPosition end = new ChessPosition(nextRow, nextCol);

            while (board.getPiece(end) == null && moveCalculator.inBounds(nextRow, nextCol)) {
                ChessMove x = new ChessMove(start, end, null);
                rMoves.add(x);
                nextRow += dRows[i];
                nextCol += dCols[i];
                end = new ChessPosition(nextRow, nextCol);
            }
            if (moveCalculator.isEnemyPiece(start, board, end)){
                ChessMove x = new ChessMove(start, end, null);
                rMoves.add(x);
            }
        }
        return rMoves;
    }

}
