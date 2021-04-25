package me.darrionat.matrixlib.vectors;

import me.darrionat.matrixlib.util.Rational;

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
    public static Rational getDotProduct(Rational[] v, Rational[] u) {
        if (v.length != u.length)
            throw new IllegalArgumentException(DIMENSION_ERROR);

        Rational dotProduct = Rational.ZERO;
        for (int i = 0; i < v.length; i++)
            dotProduct = dotProduct.add(v[i].multiply(u[i]));
        return dotProduct;
    }
}
