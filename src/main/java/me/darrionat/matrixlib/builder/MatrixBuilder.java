package me.darrionat.matrixlib.builder;

import me.darrionat.matrixlib.exceptions.DimensionException;
import me.darrionat.matrixlib.exceptions.ReadMatrixException;
import me.darrionat.matrixlib.matrices.Matrix;
import me.darrionat.matrixlib.matrices.SquareMatrix;

/**
 * Represents a builder for a matrix.
 */
public class MatrixBuilder {
    /**
     * The amount of rows in the matrix, represented as M.
     */
    protected int rowAmount;
    /**
     * The amount of columns in the matrix, represented as N.
     */
    protected int columnAmount;
    /**
     * An two dimensional array representing the rows of a matrix and the values within them.
     */
    protected double[][] entries;

    /**
     * Error message for when an incompatible row is given.
     */
    private static final String ILLEGAL_ROW = "The given row must have an equal amount of values as there are columns in the matrix.";
    /**
     * Error message for when an incompatible column is given.
     */
    private static final String ILLEGAL_COLUMN = "The new column must have an equal amount of values as there are rows in the matrix.";

        /*
     Matrix String values
     */

    /**
     * Used to signify the start of a {@link Matrix}
     */
    public static final char MATRIX_START = '[';
    /**
     * Used to signify the end of a {@link Matrix}
     */
    public static final char MATRIX_END = ']';
    /**
     * Used to signify the start of a row vector.
     */
    public static final char ROW_START = '{';
    /**
     * Used to signify the end of a row vector.
     */
    public static final char ROW_END = '}';
    /**
     * Used to separate rows in a {@link Matrix} string.
     */
    public static final String ROW_SEPARATOR = ";";
    /**
     * Used to separate values in a {@link Matrix} string.
     */
    public static final String SEPARATOR = ",";

    /**
     * Creates a new MxN matrix.
     *
     * @param rowAmount    The amount of rows in the matrix, represented as M.
     * @param columnAmount The amount of columns in the matrices.Matrix, represented as N.
     */
    public MatrixBuilder(int rowAmount, int columnAmount) {
        if (rowAmount <= 0 || columnAmount <= 0)
            throw new DimensionException(Math.min(rowAmount, columnAmount));
        this.rowAmount = rowAmount;
        this.columnAmount = columnAmount;
        this.entries = new double[rowAmount][columnAmount];
    }

    /**
     * Sets the value contained within the matrix located a position.
     *
     * @param row    The index of the row.
     * @param column The index of the column.
     * @param value  The value to set.
     */
    public MatrixBuilder setValue(int row, int column, double value) {
        entries[row][column] = value;
        return this;
    }

    /**
     * Sets a row vector of a matrix.
     *
     * @param row    The index of the row.
     * @param values The values of the row vector.
     * @throws IllegalArgumentException Thrown when the new row has more values than there are columns in the matrix.
     */
    public MatrixBuilder setRow(int row, double[] values) {
        if (values.length != columnAmount)
            throw new IllegalArgumentException(ILLEGAL_ROW);
        entries[row] = values;
        return this;
    }

    /**
     * Sets a column vector of a matrix.
     *
     * @param column The index of the row.
     * @param values The values of the column vector.
     * @throws IllegalArgumentException Thrown when the new column has more values than there are rows in the matrix.
     */
    public MatrixBuilder setColumn(int column, double[] values) {
        if (values.length != rowAmount)
            throw new IllegalArgumentException(ILLEGAL_COLUMN);
        for (int row = 0; row < rowAmount; row++)
            entries[row][column] = values[row];
        return this;
    }

    /**
     * Builds a {@link Matrix} or {@link SquareMatrix} with defined entries.
     *
     * @return Returns the built matrix
     */
    public Matrix build() {
        if (columnAmount == rowAmount)
            return new SquareMatrix(rowAmount, entries);
        return new Matrix(rowAmount, columnAmount, entries);
    }

    /**
     * Builds a {@link Matrix} from an input string. The matrix must be formatted properly.
     *
     * @param matrixString A matrix in the form of a string.
     * @return Returns a built matrix created from a string.
     */
    public static Matrix fromString(String matrixString) {
        char[] chars = matrixString.toCharArray();
        assertValidChars(chars, MATRIX_START, MATRIX_END);

        String[] allRows = matrixString.replace(MATRIX_START + "", "").replace(MATRIX_END + "", "").split(ROW_SEPARATOR);
        int rows = allRows.length;

        int columns = 0;
        MatrixBuilder builder = null;

        for (int row = 0; row < rows; row++) {
            char[] rowChars = allRows[row].toCharArray();
            assertValidChars(rowChars, ROW_START, ROW_END);

            // Split
            String[] colValues = allRows[row].replace(ROW_START + "", "").replace(ROW_END + "", "").split(SEPARATOR);

            // Init builder
            if (builder == null) {
                columns = colValues.length;
                builder = new MatrixBuilder(rows, columns);
            }
            for (int col = 0; col < columns; col++) {
                builder.setValue(row, col, Double.parseDouble(colValues[col]));
            }
        }
        if (builder == null)
            throw new NullPointerException("Empty Matrix");
        return builder.build();
    }

    /**
     * Performs a check to see if a string of a {@link Matrix} is formatted properly.
     *
     * @param arr   The array of characters to check
     * @param start The required starting character
     * @param end   The required ending character
     */
    private static void assertValidChars(char[] arr, char start, char end) {
        // Start is invalid
        char first = arr[0];
        if (first != start)
            throw new ReadMatrixException(first, start);
        // End is invalid
        char last = arr[arr.length - 1];
        if (last != end)
            throw new ReadMatrixException(last, end);
    }
}