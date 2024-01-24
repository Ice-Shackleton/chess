package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;


/**
 * This class will take an input of a {@link chess.ChessBoard} and a {@link chess.ChessPosition}
 * and return a collection of all the possible moves the piece at said position can make.
 */
public class moveCalculator {
    public static Collection<ChessMove> allTheMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        HashSet<ChessMove> moves = new HashSet<ChessMove>();

        ChessPiece thisPiece = board.getPiece(myPosition);
        if(thisPiece.isPiece(thisPiece) && inBounds(row, col)){
            ChessPiece.PieceType test = thisPiece.getPieceType();
            switch (test){
                case BISHOP: {
                    moves.addAll(BishopMove.bishopMove(board, myPosition));
                }
            }
        }
        return moves;
    }


    /**
     * A simple method for ensuring that all the various move classes stay within bounds.
     * @param row
     * @param col
     * @return
     */
    public static boolean inBounds(int row, int col){
        return row >= 1 && row < 9 && col >= 1 && col < 9;
    }



}
