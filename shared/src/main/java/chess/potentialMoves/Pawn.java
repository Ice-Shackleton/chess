package chess.potentialMoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class Pawn {

    /**
     * This function finds all the valid moves a pawn can make from its current position on the
     * chess board. It will only return diagonal moves if it can capture an enemy piece. And
     * it will only return a move of 2 forward squares if it is at its starting position.
     * @param board The current board state.
     * @param start The piece's current position on the board.
     * @return A Collection of valid {@link ChessMove}s.
     */
    public static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition start) {
        HashSet<ChessMove> pMoves = new HashSet<ChessMove>();

        ChessPiece pawn = board.getPiece(start);
        ChessPosition end;
        switch (pawn.getTeamColor()) {
            case BLACK: {
                // This is checking if a pawn can perform its special move.
                if (start.getRow() == 7) {
                    end = new ChessPosition(start.getRow() - 2, start.getColumn());
                    ChessPosition mid = new ChessPosition(start.getRow()-1, start.getColumn());
                    if (board.getPiece(end) == null
                            && board.getPiece(mid) == null
                            && board.isInBounds(end)) {
                        pMoves.add(new ChessMove(start, end, null));
                    }
                }

                // This checks if the pawn can capture diagonally to the left.
                end = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
                if (board.getPiece(end) != null
                        && board.isInBounds(end)
                        && board.isEnemyPiece(start, end)) {
                    // If the capture ends in a promotion.
                    if (end.getRow() == 1) {
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
                    } else {
                        pMoves.add(new ChessMove(start, end, null));
                    }

                }

                // This checks if the pawn can capture diagonally to the right.
                end = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
                if (board.getPiece(end) != null
                        && board.isInBounds(end)
                        && board.isEnemyPiece(start, end)) {
                    // If the capture ends in a promotion.
                    if (end.getRow() == 1) {
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
                    } else {
                        pMoves.add(new ChessMove(start, end, null));
                    }

                }

                //This checks if the pawn can simply move forward one space.
                end = new ChessPosition(start.getRow() - 1, start.getColumn());
                if (board.getPiece(end) == null && board.isInBounds(end)) {
                    //if the move ends in a promotion.
                    if (end.getRow() == 1) {
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
                    } else {
                        pMoves.add(new ChessMove(start, end, null));
                    }
                }
                break;

            }
            case WHITE: {
                // This is checking if a pawn can perform its special move.
                if (start.getRow() == 2) {
                    end = new ChessPosition(start.getRow() + 2, start.getColumn());
                    ChessPosition mid = new ChessPosition(start.getRow()+1, start.getColumn());

                    if (board.getPiece(end) == null
                            && board.getPiece(mid) == null
                            && board.isInBounds(end)) {
                        pMoves.add(new ChessMove(start, end, null));
                    }
                }

                // This checks if the pawn can capture diagonally to the left.
                end = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
                if (board.getPiece(end) != null
                        && board.isInBounds(end)
                        && board.isEnemyPiece(start, end)) {
                    // If the capture ends in a promotion.
                    if (end.getRow() == 8) {
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
                    } else {
                        pMoves.add(new ChessMove(start, end, null));
                    }

                }

                // This checks if the pawn can capture diagonally to the right.
                end = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
                if (board.getPiece(end) != null
                        && board.isInBounds(end)
                        && board.isEnemyPiece(start, end)) {
                    // If the capture ends in a promotion.
                    if (end.getRow() == 8) {
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
                    } else {
                        pMoves.add(new ChessMove(start, end, null));
                    }

                }

                //This checks if the pawn can simply move forward one space.
                end = new ChessPosition(start.getRow() + 1, start.getColumn());
                if (board.getPiece(end) == null && board.isInBounds(end)) {
                    //if the move ends in a promotion.
                    if (end.getRow() == 8) {
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
                        pMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
                    } else {
                        pMoves.add(new ChessMove(start, end, null));
                    }
                }
                break;

            }
        }
        return pMoves;
    }
}
