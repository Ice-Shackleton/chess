package chess;

import java.util.Arrays;
import static chess.EscapeSequences.*;


/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (isInBounds(position) && piece != null){
            this.board[position.getRow()-1][position.getColumn()-1] = piece;
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if(isInBounds(position)){
            return this.board[position.getRow()-1][position.getColumn()-1];
        }
        return null;

    }

    /**
     * replaces a piece at a certain position with null.
     * @param position The position to remove the piece from
     */
    public void removePiece(ChessPosition position){
        if (isInBounds(position)){
            this.board[position.getRow()-1][position.getColumn()-1] = null;
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.board = new ChessPiece[8][8];

        for (int i=0; i < 8; i++){
            this.board[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            this.board[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        this.board[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        this.board[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        this.board[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        this.board[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        this.board[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.board[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.board[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        this.board[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);

        this.board[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.board[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.board[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        this.board[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);

        this.board[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        this.board[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);

        this.board[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        this.board[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
    }

    public boolean isInBounds(ChessPosition position){
        if (position == null){
            return false;
        }
        int trueRow = position.getRow();
        int trueCol = position.getColumn();

        return ((trueRow-1) < 8 && (trueRow-1) >= 0) && ((trueCol-1) < 8 && (trueCol-1) >= 0);
    }

    public boolean isEnemyPiece(ChessPosition start, ChessPosition end){
        if (start == null || end == null){
            return false;
        }
        ChessPiece mover = this.getPiece(start);
        ChessPiece blocker = this.getPiece(end);
        if (mover == null || blocker == null) {
            return false;
        }
        return !(mover.getTeamColor() == blocker.getTeamColor());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public ChessBoard deepCopy(){
        ChessBoard temp = new ChessBoard();
        for (int r=0; r<8; r++){
            for (int c=0; c<8; c++){
                temp.board[r][c] = this.board[r][c];
            }
        }
       return temp;
    }

    /**
     * This overrides the usual toString method to print out the chess board. It prints with the White side
     * on the top, and the Black pieces on the bottom.
     * Ideally, it can be used to print out any board state.
     * @return A stringbuilder.toString that will represent the current board state.
     */
    @Override
    public String toString() {
        StringBuilder state = new StringBuilder(400);
        boolean isWhite = true;
        state.append(SET_TEXT_COLOR_WHITE + " A   B   C  D  E   F   G   H\n");
        for (int i = 7; i > -1; i--) {
            if (i != 7){ state.append("\n"); }
            for (int j = 7; j >-1; j--) {

                if ((i+j) % 2 != 0){
                    isWhite = false;
                    state.append(SET_BG_COLOR_WHITE);
                } else {
                    isWhite = true;
                    state.append(SET_BG_COLOR_DARK_GREY);
                }

                ChessPiece input = this.board[i][j];

                if (input != null) {
                    state.append(" ");
                    if (input.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        state.append(SET_TEXT_COLOR_MAGENTA);
                        state.append(input);
                    } else {
                        state.append(SET_TEXT_COLOR_GREEN);
                        state.append(input);
                    }
                    state.append(" ");
                } else {
                    state.append(EMPTY);
                }

            }

            state.append(SET_TEXT_COLOR_WHITE);
            state.append(RESET_BG_COLOR);
            state.append("  ");
            state.append(i+1);

        }
        return state.toString();
    }
}
