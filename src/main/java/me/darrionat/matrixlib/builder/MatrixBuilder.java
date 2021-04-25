package me.darrionat.matrixlib.builder;

import me.darrionat.matrixlib.exceptions.ReadMatrixException;
import me.darrionat.matrixlib.matrices.Matrix;
import me.darrionat.matrixlib.util.Rational;

/**
 * Provides methods for creating matrices from strings.
 */
public class MatrixBuilder {
    /*
     Matrix String values
     */
    /**
     * Used to signify the start of a {@code Matrix}.
     */
    public static final char MATRIX_START = '[';
    /**
     * Used to signify the end of a {@code Matrix}
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
     * Used to separate rows in a {@code Matrix} string.
     */
    public static final String ROW_SEPARATOR = ";";
    /**
     * Used to separate values in a {@code Matrix} string.
     */
    public static final String SEPARATOR = ",";

    /**
     * Builds a {@link Matrix} from an input string. The matrix must be formatted properly.
     *
     * @param matrixString A matrix in the form of a string.
     * @return Returns a built matrix created from a string.
     */
    public static Matrix parseMatrix(String matrixString) {
        char[] chars = matrixString.toCharArray();
        assertValidChars(chars, MATRIX_START, MATRIX_END);

        String[] allRows = matrixString.replace(MATRIX_START + "", "").replace(MATRIX_END + "", "").split(ROW_SEPARATOR);
        int rows = allRows.length;

        int columns = 0;
        Matrix matrix = null;

        for (int row = 0; row < rows; row++) {
            char[] rowChars = allRows[row].toCharArray();
            assertValidChars(rowChars, ROW_START, ROW_END);

            // Split
            String[] colValues = allRows[row].replace(ROW_START + "", "").replace(ROW_END + "", "").split(SEPARATOR);

            // Init builder
            if (matrix == null) {
                columns = colValues.length;
                matrix = new Matrix(rows, columns);
            }
            for (int col = 0; col < columns; col++) {
                matrix.setValue(row, col, Rational.parseRational(colValues[col]));
            }
        }
        if (matrix == null) throw new NullPointerException("Empty Matrix");
        return matrix;
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