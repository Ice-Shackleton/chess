package chess;

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
     * This is the default constructor for a chess move, where promotions do not need to be
     * evaluated.
     * @param startPosition   Start square of the move.
     * @param endPosition     End square of the move.
     */
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        this.start = startPosition;
        this.end = endPosition;
    }

    /**
     * Since only pawns can be promoted, and then only on the top/bottom row of the board,
     * this overloaded constructor handles pawn moves that end in such spaces.
     * @param startPosition   Start square of the move.
     * @param endPosition     End square of the move.
     * @param promotionPiece  The desired piece a pawn should be upgraded to.
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
}
