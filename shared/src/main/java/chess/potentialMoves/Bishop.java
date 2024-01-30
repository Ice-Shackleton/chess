package chess.potentialMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Bishop {

    /**
     * This function finds all the valid moves a bishop can make from its current position on
     * the board. A valid move is one that ends on an empty space within the bounds of the
     * {@link ChessBoard}. Every time this method is called, it resets the hash set.
     *
     * @param board The current board state as a {@link ChessBoard} object.
     * @param start The piece's current position on the board as a {@link ChessPosition} object.
     * @return A hash set containing all valid moves a bishop can make at its current position.
     */
    public static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition start){
        int[] bRows = {-1, 1, -1, 1};
        int[] bCol =  {-1,-1,  1, 1};
        HashSet<ChessMove> bMoves = new HashSet<ChessMove>();

        for (int i=0; i < 4; i++){
            ChessPosition end = new ChessPosition(start.getRow() + bRows[i],
                                                   start.getColumn() + bCol[i]);
            while (board.getPiece(end) == null && board.isInBounds(end)){
                bMoves.add(new ChessMove(start, end, null));
                end = new ChessPosition(end.getRow()+bRows[i], end.getColumn()+bCol[i]);
            }

            if (board.getPiece(end) != null && board.isEnemyPiece(start, end)){
                bMoves.add(new ChessMove(start, end, null));
            }
        }
        return bMoves;
    }

}
