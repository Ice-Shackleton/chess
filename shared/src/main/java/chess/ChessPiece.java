package chess;

import java.util.Collection;
import java.util.Objects;
import chess.PieceMoves.*;


/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor color;

    private ChessPiece.PieceType type;
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
        return moveCalculator.allTheMoves(board, myPosition);
    }

    /**
     * This is a simple method to check that a passed-in piece is not an empty space.
     * @param piece A {@link ChessPiece} or square from the board.
     * @return True if {@param piece} is a piece, false if empty.
     */
    public boolean isPiece(ChessPiece piece){
        return piece != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return this.color == that.color && this.type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.color, this.type);
    }
}
