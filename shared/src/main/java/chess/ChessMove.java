package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition start;
    private ChessPosition end;
    private ChessPiece.PieceType promotion;

    /**
     * This constructs a {@link ChessMove} object.
     * @param startPosition A piece's current position on the {@link ChessBoard}.
     * @param endPosition A valid end position for the piece on the {@link ChessBoard}.
     * @param promotionPiece This applies only to pawns moving onto either board edge. If
     *                       a promotion is possible it will specify which {@link ChessPiece}
     *                       the pawn will be promoted to. Otherwise, it will be null.
     */
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.start = startPosition;
        this.end = endPosition;
        this.promotion = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotion;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.deepEquals(this.start, chessMove.start)
                && Objects.equals(this.end, chessMove.end)
                && this.promotion == chessMove.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.start, this.end, this.promotion);
    }
}
