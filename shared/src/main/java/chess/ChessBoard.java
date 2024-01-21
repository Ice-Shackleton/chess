package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public abstract class ChessBoard implements BoardState {

    /**
     * This variable is the 8x8 chess board, and stores the location of pieces.
     */
    private ChessPiece[][] boardArray;

    public ChessBoard(ChessPiece[][] boardArray) {
        this.boardArray = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }


    /**
     * This processes a move on a board state.
     *
     * @param move This parameter takes in a requested {@link ChessMove} with a
     *             start and end position.
     * @param game The {@link ChessGame} that defers to this {@link BoardState}.
     * @return This returns whether the move is valid.
     */
    @Override
    public final boolean makeMove(ChessMove move, ChessGame game) {
        if (!validMove(move)){
            return false;
        }
        doMove(move);
        changeState(move, game);
        return true;
    }


    /**
     * This handles a state transition.
     * @param move This parameter takes in a requested {@link ChessMove} with a
     *             start and end position.
     * @param game This is a board state.
     */
    protected abstract void changeState(ChessMove move, ChessGame game);

    /**
     * This handles a piece move. It is just a data transaction.
     *
     * @param move This parameter takes in a requested {@link ChessMove} with a
     *             start and end position.
     */
    private void doMove(ChessMove move) {

    }

    /**
     * This returns whether the {@link ChessMove} move is a valid move
     * based on the current chess state
     * @param move This parameter takes in a requested {@link ChessMove} with a
     *             start and end position.
     * @return This returns whether the move is valid, dependent on the board state.
     */
    protected abstract boolean validMove(ChessMove move);


}
