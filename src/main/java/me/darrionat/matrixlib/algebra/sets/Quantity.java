package me.darrionat.matrixlib.algebra.sets;

import me.darrionat.matrixlib.algebra.math.Operable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * An immutable data type that represents a mathematical quantity or set of numbers
 * including complex numbers, rationals, etc.
 *
 * @author Darrion Thornburgh
 */
public abstract class Quantity implements Operable, Comparable<Quantity>, Serializable {
    private static final long serialVersionUID = 4704519333219901723L;
    /**
     * A cache that represents powers of this number.
     * <p>
     * The key is the integer power to raise to, and the value is the result.
     *
     * @see #pow(int)
     */
    private final HashMap<Integer, Quantity> powers = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Quantity pow(int pow) {
        // Insert predetermined values
        powers.put(0, Complex.ONE);
        powers.put(1, this);
        if (powers.containsKey(pow))
            return powers.get(pow);

        // Convert into binary representation
        // 55 -> 32+16+0+4+2+1 -> 110111
        String binary = Integer.toBinaryString(Math.abs(pow));
        // Determine all values of the powers of 2 of this complex number
        // Start at the 2nd to last binary digit because the last is already known, because it represents 2^0 or 1.
        for (int i = binary.length() - 2, expo = 1; i >= 0; i--, expo++) {
            int powerOfTwo = (int) Math.pow(2, expo);
            // Get the previous power of two, e.g. 32 -> 16
            Quantity prev = powers.get(powerOfTwo / 2);
            // Get this current power of two e.g. 32 -> complex ^ 2
            powers.put(powerOfTwo, prev.multiply(prev));
        }

        Quantity result = Complex.ONE;
        for (int i = binary.length() - 1, expo = 0; i >= 0; i--, expo++) {
            int powerOfTwo = (int) Math.pow(2, expo);
            if (binary.charAt(i) == '1')
                result = result.multiply(powers.get(powerOfTwo));
        }
        return result;
    }

    /**
     * Parses the quantity as a Java integer.
     *
     * @return The quantity as an {@code int}.
     * @throws ArithmeticException thrown when the value is not within {@code Integer.MIN_VALUE} to {@code Integer.MAX_VALUE}
     *                             or if the value cannot be represented as an integer.
     */
    public abstract int intValue() throws ArithmeticException;

    /**
     * Parses a number from a string.
     *
     * @param s The string to be parsed.
     * @return the parsed value.
     */
    public static Quantity parseNumber(String s) {
        Complex c = Complex.parseComplex(s);
        if (c.isReal()) return c.toRational();
        return c;
    }
}