package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMove {

    private static HashSet<ChessMove> nMoves = new HashSet<ChessMove>();

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
