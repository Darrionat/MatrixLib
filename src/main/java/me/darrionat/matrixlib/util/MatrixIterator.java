package me.darrionat.matrixlib.util;

import me.darrionat.matrixlib.matrices.Matrix;
import me.darrionat.matrixlib.matrices.OperableMatrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a {@link Iterator} that iterates over a {@link Matrix}. The iterator loops over the matrix from left to
 * right then up to down.
 */
public class MatrixIterator implements Iterator<Rational> {
    private final OperableMatrix matrix;
    /**
     * Used for iterating over rows
     */
    private int i = 0;
    /**
     * Used for iterating over columns
     */
    private int j = 0;

    public MatrixIterator(OperableMatrix matrix) {
        this.matrix = matrix;
    }

    /**
     * Sets the value of the current position within the {@link Matrix}.
     *
     * @param value the new value.
     * @return the iterator.
     */
    public MatrixIterator setValue(Rational value) {
        matrix.setValue(i, j, value);
        return this;
    }

    @Override
    public boolean hasNext() {
        return i != matrix.getRowAmount() - 1 || j != matrix.getColumnAmount() - 1;
    }

    @Override
    public Rational next() {
        if (!hasNext())
            throw new NoSuchElementException("There are no remaining values within the Matrix");
        if (j == matrix.getColumnAmount() - 1) {
            ++i;
            j = 0;
        }
        return matrix.getValue(i, j++);
    }
}