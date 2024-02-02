package chess;

import java.util.Arrays;

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
        int trueRow = position.getRow();
        int trueCol = position.getColumn();

        return ((trueRow-1) < 8 && (trueRow-1) >= 0) && ((trueCol-1) < 8 && (trueCol-1) >= 0);
    }

    public boolean isEnemyPiece(ChessPosition start, ChessPosition end){
        ChessPiece mover = this.getPiece(start);
        ChessPiece blocker = this.getPiece(end);

        return !(mover.getTeamColor() == blocker.getTeamColor());
    }

    /**
     * This overrides the usual toString method to print out the chess board.
     * Ideally, it can be used to print out any board state.
     * @return A stringbuilder.toString that will represent the current board state.
     */
    @Override
    public String toString() {
        StringBuilder state = new StringBuilder(200);
        for (int i = 0; i < 8; i++) {
            if (i !=0){ state.append("\n"); }

            for (int j = 0; j < 8; j++) {
                state.append("|");
                ChessPiece input = this.board[i][j];
                state.append("|");
                state.append(pieceChar(input));

                if (j == 7){ state.append("|"); }

            }
        }
        return state.toString();
    }

    /**
     * A simple method that return the string character of the input {@link ChessPiece}.
     * White pieces will be capitalized, and black pieces will be lowercase.
     * @param piece A {@link ChessPiece}.
     * @return A string consisting of a single character.
     */
    private String pieceChar(ChessPiece piece){
        String temp = "";
        if (piece == null) {
            return " ";
        }
        ChessPiece.PieceType check = piece.getPieceType();
        switch (check) {
            case KING:
                temp = "k";
                break;
            case QUEEN:
                temp = "q";
                break;
            case BISHOP:
                temp = "b";
                break;
            case KNIGHT:
                temp = "n";
                break;
            case ROOK:
                temp = "r";
                break;
            case PAWN:
                temp = "p";
                break;
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return temp.toUpperCase();
        }
        return temp;
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
}
