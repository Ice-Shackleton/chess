package chess.potentialMoves;


import chess.*;

import java.util.Collection;
import java.util.HashSet;

/**
 * This is called by ChessPiece.PieceMoves. It calculates all the possible moves a piece can make
 * from its current position.
 */
public class allTheMoves {

    public static Collection<ChessMove> moveCalculator(ChessBoard board, ChessPosition start) {
        if(!board.isInBounds(start)){
            return new HashSet<ChessMove>();
        }
        HashSet<ChessMove> moveSet = new HashSet<ChessMove>();
        ChessPiece thisPiece = board.getPiece(start);
        ChessPiece.PieceType check = thisPiece.getPieceType();

        switch (check) {
            case BISHOP: {
                moveSet.addAll(Bishop.bishopMoves(board, start));
                break;
            }
            case ROOK: {
                moveSet.addAll(Rook.rookMoves(board, start));
                break;
            }
            case QUEEN: {
                moveSet.addAll(Queen.queenMoves(board, start));
                break;
            }
            case KNIGHT: {
                moveSet.addAll(Knight.knightMoves(board, start));
                break;
            }
            case KING: {
                moveSet.addAll(King.kingMoves(board, start));
                break;
            }
            case PAWN: {
                moveSet.addAll(Pawn.pawnMoves(board, start));
                break;
            }

        }
        if(moveSet.size() > 0){
            return moveSet;
        }
        return new HashSet<>();
    }


}
