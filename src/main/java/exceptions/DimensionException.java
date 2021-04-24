package exceptions;

import matrices.Matrix;

/**
 * Thrown to indicate that the dimension of a {@link Matrix} is below or equal to 0.
 *
 * @author Darrion Thornburgh
 */
public class DimensionException extends RuntimeException {
    /**
     * Constructs a new {@code DimensionException} with an argument
     * indicating the illegal dimension.
     *
     * @param dimension the illegal dimension.
     */
    public DimensionException(int dimension) {
        super("Dimension " + dimension + " below or equal to 0");
    }
}
