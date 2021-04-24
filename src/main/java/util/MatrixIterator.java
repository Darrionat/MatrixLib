package util;

import matrices.Matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a {@link Iterator} that iterates over a {@link Matrix}.
 * The iterator loops over the matrix from left to right then up to down.
 */
public class MatrixIterator implements Iterator<Double> {
    private final Matrix matrix;
    /**
     * Used for iterating over rows
     */
    private int i;
    /**
     * Used for iterating over columns
     */
    private int j;

    public MatrixIterator(Matrix matrix) {
        this.matrix = matrix;
    }

    /**
     * Sets the value of the current position within the {@link Matrix}.
     *
     * @param value The new value.
     */
    public void setValue(double value) {
        matrix.setValue(i, j, value);
    }

    @Override
    public boolean hasNext() {
        return i != matrix.getRowAmount() - 1 || j != matrix.getColumnAmount() - 1;
    }

    @Override
    public Double next() {
        if (!hasNext())
            throw new NoSuchElementException("There are no remaining values within the Matrix");

        if (j == matrix.getColumnAmount() - 1) {
            ++i;
            j = 0;
        }
        return matrix.getValue(i, j++);
    }
}