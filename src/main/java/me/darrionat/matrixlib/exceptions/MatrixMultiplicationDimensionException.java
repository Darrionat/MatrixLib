package me.darrionat.matrixlib.exceptions;

import me.darrionat.matrixlib.matrices.Matrix;

/**
 * Thrown to indicate that the dimensions of two matrices of type {@link Matrix} are not compatible for multiplication.
 *
 * @author Darrion Thornburgh
 */
public class MatrixMultiplicationDimensionException extends RuntimeException {
    /**
     * Constructs a new {@code MatrixMultiplicationDimensionException} with two arguments defining matrices that are not
     * compatible for multiplication.
     * <p>
     * Let the {@code multiplicand} be a MxN matrix. The {@code multiplier} must then be a NxP matrix.
     * <p>
     * Therefore, the {@code multiplier} must have the same amount of rows as the {@code multiplicand} has columns.
     *
     * @param multiplicand the matrix that is being multiplied by the {@code multiplier}.
     * @param multiplier   the matrix that is multiplying the {@code multiplicand}.
     */
    public MatrixMultiplicationDimensionException(Matrix multiplicand, Matrix multiplier) {
        super("Multiplier must have " + multiplicand.getColumnAmount() + "rows but has " + multiplier.getRowAmount());
    }
}
