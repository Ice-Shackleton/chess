package chess;

import chess.potentialMoves.allTheMoves;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;

    /**
     * This creates a {@link ChessPiece} object.
     * @param pieceColor A piece's team color.
     * @param type The type of piece.
     */
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }



    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return allTheMoves.moveCalculator(board, myPosition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return this.color == that.color && this.type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public String toString() {
        return switch (this.type) {
            case PieceType.PAWN -> "♟";
            case PieceType.BISHOP -> "♝";
            case PieceType.KNIGHT -> "♞";
            case PieceType.QUEEN -> "♛";
            case PieceType.ROOK -> "♜";
            case PieceType.KING -> "♚";
            case null, default -> "";
        };
    }
}
