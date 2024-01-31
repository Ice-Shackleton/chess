package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTurn;
    private ChessBoard boardState;

    public ChessGame() {
        this.currentTurn = TeamColor.WHITE;
        this.boardState = new ChessBoard();
        this.boardState.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece temp = this.boardState.getPiece(startPosition);
        return temp.pieceMoves(this.boardState, startPosition);
    }

    /**
     * Makes a move in a chess game. Unlike hypothetical moves, this one is final and interacts
     * with the current boardstate.
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> temp = this.validMoves(move.getStartPosition());
        ChessPiece example = this.boardState.getPiece(move.getStartPosition());
        ChessGame.TeamColor tempColor = example.getTeamColor();
        if (temp.contains(move) && (tempColor == this.currentTurn)) {
            this.boardState.removePiece(move.getStartPosition());
            this.boardState.removePiece(move.getEndPosition());
            this.boardState.addPiece(move.getEndPosition(), example);
        }
    }

    /**
     * Determines if the given team is in check.
     * This function will loop through each space on the {@link ChessBoard} and collate a HashSet
     * of all possible valid moves all enemy pieces could make. It will make a deep copy of the board
     * after the hypothetical move and, if the King's position after the move is in the collated
     * HashSet, it will return true.
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.boardState = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.boardState;
    }

    /**
     * This function returns a deep copy chess board after a potential {@link ChessMove},
     * for the purposes of checking if the move leaves a piece in check.
     *
     * @param move A potential move
     * @return
     */
    public ChessBoard hypotheticalMove(ChessMove move){
        ChessBoard that = new ChessBoard();
    }
}
