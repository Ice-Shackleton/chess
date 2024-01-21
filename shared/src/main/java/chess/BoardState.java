package chess;

//inside the actual game, declare a private variable that handles the board state.

/**
 * An interface for interacting with board state data.
 */
public interface BoardState {

    /**
     * This processes a move on a board state.
     *
     * @param move This parameter takes in a requested {@link ChessMove} with a
     *             start and end position.
     * @param game This is a board state.
     * @return This returns whether the move is valid.
     */
     boolean makeMove(ChessMove move, ChessGame game);



}
