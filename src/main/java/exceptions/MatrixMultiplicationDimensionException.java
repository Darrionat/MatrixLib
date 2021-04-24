package exceptions;

import matrices.Matrix;

/**
 * Represents a fail in matrix in multiplication due to incompatible dimensions.
 *
 * @author Darrion Thornburgh
 */
public class MatrixMultiplicationDimensionException extends RuntimeException {
    /**
     * A {@link MatrixMultiplicationDimensionException} is when two matrices are incompatible
     * for matrix multiplication.
     * <p>
     * Let the multiplicand be a MxN matrix.
     * The multiplier must then be a NxP matrix.
     * <p>
     * Therefore, the multiplier must have the same amount of rows as the multiplicand has columns.
     *
     * @param multiplicand A matrix that is being multiplied by the {@code multiplier}
     * @param multiplier   A matrix that is multiplying the {@code multiplicand}
     */
    public MatrixMultiplicationDimensionException(Matrix multiplicand, Matrix multiplier) {
        super("Multiplier must have " + multiplicand.getColumnAmount() + "rows but has " + multiplier.getRowAmount());
    }
}
