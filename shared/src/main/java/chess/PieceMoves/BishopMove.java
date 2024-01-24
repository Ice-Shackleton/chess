package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMove {

    /**
     * This variable stores all the moves a single bishop can make from its current
     * position on the board.
     */
    private static HashSet<ChessMove> bMoves = new HashSet<ChessMove>();

    /**
     * This function finds all the valid moves a bishop can make from its current position on
     * the board. A valid move is one that ends on an empty space within the bounds of the
     * {@link ChessBoard}. Every time this method is called, it resets the hash set.
     * @param board The current board state as a {@link ChessBoard} object.
     * @param space The piece's current position on the board as a {@link ChessPosition} object.
     * @return A hash set containing all valid moves a bishop can make at its current position.
     */
    public static Collection<ChessMove> bishopMove(ChessBoard board, ChessPosition space) {
        bMoves = new HashSet<ChessMove>();
        int[] dRows = {-1, -1, 1, 1};
        int[] dCols = {1, -1, 1, -1};
        ChessPosition start = new ChessPosition(space.getRow(), space.getColumn());
        for (int i = 0; i < 4; i++) {
            int nextRow = start.getRow() + dRows[i];
            int nextCol = start.getColumn() + dCols[i];
            ChessPosition end = new ChessPosition(nextRow, nextCol);

            while (board.getPiece(end) == null && moveCalculator.inBounds(nextRow, nextCol)) {
                end = new ChessPosition(nextRow, nextCol);
                ChessMove x = new ChessMove(start, end, null);
                bMoves.add(x);
                nextRow += dRows[i];
                nextCol += dCols[i];
            }
        }
        return bMoves;
    }






}
