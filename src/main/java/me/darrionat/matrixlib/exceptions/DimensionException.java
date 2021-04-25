package me.darrionat.matrixlib.exceptions;

import me.darrionat.matrixlib.matrices.Matrix;

/**
 * Thrown to indicate that the dimension of a {@link Matrix} does not suit a given requirement.
 *
 * @author Darrion Thornburgh
 */
public class DimensionException extends RuntimeException {
    /**
     * Constructs a new {@code DimensionException} with an argument indicating the illegal dimension.
     *
     * @param dimension the illegal dimension.
     */
    public DimensionException(int dimension) {
        super("Illegal Dimension: " + dimension);
    }

    /**
     * Constructs a new {@code DimensionException} with two matrices as arguments to indicate that their dimensions are
     * not equal.
     *
     * @param a the first matrix.
     * @param b the second matrix.
     */
    public DimensionException(Matrix a, Matrix b) {
        super(a.getRowAmount() + "x" + a.getColumnAmount() + " not equal to " + b.getRowAmount() + "x" + b.getColumnAmount());
    }
}