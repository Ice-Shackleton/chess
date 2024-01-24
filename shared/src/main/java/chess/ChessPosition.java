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
     * This is the row variable of a chess position, where 0 represents the top-most row.
     */
    private int row;

    /**
     * This is the column variable of a chess position, where 0 represents the left-most row.
     */
    private int col;
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return this.row == that.row && this.col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.row, this.col);
    }
}
