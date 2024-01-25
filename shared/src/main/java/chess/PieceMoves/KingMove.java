package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;
public class KingMove {

    private static HashSet<ChessMove> kMoves = new HashSet<ChessMove>();

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
