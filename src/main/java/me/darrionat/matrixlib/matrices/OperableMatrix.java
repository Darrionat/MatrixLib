package me.darrionat.matrixlib.matrices;

import me.darrionat.matrixlib.builder.MatrixBuilder;
import me.darrionat.matrixlib.exceptions.DimensionException;
import me.darrionat.matrixlib.util.MatrixIterator;
import me.darrionat.matrixlib.algebra.sets.Rational;

import java.util.Iterator;

/**
 * Represents a matrix that can use row and column operations.
 *
 * @author Darrion Thornburgh
 */
public abstract class OperableMatrix implements Iterable<Rational> {
    /**
     * The amount of rows in the matrix, represented as M.
     */
    protected int rowAmount;
    /**
     * The amount of columns in the matrix, represented as N.
     */
    protected int columnAmount;
    /**
     * An two dimensional array representing the rows of a matrix and the values within them. <br> E.g. {@code double
     * value = entries[row][column]}
     */
    protected Rational[][] entries;

    /**
     * Creates a new MxN {@code OperableMatrix}.
     *
     * @param rowAmount    The amount of rows in the matrix, represented as M.
     * @param columnAmount The amount of columns in the matrix, represented as N.
     */
    public OperableMatrix(int rowAmount, int columnAmount) {
        this(rowAmount, columnAmount, null);
    }

    /**
     * Creates a new MxN {@code OperableMatrix} with defined entries.
     *
     * @param rowAmount    The amount of rows in the matrix, represented as M.
     * @param columnAmount The amount of columns in the matrix, represented as N.
     * @param entries      The values within the matrix
     */
    public OperableMatrix(int rowAmount, int columnAmount, Rational[][] entries) {
        if (rowAmount <= 0 || columnAmount <= 0)
            throw new DimensionException(Math.min(rowAmount, columnAmount));
        this.rowAmount = rowAmount;
        this.columnAmount = columnAmount;

        if (entries == null) {
            this.entries = new Rational[rowAmount][columnAmount];
            MatrixIterator iterator = iterator();
            while (iterator.hasNext())
                iterator.setValue(Rational.ZERO).next();
            return;
        }
        this.entries = entries;
    }

    /**
     * Error message for when an incompatible row is given.
     */
    private static final String ILLEGAL_ROW = "Row length does not equal amount of columns";
    /**
     * Error message for when an incompatible column is given.
     */
    private static final String ILLEGAL_COLUMN = "Column length does not equal amount of rows";

    /**
     * Gets the amount of rows within the matrix.
     *
     * @return The amount of rows within the matrix, represented as M.
     */
    public int getRowAmount() {
        return rowAmount;
    }

    /**
     * Gets the amount of columns within the matrix.
     *
     * @return the amount of columns within the matrix, represented as N.
     */
    public int getColumnAmount() {
        return columnAmount;
    }

    /**
     * Gets the values within the matrix.
     *
     * @return all values contained within the matrix,
     */
    public Rational[][] getEntries() {
        return entries;
    }

    /**
     * Gets a value contained within the matrix located at a position.
     *
     * @param row    the index of the row.
     * @param column the index of the column.
     * @return the value within the matrix at that given row and column.
     */
    public Rational getValue(int row, int column) {
        return entries[row][column];
    }

    /**
     * Sets the value contained within the matrix located a position.
     *
     * @param row    the index of the row.
     * @param column the index of the column.
     * @param value  the value to set.
     */
    public void setValue(int row, int column, Rational value) {
        entries[row][column] = value;
    }

    /**
     * Gets a row vector within the matrix.
     *
     * @param row the index of the row
     * @return returns a row vector within the matrix.
     */
    public Rational[] getRow(int row) {
        return entries[row];
    }

    /**
     * Sets a row vector of a matrix.
     *
     * @param row    the index of the row.
     * @param values the values of the row vector.
     * @throws IllegalArgumentException the new row has more values than there are columns in the matrix.
     */
    public void setRow(int row, Rational[] values) {
        if (values.length != columnAmount)
            throw new IllegalArgumentException(ILLEGAL_ROW);
        entries[row] = values;
    }

    /**
     * Gets a column vector within the matrix.
     *
     * @param column the index of the column
     * @return returns a column vector within the matrix.
     */
    public Rational[] getColumn(int column) {
        Rational[] toReturn = new Rational[rowAmount];
        for (int row = 0; row < rowAmount; row++)
            toReturn[row] = entries[row][column];
        return toReturn;
    }

    /**
     * Sets a column vector of a matrix.
     *
     * @param column the index of the row.
     * @param values the values of the column vector.
     * @throws IllegalArgumentException the new column has more values than there are rows in the matrix.
     */
    public void setColumn(int column, Rational[] values) {
        if (values.length != rowAmount)
            throw new IllegalArgumentException(ILLEGAL_COLUMN);
        for (int row = 0; row < rowAmount; row++)
            entries[row][column] = values[row];
    }

    /**
     * Swaps two rows within the matrix.
     *
     * @param a the index of the first row.
     * @param b the index of the second row.
     */
    public void swapRows(int a, int b) {
        if (a == b) return;
        Rational[] row1 = getRow(a);
        Rational[] row2 = getRow(b);
        setRow(a, row2);
        setRow(b, row1);
    }

    /**
     * Swaps two columns within the matrix.
     *
     * @param a the index of the first column.
     * @param b the index of the second column.
     */
    public void swapColumns(int a, int b) {
        Rational[] col1 = getColumn(a);
        Rational[] col2 = getColumn(b);
        setColumn(a, col2);
        setColumn(b, col1);
    }

    /**
     * Multiplies a row by a given scalar.
     *
     * @param row    the row being scaled.
     * @param scalar the value to multiply the row by.
     */
    public void multiplyRow(int row, Rational scalar) {
        for (int column = 0; column < columnAmount; column++)
            setValue(row, column, getValue(row, column).multiply(scalar));
    }

    /**
     * Divides a row by a given dividend.
     *
     * @param row      the row being divided.
     * @param dividend the value to divide the row by.
     */
    public void divideRow(int row, Rational dividend) {
        if (dividend.zero())
            throw new ArithmeticException("Cannot divide by zero");
        multiplyRow(row, dividend.reciprocal());
    }

    /**
     * Multiplies a column by a given scalar.
     *
     * @param column the column being scaled.
     * @param scalar the value to multiply the column by.
     */
    public void multiplyColumn(int column, Rational scalar) {
        for (int row = 0; row < columnAmount; row++)
            setValue(row, column, getValue(row, column).multiply(scalar));
    }

    /**
     * Divides a column by a given dividend.
     *
     * @param column   the column being divided.
     * @param dividend the value to divide the row by.
     */
    public void divideColumn(int column, Rational dividend) {
        if (dividend.zero())
            throw new ArithmeticException("Cannot divide by zero");
        multiplyColumn(column, dividend.reciprocal());
    }

    /**
     * Adds a multiple of a row to the given row.
     *
     * @param row          the effected row.
     * @param addendRow    the row being scaled and added to {@code row}.
     * @param addendScalar the scalar that the {@code addend} is being multiplied by.
     */
    public void rowSum(int row, int addendRow, Rational addendScalar) {
        for (int column = 0; column < columnAmount; column++) {
            Rational toAdd = getValue(addendRow, column).multiply(addendScalar);
            setValue(row, column, getValue(row, column).add(toAdd));
        }
    }

    /**
     * Creates a deep copy of this {@code Matrix}.
     *
     * @return gets a copy of the matrix.
     */
    public Matrix copy() {
        return new Matrix(rowAmount, columnAmount, entries);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(MatrixBuilder.MATRIX_START);
        for (int row = 0; row < rowAmount; row++) {
            if (row != 0)
                s.append(MatrixBuilder.ROW_SEPARATOR);
            s.append(MatrixBuilder.ROW_START);
            for (int col = 0; col < columnAmount; col++) {
                if (col != 0)
                    s.append(MatrixBuilder.SEPARATOR);
                s.append(getValue(row, col));
            }
            s.append(MatrixBuilder.ROW_END);
        }
        s.append(MatrixBuilder.MATRIX_END);
        return s.toString();
    }

    /**
     * Checks to see if two non null matrices are equal.
     *
     * @param obj the matrix which to compare.
     * @return {@code true} if both matrices have the same dimensions and values; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OperableMatrix))
            return false;
        OperableMatrix matrix = (OperableMatrix) obj;
        if (matrix.getRowAmount() != rowAmount || matrix.getColumnAmount() != columnAmount)
            return false;

        // Assert: both iterators have the same length
        MatrixIterator iterator = iterator();
        MatrixIterator comparedIterator = matrix.iterator();
        while (iterator.hasNext())
            if (!iterator.next().equals(comparedIterator.next()))
                return false;
        // All values are equal in both matrices
        return true;
    }

    /**
     * Creates an {@link Iterator} for a {@link Matrix}.
     *
     * @return Returns a matrix iterator.
     */
    public MatrixIterator iterator() {
        return new MatrixIterator(this);
    }
}