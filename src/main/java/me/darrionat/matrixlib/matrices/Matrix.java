package me.darrionat.matrixlib.matrices;

import me.darrionat.matrixlib.builder.MatrixBuilder;
import me.darrionat.matrixlib.exceptions.DimensionException;
import me.darrionat.matrixlib.exceptions.MatrixMultiplicationDimensionException;
import me.darrionat.matrixlib.util.MatrixIterator;
import me.darrionat.matrixlib.vectors.Vector;

import java.util.Iterator;
import java.util.Objects;

/**
 * Represents a {@code MxN} matrix.
 *
 * @author Darrion Thornburgh
 */
public class Matrix implements Iterable<Double> {
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
    protected double[][] entries;


    /**
     * Creates a new MxN matrix.
     *
     * @param rowAmount    The amount of rows in the matrix, represented as M.
     * @param columnAmount The amount of columns in the matrix, represented as N.
     */
    public Matrix(int rowAmount, int columnAmount) {
        this(rowAmount, columnAmount, null);
    }

    /**
     * Creates a new MxN matrix with defined entries.
     *
     * @param rowAmount    The amount of rows in the matrix, represented as M.
     * @param columnAmount The amount of columns in the matrix, represented as N.
     * @param entries      The values within the matrix
     */
    public Matrix(int rowAmount, int columnAmount, double[][] entries) {
        if (rowAmount <= 0 || columnAmount <= 0)
            throw new DimensionException(Math.min(rowAmount, columnAmount));
        this.rowAmount = rowAmount;
        this.columnAmount = columnAmount;
        if (entries == null)
            entries = new double[rowAmount][columnAmount];
        this.entries = entries;
    }

    /**
     * Gets the values within the matrix.
     *
     * @return All values contained within the matrix
     */
    public double[][] getEntries() {
        return entries;
    }

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
     * @return The amount of columns within the matrix, represented as N.
     */
    public int getColumnAmount() {
        return columnAmount;
    }

    /**
     * Gets a value contained within the matrix located at a position.
     *
     * @param row    The index of the row.
     * @param column The index of the column.
     * @return The value within the matrix at that given row and column.
     */
    public double getValue(int row, int column) {
        return entries[row][column];
    }

    /**
     * Sets the value contained within the matrix located a position.
     *
     * @param row    The index of the row.
     * @param column The index of the column.
     * @param value  The value to set.
     */
    public void setValue(int row, int column, double value) {
        entries[row][column] = value;
    }

    /**
     * Gets a row vector within the matrix.
     *
     * @param row The index of the row
     * @return Returns a row vector within the matrix.
     */
    public double[] getRow(int row) {
        return entries[row];
    }

    /**
     * Gets a column vector within the matrix.
     *
     * @param column The index of the column
     * @return Returns a column vector within the matrix.
     */
    public double[] getColumn(int column) {
        double[] toReturn = new double[rowAmount];
        for (int row = 0; row < rowAmount; row++)
            toReturn[row] = entries[row][column];
        return toReturn;
    }

    /**
     * Multiplies all values within the entire {@link Matrix} by a given scalar.
     *
     * @param scalar The multiplier
     */
    public void scale(double scalar) {
        MatrixIterator iterator = iterator();
        while (iterator.hasNext()) {
            double value = iterator.next();
            iterator.setValue(value * scalar);
        }
    }

    /**
     * Get the product of this matrix and another through row column matrix multiplication.
     * <p>
     * The dimensions of this matrix are MxN. The multiplier must be a NxP matrix. The product will be a MxP matrix.
     *
     * @param multiplier Another matrix that has an amount of rows equal to the amount of columns of this matrix.
     * @return Calculates the product of this matrix and another matrix.
     * @throws MatrixMultiplicationDimensionException thrown when the matrices are not compatible.
     */
    public Matrix multiply(Matrix multiplier) {
        Objects.requireNonNull(multiplier);
        // Handle bad dimensions

        if (columnAmount != multiplier.rowAmount)
            throw new MatrixMultiplicationDimensionException(this, multiplier);

        Matrix product = new Matrix(rowAmount, multiplier.columnAmount);
        for (int row = 0; row < rowAmount; row++) {
            // The row to be multiplied along
            double[] rowVector = getRow(row);
            for (int col = 0; col < multiplier.columnAmount; col++) {
                // The vector multiplying the row
                double[] columnVector = multiplier.getColumn(col);
                // Dot product of the two vectors
                double dotProduct = Vector.getDotProduct(rowVector, columnVector);
                // Set the position to the dot product
                product.setValue(row, col, dotProduct);
            }
        }
        return product;
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
        if (obj == null || !(obj instanceof Matrix))
            return false;
        Matrix matrix = (Matrix) obj;
        if (matrix.getRowAmount() != rowAmount || matrix.getColumnAmount() != columnAmount)
            return false;

        // Assert: both iterators have the same length
        MatrixIterator iterator = iterator();
        MatrixIterator comparedIterator = matrix.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() != comparedIterator.next())
                return false;
        }
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