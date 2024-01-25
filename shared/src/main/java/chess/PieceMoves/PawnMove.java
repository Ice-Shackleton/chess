package chess.PieceMoves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMove {

    /**
     * This variable stores all the moves a single rook can make from its current
     * position on the board.
     */
    private static HashSet<ChessMove> pMoves = new HashSet<ChessMove>();

    public static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition space,
                                                  ChessGame.TeamColor color) {
        pMoves = new HashSet<ChessMove>();
        ChessPosition start = new ChessPosition(space.getRow(), space.getColumn());

        switch (color) {
            case WHITE: {
                if (start.getRow() == 1) {
                    ChessPosition temp = new ChessPosition(start.getRow() + 1, start.getColumn());
                    ChessPosition leap = new ChessPosition(start.getRow() + 2, start.getColumn());

                    if (board.getPiece(temp) == null && board.getPiece(leap) == null) {
                        ChessMove x = new ChessMove(start, leap, null);
                        pMoves.add(x);
                    }
                }

                ChessPosition diag1 = new ChessPosition(start.getRow() + 1, start.getColumn() + 1);
                if (board.getPiece(diag1) != null && moveCalculator.inBounds(start.getRow() + 1,
                        start.getColumn() + 1) && moveCalculator.isEnemyPiece(start, board, diag1)) {

                    if (diag1.getRow() == 8) {
                        ChessMove z = new ChessMove(start, diag1, ChessPiece.PieceType.QUEEN);
                        pMoves.add(z);
                    } else {
                        ChessMove z = new ChessMove(start, diag1, null);
                        pMoves.add(z);
                    }
                }

                ChessPosition diag2 = new ChessPosition(start.getRow() + 1, start.getColumn() - 1);
                if (board.getPiece(diag2) != null && moveCalculator.inBounds(start.getRow() + 1,
                        start.getColumn() - 1) && moveCalculator.isEnemyPiece(start, board, diag2)) {
                    if (diag2.getRow() == 8) {
                        ChessMove v = new ChessMove(start, diag2, ChessPiece.PieceType.QUEEN);
                        pMoves.add(v);
                    } else {
                        ChessMove v = new ChessMove(start, diag2, null);
                        pMoves.add(v);
                    }

                }

                ChessPosition forward = new ChessPosition(start.getRow() + 1, start.getColumn());
                if (board.getPiece(forward) == null && moveCalculator.inBounds(start.getRow() + 1,
                        start.getColumn())) {
                    if (diag2.getRow() == 8) {
                        ChessMove n = new ChessMove(start, forward, ChessPiece.PieceType.QUEEN);
                        pMoves.add(n);
                    } else {
                        ChessMove n = new ChessMove(start, forward, null);
                        pMoves.add(n);
                    }

                }
                break;
            }

            case BLACK: {
                if (start.getRow() == 7) {
                    ChessPosition temp = new ChessPosition(start.getRow() - 1, start.getColumn());
                    ChessPosition leap = new ChessPosition(start.getRow() - 2, start.getColumn());

                    if (board.getPiece(temp) == null && board.getPiece(leap) == null) {
                        ChessMove x = new ChessMove(start, leap, null);
                        pMoves.add(x);
                    }
                }

                ChessPosition diag1 = new ChessPosition(start.getRow() - 1, start.getColumn() + 1);
                if (board.getPiece(diag1) != null && moveCalculator.inBounds(start.getRow() - 1,
                        start.getColumn() + 1) && moveCalculator.isEnemyPiece(start, board, diag1)) {

                    if (diag1.getRow() == 1) {
                        ChessMove z = new ChessMove(start, diag1, ChessPiece.PieceType.QUEEN);
                        pMoves.add(z);
                    } else {
                        ChessMove z = new ChessMove(start, diag1, null);
                        pMoves.add(z);
                    }

                }

                ChessPosition diag2 = new ChessPosition(start.getRow() - 1, start.getColumn() - 1);
                if (board.getPiece(diag2) != null && moveCalculator.inBounds(start.getRow() - 1,
                        start.getColumn() - 1) && moveCalculator.isEnemyPiece(start, board, diag2)) {
                    if (diag2.getRow() == 1) {
                        ChessMove v = new ChessMove(start, diag2, ChessPiece.PieceType.QUEEN);
                        pMoves.add(v);
                    } else {
                        ChessMove v = new ChessMove(start, diag2, null);
                        pMoves.add(v);
                    }
                }

                ChessPosition forward = new ChessPosition(start.getRow() - 1, start.getColumn());
                if (board.getPiece(forward) == null && moveCalculator.inBounds(start.getRow() - 1,
                        start.getColumn())) {
                    if (diag2.getRow() == 8) {
                        ChessMove n = new ChessMove(start, forward, ChessPiece.PieceType.QUEEN);
                        pMoves.add(n);
                    } else {
                        ChessMove n = new ChessMove(start, forward, null);
                        pMoves.add(n);
                    }
                }
                break;
            }
        }
        return pMoves;

    }
}

