package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTurn;
    private ChessBoard boardState;

    /**
     * These variables are to make finding the king's position on the chess board easy.
     */
    private ChessPosition blackKing;
    private ChessPosition whiteKing;

    /**
     * These variables will allow easy tracking of whether castling is a valid move.
     */
    private boolean blackCastle;
    private boolean whiteCastle;

    public ChessGame() {
        this.currentTurn = TeamColor.WHITE;
        this.boardState = new ChessBoard();
        this.boardState.resetBoard();
        this.whiteKing = new ChessPosition(1, 5);
        this.blackKing = new ChessPosition(8, 5);
        this.blackCastle = true;
        this.whiteCastle = true;
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

        if (!temp.contains(move) || (tempColor != this.currentTurn)) {
            throw new InvalidMoveException();
        }

        //we check if the move would leave the king in check.
        ChessBoard storage = this.boardState.deepCopy();
        this.boardState = hypotheticalMove(move);
        if (isInCheck(tempColor)){
            this.boardState = storage.deepCopy();
            throw new InvalidMoveException();
        }
            this.boardState = storage.deepCopy();
            this.boardState.removePiece(move.getStartPosition());
            this.boardState.removePiece(move.getEndPosition());
            if (move.getPromotionPiece() == null){ this.boardState.addPiece(move.getEndPosition(), example);}
            else {
                example = new ChessPiece(tempColor, move.getPromotionPiece());
                this.boardState.addPiece(move.getEndPosition(), example);
            }

            //This allows for the easy tracking of the kings' position.
            if(boardState.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING){
                if (example.getTeamColor() == TeamColor.BLACK){
                    this.blackKing = move.getEndPosition();
                    blackCastle = false;
                }
                if(example.getTeamColor() == TeamColor.WHITE){
                    this.whiteKing = move.getEndPosition();
                    this.whiteCastle = false;
                }
            }

            //Finally, we track if castling is still okay.
            if(boardState.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.ROOK){
                if (example.getTeamColor() == TeamColor.BLACK){
                    blackCastle = false;
                }
                if(example.getTeamColor() == TeamColor.WHITE){
                    this.whiteCastle = true;
                }
            }
            //Change the turn to be the opposing color.
            if (this.currentTurn == TeamColor.WHITE) {this.currentTurn = TeamColor.BLACK;}
            else if (this.currentTurn == TeamColor.BLACK) { this.currentTurn = TeamColor.WHITE;}

    }

    /**
     * Determines if the given team is in check.
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingSpace;
        if (teamColor == TeamColor.WHITE) {kingSpace = this.whiteKing;}
        else {kingSpace = this.blackKing;}
        return kingThreatened(this.boardState, kingSpace, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition that;
        switch (teamColor){
            case WHITE: {
                that = this.whiteKing;
                break;
            }
            case BLACK: {
                that = this.blackKing;
                break;
            }
            case null: {
                return false;
            }
        }
        //first, we check if king is in check.
        if (!this.isInCheck(teamColor)){
            return false;
        }

        //then, we check all the valid moves the king piece can make.
        Collection<ChessMove> kingMoves = this.validMoves(that);
        for (ChessMove valid:kingMoves){
            ChessBoard storage = this.boardState.deepCopy();
            this.boardState = hypotheticalMove(valid);
            ChessPosition whiteStorage = new ChessPosition(this.whiteKing.getRow(), this.whiteKing.getColumn());
            ChessPosition blackStorage = new ChessPosition(this.blackKing.getRow(), this.blackKing.getColumn());
            switch(teamColor){
                case WHITE: this.whiteKing = valid.getEndPosition(); break;
                case BLACK: this.blackKing = valid.getEndPosition(); break;
            }
            if (!this.isInCheck(teamColor)){
                return false;
            }
            this.boardState = storage.deepCopy();
            this.whiteKing = whiteStorage;
            this.blackKing = blackStorage;
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition that;
        switch (teamColor){
            case WHITE: {
                that = this.whiteKing;
                break;
            }
            case BLACK: {
                that = this.blackKing;
                break;
            }
            case null: {
                return false;
            }
        }
        //first, we check if king is in check. If he is, it's not a stalemate.
        if (this.isInCheck(teamColor)){
            return false;
        }

        //then, we check all the valid moves the king piece can make.
        Collection<ChessMove> kingMoves = this.validMoves(that);
        for (ChessMove valid:kingMoves){
            ChessBoard storage = this.boardState.deepCopy();
            this.boardState = hypotheticalMove(valid);
            ChessPosition whiteStorage = new ChessPosition(this.whiteKing.getRow(), this.whiteKing.getColumn());
            ChessPosition blackStorage = new ChessPosition(this.blackKing.getRow(), this.blackKing.getColumn());
            switch(teamColor){
                case WHITE: this.whiteKing = valid.getEndPosition(); break;
                case BLACK: this.blackKing = valid.getEndPosition(); break;
            }
            if (!this.isInCheck(teamColor)){
                return false;
            }
            this.boardState = storage.deepCopy();
            this.whiteKing = whiteStorage;
            this.blackKing = blackStorage;
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.boardState = board;
        this.whiteKing = new ChessPosition(20, 20);
        this.blackKing = new ChessPosition(20, 20);
        for (int i=1; i<9;i++){
            for (int j=1; j<9;j++){
                ChessPosition temp = new ChessPosition(i,j);
                ChessPiece temp2 = this.boardState.getPiece(temp);
                if (temp2 != null){
                    if (temp2.getPieceType() == ChessPiece.PieceType.KING){
                        switch (temp2.getTeamColor()){
                            case BLACK: {
                                this.blackKing = temp;
                                break;
                            }
                            case WHITE: {
                                this.whiteKing = temp;
                                break;
                            }
                        }
                    }
                }
            }

        }
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
     * @return a {@link ChessBoard} with the hypothetical move performed.
     */
    private ChessBoard hypotheticalMove(ChessMove move){
        ChessBoard that = this.boardState.deepCopy();
        Collection<ChessMove> temp = this.validMoves(move.getStartPosition());
        ChessPiece example = that.getPiece(move.getStartPosition());
        ChessGame.TeamColor tempColor = example.getTeamColor();
        if (temp.contains(move)) {
            that.removePiece(move.getStartPosition());
            that.removePiece(move.getEndPosition());
            that.addPiece(move.getEndPosition(), example);
        }
        return that;
    }

    /**
     * This function will loop through each space on the provided {@link ChessBoard}
     * where the king could be put into check by an enemy piece. If any of those spaces
     * have the appropriate enemy piece on them, it returns true.
     *
     * @param board The passed in chess board, often from a hypothetical move.
     * @param king The king's current position on the board, often from a hypothetical move.
     * @param color The color of the king who is potentially in check.
     * @return True if the king is in check, false if not.
     */
    private boolean kingThreatened(ChessBoard board, ChessPosition king, TeamColor color){
        if (!this.boardState.isInBounds(king)){
            return false;
        }
        int row = king.getRow();
        int col = king.getColumn();
        //First, we check the diagonals.
        int[] bRows = {-1, 1, -1, 1};
        int[] bCol =  {-1,-1,  1, 1};
        for (int i=0; i < 4; i++){
            ChessPosition end = new ChessPosition(row + bRows[i],
                    col + bCol[i]);
            while (board.getPiece(end) == null && board.isInBounds(end)) {
                end = new ChessPosition(end.getRow() + bRows[i], end.getColumn() + bCol[i]);
            }
            ChessPiece testing = board.getPiece(end);
            if (board.getPiece(end) != null){
                if (board.isEnemyPiece(king, end)){
                    if ((board.getPiece(end).getPieceType() == (ChessPiece.PieceType.BISHOP))
                            || (board.getPiece(end).getPieceType() == (ChessPiece.PieceType.QUEEN))){
                        return true;
                    }
                }


            }

        }

        //Then, we check the horizontals.
        bRows = new int[]{-1, 1, 0, 0};
        bCol = new int[]{0, 0, 1, -1};
        for (int i=0; i < 4; i++){
            ChessPosition end = new ChessPosition(row + bRows[i], col + bCol[i]);
            while (board.getPiece(end) == null && board.isInBounds(end)){
                end = new ChessPosition(end.getRow()+bRows[i], end.getColumn()+bCol[i]);
            }
            if (board.getPiece(end) != null && board.isEnemyPiece(king, end)){
                if ((board.getPiece(end).getPieceType() == (ChessPiece.PieceType.ROOK))
                        || (board.getPiece(end).getPieceType() == (ChessPiece.PieceType.QUEEN))){
                    return true;
            }}
        }

        //now, we test for the knights in range.
        bRows = new int[]{2, 2, -2,-2,-1, 1, -1, 1};
        bCol = new int[]{-1, 1, -1, 1, 2, 2, -2,-2};

        for (int i=0; i < 8; i++){
            ChessPosition end = new ChessPosition(row + bRows[i], col + bCol[i]);

            if      (board.getPiece(end) != null && board.isEnemyPiece(king, end)
                    && board.isInBounds(end)) {
                if (board.getPiece(end).getPieceType() == (ChessPiece.PieceType.KNIGHT)) {
                    return true;
                }
            }
        }

        //Checking for pawns in capture range.
        switch (color){
            case BLACK: {
                for (int i=-1; i < 2; i+=2){
                    ChessPosition end = new ChessPosition(row-1, col+i);
                    if (board.getPiece(end) != null && board.isInBounds(end) && board.isEnemyPiece(king, end)){
                        ChessPiece temp =  board.getPiece(end);
                        if (temp.getPieceType() == ChessPiece.PieceType.PAWN){
                            return true;
                        }
                    }
                }
                break;
            }

            case WHITE: {
                for (int i=-1; i < 2; i+=2){
                    ChessPosition end = new ChessPosition(row+1, col+i);
                    if (board.getPiece(end) != null && board.isInBounds(end) && board.isEnemyPiece(king, end)){
                        ChessPiece temp =  board.getPiece(end);
                        if (temp.getPieceType() == ChessPiece.PieceType.PAWN){
                            return true;
                        }
                    }
                }
                break;
            }

        }

        //Checking for kings in capture range.
        bRows = new int[]{0, 0, 1, 1, 1,-1,-1,-1};
        bCol =  new int[]{1,-1, 1,-1, 0, 1,-1, 0};
        for (int i=0; i < 8; i++){
            ChessPosition end = new ChessPosition(row + bRows[i], col + bCol[i]);

            if      (board.getPiece(end) != null
                    && board.isEnemyPiece(king, end)
                    && board.isInBounds(end)
                    && board.getPiece(end).getPieceType() == ChessPiece.PieceType.KING)
            {
                return true;
            }
        }

        return false;
    }


}
