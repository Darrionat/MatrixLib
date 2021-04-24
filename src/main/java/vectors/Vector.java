package vectors;

public class Vector {
    private static final String DIMENSION_ERROR = "Vector dimensions are not equal";

    /**
     * Calculates the dot product of two vectors of same dimension.
     *
     * @param v The first vector
     * @param u The second vector
     * @return Returns the dot product of two vectors
     * @throws IllegalArgumentException thrown when the vectors' dimensions are not equal.
     */
    public static double getDotProduct(double[] v, double[] u) {
        if (v.length != u.length)
            throw new IllegalArgumentException(DIMENSION_ERROR);

        double dotProduct = 0;
        for (int i = 0; i < v.length; i++) {
            dotProduct += v[i] * u[i];
        }
        return dotProduct;
    }
}
