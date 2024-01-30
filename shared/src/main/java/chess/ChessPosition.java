package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    /**
     * Please note that the rows and columns of a Chess Position are on a 1 index, rather than 0.
     */

    private int pRow;
    private int pCol;

    public ChessPosition(int row, int col) {
        this.pRow = row;
        this.pCol = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.pRow;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return this.pCol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return this.pRow == that.pRow && this.pCol == that.pCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pRow, this.pCol);
    }
}
