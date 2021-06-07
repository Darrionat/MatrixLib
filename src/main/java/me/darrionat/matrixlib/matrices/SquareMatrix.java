package me.darrionat.matrixlib.matrices;

import me.darrionat.matrixlib.algebra.sets.Complex;
import me.darrionat.matrixlib.algebra.sets.Number;

/**
 * Represents an NxN {@link Matrix} with an equal amount of rows and columns.
 *
 * @author Darrion Thornburgh
 */
public class SquareMatrix extends Matrix {
    /**
     * Creates a square {@link Matrix}.
     * <p>
     * A square matrix has an equal amount of rows and columns.
     *
     * @param dimension The amount of rows and columns the matrix will have
     */
    public SquareMatrix(int dimension) {
        super(dimension, dimension);
    }

    /**
     * Creates a square {@link Matrix} with defined entries.
     * <p>
     * A square matrix has an equal amount of rows and columns.
     *
     * @param dimension The amount of rows and columns the matrix will have
     * @param entries   The values of the matrix.
     */
    public SquareMatrix(int dimension, Number[][] entries) {
        super(dimension, dimension, entries);
    }

    /**
     * Determines if the {@link SquareMatrix} has all zeros below the diagonal.
     *
     * @return Returns {@code true} if the matrix is triangular.
     */
    public boolean isTriangular() {
        for (int row = 0; row < rowAmount; row++)
            for (int col = 0; col < row; col++)
                if (!getValue(row, col).zero()) return false;
        return true;
    }

    /**
     * Calculates the determinant of the {@code SquareMatrix}.
     *
     * @return the determinant of the matrix.
     */
    public Number det() {
        Matrix copy = copy();
        copy.ref();
        Number det = Complex.ZERO;
        for (int pivot = 0; pivot < rowAmount; pivot++)
            det = det.multiply(copy.getValue(pivot, pivot));
        return det;
    }
}