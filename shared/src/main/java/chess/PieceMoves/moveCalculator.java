package chess.PieceMoves;

import chess.*;

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
                    break;
                }
                case KING: {
                    moves.addAll(KingMove.kingMove(board, myPosition));
                    break;
                }
                case KNIGHT: {
                    moves.addAll(KnightMove.knightMove(board, myPosition));
                    break;
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


    public static boolean isEnemyPiece (ChessPosition thisPosition, ChessBoard board,
                                        ChessPosition otherPosition) {
        ChessPiece thisPiece = board.getPiece(thisPosition);
        ChessPiece otherPiece = board.getPiece(otherPosition);
        if (otherPiece == null){
            return false;
        }
        ChessGame.TeamColor thisTeam = thisPiece.getTeamColor();
        ChessGame.TeamColor otherTeam = otherPiece.getTeamColor();

        if ((thisTeam != otherTeam)){
            return true;
        }
        return false;
    }


}
